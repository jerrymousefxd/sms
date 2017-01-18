package com.eaju.sms.entity;

import java.util.Date;

public class VoiceCallWav {
    private String callidentifier;

    private String participantphoneid1;

    private String participantphoneid2;

    private String starttime;

    private String endtime;

    private String creator;

    private Date createtime;

    private String updator;

    private Date updatetime;

    private String status;

    public String getCallidentifier() {
        return callidentifier;
    }

    public void setCallidentifier(String callidentifier) {
        this.callidentifier = callidentifier == null ? null : callidentifier.trim();
    }

    public String getParticipantphoneid1() {
        return participantphoneid1;
    }

    public void setParticipantphoneid1(String participantphoneid1) {
        this.participantphoneid1 = participantphoneid1 == null ? null : participantphoneid1.trim();
    }

    public String getParticipantphoneid2() {
        return participantphoneid2;
    }

    public void setParticipantphoneid2(String participantphoneid2) {
        this.participantphoneid2 = participantphoneid2 == null ? null : participantphoneid2.trim();
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime == null ? null : starttime.trim();
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime == null ? null : endtime.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator == null ? null : updator.trim();
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}