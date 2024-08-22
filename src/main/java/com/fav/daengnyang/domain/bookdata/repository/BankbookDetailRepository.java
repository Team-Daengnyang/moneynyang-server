package com.fav.daengnyang.domain.bookdata.repository;

import com.fav.daengnyang.domain.bookdata.entity.BankbookDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BankbookDetailRepository extends JpaRepository<BankbookDetail, Long> {

    @Query("SELECT SUM(bd.amount) FROM BankbookDetail bd WHERE bd.bankbook.id = :bankbookId")
    Integer findTotalAmountByBankbookId(@Param("bankbookId") Long bankbookId);

    @Query("SELECT COUNT(bd) FROM BankbookDetail bd WHERE bd.bankbook.id = :bankbookId")
    Long countTransactionsByBankbookId(@Param("bankbookId") Long bankbookId);
}
