package com.ft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ft.domain.Campaign;
import com.ft.repository.SmsRepository;
import com.ft.security.SecurityUtils;
import com.ft.service.CampaignService;
import com.ft.web.rest.errors.BadRequestAlertException;
import com.ft.web.rest.util.HeaderUtil;
import com.ft.web.rest.util.PaginationUtil;
import com.querydsl.core.types.Predicate;
import com.ft.service.dto.CampaignDTO;
import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Campaign.
 */
@RestController
@RequestMapping("/api")
public class CampaignResource {

    private final Logger log = LoggerFactory.getLogger(CampaignResource.class);

    private static final String ENTITY_NAME = "campaign";

    private final CampaignService campaignService;

    public CampaignResource(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    /**
     * POST  /campaigns : Create a new campaign.
     *
     * @param campaignDTO the campaignDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new campaignDTO, or with status 400 (Bad Request) if the campaign has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/campaigns")
    @Timed
    public ResponseEntity<CampaignDTO> createCampaign(@Valid @RequestBody CampaignDTO campaignDTO) throws URISyntaxException {
        log.debug("REST request to save Campaign : {}", campaignDTO);
        if (campaignDTO.getId() != null) {
            throw new BadRequestAlertException("A new campaign cannot already have an ID", ENTITY_NAME, "idexists");
        }
        campaignDTO.setCreatedAt(ZonedDateTime.now());
        campaignDTO.setCreatedBy(SecurityUtils.getCurrentUserLogin().get());

        CampaignDTO result = campaignService.save(campaignDTO);
        // Process file uploading if need
        if (campaignDTO.getDatafiles() != null) {
        	result = campaignService.processDatafiles(result);
        }
        return ResponseEntity.created(new URI("/api/campaigns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /campaigns : Updates an existing campaign.
     *
     * @param campaignDTO the campaignDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated campaignDTO,
     * or with status 400 (Bad Request) if the campaignDTO is not valid,
     * or with status 500 (Internal Server Error) if the campaignDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/campaigns")
    @Timed
    public ResponseEntity<CampaignDTO> updateCampaign(@Valid @RequestBody CampaignDTO campaignDTO) throws URISyntaxException {
        log.debug("REST request to update Campaign : {}", campaignDTO);
        if (campaignDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CampaignDTO result = campaignService.save(campaignDTO);
     // Process file uploading if need
        if (campaignDTO.getDatafiles() != null) {
        	result = campaignService.processDatafiles(result);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, campaignDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /campaigns : get all the campaigns.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of campaigns in body
     */
    @GetMapping("/campaigns")
    @Timed
    public ResponseEntity<List<CampaignDTO>> getAllCampaigns(Pageable pageable) {
        log.debug("REST request to get a page of Campaigns");
        Page<CampaignDTO> page = campaignService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/campaigns");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /campaigns/:id : get the "id" campaign.
     *
     * @param id the id of the campaignDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the campaignDTO, or with status 404 (Not Found)
     */
    @GetMapping("/campaigns/{id}")
    @Timed
    public ResponseEntity<CampaignDTO> getCampaign(@PathVariable String id) {
        log.debug("REST request to get Campaign : {}", id);
        Optional<CampaignDTO> campaignDTO = campaignService.findOne(id);
        if (campaignDTO.isPresent()) {
        	if ((campaignDTO.get().getState() != null ) && (campaignDTO.get().getState() == 1 ) && campaignDTO.get().getExpiredAt().isAfter(ZonedDateTime.now())) {
        		campaignDTO.get().getStats().put("successStats", smsRepo.statsByCampaignAndState(id, 9));
        		campaignDTO.get().getStats().put("failedStats", smsRepo.statsByCampaignAndState(id,  -9));
        		campaignDTO.get().getStats().put("pendingStats", smsRepo.statsByCampaignAndState(id,  0));
        		campaignDTO.get().setState(9);
        		campaignService.save(campaignDTO.get());
        	}
        }
        return ResponseUtil.wrapOrNotFound(campaignDTO);
    }

    /**
     * DELETE  /campaigns/:id : delete the "id" campaign.
     *
     * @param id the id of the campaignDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/campaigns/{id}")
    @Timed
    public ResponseEntity<Void> deleteCampaign(@PathVariable String id) {
        log.debug("REST request to delete Campaign : {}", id);
        campaignService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * GET  /campaigns/:id/import : get the "id" campaign.
     *
     * @param id the id of the campaignDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the campaignDTO, or with status 404 (Not Found)
     */
    @PutMapping("/campaigns/{id}/import")
    @Timed
    public ResponseEntity<CampaignDTO> triggerCampaignImport(@PathVariable String id) {
        log.debug("REST request to get Campaign : {}", id);
        Optional<CampaignDTO> campaignDTO = campaignService.findOne(id);
        return ResponseEntity.accepted().body(campaignService.processDatafiles(campaignDTO.get()));
    }


    @Autowired
    SmsRepository smsRepo;

    /**
     * GET  /campaigns : get all the campaigns.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of campaigns in body
     */
    @GetMapping("/search/campaigns")
    @Timed
    public ResponseEntity<List<CampaignDTO>> searchCampaigns(@QuerydslPredicate(root = Campaign.class) Predicate predicate, Pageable pageable) {
        log.debug("REST request to get a page of Campaigns");
        Page<CampaignDTO> page = campaignService.findAll(predicate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/search/campaigns");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
