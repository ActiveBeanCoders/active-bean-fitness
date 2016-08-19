package com.activebeancoders.fitness.repository;

import com.activebeancoders.fitness.domain.Action;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Action entity.
 */
@SuppressWarnings("unused")
public interface ActionRepository extends MongoRepository<Action,String> {

}
