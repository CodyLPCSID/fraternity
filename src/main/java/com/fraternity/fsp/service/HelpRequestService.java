package com.fraternity.fsp.service;

import com.fraternity.fsp.domain.HelpRequest;
import com.fraternity.fsp.repository.HelpRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing HelpRequest.
 */
@Service
@Transactional
public class HelpRequestService {

    private final Logger log = LoggerFactory.getLogger(HelpRequestService.class);

    private final HelpRequestRepository helpRequestRepository;

    public HelpRequestService(HelpRequestRepository helpRequestRepository) {
        this.helpRequestRepository = helpRequestRepository;
    }

    /**
     * Save a helpRequest.
     *
     * @param helpRequest the entity to save
     * @return the persisted entity
     */
    public HelpRequest save(HelpRequest helpRequest) {
        log.debug("Request to save HelpRequest : {}", helpRequest);
        return helpRequestRepository.save(helpRequest);
    }

    /**
     * Get all the helpRequests.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<HelpRequest> findAll(Pageable pageable) {
        log.debug("Request to get all HelpRequests");
        return helpRequestRepository.findAll(pageable);
    }


    /**
     * Get one helpRequest by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<HelpRequest> findOne(Long id) {
        log.debug("Request to get HelpRequest : {}", id);
        return helpRequestRepository.findById(id);
    }

    /**
     * Delete the helpRequest by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete HelpRequest : {}", id);
        helpRequestRepository.deleteById(id);
    }
}
