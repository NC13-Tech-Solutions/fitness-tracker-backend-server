package com.nc13techsolutions.fitnesstrackerbackendserver.repositories;

import java.util.List;

import com.nc13techsolutions.fitnesstrackerbackendserver.models.Exercise;

public interface ExerciseRepo {
    /**
     * Adds a new exercise
     * 
     * @param exercise
     * @return 1 if insert is successful; -1 if not successful; 0 if exercise name
     *         already exists
     */
    int insertExercise(Exercise exercise);

    /**
     * Gets all the exercises
     * 
     * @return list of all exercises
     */
    List<Exercise> getExercises();

    /**
     * Finds the exercise with exId
     * 
     * @param exId
     * @return Exercise object or Null if exercise doesn't exist
     */
    Exercise getExerciseById(int exId);

    /**
     * Updates the exercise details, if it exists
     * 
     * @param exId     ID of the existing exercise
     * @param exercise new data to be updated
     * @return 1 if update is successful; -1 if exercise is not found; 0 if
     *         exercise.name already exists
     */
    int updateExercise(int exId, Exercise exercise);

    /**
     * Deletes the exercise, if it exists
     * 
     * @param exId ID of the existing exercise
     * @return 1 if delete is successful; -1 if exercise is not found; 0 if exercise
     *         has dependencies
     */
    int deleteExercise(int exId);

    /**
     * Checks if exercise already exists
     * 
     * @param exercise
     * @return 2 if exercise name and id matches; 1 if only name matches; 0 if only
     *         id matches; -1 if there is no match
     */
    public int checkIfExerciseExists(Exercise exercise);

    /**
     * Finds the heighest exercise ID
     * 
     * @return 0 if there are no exercises; heighest ID value
     */
    public int findHeighestExId();

}
