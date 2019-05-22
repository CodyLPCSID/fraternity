package com.fraternity.fsp.repository;

import com.fraternity.fsp.domain.HelpOffer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * Spring Data  repository for the HelpOffer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HelpOfferRepository extends JpaRepository<HelpOffer, Long>, JpaSpecificationExecutor<HelpOffer> {

    @Query("select help_offer from HelpOffer help_offer where help_offer.user.login = ?#{principal.username}")
    List<HelpOffer> findByUserIsCurrentUser();


    List<HelpOffer> findAllByDateStartGreaterThanEqualAndDateEndLessThanEqual(LocalDate startDate, LocalDate endDate);
}
