package com.xff.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xff.yygh.model.cmn.Dict;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


public interface DictService extends IService<Dict> {
    // service 继承 mp 提供的基础iservice ,传入实体类操作对象

    //1.根据数据id 查询子数据列表
    List<Dict> findChildData(Long id);

    //2.导出数据字典的列表 进入用户指定的文件
    void exportData(HttpServletResponse response);

    //3.导入数据进入，数字字典中的列表
    void importData(MultipartFile file);

    //4.根据上级编码与值获取数据字典名称
    String getDictName(String dictCode, String value);

    List<Dict> findByDictCode(String dictCode);
}
