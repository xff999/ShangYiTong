package com.xff.easyexcel;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

//读操作的监听器
public class ExcelListener extends AnalysisEventListener<UserData> {


    @Override
    public void invoke(UserData data, AnalysisContext context) {
          //一行一行的读取表格的内容，从第二行开始
        System.out.println(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
         //读取之后执行
    }

    @Override       //读取表头的信息
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头信息"+headMap);
    }
}
