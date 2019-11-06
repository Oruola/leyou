package com.leyou.item.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exceptions.LyException;
import com.leyou.common.utils.BeanHelper;
import com.leyou.common.vo.PageResult;
import com.leyou.item.dto.BrandDTO;
import com.leyou.item.entity.Brand;
import com.leyou.item.mapper.BrandMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 该类的作用:
 */
@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;

    public PageResult<BrandDTO> queryBrandByPage(Integer page, Integer rows,String key, String sortBy, Boolean desc) {
        Page<Brand> brandPage = new Page<>(page,rows);
        QueryWrapper<Brand> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(key),"name",key);
        if(desc){
            wrapper.orderBy(StringUtils.isNotBlank(sortBy),false,sortBy);
        }else {
            wrapper.orderBy(StringUtils.isNotBlank(sortBy),true,sortBy);
        }
        brandMapper.selectPage(brandPage,wrapper);

        //判断查询结果是否为空
        if(CollectionUtils.isEmpty(brandPage.getRecords())){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        List<BrandDTO> brandDTOS = BeanHelper.copyWithCollection(brandPage.getRecords(), BrandDTO.class);
        return new PageResult<BrandDTO>(brandPage.getTotal(),brandDTOS);
    }
}
