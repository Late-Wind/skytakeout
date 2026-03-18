package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /** 新增分类
     *
     * @param categoryDTO
     */
    @Transactional
    public void addCategory(CategoryDTO categoryDTO) {
        // 创建新分类实体，复制属性
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        // 设置分类状态，0禁用，1启用，默认0
        category.setStatus(0);

        // 设置创建时间，更新时间
        // category.setCreateTime(LocalDateTime.now());
        // category.setUpdateTime(LocalDateTime.now());

        // 设置创建用户，更新用户
        // category.setCreateUser(BaseContext.getCurrentId());
        // category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.add(category);
    }

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    public PageResult page(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);

        List<Category> records = page.getResult();
        long total = page.getTotal();
        return new PageResult(total, records);
    }

    /**
     * 修改分类信息
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                .build();

        categoryMapper.update(category);
    }

    /**
     * 删除分类
     * @param id
     */
    @Transactional
    public void deleteById(Long id) {
        // 若该分类下有菜品则无法删除
        Integer dishCount = dishMapper.countByCategoryId(id);
        if(dishCount > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        // 若该分类下有套餐则无法删除
        Integer setmealCount = setmealMapper.countByCategoryId(id);
        if(setmealCount > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        categoryMapper.deleteById(id);
    }

    /**
     * 修改分类
     * @param categoryDTO
     */
    @Transactional
    public void editCategory(CategoryDTO categoryDTO) {
        Category category = new Category();

        // 复制属性
        BeanUtils.copyProperties(categoryDTO, category);

        // 修改更改时间及用户
        // category.setUpdateUser(BaseContext.getCurrentId());
        // category.setUpdateTime(LocalDateTime.now());

        categoryMapper.update(category);
    }

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    public List<Category> queryByType(Integer type) {
        return categoryMapper.list(type);
    }
}
