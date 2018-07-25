package com.ft.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Campaign entity.
 */
public class CampaignDTO implements Serializable {

    private String id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    @Size(max = 80)
    private String code;

    private String externalId;

    @NotNull
    private String channel;

    private Integer state;

    @NotNull
    @Size(max = 40)
    private String shortCode;

    private String callbackUrl;

    private String createdBy;

    private ZonedDateTime createdAt;

    private String approvedBy;

    private ZonedDateTime approvedAt;

    private String shortMsg;

    private byte[] msisdnList;
    private String msisdnListContentType;

    private ZonedDateTime startAt;

    private ZonedDateTime expiredAt;

    private String workingHours;

    private String workingWeekdays;

    private String workingDays;

    private String spSvc;

    private String spId;

    private String cpId;

    private Integer msgQuota;

    private Integer subQuota;

    private Double rateLimit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public ZonedDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(ZonedDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public String getShortMsg() {
        return shortMsg;
    }

    public void setShortMsg(String shortMsg) {
        this.shortMsg = shortMsg;
    }

    public byte[] getMsisdnList() {
        return msisdnList;
    }

    public void setMsisdnList(byte[] msisdnList) {
        this.msisdnList = msisdnList;
    }

    public String getMsisdnListContentType() {
        return msisdnListContentType;
    }

    public void setMsisdnListContentType(String msisdnListContentType) {
        this.msisdnListContentType = msisdnListContentType;
    }

    public ZonedDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(ZonedDateTime startAt) {
        this.startAt = startAt;
    }

    public ZonedDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(ZonedDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public String getWorkingWeekdays() {
        return workingWeekdays;
    }

    public void setWorkingWeekdays(String workingWeekdays) {
        this.workingWeekdays = workingWeekdays;
    }

    public String getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(String workingDays) {
        this.workingDays = workingDays;
    }

    public String getSpSvc() {
        return spSvc;
    }

    public void setSpSvc(String spSvc) {
        this.spSvc = spSvc;
    }

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public String getCpId() {
        return cpId;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    public Integer getMsgQuota() {
        return msgQuota;
    }

    public void setMsgQuota(Integer msgQuota) {
        this.msgQuota = msgQuota;
    }

    public Integer getSubQuota() {
        return subQuota;
    }

    public void setSubQuota(Integer subQuota) {
        this.subQuota = subQuota;
    }

    public Double getRateLimit() {
        return rateLimit;
    }

    public void setRateLimit(Double rateLimit) {
        this.rateLimit = rateLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CampaignDTO campaignDTO = (CampaignDTO) o;
        if (campaignDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), campaignDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CampaignDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", code='" + getCode() + "'" +
            ", externalId='" + getExternalId() + "'" +
            ", channel='" + getChannel() + "'" +
            ", state=" + getState() +
            ", shortCode='" + getShortCode() + "'" +
            ", callbackUrl='" + getCallbackUrl() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", approvedBy='" + getApprovedBy() + "'" +
            ", approvedAt='" + getApprovedAt() + "'" +
            ", shortMsg='" + getShortMsg() + "'" +
            ", msisdnList='" + getMsisdnList() + "'" +
            ", startAt='" + getStartAt() + "'" +
            ", expiredAt='" + getExpiredAt() + "'" +
            ", workingHours='" + getWorkingHours() + "'" +
            ", workingWeekdays='" + getWorkingWeekdays() + "'" +
            ", workingDays='" + getWorkingDays() + "'" +
            ", spSvc='" + getSpSvc() + "'" +
            ", spId='" + getSpId() + "'" +
            ", cpId='" + getCpId() + "'" +
            ", msgQuota=" + getMsgQuota() +
            ", subQuota=" + getSubQuota() +
            ", rateLimit=" + getRateLimit() +
            "}";
    }
}
