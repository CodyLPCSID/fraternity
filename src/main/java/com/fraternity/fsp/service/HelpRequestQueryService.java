package com.fraternity.fsp.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.fraternity.fsp.domain.HelpRequest;
import com.fraternity.fsp.domain.*; // for static metamodels
import com.fraternity.fsp.repository.HelpRequestRepository;
import com.fraternity.fsp.service.dto.HelpRequestCriteria;

/**
 * Service for executing complex queries for HelpRequest entities in the database.
 * The main input is a {@link HelpRequestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HelpRequest} or a {@link Page} of {@link HelpRequest} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HelpRequestQueryService extends QueryService<HelpRequest> {

    private final Logger log = LoggerFactory.getLogger(HelpRequestQueryService.class);

    private final HelpRequestRepository helpRequestRepository;

    public HelpRequestQueryService(HelpRequestRepository helpRequestRepository) {
        this.helpRequestRepository = helpRequestRepository;
    }

    /**
     * Return a {@link List} of {@link HelpRequest} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HelpRequest> findByCriteria(HelpRequestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<HelpRequest> specification = createSpecification(criteria);
        return helpRequestRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link HelpRequest} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HelpRequest> findByCriteria(HelpRequestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HelpRequest> specification = createSpecification(criteria);
        return helpRequestRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HelpRequestCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HelpRequest> specification = createSpecification(criteria);
        return helpRequestRepository.count(specification);
    }

    /**
     * Function to convert HelpRequestCriteria to a {@link Specification}
     */
    private Specification<HelpRequest> createSpecification(HelpRequestCriteria criteria) {
        Specification<HelpRequest> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), HelpRequest_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), HelpRequest_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), HelpRequest_.description));
            }
            if (criteria.getDatePost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatePost(), HelpRequest_.datePost));
            }
            if (criteria.getDateStart() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateStart(), HelpRequest_.dateStart));
            }
            if (criteria.getDateEnd() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateEnd(), HelpRequest_.dateEnd));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(HelpRequest_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getCategoryId(),
                    root -> root.join(HelpRequest_.category, JoinType.LEFT).get(Category_.id)));
            }
        }
        return specification;
    }
}
