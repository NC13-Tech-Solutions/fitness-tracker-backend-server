package com.nc13techsolutions.fitnesstrackerbackendserver.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nc13techsolutions.fitnesstrackerbackendserver.models.DayData;
import com.nc13techsolutions.fitnesstrackerbackendserver.services.DayDataService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/daydata")
public class DayDataController {
    public static final int DAYDATA_ID_START = 1000;

    private final DayDataService dayDataService;

    @PostMapping("/new")
    public int addDayData(DayData data) {
        int id = dayDataService.getNewDayDataId() > 0 ? dayDataService.getNewDayDataId() : DAYDATA_ID_START;
        return dayDataService.addDayData(new DayData(id, data.getPostedOn(), data.getPostedBy(), data.getModifiedOn(),
                data.getModifiedBy(), data.getUserWeight(), data.getWorkouts()));
    }

    @GetMapping("/all")
    public List<DayData> getAllDayData() {
        return dayDataService.getAllDayData();
    }

    @GetMapping("/day/{postedOn}")
    public DayData getDayDataOnDate(@PathVariable String postedOn) {
        return dayDataService.getDayData(postedOn);
    }

    @GetMapping("/{ddId}")
    public DayData getDayDataById(@PathVariable Integer ddId) {
        return dayDataService.getDayDataById(ddId);
    }

    @PutMapping("/{ddId}")
    public int updateDayData(@PathVariable Integer ddId, @RequestBody DayData newData) {
        return dayDataService.updateDayData(ddId, new DayData(ddId, newData.getPostedOn(), newData.getPostedBy(),
                newData.getModifiedOn(), newData.getModifiedBy(), newData.getUserWeight(), newData.getWorkouts()));
    }

    @DeleteMapping("/{ddId}")
    public int deleteDayData(@PathVariable Integer ddId){
        return dayDataService.deleteDayData(ddId);
    }
}
