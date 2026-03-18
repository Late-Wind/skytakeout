package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServerImpl implements DishService {

    @Autowired
    DishMapper dishMapper;

    @Autowired
    DishFlavorMapper dishFlavorMapper;

    @Autowired
    SetmealDishMapper setmealDishMapper;

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

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        // 分页查询
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);

        // 封装数据
        long total = page.getTotal();
        List<DishVO> records = page.getResult();

        return new PageResult(total, records);
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    public List<Dish> getByCategoryId(Long categoryId) {
        return dishMapper.getByCategoryId(categoryId);
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    public Dish getById(Long id) {
        return dishMapper.getById(id);
    }

    /**
     * 菜品起售、停售
     * @param status
     */
    public void startOrStop(Integer status, Long id) {
        // 构建实体对象来传参
        Dish dish = Dish.builder()
                .status(status)
                .id(id)
                .build();

        dishMapper.update(dish);
    }

    /**
     * 修改菜品
     * @param dishDTO
     */
    @Transactional
    public void editDish(DishDTO dishDTO) {
        // 修改菜品基本信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);

        // 删除口味表相应数据
        Long dishId = dish.getId();
        dishFlavorMapper.deleteByDishId(dishId);

        // 插入口味表新数据
        List<DishFlavor> dishFlavors = dishDTO.getFlavors();
        dishFlavors.forEach(dishFlavor -> {
            dishFlavor.setDishId(dishId);
        });
        dishFlavorMapper.addBatch(dishFlavors);
    }

    /**
     * 批量删除菜品
     * @param ids
     */
    @Transactional
    public void deleteDishBatch(List<Long> ids) {
        // 判断当前菜品能否被删除(是否存在起售中的菜品)
//        for(Long id : ids) {
//            Dish dish = dishMapper.getById(id);
//
//            if(dish.getStatus() == StatusConstant.ENABLE) {
//                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
//            }
//        }
        // 批量查询
        List<Dish> dishes = dishMapper.getByIds(ids);
        for (Dish dish : dishes) {
            if(dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        // 判断当前菜品能否被删除(是否被套餐关联)
        List<Long> setmealId = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if(setmealId != null && !setmealId.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        // 删除菜品表中的数据
//        for (Long id : ids) {
//            dishMapper.deleteById(id);
//            // 删除口味表中的数据(如果存在)
//            dishFlavorMapper.deleteByDishId(id);
//        }
        // 批量删除
        dishMapper.deleteByIds(ids);
        dishFlavorMapper.deleteByDishIds(ids);
    }

    /**
     * 通过dishId返回dishVO对象
     * @param id
     * @return
     */
    public DishVO getByIdWithFlavor(Long id) {
        Dish dish = dishMapper.getById(id);
        List<DishFlavor> dishFlavor = dishFlavorMapper.getByDishId(id);

        DishVO dishVO = new DishVO();

        // 拷贝属性
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavor);

        return dishVO;
    }
}
