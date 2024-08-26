package com.fav.daengnyang.domain.pet.repository;

import com.fav.daengnyang.domain.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    Optional<Pet> findByMemberMemberId(Long memberId);
}
