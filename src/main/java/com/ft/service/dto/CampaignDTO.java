package com.ft.service.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;

import com.ft.domain.Campaign;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A DTO for the Campaign entity.
 */
public class CampaignDTO implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 8819719132188767088L;

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

    private ZonedDateTime startAt;

    private ZonedDateTime expiredAt;

    private List<Integer> workingHours;

    private List<Integer> workingWeekdays;

    private List<LocalDate> holidays;

    private String spSvc;

    private String spId;

    private String cpId;

    private Integer msgQuota;

    private Integer subQuota;

    private Double rateLimit;

    private Map<String,Object> cfg = new ConcurrentHashMap<String, Object>();

    private Map<String,Object> stats = new ConcurrentHashMap<String, Object>();

    private List<DataFileDTO> datafiles;

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

    public List<Integer> getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(List<Integer> workingHours) {
        this.workingHours = workingHours;
    }

    public List<Integer> getWorkingWeekdays() {
        return workingWeekdays;
    }

    public void setWorkingWeekdays(List<Integer> workingWeekdays) {
        this.workingWeekdays = workingWeekdays;
    }

    public List<LocalDate> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<LocalDate> holidays) {
        this.holidays = holidays;
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

    public Map<String, Object> getCfg() {
        return cfg;
    }

    public void setCfg(Map<String, Object> cfg) {
        this.cfg = cfg;
    }

    public Map<String, Object> getStats() {
		return stats;
	}

	public void setStats(Map<String, Object> stats) {
		this.stats = stats;
	}

	public List<DataFileDTO> getDatafiles() {
		return datafiles;
	}

	public void setDatafiles(List<DataFileDTO> datafiles) {
		this.datafiles = datafiles;
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
            ", startAt='" + getStartAt() + "'" +
            ", expiredAt='" + getExpiredAt() + "'" +
            ", workingHours='" + getWorkingHours() + "'" +
            ", workingWeekdays='" + getWorkingWeekdays() + "'" +
            ", holidays='" + getHolidays() + "'" +
            ", spSvc='" + getSpSvc() + "'" +
            ", spId='" + getSpId() + "'" +
            ", cpId='" + getCpId() + "'" +
            ", msgQuota=" + getMsgQuota() +
            ", subQuota=" + getSubQuota() +
            ", rateLimit=" + getRateLimit() +
            "}";
    }
}
