package com.eaju.sms.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eaju.sms.entity.VoiceCallWav;
import com.eaju.sms.entity.VoiceCallWavExample;
import com.eaju.sms.entity.VoiceCallWavExample.Criteria;
import com.eaju.sms.mapper.VoiceCallWavMapper;
import com.eaju.sms.service.VoiceCallWavService;

@Transactional
@Service
public class VoiceCallWavServiceImpl implements VoiceCallWavService {
	@Autowired
	private VoiceCallWavMapper voiceCallWavMapper;
	public int insertOrUpdate(VoiceCallWav vcw) {
		// TODO Auto-generated method stub
		VoiceCallWavExample vcwe=new VoiceCallWavExample();
		Criteria c = vcwe.createCriteria();
		c.andCallidentifierEqualTo(vcw.getCallidentifier());
		List<VoiceCallWav> list = voiceCallWavMapper.selectByExample(vcwe);
		int insert;
		if(list.size()==0){
			insert = voiceCallWavMapper.insert(vcw);
		}else{
			VoiceCallWav voiceCallWav = list.get(0);
			if(StringUtils.isNotBlank(voiceCallWav.getParticipantphoneid1())){
				vcw.setParticipantphoneid2(vcw.getParticipantphoneid1());
				vcw.setParticipantphoneid1(null);
			}
			insert=voiceCallWavMapper.updateVoiceCallWav(vcw);
		}
		return insert;
	}
	public VoiceCallWav selectByPrimaryKey(String callidentifier) {
		// TODO Auto-generated method stub
		return voiceCallWavMapper.selectByPrimaryKey(callidentifier);
	}
	
}
