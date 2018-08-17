package com.ft.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import org.mongodb.morphia.annotations.Entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import javax.validation.constraints.NotNull;

/**
 * The actual SMS messages to send out
 */
@ApiModel(description = "The actual SMS messages to send out")
@Document(collection = "sms")
@Entity
public class Sms implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    /**
     * Source Address
     */
    @ApiModelProperty(value = "Source Address")
    @Field("source")
    @NotNull
    @Indexed
    private String source;

    /**
     * Destination Address
     */
    @ApiModelProperty(value = "Destination Address")
    @Field("destination")
    @NotNull
    @Indexed
    private String destination;

    /**
     * state code, 0 = Pending, 1 = Submitted, 9 = Success, -1 - -8 = Failed, -9 = Do not retry anymore
     */
    @ApiModelProperty(value = "state code, 0 = Pending, 1 = Submitted, 9 = Success, -1 - -8 = Failed, -9 = Do not retry anymore")
    @Field("state")
    @Indexed
    private Integer state;

    /**
     * First SMS or USSD messages to send to customers
     */
    @ApiModelProperty(value = "First SMS or USSD messages to send to customers")
    @Field("short_msg")
    @NotNull
    private String shortMsg;

    /**
     * Related Campaign
     */
    @ApiModelProperty(value = "Related Campaign")
    @Field("campaign_id")
    @Indexed
    private String campaignId;

    /**
     * Management Metadata
     */
    @ApiModelProperty(value = "Management Metadata")
    @Field("cp_id")
    private String cpId;

    @Field("sp_id")
    private String spId;

    @Field("sp_svc")
    private String spSvc;

    /**
     * Sending process begin to send if NOW() > submitAt
     */
    @ApiModelProperty(value = "Sending process begin to send if NOW() > submitAt")
    @Field("submit_at")
    private ZonedDateTime submitAt;

    /**
     * Sending process ignore this message if NOW() > expiredAt
     */
    @ApiModelProperty(value = "Sending process ignore this message if NOW() > expiredAt")
    @Field("expired_at")
    private ZonedDateTime expiredAt;

    /**
     * Last Submission payload
     */
    @ApiModelProperty(value = "Last Submission payload")
    @Field("submit_request_payload")
    private Object submitRequestPayload;

    /**
     * Last Result from submission
     */
    @ApiModelProperty(value = "Last Result from submission")
    @Field("submit_response_payload")
    private Object submitResponsePayload;

    /**
     * Did remote endpoint response back?
     */
    @ApiModelProperty(value = "Did remote endpoint response back?")
    @Field("delivered_at")
    private ZonedDateTime deliveredAt;

    /**
     * Delivery Report payload
     */
    @ApiModelProperty(value = "Delivery Report payload")
    @Field("delivery_report_payload")
    private Object deliveryReportPayload;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public Sms source(String source) {
        this.source = source;
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public Sms destination(String destination) {
        this.destination = destination;
        return this;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getShortMsg() {
        return shortMsg;
    }

    public Sms shortMsg(String shortMsg) {
        this.shortMsg = shortMsg;
        return this;
    }

    public void setShortMsg(String shortMsg) {
        this.shortMsg = shortMsg;
    }

    public Integer getState() {
        return state;
    }

    public Sms state(Integer state) {
        this.state = state;
        return this;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public Sms campaignId(String campaignId) {
        this.campaignId = campaignId;
        return this;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getCpId() {
        return cpId;
    }

    public Sms cpId(String cpId) {
        this.cpId = cpId;
        return this;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    public String getSpId() {
        return spId;
    }

    public Sms spId(String spId) {
        this.spId = spId;
        return this;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public String getSpSvc() {
        return spSvc;
    }

    public Sms spSvc(String spSvc) {
        this.spSvc = spSvc;
        return this;
    }

    public void setSpSvc(String spSvc) {
        this.spSvc = spSvc;
    }

    public ZonedDateTime getSubmitAt() {
        return submitAt;
    }

    public Sms submitAt(ZonedDateTime submitAt) {
        this.submitAt = submitAt;
        return this;
    }

    public void setSubmitAt(ZonedDateTime submitAt) {
        this.submitAt = submitAt;
    }

    public ZonedDateTime getExpiredAt() {
        return expiredAt;
    }

    public Sms expiredAt(ZonedDateTime expiredAt) {
        this.expiredAt = expiredAt;
        return this;
    }

    public void setExpiredAt(ZonedDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public Object getSubmitRequestPayload() {
        return submitRequestPayload;
    }

    public Sms submitRequestPayload(Object submitRequestPayload) {
        this.submitRequestPayload = submitRequestPayload;
        return this;
    }

    public void setSubmitRequestPayload(Object submitRequestPayload) {
        this.submitRequestPayload = submitRequestPayload;
    }

    public Object getSubmitResponsePayload() {
        return submitResponsePayload;
    }

    public Sms submitResponsePayload(Object submitResponsePayload) {
        this.submitResponsePayload = submitResponsePayload;
        return this;
    }

    public void setSubmitResponsePayload(Object submitResponsePayload) {
        this.submitResponsePayload = submitResponsePayload;
    }

    public ZonedDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public Sms deliveredAt(ZonedDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
        return this;
    }

    public void setDeliveredAt(ZonedDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public Object getDeliveryReportPayload() {
        return deliveryReportPayload;
    }

    public Sms deliveryReportPayload(Object deliveryReportPayload) {
        this.deliveryReportPayload = deliveryReportPayload;
        return this;
    }

    public void setDeliveryReportPayload(Object deliveryReportPayload) {
        this.deliveryReportPayload = deliveryReportPayload;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sms sms = (Sms) o;
        if (sms.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sms.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Sms{" +
            "id=" + getId() +
            ", source='" + getSource() + "'" +
            ", destination='" + getDestination() + "'" +
            ", state=" + getState() +
            ", shortMsg='" + getShortMsg() + "'" +
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
