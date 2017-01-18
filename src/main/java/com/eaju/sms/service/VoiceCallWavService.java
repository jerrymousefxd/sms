package com.eaju.sms.service;

import com.eaju.sms.entity.VoiceCallWav;

public interface VoiceCallWavService {
	int insertOrUpdate(VoiceCallWav vcw);
	VoiceCallWav selectByPrimaryKey(String callidentifier);
}
