package com.nc13techsolutions.fitnesstrackerbackendserver.models;
import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("Days")
public class Day implements Serializable{
    @NonNull
    @Id
    private String datePosted;
    @NonNull
    private Integer postedUserId;
    @NonNull
    private String dateModified;
    @NonNull
    private Integer modifiedUsedId;
    @NonNull
    private Integer weight;
    @NonNull
    private Workout[] workouts;
}
