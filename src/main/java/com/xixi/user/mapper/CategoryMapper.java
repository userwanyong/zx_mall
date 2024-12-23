package com.xixi.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xixi.user.entity.Category;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 永
 */
public interface CategoryMapper extends BaseMapper<Category> {

    @Select("select category_name from category where open='1'")
    List<String> selectByCategoryName();
}
