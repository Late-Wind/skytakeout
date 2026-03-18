package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * 添加菜品
     * @param dishDTO
     */
    void add(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    List<Dish> getByCategoryId(Long categoryId);

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    Dish getById(Long id);

    /**
     * 菜品起售、停售
     * @param status
     */
    void startOrStop(Integer status, Long id);

    /**
     * 修改菜品
     * @param dishDTO
     */
    void editDish(DishDTO dishDTO);

    /**
     * 批量删除菜品
     * @param ids
     */
    void deleteDishBatch(List<Long> ids);

    /**
     * 通过dishId返回dishVO对象
     * @param id
     * @return
     */
    DishVO getByIdWithFlavor(Long id);
}
