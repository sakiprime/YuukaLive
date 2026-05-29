package com.sakiprime.DrivenFear.service;

import com.sakiprime.DrivenFear.entity.AICallRequestDTO;
import com.sakiprime.DrivenFear.entity.AICallTaskEntity;

public interface TaskConsumerService {
    boolean saveOrderAndDeduction(AICallRequestDTO request);
    boolean markFailedTask(AICallTaskEntity task);
}
