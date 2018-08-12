package com.ft.repository;

import com.ft.domain.Campaign;
import com.ft.domain.QCampaign;
import com.querydsl.core.types.dsl.StringPath;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Campaign entity.
 */
@Repository
public interface CampaignRepository extends MongoRepository<Campaign, String>,
	CampaignCustomRepository,
	QuerydslPredicateExecutor<Campaign>, QuerydslBinderCustomizer<QCampaign> {

	@Override
	  default public void customize(QuerydslBindings bindings, QCampaign root) {

	    bindings
	    .bind(String.class)
	    .first((StringPath path, String value) -> path.containsIgnoreCase(value));
	    bindings.excluding(root.cfg);
	    bindings.excluding(root.stats);
	  }

	public List<Campaign> findAllByState(int campaignState);
}
