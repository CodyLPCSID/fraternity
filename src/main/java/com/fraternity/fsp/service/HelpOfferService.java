package com.fraternity.fsp.service;

import com.fraternity.fsp.domain.HelpOffer;
import com.fraternity.fsp.repository.HelpOfferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing HelpOffer.
 */
@Service
@Transactional
public class HelpOfferService {

    private final Logger log = LoggerFactory.getLogger(HelpOfferService.class);

    private final HelpOfferRepository helpOfferRepository;

    public HelpOfferService(HelpOfferRepository helpOfferRepository) {
        this.helpOfferRepository = helpOfferRepository;
    }

    /**
     * Save a helpOffer.
     *
     * @param helpOffer the entity to save
     * @return the persisted entity
     */
    public HelpOffer save(HelpOffer helpOffer) {
        log.debug("Request to save HelpOffer : {}", helpOffer);
        return helpOfferRepository.save(helpOffer);
    }

    /**
     * Get all the helpOffers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<HelpOffer> findAll(Pageable pageable) {
        log.debug("Request to get all HelpOffers");
        return helpOfferRepository.findAll(pageable);
    }


    /**
     * Get one helpOffer by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<HelpOffer> findOne(Long id) {
        log.debug("Request to get HelpOffer : {}", id);
        return helpOfferRepository.findById(id);
    }

    /**
     * Delete the helpOffer by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete HelpOffer : {}", id);
        helpOfferRepository.deleteById(id);
    }


    public void intervale(LocalDate dateStart, LocalDate dateEnd){
        log.debug("Request to delete dateEnd : {}", dateEnd );
        log.debug("Request to delete dateStart : {}", dateStart);
        helpOfferRepository.findAllByDateStartGreaterThanEqualAndDateEndLessThanEqual(dateStart, dateEnd);

    }
}
