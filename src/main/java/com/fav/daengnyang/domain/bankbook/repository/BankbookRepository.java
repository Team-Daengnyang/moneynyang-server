package com.fav.daengnyang.domain.bankbook.repository;

import com.fav.daengnyang.domain.bankbook.entity.Bankbook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankbookRepository extends JpaRepository<Bankbook, Long> {
    Optional<Bankbook> findByBankbookNumber(String bankbookNumber);
}
