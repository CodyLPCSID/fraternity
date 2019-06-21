package com.fraternity.fsp.web.rest;
import com.fraternity.fsp.domain.HelpRequest;
import com.fraternity.fsp.service.HelpRequestService;
import com.fraternity.fsp.web.rest.errors.BadRequestAlertException;
import com.fraternity.fsp.web.rest.util.HeaderUtil;
import com.fraternity.fsp.web.rest.util.PaginationUtil;
import com.fraternity.fsp.service.dto.HelpRequestCriteria;
import com.fraternity.fsp.service.HelpRequestQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing HelpRequest.
 */
@RestController
@RequestMapping("/api")
public class HelpRequestResource {

    private final Logger log = LoggerFactory.getLogger(HelpRequestResource.class);

    private static final String ENTITY_NAME = "helpRequest";

    private final HelpRequestService helpRequestService;

    private final HelpRequestQueryService helpRequestQueryService;

    public HelpRequestResource(HelpRequestService helpRequestService, HelpRequestQueryService helpRequestQueryService) {
        this.helpRequestService = helpRequestService;
        this.helpRequestQueryService = helpRequestQueryService;
    }

    /**
     * POST  /help-requests : Create a new helpRequest.
     *
     * @param helpRequest the helpRequest to create
     * @return the ResponseEntity with status 201 (Created) and with body the new helpRequest, or with status 400 (Bad Request) if the helpRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/help-requests")
    public ResponseEntity<HelpRequest> createHelpRequest(@RequestBody HelpRequest helpRequest) throws URISyntaxException {
        log.debug("REST request to save HelpRequest : {}", helpRequest);
        if (helpRequest.getId() != null) {
            throw new BadRequestAlertException("A new helpRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HelpRequest result = helpRequestService.save(helpRequest);
        return ResponseEntity.created(new URI("/api/help-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /help-requests : Updates an existing helpRequest.
     *
     * @param helpRequest the helpRequest to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated helpRequest,
     * or with status 400 (Bad Request) if the helpRequest is not valid,
     * or with status 500 (Internal Server Error) if the helpRequest couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/help-requests")
    public ResponseEntity<HelpRequest> updateHelpRequest(@RequestBody HelpRequest helpRequest) throws URISyntaxException {
        log.debug("REST request to update HelpRequest : {}", helpRequest);
        if (helpRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HelpRequest result = helpRequestService.save(helpRequest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, helpRequest.getId().toString()))
            .body(result);
    }

    /**
     * GET  /help-requests : get all the helpRequests.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of helpRequests in body
     */
    @GetMapping("/help-requests")
    public ResponseEntity<List<HelpRequest>> getAllHelpRequests(HelpRequestCriteria criteria, Pageable pageable) {
        log.debug("REST request to get HelpRequests by criteria: {}", criteria);
        Page<HelpRequest> page = helpRequestQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/help-requests");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /help-requests/count : count all the helpRequests.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/help-requests/count")
    public ResponseEntity<Long> countHelpRequests(HelpRequestCriteria criteria) {
        log.debug("REST request to count HelpRequests by criteria: {}", criteria);
        return ResponseEntity.ok().body(helpRequestQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /help-requests/:id : get the "id" helpRequest.
     *
     * @param id the id of the helpRequest to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the helpRequest, or with status 404 (Not Found)
     */
    @GetMapping("/help-requests/{id}")
    public ResponseEntity<HelpRequest> getHelpRequest(@PathVariable Long id) {
        log.debug("REST request to get HelpRequest : {}", id);
        Optional<HelpRequest> helpRequest = helpRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(helpRequest);
    }

    /**
     * DELETE  /help-requests/:id : delete the "id" helpRequest.
     *
     * @param id the id of the helpRequest to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/help-requests/{id}")
    public ResponseEntity<Void> deleteHelpRequest(@PathVariable Long id) {
        log.debug("REST request to delete HelpRequest : {}", id);
        helpRequestService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
