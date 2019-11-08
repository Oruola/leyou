package com.leyou.item.controller;

import com.leyou.common.vo.PageResult;
import com.leyou.item.dto.BrandDTO;
import com.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 该类的作用:
 */
@RestController
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping("page")
    public ResponseEntity<PageResult<BrandDTO>> queryBrandByPage(
            @RequestParam(value = "page", defaultValue = "1")Integer page,
            @RequestParam(value = "rows", defaultValue = "5")Integer rows,
            @RequestParam(value = "key", required = false)String key,
            @RequestParam(value = "sortBy", required = false)String sortBy,
            @RequestParam(value = "desc", defaultValue = "false")Boolean desc
    ){

        return ResponseEntity.ok(brandService.queryBrandByPage(page,rows,key,sortBy,desc));
    }

    @PostMapping
    public ResponseEntity<Void> saveBrand(BrandDTO brandDTO,@RequestParam("cids") List<Long> cids){
        brandService.saveBrand(brandDTO,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
