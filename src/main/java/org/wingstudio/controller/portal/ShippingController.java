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
import org.wingstudio.po.Shipping;
import org.wingstudio.po.User;
import org.wingstudio.service.IShippingService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/shipping/")
@Api(value = "收货地址管理",tags = "收货地址管理")
public class ShippingController {

    @Autowired
    private IShippingService iShippingService;

    @ApiOperation(value = "添加收货地址")
    @ApiImplicitParams({

            @ApiImplicitParam(name = "session",paramType = "query"),
            @ApiImplicitParam(name = "receiverName",paramType = "query",defaultValue = "严兵"),
            @ApiImplicitParam(name = "receiverPhone",paramType = "query",defaultValue = "18728194099"),
            @ApiImplicitParam(name = "receiverMobile",paramType = "query",defaultValue = "11111"),
            @ApiImplicitParam(name = "receiverProvince",paramType = "query",defaultValue = "四川"),
            @ApiImplicitParam(name = "receiverCity",paramType = "query",defaultValue = "成都"),
            @ApiImplicitParam(name = "receiverDistrict",paramType = "query",defaultValue = "梓州大道"),
            @ApiImplicitParam(name = "receiverAddress",paramType = "query",defaultValue = "中德"),
            @ApiImplicitParam(name = "receiverZip",paramType = "query",defaultValue = "11111")
    })
    @RequestMapping(value = "add.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse add(HttpSession session,
                              @RequestParam(value = "receiverName",required = false)String receiverName,
                              @RequestParam(value = "receiverPhone",required = false)String receiverPhone,
                              @RequestParam(value = "receiverMobile",required = false)String receiverMobile,
                              @RequestParam(value = "receiverProvince",required = false)String receiverProvince,
                              @RequestParam(value = "receiverCity",required = false)String receiverCity,
                              @RequestParam(value = "receiverDistrict",required = false)String receiverDistrict,
                              @RequestParam(value = "receiverAddress",required = false)String receiverAddress,
                              @RequestParam(value = "receiverZip",required = false)String receiverZip){
        User user=(User)session.getAttribute(Const.TAG.CURRENT_USER);
        if (user==null){
            return ServerResponse.errorCodeMessage(ResponseCode.NEED_LOGIN);
        }
        return iShippingService.add(user ,new Shipping(null,receiverName,receiverPhone,receiverMobile,receiverProvince,receiverCity,receiverDistrict,receiverAddress,receiverZip));
    }

    @ApiOperation(value = "删除收货地址")
    @ApiImplicitParam(name = "session",paramType = "query")
    @RequestMapping(value = "del.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> del(HttpSession session,@RequestParam("shippingId") Long shippingId){
        User user=(User)session.getAttribute(Const.TAG.CURRENT_USER);
        if (user==null){
            return ServerResponse.errorCodeMessage(ResponseCode.NEED_LOGIN);
        }
        return iShippingService.del(user ,shippingId);
    }

    @ApiOperation(value = "更新收货地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "session",paramType = "query"),
            @ApiImplicitParam(name = "id",paramType = "query",defaultValue = "1"),
            @ApiImplicitParam(name = "receiverName",paramType = "query",defaultValue = "严兵"),
            @ApiImplicitParam(name = "receiverPhone",paramType = "query",defaultValue = "18728194099"),
            @ApiImplicitParam(name = "receiverMobile",paramType = "query",defaultValue = "11111"),
            @ApiImplicitParam(name = "receiverProvince",paramType = "query",defaultValue = "四川"),
            @ApiImplicitParam(name = "receiverCity",paramType = "query",defaultValue = "成都"),
            @ApiImplicitParam(name = "receiverDistrict",paramType = "query",defaultValue = "梓州大道"),
            @ApiImplicitParam(name = "receiverAddress",paramType = "query",defaultValue = "中德"),
            @ApiImplicitParam(name = "receiverZip",paramType = "query",defaultValue = "11111")
    })
    @RequestMapping(value = "update.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> update(HttpSession session,
                                         @RequestParam(value = "id")Long id,
                                         @RequestParam(value = "receiverName",required = false)String receiverName,
                                         @RequestParam(value = "receiverPhone",required = false)String receiverPhone,
                                         @RequestParam(value = "receiverMobile",required = false)String receiverMobile,
                                         @RequestParam(value = "receiverProvince",required = false)String receiverProvince,
                                         @RequestParam(value = "receiverCity",required = false)String receiverCity,
                                         @RequestParam(value = "receiverDistrict",required = false)String receiverDistrict,
                                         @RequestParam(value = "receiverAddress",required = false)String receiverAddress,
                                         @RequestParam(value = "receiverZip",required = false)String receiverZip){
        User user=(User)session.getAttribute(Const.TAG.CURRENT_USER);
        if (user==null){
            return ServerResponse.errorCodeMessage(ResponseCode.NEED_LOGIN);
        }
        return iShippingService.update(user , new Shipping(id,receiverName,receiverPhone,receiverMobile,receiverProvince,receiverCity,receiverDistrict,receiverAddress,receiverZip));
    }

    @ApiOperation(value = "查询收货地址")
    @ApiImplicitParam(name = "session",paramType = "query")
    @RequestMapping(value = "select.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<Shipping> select(HttpSession session,@RequestParam("shippingId") Long shippingId){
        User user=(User)session.getAttribute(Const.TAG.CURRENT_USER);
        if (user==null){
            return ServerResponse.errorCodeMessage(ResponseCode.NEED_LOGIN);
        }
        return iShippingService.select(user ,shippingId);
    }

    @ApiOperation(value = "查询收货地址列表")
    @ApiImplicitParam(name = "session",paramType = "query")
    @RequestMapping(value = "list.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<Page<Shipping>> list(
            HttpSession session,
            @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
            @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        User user=(User)session.getAttribute(Const.TAG.CURRENT_USER);
        if (user==null){
            return ServerResponse.errorCodeMessage(ResponseCode.NEED_LOGIN);
        }
        return iShippingService.select(user,pageNum,pageSize);
    }
}
