package com.billykid.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billykid.library.entity.Author;

public interface AuthorRepository extends  JpaRepository<Author,Integer> {
    public List<Author> findByNameContainingIgnoreCase(String name);
}
