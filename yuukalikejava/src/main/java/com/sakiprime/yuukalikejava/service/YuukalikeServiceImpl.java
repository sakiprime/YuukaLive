package com.sakiprime.yuukalikejava.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sakiprime.yuukalikejava.entity.DayRecordEntity;
import com.sakiprime.yuukalikejava.entity.MonthRecordEntity;
import com.sakiprime.yuukalikejava.entity.TempRecordEntity;
import com.sakiprime.yuukalikejava.mapper.DayRecordMapper;
import com.sakiprime.yuukalikejava.mapper.MonthRecordMapper;
import com.sakiprime.yuukalikejava.mapper.TempRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class YuukalikeServiceImpl implements YuukalikeService {
    private final DayRecordMapper dayRecordMapper;
    private final MonthRecordMapper monthRecordMapper;
    private final TempRecordMapper tempRecordMapper;

    public YuukalikeServiceImpl(DayRecordMapper dayRecordMapper,
                                MonthRecordMapper monthRecordMapper,
                                TempRecordMapper tempRecordMapper) {
        this.dayRecordMapper = dayRecordMapper;
        this.monthRecordMapper = monthRecordMapper;
        this.tempRecordMapper = tempRecordMapper;
    }//构造器注入，可以声明依赖的final类型，比@Autowired更好。

    // ===================== 你要的两个私有时间字符串 =====================
    private final DateTimeFormatter FORMAT_DAY = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter FORMAT_MONTH = DateTimeFormatter.ofPattern("yyyy-MM");

    // 获取今天：yyyy-MM-dd
    private String getToday() {
        return LocalDate.now().format(FORMAT_DAY);
    }

    // 获取本月：yyyy-MM
    private String getThisMonth() {
        return LocalDate.now().format(FORMAT_MONTH);
    }

    // ====================== 日记录 ======================
    @Override
    public boolean existsDayRecord(String dayDate) {
        return dayRecordMapper.selectById(dayDate)!=null;

    }

    @Override
    public DayRecordEntity getDayRecord(String dayDate) {
        if (!existsDayRecord(dayDate)) {
            DayRecordEntity dayRecord = new DayRecordEntity();
            dayRecord.setDayDate(dayDate);
            addDayRecord(dayRecord);
        }
        return dayRecordMapper.selectById(dayDate);

    }

    @Override
    public boolean addDayRecord(DayRecordEntity dayRecord){
        return dayRecordMapper.insert(dayRecord)>0;
    }

    @Override
    public boolean updateDayRecord(TempRecordEntity tempRecord) {
        DayRecordEntity dayRecord = getDayRecord(getToday());

        float count = tempRecord.getCount();
        String type = tempRecord.getType();

        if (count > 0) {
            // ===================== 正数 = 支出 =====================
            dayRecord.setDayTotalExpenditure(dayRecord.getDayTotalExpenditure() + count);

            switch (type) {
                case "A": // 早餐
                    dayRecord.setBreakfast(dayRecord.getBreakfast() + count);
                    break;
                case "B": // 午餐
                    dayRecord.setLunch(dayRecord.getLunch() + count);
                    break;
                case "C": // 晚餐
                    dayRecord.setDinner(dayRecord.getDinner() + count);
                    break;
                case "D": // 零食/饮料
                    dayRecord.setSnacks(dayRecord.getSnacks() + count);
                    break;
                case "E": // 娱乐费用
                    dayRecord.setEntertainment(dayRecord.getEntertainment() + count);
                    break;
                case "F": // 其他/交通
                    dayRecord.setOtherTransportation(dayRecord.getOtherTransportation() + count);
                    break;
                default:
                    break;
            }
        } else if (count < 0) {
            // ===================== 负数 = 收入（正值） =====================
            float income = Math.abs(count);
            dayRecord.setDayTotalIncome(dayRecord.getDayTotalIncome() + income);

            if ("G".equals(type)) {
                dayRecord.setVariousIncome(dayRecord.getVariousIncome() + income);
            }
        }

        dayRecord.setDayTotalRecords(dayRecord.getDayTotalRecords() + 1);

        dayRecord.setDayBalance(dayRecord.getDayTotalIncome() - dayRecord.getDayTotalExpenditure());

        return dayRecordMapper.updateById(dayRecord) > 0;
    }

    @Override
    public boolean deleteDayRecord(String dayDate){

        return dayRecordMapper.deleteById(dayDate)>0;
    }

    // ====================== 月记录 ======================
    @Override
    public boolean existsMonthRecord(String monthDate){

        return monthRecordMapper.selectById(monthDate)!=null;
    }

    @Override
    public MonthRecordEntity getMonthRecord(String monthDate){
        if (!existsMonthRecord(monthDate)) {
            MonthRecordEntity monthRecord = new MonthRecordEntity();
            monthRecord.setMonthDate(monthDate);
            addMonthRecord(monthRecord);
        }

        return monthRecordMapper.selectById(monthDate);
    }

    @Override
    public boolean addMonthRecord(MonthRecordEntity monthRecord){

        return monthRecordMapper.insert(monthRecord)>0;
    }

    @Override
    public boolean updateMonthRecord(TempRecordEntity tempRecord) {
        // 1. 获取本月月记录（不存在自动创建，和日记录同款逻辑）
        MonthRecordEntity monthRecord = getMonthRecord(getThisMonth());

        // 2. 取出临时记录数据
        float count = tempRecord.getCount();
        String type = tempRecord.getType();

        // 3. 核心收支逻辑
        if (count > 0) {
            // ===================== 正数 = 支出 =====================
            monthRecord.setMonthTotalExpenditure(monthRecord.getMonthTotalExpenditure() + count);

            // 类型映射：累加对应月度分类
            switch (type) {
                case "A": // 早餐
                    monthRecord.setMonthBreakfast(monthRecord.getMonthBreakfast() + count);
                    break;
                case "B": // 午餐
                    monthRecord.setMonthLunch(monthRecord.getMonthLunch() + count);
                    break;
                case "C": // 晚餐
                    monthRecord.setMonthDinner(monthRecord.getMonthDinner() + count);
                    break;
                case "D": // 零食/饮料
                    monthRecord.setMonthSnacks(monthRecord.getMonthSnacks() + count);
                    break;
                case "E": // 娱乐费用
                    monthRecord.setMonthEntertainment(monthRecord.getMonthEntertainment() + count);
                    break;
                case "F": // 其他/交通
                    monthRecord.setMonthOtherTransportation(monthRecord.getMonthOtherTransportation() + count);
                    break;
                default:
                    break;
            }
        } else if (count < 0) {
            // ===================== 负数 = 收入（自动转正值） =====================
            float income = Math.abs(count);
            monthRecord.setMonthTotalIncome(monthRecord.getMonthTotalIncome() + income);

            // G = 各项收入，累加月度杂项收入
            if ("G".equals(type)) {
                monthRecord.setMonthVariousIncome(monthRecord.getMonthVariousIncome() + income);
            }
        }

        // 4. 自动计算月度结余
        monthRecord.setMonthBalance(monthRecord.getMonthTotalIncome() - monthRecord.getMonthTotalExpenditure());

        // 5. 执行数据库更新
        return monthRecordMapper.updateById(monthRecord) > 0;
    }

    @Override
    public boolean deleteMonthRecord(String monthDate){

        return monthRecordMapper.deleteById(monthDate)>0;
    }

    // ====================== 流水记录 ======================
    @Override
    public List<TempRecordEntity> getItemRecord(String tempDate){
        LambdaQueryWrapper<TempRecordEntity> queryWrapper = new LambdaQueryWrapper<>();
        // 匹配实体类的tempDate。
        queryWrapper.eq(TempRecordEntity::getTempDate, tempDate);
        return tempRecordMapper.selectList(queryWrapper);
    }

    @Override
    public boolean addItemRecord(TempRecordEntity tempRecord)
    {
        return tempRecordMapper.insert(tempRecord)>0;
    }

    @Override
    public boolean updateItemRecord(TempRecordEntity tempRecord){
        return tempRecordMapper.updateById(tempRecord)>0;
    }

    @Override
    public boolean deleteItemRecord(Integer itemId){
        return tempRecordMapper.deleteById(itemId)>0;
    }
}