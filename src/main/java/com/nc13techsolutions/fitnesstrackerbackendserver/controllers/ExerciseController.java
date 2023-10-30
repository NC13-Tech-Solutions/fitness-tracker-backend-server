package com.nc13techsolutions.fitnesstrackerbackendserver.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nc13techsolutions.fitnesstrackerbackendserver.models.Exercise;
import com.nc13techsolutions.fitnesstrackerbackendserver.services.ExerciseService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/exercise")
public class ExerciseController {
    public static final int EXERCISE_ID_START = 1000;

    @Autowired
    private ExerciseService exerciseService;

    @PostMapping("/new")
    public int addExercise(@RequestBody Exercise exercise) {
        int id = exerciseService.getNewExerciseId() > 0 ? exerciseService.getNewExerciseId() : EXERCISE_ID_START;
        return exerciseService.addExercise(new Exercise(id, exercise.getName(), exercise.getDescription(),exercise.getMiscDataType(), exercise.getMiscData(), exercise.isDisabled()));
    }

    @GetMapping("/all")
    public List<Exercise> getAllExercises() {
        return exerciseService.getAllExercises();
    }

    @GetMapping("/{exId}")
    public Exercise getExerciseById(@PathVariable int exId) {
        return exerciseService.getExerciseById(exId);
    }

    @PutMapping("/{exId}")
    public int modifyExercise(@PathVariable int exId, @RequestBody Exercise exercise) {
        return exerciseService.modifyExercise(exId, new Exercise(exId, exercise.getName(), exercise.getDescription(), exercise.getMiscDataType(), exercise.getMiscData(), exercise.isDisabled()));
    }

    @DeleteMapping("/{exId}")
    public int deleteExercise(@PathVariable int exId) {
        return exerciseService.deleteExercise(exId);
    }
}
