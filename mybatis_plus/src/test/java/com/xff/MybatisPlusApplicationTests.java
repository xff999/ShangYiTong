package com.xff;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xff.entity.User;
import com.xff.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class MybatisPlusApplicationTests {

    @Autowired  //住入userMapper 的自动创建对象
    private UserMapper userMapper;

    @Test
    public void findAll() {
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }
    //==========添加修改============
    //添加
    @Test
    public void testAdd() {
        User user = new User();
        user.setName("王五");
        user.setAge(20);
        user.setEmail("1243@qq.com");
        int insert = userMapper.insert(user);
        System.out.println(insert);
    }
    //修改
    @Test
    public void testUpdate() {
        User user = new User();
        user.setId(1525702301707173889L);
        user.setName("xff3");   //通过id 修改用户
        user.setAge(23);
        int count = userMapper.updateById(user);
        System.out.println(count);
    }
    //==========乐观锁============
    //测试乐观锁的version
    @Test
    public void testOptimistLocker(){
           // 先添加一个user ha版本为1  在查询修改
        User user = userMapper.selectById(1525707507387940866L); //先查询
        user.setName("haha");
          //user.setVersion(user.getVersion()+1); //mp 自动做了操作
        userMapper.updateById(user);
    }
//==========查询============
    //多个id批量查询
    @Test
    public void testSelect1() {
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
        System.out.println(users);
    }

    //简单条件查询
    @Test
    public void testSelect2() {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("name","Jack");
        columnMap.put("age",20);
        List<User> users = userMapper.selectByMap(columnMap);
        System.out.println(users);
    }

    //分页查询
    @Test
    public void testSelectPage() {
        Page<User> page = new Page(1,3);
        Page<User> userPage = userMapper.selectPage(page, null);
        //返回对象得到分页所有数据
        long pages = userPage.getPages(); //总页数
        long current = userPage.getCurrent(); //当前页
        List<User> records = userPage.getRecords(); //查询数据集合
        long total = userPage.getTotal(); //总记录数
        boolean hasNext = userPage.hasNext();  //下一页
        boolean hasPrevious = userPage.hasPrevious(); //上一页

        System.out.println(pages);
        System.out.println(current);
        System.out.println(records);
        System.out.println(total);
        System.out.println(hasNext);
        System.out.println(hasNext);
        System.out.println(hasPrevious);
        }

    @Test
    public void testSelectMapsPage() {
          //Page不需要泛型
        Page<Map<String, Object>> page = new Page<>(1, 5);
        Page<Map<String, Object>> pageParam = userMapper.selectMapsPage(page, null);
        List<Map<String, Object>> records = pageParam.getRecords();
        records.forEach(System.out::println);
        System.out.println(pageParam.getCurrent());
        System.out.println(pageParam.getPages());
        System.out.println(pageParam.getSize());
        System.out.println(pageParam.getTotal());
        System.out.println(pageParam.hasNext());
        System.out.println(pageParam.hasPrevious());
    }
//======================删除=============================
    @Test
    public void testDeleteById(){
        int result = userMapper.deleteById(5L);
        System.out.println(result);
    }
    @Test
    public void testDeleteBatchIds() {
        int result = userMapper.deleteBatchIds(Arrays.asList(8, 9, 10));
        System.out.println(result);
    }
    @Test
    public void testDeleteByMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "Helen");
        map.put("age", 18);
        int result = userMapper.deleteByMap(map);
        System.out.println(result);
    }

    //================逻辑删除==========================
    //物理删除：真实删除，将对应数据从数据库中删除，之后查询不到此条被删除数据
    //逻辑删除：假删除，将对应数据中代表是否被删除字段状态修改为“被删除状态”，之后在数据库中仍旧能看到此条数据记录
//逻辑删除的使用场景：
//可以进行数据恢复
//有关联数据，不便删除


    //流程  数据库添加 deleted字段  实体类添加deleted 字段，并加上 @TableLogic 注解
               //在插入时 hander 中给他的值自动填充 设为0，表示存在
    // 先添加，在逻辑删除，在查询
    @Test
    public void testLogicDelete() {
        int result = userMapper.deleteById(1525713547449929730L);
        System.out.println(result);
    }
    //测试逻辑删除后的查询
    @Test
    public void testLogicDeleteSelect() {
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }









}
