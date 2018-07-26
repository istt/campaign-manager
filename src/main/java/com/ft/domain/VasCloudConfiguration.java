package com.ft.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Objects;

/**
 * A VasCloudConfiguration.
 */
@Document(collection = "vas_cloud_configuration")
public class VasCloudConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("end_point")
    private String endPoint;

    @Field("username")
    private String username;

    @Field("password")
    private String password;

    @Field("service_code")
    private String serviceCode;

    @Field("cp_code")
    private String cpCode;

    @Field("cp_charge")
    private String cpCharge;

    @Field("service_id")
    private String serviceId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public VasCloudConfiguration endPoint(String endPoint) {
        this.endPoint = endPoint;
        return this;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getUsername() {
        return username;
    }

    public VasCloudConfiguration username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public VasCloudConfiguration password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public VasCloudConfiguration serviceCode(String serviceCode) {
        this.serviceCode = serviceCode;
        return this;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getCpCode() {
        return cpCode;
    }

    public VasCloudConfiguration cpCode(String cpCode) {
        this.cpCode = cpCode;
        return this;
    }

    public void setCpCode(String cpCode) {
        this.cpCode = cpCode;
    }

    public String getCpCharge() {
        return cpCharge;
    }

    public VasCloudConfiguration cpCharge(String cpCharge) {
        this.cpCharge = cpCharge;
        return this;
    }

    public void setCpCharge(String cpCharge) {
        this.cpCharge = cpCharge;
    }

    public String getServiceId() {
        return serviceId;
    }

    public VasCloudConfiguration serviceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
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
        VasCloudConfiguration vasCloudConfiguration = (VasCloudConfiguration) o;
        if (vasCloudConfiguration.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vasCloudConfiguration.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VasCloudConfiguration{" +
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
