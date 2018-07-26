package com.ft.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.ft.service.dto.VasCloudConfigurationDTO;

/**
 * Properties specific to Campaign Manager.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

	private VasCloudConfigurationDTO vasCloud;

	public VasCloudConfigurationDTO getVasCloud() {
		return vasCloud;
	}

	public void setVasCloud(VasCloudConfigurationDTO vasCloud) {
		this.vasCloud = vasCloud;
	}


}
