package com.sakiprime.yuukalikejava.controller;

import com.sakiprime.yuukalikejava.entity.MonthRecordEntity;
import com.sakiprime.yuukalikejava.entity.TempRecordEntity;
import com.sakiprime.yuukalikejava.entity.DayRecordEntity;
import com.sakiprime.yuukalikejava.service.YuukalikeService;
import com.sakiprime.yuukalikejava.util.Result;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class YuukalikeController {
    private final YuukalikeService yuukalikeService;
    public YuukalikeController(YuukalikeService yuukalikeService) {
        this.yuukalikeService = yuukalikeService;
    }

    @PostMapping("/savejson")
    public Result<Void> saveJson(@RequestBody TempRecordEntity tempRecord) {

        boolean success = yuukalikeService.updateDayRecord(tempRecord);

        return (success)?Result.success(null):Result.fail();
    }
    @GetMapping("/getjsondata")
    public Result<DayRecordEntity> getJsonData(@RequestParam String dayDate) {

    DayRecordEntity dayRecordEntity = yuukalikeService.getDayRecord(dayDate);

    return Result.success(dayRecordEntity);//此方法不会出错。
    }
    @GetMapping("/getmonthjsondata")
    public Result<MonthRecordEntity> getMonthJsonData(@RequestParam String monthDate) {

        MonthRecordEntity monthRecordEntity = yuukalikeService.getMonthRecord(monthDate);

        return Result.success(monthRecordEntity);//此方法不会出错。
    }
    @GetMapping("/getitemjsondata")
    public Result<List<TempRecordEntity>> getItemJsonData(@RequestParam String itemDate) {

    List<TempRecordEntity> tempRecordEntityList = yuukalikeService.getItemRecord(itemDate);

    return Result.success(tempRecordEntityList);
    }//还需要改，我没加URL数据传输。
}
