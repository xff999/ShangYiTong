package com.xff.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xff.entity.User;
import org.springframework.stereotype.Repository;

@Repository
//添加这个注解表示，是数据库层面的 自动创建语句对象
public interface UserMapper extends BaseMapper<User> {
                //重要的是接口  继承了BaseMapper，内部封装了基本的语句
}