package com.sakiprime.DrivenFear.service;

import com.sakiprime.DrivenFear.entity.AICallRequestDTO;
import com.sakiprime.DrivenFear.entity.AICallTaskEntity;
import com.sakiprime.DrivenFear.mapper.AICallTaskMapper;
import com.sakiprime.DrivenFear.mapper.UserMapper;
import com.sakiprime.DrivenFear.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskConsumerServiceImpl implements TaskConsumerService {
    private final AICallTaskMapper aiCallTaskMapper;
    private final UserMapper userMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)//事务：扣token和存储订单强一致
    public boolean saveOrderAndDeduction(AICallRequestDTO request) {
        try{
            AICallTaskEntity task = new AICallTaskEntity(request);
            task.setUpdateTime(TimeUtil.nowSecond());
            //构造订单实体，并保存订单
            boolean insertSuccess = aiCallTaskMapper.insert(task)>0;
            //MySQL用户数据扣token
            boolean deductSuccess = userMapper.deductToken(task.getUserId(), task.getTokenCost())>0;
            if(!insertSuccess){
                log.error("存储任务订单失败,用户:{},订单号:{}",task.getUserId(),task.getOrderId());
                throw new RuntimeException("存储任务订单失败");
            }
            if(!deductSuccess){
                log.error("MySQL用户token扣除失败,用户:{},订单号:{}",task.getUserId(),task.getOrderId());
                throw new RuntimeException("MySQL用户token扣除失败");
            }

        }
        catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            //TODO 探寻不手动回滚/不拆分业务逻辑/不自注入的事务管理方法
            return false;
        }
        return true;
    }

    @Override
    public boolean markFailedTask(AICallTaskEntity task) {
    String userId = task.getUserId();
    task.setTaskStatus("FAILED");
    task.setUpdateTime(TimeUtil.nowSecond());
    task.setRequireManual(true);
    boolean markSuccess = userMapper.updateTaskNeedManual(userId,true)>0;
    boolean insertSuccess = aiCallTaskMapper.insert(task)>0;
    return markSuccess && insertSuccess;
    }


}
