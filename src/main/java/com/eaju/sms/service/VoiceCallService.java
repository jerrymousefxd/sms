package com.eaju.sms.service;

import com.eaju.sms.entity.VoiceCall;

public interface VoiceCallService {
	int insert(VoiceCall vc);
	String findByString(String example,String id);
	void update(int vc ,String callId);
	int updateVcByPhoneId(String phoneId1,String PhoneId2,String file);
}
