package com.eaju.sms.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eaju.sms.entity.VoiceCallWav;
import com.eaju.sms.service.VoiceCallService;
import com.eaju.sms.service.VoiceCallWavService;
import com.eaju.sms.util.ResourcesUtil;
import com.google.gson.Gson;

import EcpOpen.Model.NotifyCallInformation;
import EcpOpen.Model.ParticipantStateInformation;
import EcpOpen.Model.SimpleReference;
import EcpOpen.Model.Sms;
import EcpOpen.constant.Constants;
import EcpOpen.http.Extend.MCSM.MessageAnalyzer;
import EcpOpen.http.Extend.MCSM.MessageHandler;
import EcpOpen.http.Extend.VCSM.callTransferToConference;

@Controller
@RequestMapping("/")
public class RecvMessageController {
	@Autowired
	private VoiceCallService voiceCallService;
	@Autowired
	private VoiceCallWavService voiceCallWavService;
	private final static Logger logger = LoggerFactory.getLogger(RecvMessageController.class);

	@POST
	@RequestMapping("/recv")
	public void recv(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BufferedReader reader = req.getReader();
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		logger.info("accept: " + sb.toString());
		final VoiceCallService voice = voiceCallService;
		MessageAnalyzer analyzer = new MessageAnalyzer(new MessageHandler() {
			/**
			 * 处理收到呼叫状态通知的回调方法
			 */
			@Override
			public void onRecvNotifyCallInformation(NotifyCallInformation information) {
				logger.info("收到呼叫状态消息:");
				logger.info("呼叫标识：" + information.getCallIdentifier());
				logger.info("电话号码：" + information.getPhone());
				logger.info("呼叫状态：" + information.getCallStatus().toString());
				logger.info("\n");
				// TODO
				if ("RINGING".equals(information.getCallStatus().toString())) {
					System.out.println(information.getPhoneID());
					String findByString = voice.findByString(information.getCallIdentifier(), information.getPhoneID());
					if (findByString != null) {
						try {
							Constants.extendUrl = ResourcesUtil.getValue("http", "extendUrl");
							Constants.spid = ResourcesUtil.getValue("http", "spid");
							Constants.serviceKey = ResourcesUtil.getValue("http", "serviceKey");
							Constants.appid = ResourcesUtil.getValue("http", "appid");
							Constants.appkey = ResourcesUtil.getValue("http", "appkey");
							Constants.type = ResourcesUtil.getValue("http", "type");
							SimpleReference simpleRef = new SimpleReference();
							simpleRef.setEndpoint(ResourcesUtil.getValue("http", "endPoint1"));
							simpleRef.setInterfaceName("notifySmsDeliveryStatus");
							simpleRef.setCorrelator("hello world");
							callTransferToConference calltranExample = new callTransferToConference();
							calltranExample.init();
							calltranExample
									.setAccount("sip:" + ResourcesUtil.getValue("http", "account") + "@ecplive.com"); // 设置账号,此处账号为格式为sip:xxxxxx@ecplive.com,与上面的makecall账号需一致
							calltranExample.setCallIdentifier(information.getCallIdentifier()); // 获取通话id（同步返回）status[1]
							calltranExample.setMaxDuration("0"); // 此处不能为空，0为不限制
							calltranExample.setMaxParticipants("10");
							calltranExample.setParticipantsPhoneID(findByString + "," + information.getPhoneID()); // 从接口[endpoint]日志获取，主被叫号码的phoneID，非电话号码
							calltranExample.setPrompted("0");
							calltranExample.setRecorded("1");
							calltranExample.setVoiceFile("");
							calltranExample.setStatusRequest(simpleRef);
							String response1 = calltranExample.invokeClient(true);
							logger.info(response1);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}

			/**
			 * 处理收到的短信消息的回调方法
			 */
			@Override
			public void onRecvNotifySmsReceptionRequest(Sms sms) {
				System.out.println(sms.getDateTime() + "收到短信：");
				System.out.println("短信内容：" + sms.getMessage());
				System.out.println("发送者号码：" + sms.getSenderAddress());
				System.out.println("接受者号码：" + sms.getReciverNumber());
				System.out.println();
				// TODO
				// 这个添加将短信入库的代码
			}
			/**
			 * 重写其它事件的处理方法
			 */
		});
		analyzer.analyze(sb.toString());
	}

	// @RequestMapping(value="/recvto",produces =
	// {ContentType.APPLICATION_JSON_UTF_8})
	// @Produces({ "application/json;charset=UTF-8"})
	// @Consumes({MediaType.APPLICATION_JSON })
	// @ResponseBody
	// public void recvto(@RequestBody VoiceBack vb) throws Exception {
	// System.out.println("-----------------------------");
	// System.out.println(vb);
	// System.out.println("-----------------------------");
	//
	// }
	@RequestMapping(value="/recvto")
	public void  recvtoto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.info("---------------servlet--------------");
		Gson gson=new Gson();
		BufferedReader reader = req.getReader();
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		if(sb.toString().trim().length()==0){
			return;
		}
		logger.info("accept: " + sb.toString());
		if(sb.toString().trim().startsWith("{")){
			fileModel fromJson = gson.fromJson(sb.toString(), fileModel.class);
			logger.info(fromJson.toString());
			VoiceCallWav vcw=new VoiceCallWav();
			vcw.setCallidentifier(fromJson.conferenceid);
			vcw.setStarttime(fromJson.starttime);
			vcw.setEndtime(fromJson.endtime);
			voiceCallWavService.insertOrUpdate(vcw);
			VoiceCallWav vcww = voiceCallWavService.selectByPrimaryKey(fromJson.conferenceid);
			voiceCallService.updateVcByPhoneId(vcww.getParticipantphoneid1(), vcww.getParticipantphoneid2(), fromJson.recordfile);
			return;
		}
		MessageAnalyzer analyzer = new MessageAnalyzer(new MessageHandler() {
			/**
			 * 处理收到录音状态通知的回调方法
			 */
			@Override
			public void onRecvParticipantStateInformation(ParticipantStateInformation information) {
				logger.info("收到录音状态消息:");
				logger.info("ConferenceIdentifier：" + information.getConferenceIdentifier());
				logger.info("Participant：" + information.getParticipant());
				logger.info("StartTime：" + information.getStartTime());
				logger.info("Duration：" + information.getDuration());
				logger.info("Status：" + information.getStatus());
				logger.info("ParticipantPhoneID：" + information.getParticipantPhoneID());
				logger.info("Muted：" + information.isMuted());
				logger.info("Silent：" + information.isSilent());
				logger.info("\n");
				VoiceCallWav vcw=new VoiceCallWav();
				vcw.setCallidentifier(information.getConferenceIdentifier());
				vcw.setParticipantphoneid1(Integer.toString(information.getParticipantPhoneID()));
				voiceCallWavService.insertOrUpdate(vcw);
			}
		});
		analyzer.analyze(sb.toString());
	}
	private class fileModel{
		private String conferenceid;
		private String 	endtime;
		private String 	filepushed;
		private String 	recordfile;
		private String 	starttime;
		public String getConferenceid() {
			return conferenceid;
		}
		public void setConferenceid(String conferenceid) {
			this.conferenceid = conferenceid;
		}
		public String getEndtime() {
			return endtime;
		}
		public void setEndtime(String endtime) {
			this.endtime = endtime;
		}
		public String getFilepushed() {
			return filepushed;
		}
		public void setFilepushed(String filepushed) {
			this.filepushed = filepushed;
		}
		public String getRecordfile() {
			return recordfile;
		}
		public void setRecordfile(String recordfile) {
			this.recordfile = recordfile;
		}
		public String getStarttime() {
			return starttime;
		}
		public void setStarttime(String starttime) {
			this.starttime = starttime;
		}
		@Override
		public String toString() {
			return "fileModel [conferenceid=" + conferenceid + ", endtime=" + endtime + ", filepushed=" + filepushed
					+ ", recordfile=" + recordfile + ", starttime=" + starttime + "]";
		}
		
	}
}
