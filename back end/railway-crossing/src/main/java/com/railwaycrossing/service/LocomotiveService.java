package com.railwaycrossing.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.railwaycrossing.entity.Locomotive;
import com.railwaycrossing.exception.DeleteException;
import com.railwaycrossing.exception.InsertException;
import com.railwaycrossing.exception.UpdateException;
import com.railwaycrossing.utils.PageUtils;

import java.util.List;

public interface LocomotiveService extends IService<Locomotive> {
    JSONObject insert(JSONObject message) throws InsertException;
    JSONObject delete(Integer locomotiveId) throws DeleteException;
    JSONObject deleteBatch(List<Integer> locomotiveIds) throws DeleteException;
    JSONObject updateById(JSONObject message) throws UpdateException;
    PageUtils queryPage(JSONObject message);
    PageUtils queryPageByCondition(JSONObject message);

    JSONObject listLoco(JSONObject message);


}
