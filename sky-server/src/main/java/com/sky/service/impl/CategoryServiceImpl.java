package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /** 新增分类
     *
     * @param categoryDTO
     */
    public void addCategory(CategoryDTO categoryDTO) {
        // 创建新分类实体，复制属性
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        // 设置分类状态，0禁用，1启用，默认0
        category.setStatus(0);

        // 设置创建时间，更新时间
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());

        // 设置创建用户，更新用户
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.add(category);
    }
}
