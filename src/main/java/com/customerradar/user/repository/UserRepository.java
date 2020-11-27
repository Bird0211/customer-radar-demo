package com.customerradar.user.repository;

import com.customerradar.user.po.User;

import org.springframework.data.repository.CrudRepository;

/**
 * User Repository
 */


public interface UserRepository extends CrudRepository<User, Integer> {
    
}
