package org.wingstudio.controller.portal;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wingstudio.common.ServerResponse;
import org.wingstudio.service.IProductService;
import org.wingstudio.vo.ProductDetailVo;
import org.wingstudio.vo.ProductListVo;

@Controller
@RequestMapping("/product/")
@Api(tags = "前台产品操作")
public class ProductController {

    @Autowired
    private IProductService productService;

    @ApiOperation("产品详情")
    @ApiImplicitParam(paramType = "query",name = "productId",defaultValue = "1")
    @RequestMapping(value = "detail.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ProductDetailVo> detail(@RequestParam("productId") Long productId){
        return productService.getProductDetail(productId);
    }

    @ApiOperation("产品搜索及动态排序")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "keyword",defaultValue = ""),
            @ApiImplicitParam(paramType = "query",name = "categoryId",defaultValue = "1"),
            @ApiImplicitParam(paramType = "query",name = "orderBy",defaultValue = "")
    })
    @RequestMapping(value = "list.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Page<ProductListVo>> search(
            @RequestParam(value = "keyword",required = false)String keyword,
            @RequestParam(value = "categoryId",required = false)Integer categoryId,
            @RequestParam(name = "pageNum",required = false,defaultValue = "1")Integer pageNum,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
            @RequestParam(value = "orderBy",required = false,defaultValue = "")String orderBy
    ){
        return productService.searchProductDetail(keyword, categoryId, pageNum, pageSize, orderBy);
    }

}
