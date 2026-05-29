package com.sakiprime.DrivenFear.controller;

import com.sakiprime.DrivenFear.annotation.ApiRateLimit;
import com.sakiprime.DrivenFear.annotation.RequireRole;
import com.sakiprime.DrivenFear.entity.AICallRequestDTO;
import com.sakiprime.DrivenFear.service.aicall.AITaskFactory;
import com.sakiprime.DrivenFear.service.aicall.AITaskStrategy;
import com.sakiprime.DrivenFear.common.util.Result;
import com.sakiprime.DrivenFear.service.aicall.impl.TextTaskStrategy;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * aicall控制器
 *
 * @author 凋零
 * @since 2026/05/04
 */
@RestController
@RequestMapping("/ai")
public class AICallController {
    private final AITaskFactory aiTaskFactory;

    /**
     * aicall控制器
     *
     * @param aiTaskFactory ai任务工厂
     */
    public AICallController(AITaskFactory aiTaskFactory) {
        this.aiTaskFactory = aiTaskFactory;
    }

    /**
     * 创建任务
     *
     * @param request 请求
     * @return {@link Result }<{@link Void }>
     */
    @PostMapping("/tasks")
    @ApiRateLimit(interFace = "createTask")
    @RequireRole
    public Result<Void> createTask(@RequestBody AICallRequestDTO request){//策略模式
    AITaskStrategy strategy = aiTaskFactory.getStrategy(request.getTaskType());
    boolean isSuccess = strategy.execute(request);
    if (!isSuccess) { //调用失败的情况下Redis不会扣款，也不会推送MQ消息。
        return Result.fail(500,"调用失败");
    }
        return Result.success("任务创建成功，排队中",null);
    }

    /**
     * 获取文本模型
     *
     * @return {@link Result }<{@link Map }<{@link String },{@link Integer }>>
     */
    @GetMapping("/tasks/textmodel")
    @ApiRateLimit(interFace = "getTextModel")
    @RequireRole
    public Result<Map<String,Integer>> getTextModel(){
        //显而易见地这里可以根据任务类型做个多态，但是我累了....
        return Result.success(TextTaskStrategy.MODEL_COST_MAP);
    }
}
