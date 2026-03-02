package com.htm.e20nomics.briefing.repository;

import com.htm.e20nomics.briefing.domain.Briefing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BriefingRepository extends JpaRepository<Briefing, Long> {
}
