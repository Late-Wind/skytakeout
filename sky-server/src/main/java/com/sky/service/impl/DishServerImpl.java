package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishServerImpl implements DishService {

    @Autowired
    DishMapper dishMapper;

    @Autowired
    DishFlavorMapper dishFlavorMapper;

    /**
     * 新增菜品
     * @param dishDTO
     */
    public void add(DishDTO dishDTO) {
        // 封装为实体类传参
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // 菜品默认为禁用
        dish.setStatus(StatusConstant.DISABLE);

        dishMapper.add(dish);

        Long dishId = dish.getId();

        // 菜品风味提取出来写入数据库
        List<DishFlavor> dishFlavors = dishDTO.getFlavors();
        if(!dishFlavors.isEmpty() && dishFlavors != null) {
//            for(DishFlavor dishFlavor : dishFlavors) {
//                dishFlavor.setDishId(dishId);
//                dishFlavorMapper.add(dishFlavor);
//            }
            // 批量插入
            dishFlavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            dishFlavorMapper.addBatch(dishFlavors);
        }
    }
}
