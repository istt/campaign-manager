package com.ft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ft.config.ApplicationProperties;
import com.ft.service.VasCloudConfigurationService;
import com.ft.web.rest.errors.BadRequestAlertException;
import com.ft.web.rest.util.HeaderUtil;
import com.ft.service.dto.DataFileDTO;
import com.ft.service.dto.VasCloudConfigurationDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing VasCloudConfiguration.
 */
@RestController
@RequestMapping("/api")
public class VasCloudConfigurationResource {

    private final Logger log = LoggerFactory.getLogger(VasCloudConfigurationResource.class);

    private static final String ENTITY_NAME = "vasCloudConfiguration";

    private final VasCloudConfigurationService vasCloudConfigurationService;

    @Autowired
    ApplicationProperties props;

    public VasCloudConfigurationResource(VasCloudConfigurationService vasCloudConfigurationService) {
        this.vasCloudConfigurationService = vasCloudConfigurationService;
    }

    /**
     * POST  /vas-cloud-configurations : Create a new vasCloudConfiguration.
     *
     * @param vasCloudConfigurationDTO the vasCloudConfigurationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vasCloudConfigurationDTO, or with status 400 (Bad Request) if the vasCloudConfiguration has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/vas-cloud-configurations")
    @Timed
    public ResponseEntity<VasCloudConfigurationDTO> createVasCloudConfiguration(@RequestBody VasCloudConfigurationDTO vasCloudConfigurationDTO) throws URISyntaxException {
        log.debug("REST request to save VasCloudConfiguration : {}", vasCloudConfigurationDTO);
        if (vasCloudConfigurationDTO.getId() != null) {
            throw new BadRequestAlertException("A new vasCloudConfiguration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VasCloudConfigurationDTO result = vasCloudConfigurationService.save(vasCloudConfigurationDTO);
        return ResponseEntity.created(new URI("/api/vas-cloud-configurations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /vas-cloud-configurations : Updates an existing vasCloudConfiguration.
     *
     * @param vasCloudConfigurationDTO the vasCloudConfigurationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vasCloudConfigurationDTO,
     * or with status 400 (Bad Request) if the vasCloudConfigurationDTO is not valid,
     * or with status 500 (Internal Server Error) if the vasCloudConfigurationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/vas-cloud-configurations")
    @Timed
    public ResponseEntity<VasCloudConfigurationDTO> updateVasCloudConfiguration(@RequestBody VasCloudConfigurationDTO vasCloudConfigurationDTO) throws URISyntaxException {
        log.debug("REST request to update VasCloudConfiguration : {}", vasCloudConfigurationDTO);
        if (vasCloudConfigurationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VasCloudConfigurationDTO result = vasCloudConfigurationService.save(vasCloudConfigurationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, vasCloudConfigurationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /vas-cloud-configurations : get all the vasCloudConfigurations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of vasCloudConfigurations in body
     */
    @GetMapping("/vas-cloud-configurations")
    @Timed
    public List<VasCloudConfigurationDTO> getAllVasCloudConfigurations() {
        log.debug("REST request to get all VasCloudConfigurations");
        return vasCloudConfigurationService.findAll();
    }

    /**
     * GET  /vas-cloud-configurations/:id : get the "id" vasCloudConfiguration.
     *
     * @param id the id of the vasCloudConfigurationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vasCloudConfigurationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/vas-cloud-configurations/{id}")
    @Timed
    public ResponseEntity<VasCloudConfigurationDTO> getVasCloudConfiguration(@PathVariable String id) {
        log.debug("REST request to get VasCloudConfiguration : {}", id);
        if (id.equalsIgnoreCase("DEFAULT")) return ResponseEntity.ok().body(props.getVasCloud());
        Optional<VasCloudConfigurationDTO> vasCloudConfigurationDTO = vasCloudConfigurationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vasCloudConfigurationDTO);
    }

    /**
     * DELETE  /vas-cloud-configurations/:id : delete the "id" vasCloudConfiguration.
     *
     * @param id the id of the vasCloudConfigurationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/vas-cloud-configurations/{id}")
    @Timed
    public ResponseEntity<Void> deleteVasCloudConfiguration(@PathVariable String id) {
        log.debug("REST request to delete VasCloudConfiguration : {}", id);
        vasCloudConfigurationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * PUT  /vas-cloud-configurations : Updates an existing vasCloudConfiguration.
     *
     * @param vasCloudConfigurationDTO the vasCloudConfigurationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vasCloudConfigurationDTO,
     * or with status 400 (Bad Request) if the vasCloudConfigurationDTO is not valid,
     * or with status 500 (Internal Server Error) if the vasCloudConfigurationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/import/vas-cloud-configurations")
    @Timed
    public ResponseEntity<List<VasCloudConfigurationDTO>> importVasCloudConfiguration(@RequestBody DataFileDTO dataFile) throws URISyntaxException {
    	return ResponseEntity.ok().body(vasCloudConfigurationService.importData(dataFile));
    }
}
