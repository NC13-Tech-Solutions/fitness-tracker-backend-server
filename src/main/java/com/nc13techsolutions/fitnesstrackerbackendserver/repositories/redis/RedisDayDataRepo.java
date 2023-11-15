package com.nc13techsolutions.fitnesstrackerbackendserver.repositories.redis;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.nc13techsolutions.fitnesstrackerbackendserver.models.DayData;
import com.nc13techsolutions.fitnesstrackerbackendserver.repositories.DayDataRepo;

@Repository("redis daydata")
@EnableAutoConfiguration
public class RedisDayDataRepo implements DayDataRepo {

    public static final String DAYDATA_HASH_KEY = "DayData";
    @Autowired
    private RedisTemplate<String, DayData> template;

    @Override
    public int insertDayData(DayData data) {
        int result = checkIfDayDataExists(data);
        if (result == -1) {
            HashOperations<String, Integer, DayData> ho = template.opsForHash();
            ho.put(DAYDATA_HASH_KEY,data.getDdId(), data);

            return 1;
        } else if (result == 1) {
            return 0;
        }

        return -1;
    }

    @Override
    public List<DayData> getDayData() {
        HashOperations<String, Integer, DayData> ho = template.opsForHash();
        return ho.values(DAYDATA_HASH_KEY);
    }

    @Override
    public DayData getDayData(String postedOn) {

        HashOperations<String, Integer, DayData> ho = template.opsForHash();
        Map<Integer, DayData> m = ho.entries(DAYDATA_HASH_KEY);

        for (Map.Entry<Integer, DayData> mdd : m.entrySet()) {
            if (mdd.getValue().getPostedOn().trim().equalsIgnoreCase(postedOn.trim())) {
                return mdd.getValue();
            }
        }

        return null;
    }

    @Override
    public DayData getDayDataById(Integer ddId) {
        HashOperations<String, Integer, DayData> ho = template.opsForHash();
        return ho.get(DAYDATA_HASH_KEY, ddId);
    }

    @Override
    public int updateDayData(Integer ddId, DayData newData) {
        int result = checkIfDayDataExists(newData);
        if (result == 0 || result == 2) {
            HashOperations<String, Integer, DayData> ho = template.opsForHash();
            ho.put(DAYDATA_HASH_KEY, ddId, newData);

            return 1;
        } else if (result == 1) {
            return 0;
        }

        return -1;
    }

    @Override
    public int checkIfDayDataExists(DayData data) {
        int result = -1;
        HashOperations<String, Integer, DayData> ho = template.opsForHash();
        Map<Integer, DayData> m = ho.entries(DAYDATA_HASH_KEY);

        for (Map.Entry<Integer, DayData> mdd : m.entrySet()) {
            if (mdd.getValue().getPostedOn().trim().equalsIgnoreCase(data.getPostedOn().trim())) {
                if (mdd.getValue().getDdId().compareTo(data.getDdId()) == 0) {
                    return 2;
                }
                return 1;
            } else if (mdd.getValue().getDdId().compareTo(data.getDdId()) == 0) {
                result = 0;
            }
        }

        return result;
    }

    @Override
    public Integer findHeighestDdId() {
        int result = 0;
        HashOperations<String, Integer, DayData> ho = template.opsForHash();
        Map<Integer, DayData> m = ho.entries(DAYDATA_HASH_KEY);
        for (Map.Entry<Integer, DayData> mdd : m.entrySet()) {
            if (mdd.getValue().getDdId().intValue() >= result) {
                result = mdd.getValue().getDdId().intValue() + 1;
            }
        }
        return result;
    }

    @Override
    public int deleteDayData(Integer ddId) {
        int result = checkIfDayDataExists(new DayData(ddId, "", 0, "", 0, 0, null));
        if (result == 0) {
            HashOperations<String, Integer, DayData> ho = template.opsForHash();
            ho.delete(DAYDATA_HASH_KEY, ddId);
            return 1;
        }
        return -1;
    }

}
