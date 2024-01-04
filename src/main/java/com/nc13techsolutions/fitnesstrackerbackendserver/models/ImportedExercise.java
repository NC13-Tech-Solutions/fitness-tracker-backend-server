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
public class ImportedExercise implements Serializable{
    @NonNull
    private Integer slNo;
    @NonNull
    private Integer exId;
    private int[] weightsUsed;
    private int dropSets;
    private String repRange;
    private int sets;
    private int restTime;
    private Integer superSetOf;
    private String exerciseExplainer;
    private VideoData[] exerciseFormVideos;
}
