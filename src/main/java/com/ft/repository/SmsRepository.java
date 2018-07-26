package com.ft.repository;

import com.ft.domain.Sms;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Sms entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SmsRepository extends MongoRepository<Sms, String> {

	List<Sms> findAllByCampaignIdAndStateLessThan(String campaignID, int state);

}
