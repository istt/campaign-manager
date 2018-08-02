package com.ft.domain;

import org.mongodb.morphia.annotations.Entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import io.swagger.annotations.ApiModelProperty;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/**
 * A DataFile.
 */
@Document(collection = "data_file")
@Entity
public class DataFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("tags")
    private String tags;

    @Field("data")
    private byte[] data;

    @Field("data_content_type")
    private String dataContentType;

    @ApiModelProperty(value = "Time when file is uploaded")
    @Field("upload_at")
    private ZonedDateTime uploadAt;

    @ApiModelProperty(value = "Time when file is process")
    @Field("process_at")
    private ZonedDateTime processAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Field("data_csv_headers")
    private List<String> dataCsvHeaders;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public DataFile name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTags() {
        return tags;
    }

    public DataFile tags(String tags) {
        this.tags = tags;
        return this;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public byte[] getData() {
        return data;
    }

    public DataFile data(byte[] data) {
        this.data = data;
        return this;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getDataContentType() {
        return dataContentType;
    }

    public DataFile dataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
        return this;
    }

    public void setDataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
    }

    public ZonedDateTime getUploadAt() {
		return uploadAt;
	}

    public DataFile uploadAt(ZonedDateTime uploadAt) {
		this.uploadAt = uploadAt;
		return this;
	}

	public void setUploadAt(ZonedDateTime uploadAt) {
		this.uploadAt = uploadAt;
	}

	public ZonedDateTime getProcessAt() {
		return processAt;
	}

	public DataFile processAt(ZonedDateTime processAt) {
		this.processAt = processAt;
		return this;
	}

	public void setProcessAt(ZonedDateTime processAt) {
		this.processAt = processAt;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

	public List<String> getDataCsvHeaders() {
		return dataCsvHeaders;
	}

	public DataFile dataCsvHeaders(List<String> dataCsvHeaders) {
		this.dataCsvHeaders = dataCsvHeaders;
		return this;
	}

	public void setDataCsvHeaders(List<String> dataCsvHeaders) {
		this.dataCsvHeaders = dataCsvHeaders;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataFile dataFile = (DataFile) o;
        if (dataFile.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dataFile.getId());
    }

	@Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DataFile{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", tags='" + getTags() + "'" +
            ", data='" + getData() + "'" +
            ", dataContentType='" + getDataContentType() + "'" +
            "}";
    }
}
