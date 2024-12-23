package com.xixi.user.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xixi.user.entity.Category;
import com.xixi.user.mapper.CategoryMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 永
 */
@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {
    @Resource
    private CategoryMapper categoryMapper;


    public List<String> selectByCategoryName() {
        return categoryMapper.selectByCategoryName();
    }
}
