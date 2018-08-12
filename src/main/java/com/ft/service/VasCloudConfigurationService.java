package com.ft.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.domain.VasCloudConfiguration;
import com.ft.repository.VasCloudConfigurationRepository;
import com.ft.service.dto.DataFileDTO;
import com.ft.service.dto.VasCloudConfigurationDTO;
import com.ft.service.mapper.VasCloudConfigurationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Service Implementation for managing VasCloudConfiguration.
 */
@Service
public class VasCloudConfigurationService {

    private final Logger log = LoggerFactory.getLogger(VasCloudConfigurationService.class);

    private final VasCloudConfigurationRepository vasCloudConfigurationRepository;

    private final VasCloudConfigurationMapper vasCloudConfigurationMapper;

    public VasCloudConfigurationService(VasCloudConfigurationRepository vasCloudConfigurationRepository, VasCloudConfigurationMapper vasCloudConfigurationMapper) {
        this.vasCloudConfigurationRepository = vasCloudConfigurationRepository;
        this.vasCloudConfigurationMapper = vasCloudConfigurationMapper;
    }

    /**
     * Save a vasCloudConfiguration.
     *
     * @param vasCloudConfigurationDTO the entity to save
     * @return the persisted entity
     */
    public VasCloudConfigurationDTO save(VasCloudConfigurationDTO vasCloudConfigurationDTO) {
        log.debug("Request to save VasCloudConfiguration : {}", vasCloudConfigurationDTO);
        VasCloudConfiguration vasCloudConfiguration = vasCloudConfigurationMapper.toEntity(vasCloudConfigurationDTO);
        vasCloudConfiguration = vasCloudConfigurationRepository.save(vasCloudConfiguration);
        return vasCloudConfigurationMapper.toDto(vasCloudConfiguration);
    }

    /**
     * Get all the vasCloudConfigurations.
     *
     * @return the list of entities
     */
    public List<VasCloudConfigurationDTO> findAll() {
        log.debug("Request to get all VasCloudConfigurations");
        return vasCloudConfigurationRepository.findAll().stream()
            .map(vasCloudConfigurationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one vasCloudConfiguration by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<VasCloudConfigurationDTO> findOne(String id) {
        log.debug("Request to get VasCloudConfiguration : {}", id);
        return vasCloudConfigurationRepository.findById(id)
            .map(vasCloudConfigurationMapper::toDto);
    }

    /**
     * Delete the vasCloudConfiguration by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete VasCloudConfiguration : {}", id);
        vasCloudConfigurationRepository.deleteById(id);
    }

    @Autowired
    ObjectMapper mapper;

	public List<VasCloudConfigurationDTO> importData(DataFileDTO dataFile) {
		if (dataFile.getDataContentType().contains("json")){
            try {
                List<VasCloudConfigurationDTO> data = mapper.readValue(dataFile.getData(), new TypeReference<List<VasCloudConfigurationDTO>>() { });
                return vasCloudConfigurationRepository.saveAll(
                	data .stream()
	                .map(vasCloudConfigurationMapper::toEntity)
	                .collect(Collectors.toCollection(LinkedList::new))
                )
                .stream()
                .map(vasCloudConfigurationMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
            } catch (IOException e) {
                    log.error("Cannot parse JSON", e);
            }
		}

		return null;
	}
}
