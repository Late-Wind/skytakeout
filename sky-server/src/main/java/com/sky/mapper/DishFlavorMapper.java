package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 插入口味数据
     * @param dishFlavor
     */
    @Insert("insert into dish_flavor (id, dish_id, name, value) " +
            "values (#{id}, #{dishId}, #{name}, #{value})")
    void add(DishFlavor dishFlavor);

    /**
     * 批量插入口味数据
     * @param dishFlavors
     */
    void addBatch(List<DishFlavor> dishFlavors);
}
