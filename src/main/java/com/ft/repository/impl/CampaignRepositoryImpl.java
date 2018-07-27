package com.ft.repository.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.domain.Campaign;
import com.ft.repository.CampaignCustomRepository;

public class CampaignRepositoryImpl implements CampaignCustomRepository {

	private final Logger log = LoggerFactory.getLogger(CampaignRepositoryImpl.class);

	@Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    ObjectMapper mapper;

	@Override
	public List<Campaign> findAllPendingCampaign() {
		Query query = new Query().addCriteria(createPendingCriteria());
		log.debug("Query: " + query);
		return mongoTemplate.find(query, Campaign.class);
	}

	@Override
	public Page<Campaign> findAllPendingCampaign(Pageable pageable) {
		Query query = new Query().addCriteria(createPendingCriteria());
		return new PageImpl<Campaign>(mongoTemplate.find(query.with(pageable), Campaign.class), pageable, mongoTemplate.count(query, Campaign.class));
	}

	protected Criteria createPendingCriteria() {
		List<Criteria> criteria = new ArrayList<Criteria>();
		criteria.add(Criteria.where("state").is(1)); // Only select those are approved
		ZonedDateTime now = ZonedDateTime.now();
		log.debug("ZonedDateTime.now(): " + now);
			criteria.add(Criteria.where("start_at").lte(now));
			criteria.add(Criteria.where("expired_at").gte(now));
		DayOfWeek thisWeekday = now.toLocalDate().getDayOfWeek();
		log.debug("Day of week: " + thisWeekday + " : " + thisWeekday.getValue());
			criteria.add(Criteria.where("working_weekdays").is(thisWeekday.getValue()));
		int thisHour = now.toLocalDateTime().getHour();
		log.debug("This Hour: " + thisHour);
			criteria.add(Criteria.where("working_hours").is(thisHour));
//		LocalDate today = now.toLocalDate();
//		log.debug("Today: " + today);
//			TODO: Add criteria for Holiday
//			criteria.add(Criteria.where("working_days").nin(today));

		return (criteria.size() > 0) ? new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])) : new Criteria();

	}

}
