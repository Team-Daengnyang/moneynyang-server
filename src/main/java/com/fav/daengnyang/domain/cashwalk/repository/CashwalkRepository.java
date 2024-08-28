package com.fav.daengnyang.domain.cashwalk.repository;

import com.fav.daengnyang.domain.cashwalk.entity.Cashwalk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashwalkRepository extends JpaRepository<Cashwalk, Long> {
}
