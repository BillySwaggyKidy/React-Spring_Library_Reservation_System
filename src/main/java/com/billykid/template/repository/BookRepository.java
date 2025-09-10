package com.billykid.template.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.billykid.template.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book>{

    public List<Book> findByTitleContainingIgnoreCase(String title);

    public List<Book> findByAuthor_NameContainingIgnoreCase(String authorName);

    public List<Book> findByBookStatus_IsAvailable(Boolean available);
}
