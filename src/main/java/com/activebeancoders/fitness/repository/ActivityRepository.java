package com.activebeancoders.fitness.repository;

import com.activebeancoders.fitness.domain.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ActivityRepository extends MongoRepository<Activity, String> {

//    Optional<Activity> findOneById(Long id);

    /*
    public static final String QUERY_FULL_TEXT_SEARCH = "activityFullTextSearch";
    public static final String QUERY_FIND_MAX_ID = "activityFindMaxId";
    public static final String QUERY_FIND_MOST_RECENT = "findMostRecentActivities";
    public static final String EXEC_ADD_FULLTEXT_INDEX = "addFulltextIndexToActivity";
    public static final String EXEC_DELETE_ALL = "deleteAllActivities";
     */

    @Override
    void delete(Activity t);

}
