package com.nc13techsolutions.fitnesstrackerbackendserver.repositories;

import java.util.List;

import com.nc13techsolutions.fitnesstrackerbackendserver.models.DayData;

public interface DayDataRepo {
    /**
     * Adds a new day data
     * 
     * @param data
     * @return {@code 1} if insert is successful; {@code -1} if not successful;
     *         {@code 0} if another day
     *         data is already posted on that day
     */
    int insertDayData(DayData data);

    /**
     * Gets all day data
     * 
     * @return list of all day data
     */
    List<DayData> getDayData();

    /**
     * Finds the day data posted on {@code postedOn} date
     * 
     * @param postedOn date on which the day data was posted
     * @return {@code DayData} object or {@code null} if daydata doesn't exist on
     *         that date
     */
    DayData getDayData(String postedOn);

    /**
     * Finds the day data by Id
     * 
     * @param ddId Id of the {@code DayData} object
     * @return {@code DayData} object or {@code null} if daydata doesn't exist on
     *         that date
     */
    DayData getDayDataById(Integer ddId);

    /**
     * Updates the day data
     * 
     * @param ddId    Id of the previous {@code DayData}
     * @param newData the new {@code DayData}
     * @return {@code 1} if update is successful; {@code -1} if previous DayData was
     *         not found; {@code 0} if another day
     *         data is already posted on that day
     * @see #checkIfDayDataExists(DayData)
     */
    int updateDayData(Integer ddId, DayData newData);

    /**
     * Checks if {@code DayData} already exists
     * 
     * @param data
     * @return {@code 2} if Id and postedOn date matches; {@code 1} if only postedOn
     *         date matches; {@code 0} if only Id matches; {@code -1} if there is no
     *         match
     */
    int checkIfDayDataExists(DayData data);
    
    /**
     * Finds the heighest day data Id
     * 
     * @return {@code 0} if there are no day data; heighest ID value
     */
    public Integer findHeighestDdId();

    /**
     * Deletes the day data, if it exists
     * 
     * @param ddId ID of the existing day data
     * @return {@code 1} if delete is successful; {@code -1} if day data is not found;
     */
    int deleteDayData(Integer ddId);
}
