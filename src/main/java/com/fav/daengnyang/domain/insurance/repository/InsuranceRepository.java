package com.fav.daengnyang.domain.insurance.repository;

import com.fav.daengnyang.domain.insurance.entity.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
    Optional<Insurance> findInsuranceByInsuranceId(Long insuranceId);
    List<Insurance> findByInsuranceType(String insuranceType);
}
