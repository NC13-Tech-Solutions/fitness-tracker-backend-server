package com.nc13techsolutions.fitnesstrackerbackendserver.repositories.redis;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.nc13techsolutions.fitnesstrackerbackendserver.models.Exercise;
import com.nc13techsolutions.fitnesstrackerbackendserver.repositories.ExerciseRepo;

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
    public Exercise getExerciseById(Integer exId) {
        HashOperations<String, Integer, Exercise> ho = template.opsForHash();
        return ho.get(EXERCISE_HASH_KEY, exId);
    }

    @Override
    public int updateExercise(Integer exId, Exercise exercise) {
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
    public int deleteExercise(Integer exId) {
        /*
         * TODO: This is only for testing. On production version, either there will not
         * be any option to delete exercises, as we already have an option to disable
         * them, or we'll have to check if exId is present anywhere else and remove
         * those entries as well
         */
        int result = checkIfExerciseExists(new Exercise(exId, "", ""));
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
                if (me.getValue().getExId().compareTo(exercise.getExId()) == 0) {
                    return 2;
                }
                return 1;
            } else if (me.getValue().getExId().compareTo(exercise.getExId()) == 0) {
                result = 0;
            }
        }

        return result;

    }

    public Integer findHeighestExId() {
        int result = 0;
        HashOperations<String, Integer, Exercise> ho = template.opsForHash();
        Map<Integer, Exercise> m = ho.entries(EXERCISE_HASH_KEY);
        for (Map.Entry<Integer, Exercise> me : m.entrySet()) {
            if (me.getValue().getExId().intValue() >= result) {
                result = me.getValue().getExId().intValue() + 1;
            }
        }
        return result;
    }

}
