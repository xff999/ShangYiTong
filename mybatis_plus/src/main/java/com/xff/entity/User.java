package com.xff.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
public class User {
    @TableId(type = IdType.ASSIGN_ID)  //关于主键生成的策略
    private Long id;

    private String name;
    private Integer age;
    private String email;


    @TableField(fill = FieldFill.INSERT)  //自动填充的功能 在什么时候生效 insert
    private Date createTime;  //是数据库是下划线create_time

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime; //update_time

    @Version  //添加注解
    @TableField(fill = FieldFill.INSERT)
    private Integer version; // 高并发的乐观锁 加版本号字段限制

    @TableLogic  //逻辑删除的字段注解
    @TableField(fill = FieldFill.INSERT)  //自动填充的功能 在什么时候生效 insert
    private Integer deleted;
}