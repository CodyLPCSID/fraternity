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

import com.fraternity.fsp.domain.HelpOffer;
import com.fraternity.fsp.domain.*; // for static metamodels
import com.fraternity.fsp.repository.HelpOfferRepository;
import com.fraternity.fsp.service.dto.HelpOfferCriteria;

/**
 * Service for executing complex queries for HelpOffer entities in the database.
 * The main input is a {@link HelpOfferCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HelpOffer} or a {@link Page} of {@link HelpOffer} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HelpOfferQueryService extends QueryService<HelpOffer> {

    private final Logger log = LoggerFactory.getLogger(HelpOfferQueryService.class);

    private final HelpOfferRepository helpOfferRepository;

    public HelpOfferQueryService(HelpOfferRepository helpOfferRepository) {
        this.helpOfferRepository = helpOfferRepository;
    }

    /**
     * Return a {@link List} of {@link HelpOffer} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HelpOffer> findByCriteria(HelpOfferCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<HelpOffer> specification = createSpecification(criteria);
        return helpOfferRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link HelpOffer} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HelpOffer> findByCriteria(HelpOfferCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HelpOffer> specification = createSpecification(criteria);
        return helpOfferRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HelpOfferCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HelpOffer> specification = createSpecification(criteria);
        return helpOfferRepository.count(specification);
    }

    /**
     * Function to convert HelpOfferCriteria to a {@link Specification}
     */
    private Specification<HelpOffer> createSpecification(HelpOfferCriteria criteria) {
        Specification<HelpOffer> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), HelpOffer_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), HelpOffer_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), HelpOffer_.description));
            }
            if (criteria.getDatePost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatePost(), HelpOffer_.datePost));
            }
            if (criteria.getDateStart() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateStart(), HelpOffer_.dateStart));
            }
            if (criteria.getDateEnd() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateEnd(), HelpOffer_.dateEnd));
            }
            if (criteria.getHelpOId() != null) {
                specification = specification.and(buildSpecification(criteria.getHelpOId(),
                    root -> root.join(HelpOffer_.helpO, JoinType.LEFT).get(HelpAction_.id)));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(HelpOffer_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getCategoryId(),
                    root -> root.join(HelpOffer_.category, JoinType.LEFT).get(Category_.id)));
            }
        }
        return specification;
    }
}
