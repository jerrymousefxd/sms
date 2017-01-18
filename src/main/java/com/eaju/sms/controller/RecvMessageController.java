package com.eaju.sms.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.eaju.sms.entity.VoiceBack;
import com.eaju.sms.service.VoiceCallService;
import com.eaju.sms.util.ResourcesUtil;

import EcpOpen.Model.NotifyCallInformation;
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
	@POST
	@RequestMapping("/recv")
	public void  recv(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BufferedReader reader = req.getReader();
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		System.out.println("accept: " + sb.toString());
		final VoiceCallService voice = voiceCallService;
		MessageAnalyzer analyzer = new MessageAnalyzer(new MessageHandler() {
			/**
			 * 处理收到呼叫状态通知的回调方法
			 */
			@Override
			public void onRecvNotifyCallInformation(NotifyCallInformation information) {
				System.out.println("收到呼叫状态消息:");
				System.out.println("呼叫标识：" + information.getCallIdentifier());
				System.out.println("电话号码：" + information.getPhone());
				System.out.println("呼叫状态：" + information.getCallStatus().toString());
				System.out.println();
				// TODO
				if ("RINGING".equals(information.getCallStatus().toString())) {
					System.out.println(information.getPhoneID());
					String findByString = voice.findByString(information.getCallIdentifier(),information.getPhoneID());
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
							calltranExample.setAccount("sip:" + ResourcesUtil.getValue("http", "account") + "@ecplive.com"); // 设置账号,此处账号为格式为sip:xxxxxx@ecplive.com,与上面的makecall账号需一致
							calltranExample.setCallIdentifier(information.getCallIdentifier()); // 获取通话id（同步返回）status[1]
							calltranExample.setMaxDuration("0"); // 此处不能为空，0为不限制
							calltranExample.setMaxParticipants("10");
							calltranExample.setParticipantsPhoneID(findByString + "," + information.getPhoneID()); // 从接口[endpoint]日志获取，主被叫号码的phoneID，非电话号码
							calltranExample.setPrompted("0");
							calltranExample.setRecorded("1");
							calltranExample.setVoiceFile("");
							calltranExample.setStatusRequest(simpleRef);
							String response1 = calltranExample.invokeClient(true);
							System.out.println(response1);
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
	@RequestMapping(value="/recvto",produces = {ContentType.APPLICATION_JSON_UTF_8})
	//@Produces({ "application/json;charset=UTF-8"})
	@Consumes({MediaType.APPLICATION_JSON })
	@ResponseBody
	public void recvto(@RequestBody VoiceBack vb) throws Exception {
		System.out.println("-----------------------------");
		System.out.println(vb);
		System.out.println("-----------------------------");
		
	}
}
