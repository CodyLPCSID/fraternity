package com.fraternity.fsp.repository;

import com.fraternity.fsp.domain.HelpOffer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the HelpOffer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HelpOfferRepository extends JpaRepository<HelpOffer, Long> {

}
