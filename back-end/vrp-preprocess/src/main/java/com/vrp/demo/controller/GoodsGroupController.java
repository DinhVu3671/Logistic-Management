package com.vrp.demo.controller;

import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.GoodsGroupModel;
import com.vrp.demo.models.enu.Code;
import com.vrp.demo.models.response.ResponseData;
import com.vrp.demo.models.search.GoodsGroupSearch;
import com.vrp.demo.service.GoodsGroupService;
import com.vrp.demo.utils.ResponsePreProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/goods-groups")
public class GoodsGroupController {

    @Autowired
    private GoodsGroupService goodsGroupService;
    @Autowired
    private ResponsePreProcessor responsePreProcessor;

    @PostMapping(value = {"/search"})
    public ResponseEntity<ResponseData> search(@RequestBody GoodsGroupSearch search) {
        if (!search.isPaged()) {
            List<GoodsGroupModel> goodsGroupModels = goodsGroupService.find(search);
            return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, goodsGroupModels);
        }
        Page<GoodsGroupModel> goodsGroupModels = goodsGroupService.search(search);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, goodsGroupModels);
    }

    @PostMapping(value = {""})
    public ResponseEntity<ResponseData> create(@RequestBody GoodsGroupModel goodsGroupModel) throws CustomException {
        goodsGroupModel = goodsGroupService.create(goodsGroupModel);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, goodsGroupModel);
    }


    @PutMapping(value = {"/{id}"})
    public ResponseEntity<ResponseData> update(@RequestBody GoodsGroupModel goodsGroupModel) throws CustomException {
        goodsGroupModel = goodsGroupService.update(goodsGroupModel);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, goodsGroupModel);
    }

    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<ResponseData> delete(@PathVariable Long id) throws CustomException {
        int deletedNumber = goodsGroupService.delete(id);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, deletedNumber);
    }

}
