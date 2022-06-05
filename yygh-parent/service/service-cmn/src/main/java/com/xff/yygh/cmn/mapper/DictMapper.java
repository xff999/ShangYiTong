package com.xff.yygh.cmn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xff.yygh.model.cmn.Dict;

public interface DictMapper extends BaseMapper<Dict> {
    //基础的医院的增删改查mapper映射语句  继承mp 的 传入实体类 (要在pom 中引入model 模块）
}
