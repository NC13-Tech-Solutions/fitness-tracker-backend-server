package com.nc13techsolutions.fitnesstrackerbackendserver.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.nc13techsolutions.fitnesstrackerbackendserver.models.Exercise;
import com.nc13techsolutions.fitnesstrackerbackendserver.repositories.ExerciseRepo;

@Service
public class ExerciseService {
    private final ExerciseRepo exerciseRepo;

    public ExerciseService(@Qualifier("redis exercise") ExerciseRepo exerciseRepo) {
        this.exerciseRepo = exerciseRepo;
    }

    public int addExercise(Exercise exercise) {
        return exerciseRepo.insertExercise(exercise);
    }

    public List<Exercise> getAllExercises() {
        return exerciseRepo.getExercises();
    }

    public Exercise getExerciseById(Integer exId) {
        return exerciseRepo.getExerciseById(exId);
    }

    public int modifyExercise(Integer exId, Exercise exercise) {
        return exerciseRepo.updateExercise(exId, exercise);
    }

    public int deleteExercise(Integer exId) {
        return exerciseRepo.deleteExercise(exId);
    }

    public int getNewExerciseId() {
        return exerciseRepo.findHeighestExId();
    }
}
