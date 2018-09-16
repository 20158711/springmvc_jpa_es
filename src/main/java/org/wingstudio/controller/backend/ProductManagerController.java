package org.wingstudio.controller.backend;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.wingstudio.common.ServerResponse;
import org.wingstudio.po.Product;
import org.wingstudio.service.IProductService;
import org.wingstudio.service.IUserService;
import org.wingstudio.vo.ProductDetailVo;
import org.wingstudio.vo.ProductListVo;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manager/product")
@Api(tags = "商品管理")
public class ProductManagerController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IProductService productService;

    @ApiOperation("新增或修改产品")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "product",defaultValue = ""),
            @ApiImplicitParam(paramType = "query", name = "id", defaultValue = "1"),
            @ApiImplicitParam(paramType = "query", name = "name", defaultValue = "测试新增产品"),
            @ApiImplicitParam(paramType = "query", name = "subImages", defaultValue = "http://g.hiphotos.baidu.com/image/pic/item/0b46f21fbe096b6340b5e86c01338744ebf8ac5a.jpg,http://c.hiphotos.baidu.com/image/pic/item/8694a4c27d1ed21b3c778fdda06eddc451da3f4f.jpg"),
            @ApiImplicitParam(paramType = "query",name = "subTitle",defaultValue = "子标题"),
            @ApiImplicitParam(paramType = "query", name = "detail", defaultValue = "详细信息"),
            @ApiImplicitParam(paramType = "query",name = "price",defaultValue = "1000"),
            @ApiImplicitParam(paramType = "query",name = "stock",defaultValue = "100"),
            @ApiImplicitParam(paramType = "query",name = "status",defaultValue = "1"),
            @ApiImplicitParam(paramType = "query",name = "categoryId",defaultValue = "2"),
            @ApiImplicitParam(paramType = "query",name = "session",defaultValue = "")
    })
    @RequestMapping(value = "save.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSave(HttpSession session,
                                      Product product,
                                      @RequestParam(value = "categoryId", required = false) Integer categoryId) {
        ServerResponse checkAdmin = userService.checkAdmin(session);
        if (!checkAdmin.isSuccess())
            return checkAdmin;
        return productService.save(product, categoryId);
    }

    @ApiOperation("产品上下架")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "productId",defaultValue = "1"),
            @ApiImplicitParam(paramType = "query",name = "status",defaultValue = "1"),
            @ApiImplicitParam(paramType = "query",name = "session",defaultValue = "")
    })
    @RequestMapping(value = "set_sale_status.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setStatus(HttpSession session,
                                    @RequestParam("productId") Long productId,
                                    @RequestParam("status") Byte status) {
        ServerResponse checkAdmin = userService.checkAdmin(session);
        if (!checkAdmin.isSuccess())
            return checkAdmin;
        return productService.setSaleStatus(productId, status);
    }


    @ApiOperation("产品详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "session",defaultValue = ""),
            @ApiImplicitParam(paramType = "query",name = "productId",defaultValue = "1")
    })
    @RequestMapping(value = "detail.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ProductDetailVo> getProduct(HttpSession session,
                                                      @RequestParam("productId") Long productId) {
        ServerResponse checkAdmin = userService.checkAdmin(session);
        if (!checkAdmin.isSuccess())
            return checkAdmin;
        return productService.managerProductDetail(productId);
    }

    @ApiOperation("产品列表")
    @ApiImplicitParam(paramType = "query",name = "session")
    @RequestMapping(value = "list.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<Page<ProductListVo>> getList(
            HttpSession session,
            @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        ServerResponse checkAdmin = userService.checkAdmin(session);
        if (!checkAdmin.isSuccess())
            return checkAdmin;
        //pageNum-1操作在Service中完成
        return productService.getProductList(pageNum, pageSize);
    }

    @ApiOperation("产品搜索")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "session",defaultValue = ""),
            @ApiImplicitParam(paramType = "query",name = "productName",defaultValue = "测试")
    })
    @RequestMapping(value = "search.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Page<ProductListVo>> search(
            HttpSession session,
            @RequestParam(value = "productName", required = false,defaultValue = "") String productName,
            @RequestParam(value = "productId", required = false) Long productId,
            @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        ServerResponse checkAdmin = userService.checkAdmin(session);
        if (!checkAdmin.isSuccess())
            return checkAdmin;
        return productService.searchProductList(productName, productId, pageNum, pageSize);
    }


}