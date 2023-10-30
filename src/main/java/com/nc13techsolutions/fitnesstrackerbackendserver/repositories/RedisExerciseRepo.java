package com.nc13techsolutions.fitnesstrackerbackendserver.repositories;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.nc13techsolutions.fitnesstrackerbackendserver.models.Exercise;

@Repository("redis exercise")
@EnableAutoConfiguration
public class RedisExerciseRepo implements ExerciseRepo {

    public static final String EXERCISE_HASH_KEY = "Exercises";
    @Autowired
    private RedisTemplate<String, Exercise> template;

    @Override
    public int insertExercise(Exercise exercise) {
        int result = checkIfExerciseExists(exercise);
        if (result == -1) {
            HashOperations<String, Integer, Exercise> ho = template.opsForHash();
            ho.put(EXERCISE_HASH_KEY, exercise.getExId(), exercise);
            return 1;
        } else if (result == 1) {
            return 0;
        }
        return -1;
    }

    @Override
    public List<Exercise> getExercises() {
        HashOperations<String, Integer, Exercise> ho = template.opsForHash();
        return ho.values(EXERCISE_HASH_KEY);
    }

    @Override
    public Exercise getExerciseById(int exId) {
        HashOperations<String, Integer, Exercise> ho = template.opsForHash();
        return ho.get(EXERCISE_HASH_KEY, exId);
    }

    @Override
    public int updateExercise(int exId, Exercise exercise) {
        int result = checkIfExerciseExists(exercise);
        if (result == 0 || result == 2) {
            HashOperations<String, Integer, Exercise> ho = template.opsForHash();
            ho.put(EXERCISE_HASH_KEY, exId, exercise);

            return 1;
        } else if (result == 1) {
            return 0;
        }

        return -1;

    }

    @Override
    public int deleteExercise(int exId) {
        // TODO: Have to check if exId is present in any Days
        int result = checkIfExerciseExists(new Exercise(exId,"",""));
        if (result == 0) {
            HashOperations<String, Integer, Exercise> ho = template.opsForHash();
            ho.delete(EXERCISE_HASH_KEY, exId);
            return 1;
        }
        return -1;

    }

    public int checkIfExerciseExists(Exercise exercise) {
        int result = -1;
        HashOperations<String, Integer, Exercise> ho = template.opsForHash();
        Map<Integer, Exercise> m = ho.entries(EXERCISE_HASH_KEY);

        for (Map.Entry<Integer, Exercise> me : m.entrySet()) {
            if (me.getValue().getName().trim().equalsIgnoreCase(exercise.getName().trim())) {
                if (me.getValue().getExId() == exercise.getExId()) {
                    return 2;
                }
                return 1;
            } else if (me.getValue().getExId() == exercise.getExId()) {
                result = 0;
            }
        }

        return result;

    }

    /**
     * Finds the heighest exercise ID
     * 
     * @return 0 if there are no exercises; heighest ID value
     */
    public int findHeighestExId() {
        int result = 0;
        HashOperations<String, Integer, Exercise> ho = template.opsForHash();
        Map<Integer, Exercise> m = ho.entries(EXERCISE_HASH_KEY);
        for (Map.Entry<Integer, Exercise> me : m.entrySet()) {
            if (me.getValue().getExId() >= result) {
                result = me.getValue().getExId() + 1;
            }
        }
        return result;
    }

}
