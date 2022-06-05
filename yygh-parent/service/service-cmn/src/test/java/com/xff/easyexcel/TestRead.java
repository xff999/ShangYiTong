package com.xff.easyexcel;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;

public class TestRead {

    @Test
    public void test02(){

        //设置excel的文件路径，和文件名称
        String fileName = "D:/Java_Project/TestExcel/01.xlsx";

        //调用方法读取
        EasyExcel.read(fileName,UserData.class,new ExcelListener()).sheet().doRead();

    }
}
