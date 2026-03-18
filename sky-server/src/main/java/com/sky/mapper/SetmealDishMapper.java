package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 通过菜品id查询套餐id（多对多）
     * @param dishIds
     * @return
     */
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

    /**
     * 插入多条套餐内菜品数据
     * @param setmealDishes
     */
    void insert(List<SetmealDish> setmealDishes);
}
