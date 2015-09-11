package com.activebeancoders.dao.es;

import com.activebeancoders.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ActivityRepository extends ElasticsearchRepository<Activity, String> {

    Activity findByUserIdAndActivity(String userId, String activity);

    @Query("{ \"match_all\": {} }, \"sort\": { \"date\": { \"order\": \"desc\" } }")
    Page<Activity> findTopNOrderByDateDesc(Pageable pageable);

}
