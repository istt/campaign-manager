package com.ft.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the DataFile entity.
 */
public class DataFileSmDTO implements Serializable {

    private String id;

    private String name;

    private String tags;

    private ZonedDateTime uploadAt;

    private ZonedDateTime processAt;

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

    public void setName(String name) {
        this.name = name;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public ZonedDateTime getUploadAt() {
		return uploadAt;
	}

	public void setUploadAt(ZonedDateTime uploadAt) {
		this.uploadAt = uploadAt;
	}

	public ZonedDateTime getProcessAt() {
		return processAt;
	}

	public void setProcessAt(ZonedDateTime processAt) {
		this.processAt = processAt;
	}

	public List<String> getDataCsvHeaders() {
		return dataCsvHeaders;
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

        DataFileSmDTO dataFileDTO = (DataFileSmDTO) o;
        if (dataFileDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dataFileDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DataFileDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", tags='" + getTags() + "'" +
            ", dataCsvHeaders='" + getDataCsvHeaders() + "'" +
            ", uploadAt='" + getUploadAt() + "'" +
            ", processAt='" + getProcessAt() + "'" +
            "}";
    }
}
