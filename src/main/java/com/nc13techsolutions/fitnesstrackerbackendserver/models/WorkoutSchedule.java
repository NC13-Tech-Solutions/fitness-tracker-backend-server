package com.nc13techsolutions.fitnesstrackerbackendserver.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class WorkoutSchedule implements Serializable{
    @NonNull
    private Integer scheduleId;
    private String[] scheduledDaysOfTheWeek;
    @NonNull
    private Integer priority;
    @NonNull
    private ImportedExercise[] exercises;
}
