package com.activebeancoders.dao.es;

import com.activebeancoders.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ActivityRepository extends ElasticsearchRepository<Activity, String> {

    public Activity findByActivity(String activity);
    public List<Activity> findByUnit(String unit);

    @Query("{ \"match_all\": {} }, \"sort\": { \"date\": { \"order\": \"desc\" }, \"size\": ?0 }")
    Page<Activity> findTopNOrderByDateDesc(Pageable pageable);

}
