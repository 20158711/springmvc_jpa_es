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
import org.wingstudio.common.Const;
import org.wingstudio.common.ResponseCode;
import org.wingstudio.common.ServerResponse;
import org.wingstudio.po.User;
import org.wingstudio.service.IOrderService;
import org.wingstudio.vo.OrderVo;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/order/")
@Api(value = "订单模块",tags = "订单模块")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @ApiOperation("创建订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "session",paramType = "query"),
            @ApiImplicitParam(name = "shippingId",paramType = "query")
    })
    @RequestMapping(value = "create.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse create(HttpSession session,
                                 @RequestParam("shippingId")Long shippingId){
        User user=(User)session.getAttribute(Const.TAG.CURRENT_USER);
        if (user == null) {
            return ServerResponse.errorCodeMessage(ResponseCode.NEED_LOGIN);
        }
        return orderService.create(user,shippingId);
    }

    @ApiOperation("取消订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "session",paramType = "query"),
            @ApiImplicitParam(name = "orderNo",paramType = "query")
    })
    @RequestMapping(value = "cancel.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse cancel(HttpSession session,
                                 @RequestParam("orderNo")Long orderNo){
        User user=(User)session.getAttribute(Const.TAG.CURRENT_USER);
        if (user == null) {
            return ServerResponse.errorCodeMessage(ResponseCode.NEED_LOGIN);
        }
        return orderService.cancel(user,orderNo);
    }

    @ApiOperation("获取订单中已经选中的商品详情")
    @ApiImplicitParam(name = "session",paramType = "query")
    @RequestMapping(value = "get_order_cart_product.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getOrderCartProduct(HttpSession session){
        User user=(User)session.getAttribute(Const.TAG.CURRENT_USER);
        if (user == null) {
            return ServerResponse.errorCodeMessage(ResponseCode.NEED_LOGIN);
        }
        return orderService.getOrderCartProduct(user);
    }

    @ApiOperation("获取订单详情")
    @ApiImplicitParam(name = "session",paramType = "query")
    @RequestMapping(value = "detail.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<OrderVo> detail(HttpSession session,
                                          @RequestParam("orderNo")Long orderNo){
        User user=(User)session.getAttribute(Const.TAG.CURRENT_USER);
        if (user == null) {
            return ServerResponse.errorCodeMessage(ResponseCode.NEED_LOGIN);
        }
        return orderService.getOrderDetail(user,orderNo);
    }

    @ApiOperation("获取订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "session",paramType = "query"),
            @ApiImplicitParam(name = "pageNum",paramType = "query",defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize",paramType = "query",defaultValue = "10")
    })
    @RequestMapping(value = "list.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<Page<OrderVo>> detail(HttpSession session,
                                         @RequestParam(value = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                                         @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize){
        User user=(User)session.getAttribute(Const.TAG.CURRENT_USER);
        if (user == null) {
            return ServerResponse.errorCodeMessage(ResponseCode.NEED_LOGIN);
        }
        return orderService.getOrderList(user,pageNum,pageSize);
    }


}
