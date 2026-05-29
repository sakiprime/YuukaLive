package com.sakiprime.yuukalikejava.service;

import com.sakiprime.yuukalikejava.entity.DayRecordEntity;
import com.sakiprime.yuukalikejava.entity.MonthRecordEntity;
import com.sakiprime.yuukalikejava.entity.TempRecordEntity;

import java.util.List;

public interface YuukalikeService {

    // ====================== 日记录 ======================
    /**
     * 判断日记录是否存在
     */
    boolean existsDayRecord(String dayDate);

    /**
     * 获取日记录
     */
    DayRecordEntity getDayRecord(String dayDate);

    /**
     * 新增日记录
     */
    boolean addDayRecord(DayRecordEntity dayRecord);

    /**
     * 更新日记录
     */
    boolean updateDayRecord(TempRecordEntity tempRecord);

    /**
     * 删除日记录
     */
    boolean deleteDayRecord(String dayDate);


    // ====================== 月记录 ======================
    /**
     * 判断月记录是否存在
     */
    boolean existsMonthRecord(String monthDate);

    /**
     * 获取月记录
     */
    MonthRecordEntity getMonthRecord(String monthDate);

    /**
     * 新增月记录
     */
    boolean addMonthRecord(MonthRecordEntity monthRecord);

    /**
     * 更新月记录
     */
    boolean updateMonthRecord(TempRecordEntity tempRecord);

    /**
     * 删除月记录
     *
     */
    boolean deleteMonthRecord(String monthDate);


    // ====================== 流水记录 ======================
    /**
     * 获取流水记录
     */
    List<TempRecordEntity> getItemRecord(String tempDate);

    /**
     * 新增流水记录
     */
    boolean addItemRecord(TempRecordEntity tempRecord);

    /**
     * 更新流水记录
     */
    boolean updateItemRecord(TempRecordEntity tempRecord);

    /**
     * 删除流水记录
     */
    boolean deleteItemRecord(Integer itemId);
}