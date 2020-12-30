package com.railwaycrossing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.railwaycrossing.entity.Instruction;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InstructionDao extends BaseMapper<Instruction> {

}
