package com.activebeancoders.fitness.entity;

import net.pladform.elasticsearch.entity.IdAware;

import javax.persistence.*;
import java.util.Date;

// TODO: externalize annotations somehow?  Or maybe we don't care.
@NamedQueries({
        @NamedQuery(
                name = Activity.QUERY_FIND_BY_ID,
                query = "select a from Activity a where a.id = :id"
        )
})
@NamedNativeQueries({
        // see: https://dev.mysql.com/doc/refman/5.1/en/fulltext-fine-tuning.html
        @NamedNativeQuery(
                name = Activity.QUERY_FULL_TEXT_SEARCH,
                query = "SELECT * FROM activity WHERe MATCH(`alltext`) AGAINST (:criteria);",
                resultClass = Activity.class
        ),
        @NamedNativeQuery(
                name = Activity.QUERY_FIND_MAX_ID,
                query = "SELECT MAX(id) AS id FROM activity;"
        ),
        @NamedNativeQuery(
                name = Activity.EXEC_ADD_FULLTEXT_INDEX,
                query = "ALTER TABLE `activity` ADD FULLTEXT INDEX `alltext` (`alltext`);"
        ),
        @NamedNativeQuery(
                name = Activity.EXEC_DELETE_ALL,
                query = "TRUNCATE TABLE activity;"
        )
})
@Entity
@Table(name = "activity")
public class Activity implements IdAware<Long> {

    public static final String QUERY_FIND_BY_ID = "findActivityById";
    public static final String QUERY_FULL_TEXT_SEARCH = "activityFullTextSearch";
    public static final String QUERY_FIND_MAX_ID = "activityFindMaxId";
    public static final String EXEC_ADD_FULLTEXT_INDEX = "addFulltextIndexToActivity";
    public static final String EXEC_DELETE_ALL = "deleteAllActivities";

    protected Long id;
    protected Long userId;
    protected String activity;
    protected Date date;
    protected String unit;
    protected Double distance;
    protected String comment;
    protected Long distHour;
    protected Long distMin;
    protected Long distSec;

    private static final String SPLIT_REGEX = "\\W"; // \W == non-word characters.

    public Activity() {
        userId = -1L;
    }

    @Id
    @Column(name = "id")
    // Do not auto-generate ID because we must share an ID with other storage mechanisms.
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @Column(name = "user_id", nullable = false)
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    @Column(name = "activity")
    public String getActivity() { return activity; }
    public void setActivity(String activity) { this.activity = activity; }

    @Column(name = "date")
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    @Column(name = "unit")
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    @Column(name = "distance")
    public Double getDistance() { return distance; }
    public void setDistance(Double distance) { this.distance = distance; }

    @Column(name = "comment", length = 4000)
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    @Column(name = "dist_hour")
    public Long getDistHour() { return distHour; }
    public void setDistHour(Long distHour) { this.distHour = distHour; }

    @Column(name = "dist_min")
    public Long getDistMin() { return distMin; }
    public void setDistMin(Long distMin) { this.distMin = distMin; }

    @Column(name = "dist_sec")
    public Long getDistSec() { return distSec; }
    public void setDistSec(Long distSec) { this.distSec = distSec; }

    // MySQL provides a "FULLTEXT INDEX", but can be used only on string columns.
    // The following method provides a means to create a FULLTEXT index on a single column
    // which is a concatenation of all columns (with certain restrictions).
    @Column(name = "alltext", length = 4000)
    protected String getAllText() {
        StringBuilder fullText = new StringBuilder();
        if (activity != null) {
            fullText.append(activity);
            fullText.append(" ");
        }
        if (date != null) {
            String[] tokens = date.toString().split(SPLIT_REGEX);
            if (tokens != null) {
                for (String token : tokens) {
                    if (token.length() > 2) {
                        fullText.append(token);
                        fullText.append(" ");
                    }
                }
            }
        }
        if (unit != null) {
            fullText.append(unit);
            fullText.append(" ");
        }
        if (distance != null) {
            fullText.append(distance);
            fullText.append(" ");
        }
        if (comment != null) {
            String[] tokens = comment.toString().split(SPLIT_REGEX);
            if (tokens != null) {
                for (String token : tokens) {
                    if (token.length() > 2) {
                        fullText.append(token);
                        fullText.append(" ");
                    }
                }
            }
        }
        if (distHour != null) {
            fullText.append(distHour);
            fullText.append(" ");
        }
        if (distMin != null) {
            fullText.append(distMin);
            fullText.append(" ");
        }
        if (distSec != null) {
            fullText.append(distSec);
            fullText.append(" ");
        }
        return fullText.toString().trim();
    }
    public void setAllText(String allText) {}

}
