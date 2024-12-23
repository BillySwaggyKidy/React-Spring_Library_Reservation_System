package com.billykid.template.repository;

import org.springframework.data.repository.CrudRepository;

import com.billykid.template.entity.User;

public interface UserRepository extends CrudRepository<User, Integer> {

}
