package com.ft.service;

import com.ft.domain.DataFile;
import com.ft.repository.DataFileRepository;
import com.ft.service.dto.DataFileDTO;
import com.ft.service.dto.DataFileSmDTO;
import com.ft.service.mapper.DataFileMapper;
import com.ft.service.mapper.DataFileSmMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Optional;
/**
 * Service Implementation for managing DataFile.
 */
@Service
public class DataFileService {

    private final Logger log = LoggerFactory.getLogger(DataFileService.class);

    private final DataFileRepository dataFileRepository;

    private final DataFileMapper dataFileMapper;

    private final DataFileSmMapper dataFileSmMapper;

    public DataFileService(DataFileRepository dataFileRepository, DataFileMapper dataFileMapper, DataFileSmMapper dataFileSmMapper) {
        this.dataFileRepository = dataFileRepository;
        this.dataFileMapper = dataFileMapper;
        this.dataFileSmMapper = dataFileSmMapper;
    }

    /**
     * Save a dataFile.
     *
     * @param dataFileDTO the entity to save
     * @return the persisted entity
     */
    public DataFileDTO save(DataFileDTO dataFileDTO) {
        log.debug("Request to save DataFile : {}", dataFileDTO);
        DataFile dataFile = dataFileMapper.toEntity(dataFileDTO);
        dataFile = dataFileRepository.save(dataFile);
        return dataFileMapper.toDto(dataFile);
    }

    /**
     * Get all the dataFiles.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public Page<DataFileSmDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DataFiles");
        return dataFileRepository.findAll(pageable)
            .map(dataFileSmMapper::toDto);
    }


    /**
     * Get one dataFile by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<DataFileDTO> findOne(String id) {
        log.debug("Request to get DataFile : {}", id);
        return dataFileRepository.findById(id)
            .map(dataFileMapper::toDto);
    }

    /**
     * Delete the dataFile by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete DataFile : {}", id);
        dataFileRepository.deleteById(id);
    }
}
