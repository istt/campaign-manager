package com.ft.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ft.domain.Campaign;

public interface CampaignCustomRepository {

	List<Campaign> findAllPendingCampaign();

	Page<Campaign> findAllPendingCampaign(Pageable pageable);

	long setExpiredCampaign();

	long setFinishCampaign();

	long setRunningCampaign();


}
