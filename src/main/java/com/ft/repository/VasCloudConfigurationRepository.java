package com.ft.repository;

import com.ft.domain.VasCloudConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the VasCloudConfiguration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VasCloudConfigurationRepository extends MongoRepository<VasCloudConfiguration, String> {

}
