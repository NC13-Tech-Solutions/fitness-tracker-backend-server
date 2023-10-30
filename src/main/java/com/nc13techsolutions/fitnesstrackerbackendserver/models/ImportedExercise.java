package com.nc13techsolutions.fitnesstrackerbackendserver.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ImportedExercise implements Serializable{
    @NonNull
    private Integer exId;
    private String misc;
}
