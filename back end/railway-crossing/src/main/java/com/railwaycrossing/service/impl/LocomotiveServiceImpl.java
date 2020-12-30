package com.railwaycrossing.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.railwaycrossing.constants.Constants;
import com.railwaycrossing.dao.LocomotiveDao;
import com.railwaycrossing.dao.UserDao;
import com.railwaycrossing.entity.Locomotive;
import com.railwaycrossing.exception.DeleteException;
import com.railwaycrossing.exception.InsertException;
import com.railwaycrossing.exception.UpdateException;
import com.railwaycrossing.service.LocomotiveService;
import com.railwaycrossing.utils.JSONUtil;
import com.railwaycrossing.utils.PageUtils;
import com.railwaycrossing.utils.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service("locomotiveService")
public class LocomotiveServiceImpl extends ServiceImpl<LocomotiveDao, Locomotive> implements LocomotiveService {


    @Resource
    private LocomotiveDao locomotiveDao;

    @Override
    public JSONObject insert(JSONObject message) throws InsertException {
        Locomotive locomotive = new Locomotive();
        //各种属性
        locomotive.setLocomotiveName(message.getString("locomotiveId"));
        locomotive.setLocomotiveGPS(message.getString("locomotiveGPS"));
        locomotive.setNextCrossingId(message.getInteger("nextCrossingId"));
        locomotive.setTrackId(message.getString("trackId"));
        locomotive.setResponseStatus(message.getBoolean("responseStatus"));
        int result = baseMapper.insert(locomotive);

        if (result != 1) {
            throw new InsertException();
        } else {
            return JSONUtil.successJSON();
        }
    }

    @Override
    public JSONObject delete(Integer locomotiveId) throws DeleteException {
        int result = baseMapper.deleteById(locomotiveId);

        if (result != 1) {
            throw new DeleteException();
        } else {
            return JSONUtil.successJSON();
        }
    }

    @Override
    public JSONObject deleteBatch(List<Integer> locomotiveIds) throws DeleteException {
        int result = baseMapper.deleteBatchIds(locomotiveIds);

        if (result != locomotiveIds.size()) {
            throw new DeleteException();
        } else {
            return JSONUtil.successJSON();
        }
    }

    @Override
    public JSONObject updateById(JSONObject message) throws UpdateException {
        Locomotive locomotive = new Locomotive();
        //各种属性
        locomotive.setLocomotiveId(message.getInteger("locomotiveId"));
        locomotive.setLocomotiveName(message.getString("locomotiveId"));
        locomotive.setLocomotiveGPS(message.getString("locomotiveGPS"));
        locomotive.setNextCrossingId(message.getInteger("nextCrossingId"));
        locomotive.setTrackId(message.getString("trackId"));
        locomotive.setResponseStatus(message.getBoolean("responseStatus"));
        int result = baseMapper.update(locomotive, new UpdateWrapper<Locomotive>().eq("locomotiveId",locomotive.getLocomotiveId()));
        if (result != 1) {
            throw new UpdateException();
        } else {
            return JSONUtil.successJSON();
        }
    }

    @Override
    public PageUtils queryPage(JSONObject message) {
        QueryWrapper<Locomotive> queryWrapper = new QueryWrapper<>();

        IPage<Locomotive> page = this.page(
                new Query<Locomotive>().getPage(message),
                queryWrapper
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByCondition(JSONObject message) {
        //1.获取key
        String locomotiveName = message.getString("locomotiveName");
        Integer locomotiveId = message.getInteger("locomotiveId");
        Integer nextCrossingId = message.getInteger("nextCrossingId");
        String trackId = message.getString("trackId");
        Boolean responseStatus = message.getBoolean("responseStatus");
        QueryWrapper<Locomotive> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(locomotiveName)) {
            queryWrapper.like("locomotiveName", locomotiveName);
        }
        if (!StringUtils.isEmpty(locomotiveId)) {
            queryWrapper.or().eq("locomotiveId", locomotiveId);
        }
        if (!StringUtils.isEmpty(nextCrossingId)) {
            queryWrapper.or().eq("nextCrossingId", nextCrossingId);
        }
        if (!StringUtils.isEmpty(trackId)) {
            queryWrapper.or().eq("trackId", trackId);
        }
        if (!StringUtils.isEmpty(responseStatus)) {
            queryWrapper.or().eq("responseStatus", responseStatus);
        }
        IPage<Locomotive> page = this.page(
                new Query<Locomotive>().getPage(message),
                queryWrapper
        );
        return new PageUtils(page);
    }

    @Override
    public JSONObject listLoco(JSONObject message) {

        JSONObject jsonObject = locomotiveDao.listLoco(message.getString("locomotiveId"));

        System.out.println(jsonObject);

        if (jsonObject != null) {


            System.out.println("-------- sun -----------");

            return JSONUtil.successJSON(jsonObject);

        } else {

            return JSONUtil.errorJSON(Constants.QUERY_FAILED);

        }

    }

}
