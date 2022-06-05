package com.xff.yygh.cmn.service.impl;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xff.yygh.cmn.listener.DictListener;
import com.xff.yygh.cmn.mapper.DictMapper;
import com.xff.yygh.cmn.service.DictService;
import com.xff.yygh.model.cmn.Dict;
import com.xff.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//创建service接口的实现类
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict>
                                    implements DictService {
                      //继承mp 封装的 impl 传入参数，，实现自己的service接口
   @Autowired
  private DictMapper dictMapper;
    //底层自动封装了 baseMappe

    //1.根据数据i查询数据列表
    @Override
   //@Cacheable(value = "dict", keyGenerator = "keyGenerator") //value  是规则   添加缓存 的注解，第一次查询得到放入缓存中
    public List<Dict> findChildData(Long id) {
         //创建查询的条件
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);

        List<Dict> dictList = baseMapper.selectList(wrapper);
         //向 list 集合中每个dict 对象中设置 hasChildren 的新值
        for ( Dict dict: dictList ){
            Long DictId = dict.getId();
            boolean isChild = this.isChildren(DictId);
            dict.setHasChildren(isChild);
        }
        return dictList;
    }
    //判断id 下面是否有子节点
    private boolean isChildren (Long id){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        Integer count = baseMapper.selectCount(wrapper);
        return count>0 ;
    }

    //2.导出数据字典的列表 进入用户指定的文件
    @Override
    public void exportData(HttpServletResponse response) {


        //设置下载的信息
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = "dict";  //设置文件名称
        response.setHeader("Content-disposition",
                          "attachment;filename="+ fileName + ".xlsx");

        //查询数据库，得到信息
        List<Dict> dictList = baseMapper.selectList(null);
           // Dict --->DictEeVo
        List<DictEeVo> dictVoList = new ArrayList<>(dictList.size());
        for(Dict dict : dictList) {
            DictEeVo dictVo = new DictEeVo();
            BeanUtils.copyProperties(dict, dictVo);
            dictVoList.add(dictVo);
        }

        try {
        //调用写的方法，进行写进文件中
            EasyExcel.write( response.getOutputStream(), DictEeVo.class)
                     .sheet("dict")
                     .doWrite(dictVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //3.导入数据进入，数字字典中的列表
    @Override
   // @CacheEvict(value = "dict", allEntries=true)
            //导入数据，缓存要变化，清空
    public void importData(MultipartFile file) {
         //读取 excel 表格数据 需要一个监听器
        try {
            EasyExcel.read(file.getInputStream(),DictEeVo.class,new DictListener(dictMapper))
                     .sheet().doRead();  //取出excel 添加进入数据库，
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //4.根据上级编码与值获取数据字典名称
    //  @Cacheable(value = "dict",keyGenerator = "keyGenerator")  //开启redis 缓存的
    @Override
    public String getDictName(String dictCode, String value) {
          //如果value能唯一定位数据字典，dictCode可以传空，例如：省市区的value值能够唯一确定
        if(StringUtils.isEmpty(dictCode)) {
            Dict dict = baseMapper.selectOne(new QueryWrapper<Dict>().eq("value", value));
            if(null != dict) {
                return dict.getName();
            }
        } else {
            //根据dictcode 查询 dict 对象 得到他的id
            Dict codeDict = this.getByDictCode(dictCode);

            if(null == codeDict) return "";
            Dict finddict = dictMapper.selectOne(
                            new QueryWrapper<Dict>()
                                    .eq("parent_id",dictCode )
                                    .eq("value", value));
            if(null != finddict) {
                return finddict.getName();
            }
        }
        return "";
    }

     //根据传入的 dictcode 获得 这个数据字典
    private Dict getByDictCode(String dictCode){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code", dictCode);
        Dict codeDict = baseMapper.selectOne(wrapper);
        return  codeDict;
    }

    //5.根据dictCode 获取下级节点  (做到省市的联动查询，
    // 根据省的 dictcode ，得到查询省份信息， 根据id 得到子节点信息
    @Override
    public List<Dict> findByDictCode(String dictCode) {
        //根据dictcode  获得对应的 id 值
        Dict dict = this.getByDictCode(dictCode);
        //根据 字典的 id 查询子节点 的数据
        List<Dict> childData = this.findChildData(dict.getId());

        return childData;
    }

}
