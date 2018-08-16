package com.ft.repository.impl;

import java.time.DayOfWeek;
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
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.domain.Sms;
import com.ft.repository.SmsCustomRepository;
import com.ft.service.dto.SmsDTO;
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

	@Override
	public List<Object> statsByCampaign(String campaignId) {
		Aggregation agg = Aggregation.newAggregation(
				Aggregation.match(
						Criteria.where("campaignId").is(campaignId)
				),
                Aggregation.group("state").count().as("cnt")
                );
		AggregationResults<Object> result = mongoTemplate.aggregate(agg, Sms.class, Object.class);
		return result.getMappedResults();
	}

	@Override
	public List<Object> stats(SmsDTO searchModel) {
		Aggregation agg = Aggregation.newAggregation(
				Aggregation.match( createCriteria(searchModel) ),
				Aggregation.project()
                .andExpression("dateToString('%Y-%m-%d', submitAt)").as("date"),
                Aggregation.group("date").count().as("cnt")
                );
		AggregationResults<Object> result = mongoTemplate.aggregate(agg, Sms.class, Object.class);
		return result.getMappedResults();
	}

	protected Criteria createCriteria(SmsDTO searchModel) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (searchModel.getCampaignId() != null) criteria.add(Criteria.where("campaignId").is(searchModel.getCampaignId()));
		if (searchModel.getSource() != null) criteria.add(Criteria.where("source").is(searchModel.getSource()));
		if (searchModel.getDestination() != null) criteria.add(Criteria.where("destination").is(searchModel.getDestination()));
		if (searchModel.getState() != null) criteria.add(Criteria.where("state").is(searchModel.getState()));
		if (searchModel.getCpId() != null) criteria.add(Criteria.where("cpId").is(searchModel.getCpId()));
		if (searchModel.getSpId() != null) criteria.add(Criteria.where("spId").is(searchModel.getSpId()));
		if (searchModel.getSubmitAt() != null) criteria.add(Criteria.where("submitAt").gte(searchModel.getSubmitAt()));
		if (searchModel.getExpiredAt() != null) criteria.add(Criteria.where("submitAt").lte(searchModel.getExpiredAt()));

		return (criteria.size() > 0) ? new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])) : new Criteria();

	}



}
