package com.xff.easyexcel;


import com.alibaba.excel.EasyExcel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestWrite {

    @Test
    public  void Test01() {
        //设置excel的文件路径，和文件名称
        String fileName = "D:\\Java_Project\\TestExcel\\01.xlsx";

        //创建写的数据内容
        List<UserData> list = new ArrayList<>();

        for (int i = 0; i <10 ; i++) {
            UserData data = new UserData();
            data.setUid(i);
            data.setUsername("名字 "+i);
            list.add(data);   //放入list
        }
       // System.out.println(list.get(3));

        //3.调用方法，实现写操作
        EasyExcel.write(fileName,UserData.class).sheet("用户信息")
                  .doWrite(list);  //写 在哪，什么类型，sheet工作区，些什么内容


    }
}
