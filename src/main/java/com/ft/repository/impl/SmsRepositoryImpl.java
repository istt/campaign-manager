package com.ft.repository.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.domain.Sms;
import com.ft.repository.SmsCustomRepository;
import com.mongodb.WriteResult;
import com.mongodb.client.result.UpdateResult;

public class SmsRepositoryImpl implements SmsCustomRepository {

	private final Logger log = LoggerFactory.getLogger(SmsRepositoryImpl.class);

	@Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    ObjectMapper mapper;

	@Override
	public List<Sms> findAllPendingSms() {
		Query query = new Query().addCriteria(createPendingCriteria());
		log.debug("Query: " + query);
		return mongoTemplate.find(query, Sms.class);
	}

	@Override
	public Page<Sms> findAllPendingSms(Pageable pageable) {
		Query query = new Query().addCriteria(createPendingCriteria());
		return new PageImpl<Sms>(mongoTemplate.find(query.with(pageable), Sms.class), pageable, mongoTemplate.count(query, Sms.class));
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

	@Override
	public long setExpiredSms() {
		UpdateResult result = mongoTemplate.updateMulti(new Query(
				new Criteria().andOperator(
						Criteria.where("expired_at").lt(ZonedDateTime.now()),
						Criteria.where("state").lte(0),
						Criteria.where("state").gt(-9)
				)
				), Update.update("state", -9),
				Sms.class);
		return result.getModifiedCount();
	}

	@Override
	public List<Object> statsByCampaignAndState(String campaignId, int state) {
		Aggregation agg = Aggregation.newAggregation(
				Aggregation.match(
						new Criteria().andOperator(
								Criteria.where("campaignId").is(campaignId),
								Criteria.where("state").is(state)
						)
				),
				Aggregation.project()
                .andExpression("dateToString('%Y-%m-%d', submitAt)").as("date"),
                Aggregation.group("date").count().as("cnt")
                );
		AggregationResults<Object> result = mongoTemplate.aggregate(agg, Sms.class, Object.class);
		return result.getMappedResults();
	}

}
