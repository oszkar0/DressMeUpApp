package com.dressmeup.DressMeUpAPI.repositories;

import com.dressmeup.DressMeUpAPI.entities.Rate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RateRepository extends JpaRepository<Rate, Long> {
    List<Rate> findByUserIdAndPostId(Long userid, Long postId);
    List<Rate> findByPostId(Long id);
}
