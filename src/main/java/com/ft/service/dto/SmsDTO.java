package com.ft.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Sms entity.
 */
public class SmsDTO implements Serializable {

    private String id;

    private String source;

    private String destination;

    private Integer state;

    private String shortMsg;

    private String campaignId;

    private String cpId;

    private String spId;

    private String spSvc;

    private ZonedDateTime submitAt;

    private ZonedDateTime expiredAt;

    private Object submitRequestPayload;

    private Object submitResponsePayload;

    private ZonedDateTime deliveredAt;

    private Object deliveryReportPayload;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getShortMsg() {
		return shortMsg;
	}

	public void setShortMsg(String shortMsg) {
		this.shortMsg = shortMsg;
	}

	public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getCpId() {
        return cpId;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public String getSpSvc() {
        return spSvc;
    }

    public void setSpSvc(String spSvc) {
        this.spSvc = spSvc;
    }

    public ZonedDateTime getSubmitAt() {
        return submitAt;
    }

    public void setSubmitAt(ZonedDateTime submitAt) {
        this.submitAt = submitAt;
    }

    public ZonedDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(ZonedDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public Object getSubmitRequestPayload() {
        return submitRequestPayload;
    }

    public void setSubmitRequestPayload(Object submitRequestPayload) {
        this.submitRequestPayload = submitRequestPayload;
    }

    public Object getSubmitResponsePayload() {
        return submitResponsePayload;
    }

    public void setSubmitResponsePayload(Object submitResponsePayload) {
        this.submitResponsePayload = submitResponsePayload;
    }

    public ZonedDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(ZonedDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public Object getDeliveryReportPayload() {
        return deliveryReportPayload;
    }

    public void setDeliveryReportPayload(Object deliveryReportPayload) {
        this.deliveryReportPayload = deliveryReportPayload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SmsDTO smsDTO = (SmsDTO) o;
        if (smsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmsDTO{" +
            "id=" + getId() +
            ", source='" + getSource() + "'" +
            ", destination='" + getDestination() + "'" +
            ", state=" + getState() +
            ", shortMsg=" + getShortMsg() +
            ", campaignId='" + getCampaignId() + "'" +
            ", cpId='" + getCpId() + "'" +
            ", spId='" + getSpId() + "'" +
            ", spSvc='" + getSpSvc() + "'" +
            ", submitAt='" + getSubmitAt() + "'" +
            ", expiredAt='" + getExpiredAt() + "'" +
            ", submitRequestPayload='" + getSubmitRequestPayload() + "'" +
            ", submitResponsePayload='" + getSubmitResponsePayload() + "'" +
            ", deliveredAt='" + getDeliveredAt() + "'" +
            ", deliveryReportPayload='" + getDeliveryReportPayload() + "'" +
            "}";
    }
}
