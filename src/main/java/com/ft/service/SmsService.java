package com.ft.service;

import com.ft.domain.Sms;
import com.ft.repository.SmsRepository;
import com.ft.service.dto.DataFileDTO;
import com.ft.service.dto.SmsDTO;
import com.ft.service.mapper.SmsMapper;
import com.querydsl.core.types.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
/**
 * Service Implementation for managing Sms.
 */
@Service
public class SmsService {

    private final Logger log = LoggerFactory.getLogger(SmsService.class);

    private final SmsRepository smsRepository;

    private final SmsMapper smsMapper;

    public SmsService(SmsRepository smsRepository, SmsMapper smsMapper) {
        this.smsRepository = smsRepository;
        this.smsMapper = smsMapper;
    }

    /**
     * Save a sms.
     *
     * @param smsDTO the entity to save
     * @return the persisted entity
     */
    public SmsDTO save(SmsDTO smsDTO) {
        log.debug("Request to save Sms : {}", smsDTO);
        Sms sms = smsMapper.toEntity(smsDTO);
        sms = smsRepository.save(sms);
        return smsMapper.toDto(sms);
    }

    /**
     * Get all the sms.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public Page<SmsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Sms");
        return smsRepository.findAll(pageable)
            .map(smsMapper::toDto);
    }


    /**
     * Get one sms by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<SmsDTO> findOne(String id) {
        log.debug("Request to get Sms : {}", id);
        return smsRepository.findById(id)
            .map(smsMapper::toDto);
    }

    /**
     * Delete the sms by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Sms : {}", id);
        smsRepository.deleteById(id);
    }

	public Page<SmsDTO> findAll(Predicate predicate, Pageable pageable) {
		log.debug("Request to get all Sms with predicate " + predicate);
        return smsRepository.findAll(predicate, pageable)
            .map(smsMapper::toDto);
	}

	public List<Object> stats(SmsDTO predicate) {
		return smsRepository.stats(predicate);
	}

	public DataFileDTO export(Predicate predicate) {
		DataFileDTO result = new DataFileDTO();
		result.setDataContentType("text/csv");
        String rs = "";
		Iterator<Sms> smsIt = smsRepository.findAll(predicate).iterator();
		while (smsIt.hasNext()) {
			rs += smsIt.next().getDestination() + "\n";
		}
		result.setData(rs.getBytes());
		return result;
	}
}
