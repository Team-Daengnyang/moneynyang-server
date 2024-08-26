package com.fav.daengnyang.domain.targetDetail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookdataRepository extends JpaRepository<BookdataDetail, Long> {

    @Query("SELECT SUM(bd.amount) FROM BookdataDetail bd WHERE bd.bankbook.id = :bankbookId")
    Integer findTotalAmountByBankbookId(@Param("bankbookId") Long bankbookId);

    @Query("SELECT COUNT(bd) FROM BookdataDetail bd WHERE bd.bankbook.id = :bankbookId")
    Long countTransactionsByBankbookId(@Param("bankbookId") Long bankbookId);
}
