package com.leyou.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leyou.item.entity.Brand;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 该类的作用:
 */
public interface BrandMapper extends BaseMapper<Brand> {
    int insertCategoryBrand(@Param("bid") Long id,@Param("ids") List<Long> cids);
}
