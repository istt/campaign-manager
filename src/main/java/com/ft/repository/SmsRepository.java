package com.ft.repository;

import com.ft.domain.QSms;
import com.ft.domain.Sms;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.StringPath;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Sms entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SmsRepository extends MongoRepository<Sms, String>, SmsCustomRepository,
QuerydslPredicateExecutor<Sms>, QuerydslBinderCustomizer<QSms> {

	List<Sms> findAllByCampaignIdAndStateLessThan(String campaignID, int state);

	Page<Sms> findAllByCampaignIdAndStateLessThan(String id, int i, Pageable pageable);

	Long deleteByStateAndCampaignId(int pending, String campaignId);

	@Override
	  default public void customize(QuerydslBindings bindings, QSms root) {
	    bindings
	    .bind(String.class)
	    .first((StringPath path, String value) -> path.containsIgnoreCase(value));

	    // FIXME: This won't work
	    bindings.bind(root.submitAt)
	    .first((DateTimePath<ZonedDateTime> path, ZonedDateTime value) -> path.after(value));

	    bindings.bind(root.expiredAt)
	    .first((DateTimePath<ZonedDateTime> path, ZonedDateTime value) -> path.before(value));
	  }

	Page<Sms> findAllByCampaignIdAndState(String id, int i, Pageable pageable);

	Page<Sms> findAllByCampaignIdAndStateLessThanAndStateGreaterThan(String id, int i, int j, Pageable pageable);

	Long countByCampaignIdAndState(String id, int i);

	Long countByCampaignId(String id);
}
