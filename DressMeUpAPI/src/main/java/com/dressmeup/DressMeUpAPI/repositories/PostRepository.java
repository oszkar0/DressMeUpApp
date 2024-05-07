package com.dressmeup.DressMeUpAPI.repositories;

import com.dressmeup.DressMeUpAPI.entities.Post;
import com.dressmeup.DressMeUpAPI.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    public List<Post> findByUserId(Long id);
}