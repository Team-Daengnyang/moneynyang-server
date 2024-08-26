package com.fav.daengnyang.domain.account.repository;

import com.fav.daengnyang.domain.account.entity.AccountCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountCodeRepository extends JpaRepository<AccountCode, Long> {
    Optional<AccountCode> findByAccountCode(String accountCode);
}
