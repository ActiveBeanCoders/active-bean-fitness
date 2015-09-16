package com.activebeancoders.fitness.dao.hib;

import com.activebeancoders.fitness.entity.Activity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityHibDao2 extends CrudRepository<Activity, Long> {

}

