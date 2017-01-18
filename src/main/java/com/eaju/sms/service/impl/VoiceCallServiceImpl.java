package com.eaju.sms.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eaju.sms.controller.VoiceCallController;
import com.eaju.sms.entity.VoiceCall;
import com.eaju.sms.entity.VoiceCallExample;
import com.eaju.sms.entity.VoiceCallExample.Criteria;
import com.eaju.sms.mapper.VoiceCallMapper;
import com.eaju.sms.service.VoiceCallService;

@Transactional
@Service
public class VoiceCallServiceImpl implements VoiceCallService {
	@Autowired
	private VoiceCallMapper voiceCallMapper;

	private final static Logger logger = LoggerFactory.getLogger(VoiceCallServiceImpl.class);
	public String findByString(String example, String id) {
		VoiceCallExample exa = new VoiceCallExample();
		Criteria createCriteria = exa.createCriteria();
		createCriteria.andCallidentifierEqualTo(example);
		List<VoiceCall> selectByExample = voiceCallMapper.selectByExample(exa);
		if (selectByExample.size() == 0)
			return null;
		if (StringUtils.isBlank(selectByExample.get(0).getPhoneid1())) {
			selectByExample.get(0).setPhoneid1(id);
			voiceCallMapper.updateByExample(selectByExample.get(0), exa);
			return null;
		}
		selectByExample.get(0).setPhoneid2(id);
		voiceCallMapper.updateByExample(selectByExample.get(0), exa);
		return selectByExample.get(0).getPhoneid1();

	}
	public int insert(VoiceCall vc) {
		// TODO Auto-generated method stub
		logger.info(vc.getPhonenumberfrom());
		logger.info(vc.getPhonenumberto());
		if(StringUtils.isBlank(vc.getPhonenumberfrom())||StringUtils.isBlank(vc.getPhonenumberto())){
			return 0;
		}
		voiceCallMapper.insert(vc);
		return vc.getId().intValue();
	}
	public void update(int id, String callId) {
		// TODO Auto-generated method stub
		VoiceCall vc=new VoiceCall();
		vc.setCallidentifier(callId);
		vc.setId(new BigDecimal(id));
		voiceCallMapper.updateByPrimaryKeySelective(vc);
	}

}
