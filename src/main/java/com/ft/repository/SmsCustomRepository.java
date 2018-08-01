package com.ft.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ft.domain.Sms;

public interface SmsCustomRepository {

	List<Sms> findAllPendingSms();

	Page<Sms> findAllPendingSms(Pageable pageable);

	long setExpiredSms();

	List<Object> statsByCampaignAndState(String campaignId, int state);

}
