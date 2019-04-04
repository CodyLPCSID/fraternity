package com.fraternity.fsp.repository;

import com.fraternity.fsp.domain.HelpAction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the HelpAction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HelpActionRepository extends JpaRepository<HelpAction, Long> {

}
