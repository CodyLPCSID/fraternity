package com.fraternity.fsp.web.rest;
import com.fraternity.fsp.domain.HelpOffer;
import com.fraternity.fsp.service.HelpOfferService;
import com.fraternity.fsp.web.rest.errors.BadRequestAlertException;
import com.fraternity.fsp.web.rest.util.HeaderUtil;
import com.fraternity.fsp.service.dto.HelpOfferCriteria;
import com.fraternity.fsp.service.HelpOfferQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing HelpOffer.
 */
@RestController
@RequestMapping("/api")
public class HelpOfferResource {

    private final Logger log = LoggerFactory.getLogger(HelpOfferResource.class);

    private static final String ENTITY_NAME = "helpOffer";

    private final HelpOfferService helpOfferService;

    private final HelpOfferQueryService helpOfferQueryService;

    public HelpOfferResource(HelpOfferService helpOfferService, HelpOfferQueryService helpOfferQueryService) {
        this.helpOfferService = helpOfferService;
        this.helpOfferQueryService = helpOfferQueryService;
    }

    /**
     * POST  /help-offers : Create a new helpOffer.
     *
     * @param helpOffer the helpOffer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new helpOffer, or with status 400 (Bad Request) if the helpOffer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/help-offers")
    public ResponseEntity<HelpOffer> createHelpOffer(@RequestBody HelpOffer helpOffer) throws URISyntaxException {
        log.debug("REST request to save HelpOffer : {}", helpOffer);
        if (helpOffer.getId() != null) {
            throw new BadRequestAlertException("A new helpOffer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HelpOffer result = helpOfferService.save(helpOffer);
        return ResponseEntity.created(new URI("/api/help-offers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /help-offers : Updates an existing helpOffer.
     *
     * @param helpOffer the helpOffer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated helpOffer,
     * or with status 400 (Bad Request) if the helpOffer is not valid,
     * or with status 500 (Internal Server Error) if the helpOffer couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/help-offers")
    public ResponseEntity<HelpOffer> updateHelpOffer(@RequestBody HelpOffer helpOffer) throws URISyntaxException {
        log.debug("REST request to update HelpOffer : {}", helpOffer);
        if (helpOffer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HelpOffer result = helpOfferService.save(helpOffer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, helpOffer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /help-offers : get all the helpOffers.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of helpOffers in body
     */
    @GetMapping("/help-offers")
    public ResponseEntity<List<HelpOffer>> getAllHelpOffers(HelpOfferCriteria criteria) {
        log.debug("REST request to get HelpOffers by criteria: {}", criteria);
        List<HelpOffer> entityList = helpOfferQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /help-offers/count : count all the helpOffers.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/help-offers/count")
    public ResponseEntity<Long> countHelpOffers(HelpOfferCriteria criteria) {
        log.debug("REST request to count HelpOffers by criteria: {}", criteria);
        return ResponseEntity.ok().body(helpOfferQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /help-offers/:id : get the "id" helpOffer.
     *
     * @param id the id of the helpOffer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the helpOffer, or with status 404 (Not Found)
     */
    @GetMapping("/help-offers/{id}")
    public ResponseEntity<HelpOffer> getHelpOffer(@PathVariable Long id) {
        log.debug("REST request to get HelpOffer : {}", id);
        Optional<HelpOffer> helpOffer = helpOfferService.findOne(id);
        return ResponseUtil.wrapOrNotFound(helpOffer);
    }

    /**
     * DELETE  /help-offers/:id : delete the "id" helpOffer.
     *
     * @param id the id of the helpOffer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/help-offers/{id}")
    public ResponseEntity<Void> deleteHelpOffer(@PathVariable Long id) {
        log.debug("REST request to delete HelpOffer : {}", id);
        helpOfferService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
