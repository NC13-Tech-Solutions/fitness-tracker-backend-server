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
@RedisHash("DayData")
public class DayData implements Serializable{
    @NonNull
    @Id
    private Integer ddId;
    @NonNull
    private String postedOn;
    @NonNull
    private Integer postedBy;
    @NonNull
    private String modifiedOn;
    @NonNull
    private Integer modifiedBy;
    @NonNull
    private Integer userWeight;
    @NonNull
    private Workout[] workouts;
}
