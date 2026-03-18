package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 插入口味数据
     *
     * @param dishFlavor
     */
    @Insert("insert into dish_flavor (id, dish_id, name, value) " +
            "values (#{id}, #{dishId}, #{name}, #{value})")
    void add(DishFlavor dishFlavor);

    /**
     * 批量插入口味数据
     *
     * @param dishFlavors
     */
    void addBatch(List<DishFlavor> dishFlavors);

    /**
     * 根据菜品id查找口味信息
     *
     * @param dishId
     * @return
     */
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);

    /**
     * 根据菜品id删除对应口味表数据
     * @param dishId
     */
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);

    /**
     * 根据菜品id批量删除对应口味表数据
     * @param dishIds
     */
    void deleteByDishIds(List<Long> dishIds);
}
