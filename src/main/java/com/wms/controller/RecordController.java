package com.wms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Goods;
import com.wms.entity.Record;
import com.wms.service.GoodsService;
import com.wms.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author nty
 * @since 2024-01-14
 */
@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;
    @Autowired
    private GoodsService goodsService;

    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
//        String id = (String)param.get("id");
        String storage = (String)param.get("storage");
        String goodsType = (String)param.get("goodsType");
        String name = (String)param.get("name");
//        String count = (String)param.get("count");
//        String remark = (String)param.get("remark");
        String roleId = (String)param.get("roleId");
        String userId = (String)param.get("userId");

        Page<Record> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        QueryWrapper<Record> queryWrapper = new QueryWrapper();
        queryWrapper.apply(" a.goods=b.id and b.storage=c.id and b.goodsType=d.id ");

        if ("2".equals(roleId)) {
            queryWrapper.apply(" a.user_id= "+userId);
        }

        if (StringUtils.isNotBlank(storage) && !"null".equals(storage)) {
            queryWrapper.eq("c.id",storage);
        }
        if (StringUtils.isNotBlank(goodsType) && !"null".equals(goodsType)) {
            queryWrapper.eq("d.id",goodsType);
        }
        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            queryWrapper.like("b.name",name);
        }
//        if (StringUtils.isNotBlank(count)) {
//            queryWrapper.like("b.name",name);
//        }
//        if (StringUtils.isNotBlank(remark)) {
//            queryWrapper.like("b.name",name);
//        }

        IPage result = recordService.pageCC(page, queryWrapper);
        return Result.suc(result.getRecords(), result.getTotal());
    }
    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Record record){

        Goods goods = goodsService.getById(record.getGoods());
        int n = record.getCount();
        //出库
        if ("2".equals(record.getAction())) {
            n=-n;
            record.setCount(n);
        }
        int num = goods.getCount()+n;
        goods.setCount(num);
        goodsService.updateById(goods);

        return recordService.save(record)?Result.suc():Result.fail();
    }
}
