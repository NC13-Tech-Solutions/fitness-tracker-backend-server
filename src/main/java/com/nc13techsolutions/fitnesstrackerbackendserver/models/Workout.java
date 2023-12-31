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
@NoArgsConstructor
@RequiredArgsConstructor
public class Workout implements Serializable{
    @NonNull
    private String time;
    @NonNull
    private ImportedExercise[] exercises;
    private String text;
    private String[] photos;
    private VideoData[] videos;
}
