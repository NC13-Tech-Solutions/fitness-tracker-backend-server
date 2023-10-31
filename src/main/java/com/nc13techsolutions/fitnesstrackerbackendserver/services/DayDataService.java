package com.nc13techsolutions.fitnesstrackerbackendserver.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.nc13techsolutions.fitnesstrackerbackendserver.models.DayData;
import com.nc13techsolutions.fitnesstrackerbackendserver.repositories.DayDataRepo;

@Service
public class DayDataService {
    private final DayDataRepo dayDataRepo;

    public DayDataService(@Qualifier("redis daydata") DayDataRepo dayDataRepo) {
        this.dayDataRepo = dayDataRepo;
    }

    public int addDayData(DayData data) {
        return dayDataRepo.insertDayData(data);
    }

    public List<DayData> getAllDayData() {
        return this.dayDataRepo.getDayData();
    }

    public DayData getDayData(String postedOn) {
        return this.dayDataRepo.getDayData(postedOn);
    }

    public DayData getDayDataById(Integer ddId){
        return this.dayDataRepo.getDayDataById(ddId);
    }

    public int updateDayData(Integer ddId, DayData newData){
        return this.dayDataRepo.updateDayData(ddId, newData);
    }

    public int deleteDayData(Integer ddId){
        return this.dayDataRepo.deleteDayData(ddId);
    }

    public int getNewDayDataId(){
        return this.dayDataRepo.findHeighestDdId();
    }
}
