package com.nc13techsolutions.fitnesstrackerbackendserver.models;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Workout implements Serializable{
    @NonNull
    private String time;
    @NonNull
    private ImportedExercise[] exercises;
    private int sets;
    private String text;
    private String[] photos;
    private String[] videos;
}
