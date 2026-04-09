package com.sakiprime.PowerfulEmpathy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sakiprime.PowerfulEmpathy.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}
