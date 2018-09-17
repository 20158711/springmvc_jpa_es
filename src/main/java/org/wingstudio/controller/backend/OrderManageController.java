package org.wingstudio.controller.backend;

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
import org.wingstudio.service.IOrderService;
import org.wingstudio.service.IUserService;
import org.wingstudio.vo.OrderVo;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/order/")
@Api(value = "后台订单管理模块",tags = "后台订单管理模块")
public class OrderManageController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderService orderService;

    @ApiOperation(value = "订单列表")
    @RequestMapping(value = "list.do",method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "session",paramType = "query"),
            @ApiImplicitParam(name = "pageNum",paramType = "query",defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize",paramType = "query",defaultValue = "10")
    })
    @ResponseBody
    public ServerResponse<Page<OrderVo>> orderList(HttpSession session,
                                                   @RequestParam(value = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                                                   @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize){
        ServerResponse checkAdmin = userService.checkAdmin(session);
        if (!checkAdmin.isSuccess())
            return checkAdmin;
        return orderService.manageList(pageNum,pageSize);
    }

    @ApiOperation("订单详情")
    @RequestMapping(value = "detail.do",method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "session",paramType = "query"),
            @ApiImplicitParam(name = "orderNo",paramType = "query")
    })
    @ResponseBody
    public ServerResponse<OrderVo> orderDetail(HttpSession session,
                                                   @RequestParam("orderNo")Long orderNo){
        ServerResponse checkAdmin = userService.checkAdmin(session);
        if (!checkAdmin.isSuccess())
            return checkAdmin;
        return orderService.manageDetail(orderNo);
    }

    @ApiOperation("搜索订单")
    @RequestMapping(value = "search.do",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "session",paramType = "query"),
            @ApiImplicitParam(name = "orderNo",paramType = "query"),
            @ApiImplicitParam(name = "pageNum",paramType = "query",defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize",paramType = "query",defaultValue = "10")
    })
    @ResponseBody
    public ServerResponse<Page<OrderVo>> orderSearch(HttpSession session,
                                                     @RequestParam("orderNo")Long orderNo,
                                                     @RequestParam(value = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                                                     @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize){
        ServerResponse checkAdmin = userService.checkAdmin(session);
        if (!checkAdmin.isSuccess())
            return checkAdmin;
        return orderService.manageSearch(orderNo,pageNum,pageSize);
    }

    @ApiOperation("发货")
    @RequestMapping(value = "send_goods.do",method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "session",paramType = "query"),
            @ApiImplicitParam(name = "orderNo",paramType = "query")
    })
    @ResponseBody
    public ServerResponse<String> sendGoods(HttpSession session,
                                               @RequestParam("orderNo")Long orderNo){
        ServerResponse checkAdmin = userService.checkAdmin(session);
        if (!checkAdmin.isSuccess())
            return checkAdmin;
        return orderService.sendGoods(orderNo);
    }

}
