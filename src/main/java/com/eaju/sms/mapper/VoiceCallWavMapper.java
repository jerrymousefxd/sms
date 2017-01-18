package com.eaju.sms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.eaju.sms.entity.VoiceCallWav;
import com.eaju.sms.entity.VoiceCallWavExample;

public interface VoiceCallWavMapper {
    int countByExample(VoiceCallWavExample example);

    int deleteByExample(VoiceCallWavExample example);

    int deleteByPrimaryKey(String callidentifier);

    int insert(VoiceCallWav record);

    int insertSelective(VoiceCallWav record);

    List<VoiceCallWav> selectByExample(VoiceCallWavExample example);

    VoiceCallWav selectByPrimaryKey(String callidentifier);

    int updateByExampleSelective(@Param("record") VoiceCallWav record, @Param("example") VoiceCallWavExample example);

    int updateByExample(@Param("record") VoiceCallWav record, @Param("example") VoiceCallWavExample example);

    int updateByPrimaryKeySelective(VoiceCallWav record);

    int updateByPrimaryKey(VoiceCallWav record);
    
    int updateVoiceCallWav(VoiceCallWav record);
}