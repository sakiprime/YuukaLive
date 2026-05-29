package com.sakiprime.DrivenFear.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sakiprime.DrivenFear.entity.AICallTaskEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AICallTaskMapper extends BaseMapper<AICallTaskEntity> {
    @Select("SELECT user_id, ai_model, task_type, text_message, image_url, video_url, update_time " +
            "FROM ai_call_task " +
            "WHERE is_deleted = 0 " +
            "ORDER BY update_time DESC")
    List<AICallTaskEntity> selectSimpleList();
}
