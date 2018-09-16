package org.wingstudio.controller.portal;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wingstudio.common.Const;
import org.wingstudio.common.ResponseCode;
import org.wingstudio.common.ServerResponse;
import org.wingstudio.po.User;
import org.wingstudio.service.ICartService;
import org.wingstudio.vo.CartVo;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart/")
@Api(tags = "购物车")
public class CartController {

    @Autowired
    private ICartService cartService;

    @ApiOperation("添加商品到购物车")
    @ApiImplicitParam(name = "session",paramType = "query")
    @RequestMapping(value = "add.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> add(HttpSession session,
                                      @RequestParam(value = "count",required = false) Integer count,
                                      @RequestParam(value = "productId") Long productId){
        User user=(User)session.getAttribute(Const.TAG.CURRENT_USER);
        if (user == null) {
            return ServerResponse.errorCodeMessage(ResponseCode.NEED_LOGIN);
        }
        return cartService.add(user.getId(),productId,count);
    }

    @ApiOperation("更新购物车")
    @ApiImplicitParam(name = "session",paramType = "query")
    @RequestMapping(value = "update.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> update(HttpSession session,
                                         @RequestParam(value = "count",required = false) Integer count,
                                         @RequestParam(value = "productId") Long productId){
        User user=(User)session.getAttribute(Const.TAG.CURRENT_USER);
        if (user == null) {
            return ServerResponse.errorCodeMessage(ResponseCode.NEED_LOGIN);
        }
        return cartService.update(user.getId(),productId,count);
    }

    @ApiOperation("删除购物车产品")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "session",paramType = "query")
    })
    @RequestMapping(value = "delete.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> deleteProduct(HttpSession session,
                                                @RequestParam(value = "productIds") String productIds){
        User user=(User)session.getAttribute(Const.TAG.CURRENT_USER);
        if (user == null) {
            return ServerResponse.errorCodeMessage(ResponseCode.NEED_LOGIN);
        }
        return cartService.deleteProduct(user.getId(),productIds);
    }

    @ApiOperation("查询购物车产品")
    @ApiImplicitParam(name = "session",paramType = "query")
    @RequestMapping(value = "list.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<CartVo> list(HttpSession session){
        User user=(User)session.getAttribute(Const.TAG.CURRENT_USER);
        if (user == null) {
            return ServerResponse.errorCodeMessage(ResponseCode.NEED_LOGIN);
        }
        return cartService.list(user.getId());
    }

    @ApiOperation("全选")
    @ApiImplicitParam(name = "session",paramType = "query")
    @RequestMapping(value = "select_all.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<CartVo> selectAll(HttpSession session){
        User user=(User)session.getAttribute(Const.TAG.CURRENT_USER);
        if (user == null) {
            return ServerResponse.errorCodeMessage(ResponseCode.NEED_LOGIN);
        }
        return cartService.selectOrUnSelectAll(user.getId(), Const.Cart.CHECKED);
    }

    @ApiOperation("全反选")
    @ApiImplicitParam(name = "session",paramType = "query")
    @RequestMapping(value = "un_select_all.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpSession session){
        User user=(User)session.getAttribute(Const.TAG.CURRENT_USER);
        if (user == null) {
            return ServerResponse.errorCodeMessage(ResponseCode.NEED_LOGIN);
        }
        return cartService.selectOrUnSelectAll(user.getId(), Const.Cart.UN_CHECKED);
    }

    @ApiOperation("选择")
    @ApiImplicitParam(name = "session",paramType = "query")
    @RequestMapping(value = "select.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<CartVo> select(HttpSession session, @RequestParam("productId") Long productId){
        User user=(User)session.getAttribute(Const.TAG.CURRENT_USER);
        if (user == null) {
            return ServerResponse.errorCodeMessage(ResponseCode.NEED_LOGIN);
        }
        return cartService.selectOrUnSelectProduct(user.getId(),productId, Const.Cart.CHECKED);
    }

    @ApiOperation("反选")
    @ApiImplicitParam(name = "session",paramType = "query")
    @RequestMapping(value = "un_select.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<CartVo> unSelect(HttpSession session, @RequestParam("productId") Long productId){
        User user=(User)session.getAttribute(Const.TAG.CURRENT_USER);
        if (user == null) {
            return ServerResponse.errorCodeMessage(ResponseCode.NEED_LOGIN);
        }
        return cartService.selectOrUnSelectProduct(user.getId(),productId, Const.Cart.UN_CHECKED);
    }

    @ApiOperation("获取购买数量")
    @ApiImplicitParam(name = "session",paramType = "query")
    @RequestMapping(value = "get_cart_product_count.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<Long> getCartProductCount(HttpSession session){
        User user=(User)session.getAttribute(Const.TAG.CURRENT_USER);
        if (user == null) {
            return ServerResponse.success(0L);
        }
        return cartService.getCartProductCount(user.getId());
    }

}
