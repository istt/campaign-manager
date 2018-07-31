package com.ft.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the VasCloudConfiguration entity.
 */
public class VasCloudConfigurationDTO implements Serializable {

    private String id;

    private String endPoint;

    private String username;

    private String password;

    private String serviceCode;

    private String cpCode;

    private String cpCharge;

    private String serviceId;

    private String packageCode;

    private String shortCode;

    private Long rateLimit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getCpCode() {
        return cpCode;
    }

    public void setCpCode(String cpCode) {
        this.cpCode = cpCode;
    }

    public String getCpCharge() {
        return cpCharge;
    }

    public void setCpCharge(String cpCharge) {
        this.cpCharge = cpCharge;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public Long getRateLimit() {
		return rateLimit;
	}

	public void setRateLimit(Long rateLimit) {
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

        VasCloudConfigurationDTO vasCloudConfigurationDTO = (VasCloudConfigurationDTO) o;
        if (vasCloudConfigurationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vasCloudConfigurationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VasCloudConfigurationDTO{" +
            "id=" + getId() +
            ", endPoint='" + getEndPoint() + "'" +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", serviceCode='" + getServiceCode() + "'" +
            ", cpCode='" + getCpCode() + "'" +
            ", cpCharge='" + getCpCharge() + "'" +
            ", serviceId='" + getServiceId() + "'" +
            "}";
    }
}
