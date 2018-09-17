package org.wingstudio.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.wingstudio.common.Const;
import org.wingstudio.common.ServerResponse;
import org.wingstudio.dao.CartDao;
import org.wingstudio.dao.OrderDao;
import org.wingstudio.dao.OrderItemDao;
import org.wingstudio.dao.ProductDao;
import org.wingstudio.po.*;
import org.wingstudio.service.IOrderService;
import org.wingstudio.util.DateTimeUtil;
import org.wingstudio.util.PageUtil;
import org.wingstudio.util.PropertiesUtil;
import org.wingstudio.vo.OrderItemVo;
import org.wingstudio.vo.OrderProductVo;
import org.wingstudio.vo.OrderVo;
import org.wingstudio.vo.ShippingVo;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private CartDao cartDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private ProductDao productDao;

    @Override
    public ServerResponse create(User user, Long shippingId) {
        //查询勾选的购物车信息
        List<Cart> cartList=cartDao.findByUserAndChecked(user,Const.Cart.CHECKED);

        ServerResponse<List<OrderItem>> serverResponse=getCartOrderItem(user,cartList);
        if (!serverResponse.isSuccess()){
            return serverResponse;
        }
        //计算总价
        List<OrderItem> orderItems= serverResponse.getData();
        long payment=orderItems.stream().mapToLong(OrderItem::getTotalPrice).sum();

        //生成订单
        EsOrder order=assembleOrder(user,new Shipping(shippingId),payment);
        if (order == null) {
            return ServerResponse.errorMessage("生成订单错误");
        }
        if (CollectionUtils.isEmpty(orderItems)){
            return ServerResponse.errorMessage("购物车为空");
        }
        orderItems.forEach(e->{e.setOrderNo(order.getOrderNo());e.setEsOrder(order);});
        orderItemDao.save(orderItems);
        //减库存
        reduceProductStock(orderItems);

        //清空购物车
        cleanCart(cartList);

        OrderVo orderVo=assembleOrderVo(order,orderItems);

        return ServerResponse.success(orderVo);
    }

    @Override
    public ServerResponse cancel(User user, Long orderNo) {
        EsOrder order=orderDao.findByUserAndOrderNo(user,orderNo);
        if (order == null) {
            return ServerResponse.errorMessage("该用户此订单不存在");
        }
        if(order.getStatus()!=Const.OrderStatusEnum.NO_PAY.getCode()){
            return ServerResponse.errorMessage("已付款，无法取消订单");
        }
        order.setStatus(Const.OrderStatusEnum.CANCELED.getCode());
        EsOrder save=orderDao.save(order);
        if (save == null) {
            return ServerResponse.error();
        }
        return ServerResponse.success();
    }

    @Override
    public ServerResponse getOrderCartProduct(User user) {
        OrderProductVo orderProductVo=new OrderProductVo();

        List<Cart> cartList=cartDao.findByUserAndChecked(user,Const.Cart.CHECKED);
        ServerResponse serverResponse=getCartOrderItem(user,cartList);
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }
        List<OrderItem> orderItemList= (List<OrderItem>) serverResponse.getData();
        List<OrderItemVo> orderItemVoList=orderItemList.stream().map(this::assembleOrderItemVo).collect(Collectors.toList());
        long payment=orderItemList.stream().mapToLong(e->e.getTotalPrice()).sum();

        orderProductVo.setProductTotalPrice(payment);
        orderProductVo.setOrderItemVoList(orderItemVoList);
        orderProductVo.setImageHost(PropertiesUtil.getProperty("fileServer.prefix"));
        return ServerResponse.success(orderProductVo);

    }

    @Override
    @Transactional
    public ServerResponse<OrderVo> getOrderDetail(User user, Long orderNo) {
        EsOrder order=orderDao.findByUserAndOrderNo(user, orderNo);
        if (order == null) {
            return ServerResponse.errorMessage("没有找到该订单");
        }
        OrderVo orderVo=assembleOrderVo(order,order.getOrderItems());
        return ServerResponse.success(orderVo);
    }

    @Override
    public ServerResponse<Page<OrderVo>> getOrderList(User user, Integer pageNum, Integer pageSize) {
        Pageable pageable=PageUtil.getPageable(pageNum,pageSize);
        Page<EsOrder> orderPage=orderDao.findByUser(user,pageable);
        Page<OrderVo> voPage=new PageImpl<>(assembleOrderVoList(orderPage.getContent()),pageable,orderPage.getTotalElements());
        return ServerResponse.success(voPage);
    }

    @Override
    public ServerResponse<Page<OrderVo>> manageList(Integer pageNum, Integer pageSize) {
        Pageable pageable=PageUtil.getPageable(pageNum,pageSize);
        Page<EsOrder> orderPage=orderDao.findAll(pageable);
        List<OrderVo> orderVoList=assembleOrderVoList(orderPage.getContent());
        Page<OrderVo> voPage=new PageImpl<>(orderVoList,pageable,orderPage.getTotalElements());
        return ServerResponse.success(voPage);
    }

    @Override
    public ServerResponse<OrderVo> manageDetail(Long orderNo) {
        EsOrder order=orderDao.findByOrderNo(orderNo);
        if (order == null) {
            return ServerResponse.errorMessage("订单不存在");
        }
        OrderVo orderVo=assembleOrderVo(order,order.getOrderItems());
        return ServerResponse.success(orderVo);
    }

    @Override
    public ServerResponse<Page<OrderVo>> manageSearch(Long orderNo, Integer pageNum, Integer pageSize) {
        Pageable pageable=PageUtil.getPageable(pageNum,pageSize);
        Page<EsOrder> orderPage=orderDao.findByOrderNo(orderNo,pageable);
        List<OrderVo> orderVoList=assembleOrderVoList(orderPage.getContent());
        Page<OrderVo> voPage=new PageImpl<>(orderVoList,pageable,orderPage.getTotalElements());
        return ServerResponse.success(voPage);
    }

    @Override
    public ServerResponse<String> sendGoods(Long orderNo) {
        EsOrder order=orderDao.findByOrderNo(orderNo);
        if (order == null) {
            return ServerResponse.errorMessage("订单不存在");
        }
        if(order.getStatus()==Const.OrderStatusEnum.PAID.getCode()){
            order.setStatus(Const.OrderStatusEnum.SHIPPED.getCode());
            order.setSendTime(new Date());
            EsOrder save=orderDao.save(order);
            if (save != null) {
                return ServerResponse.success("发货成功");
            }
        }
        return ServerResponse.error();
    }

    private List<OrderVo> assembleOrderVoList(List<EsOrder> orderList){
            List<OrderVo> collect = orderList.stream().map(
                    order -> {
                        OrderVo vo=assembleOrderVo(order, order.getOrderItems());
                        return vo;
                    }
            ).collect(Collectors.toList());
            return collect;
    }

    private OrderVo assembleOrderVo(EsOrder order,List<OrderItem> orderItemList) {
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymentType(order.getPaymentType());
        orderVo.setPaymentTypeDesc(Const.PaymentTypeEnum.codeOf(order.getPaymentType()).getValue());
        orderVo.setPostage(order.getPostage());
        orderVo.setStatus(order.getStatus());
        orderVo.setStatusDesc(Const.OrderStatusEnum.codeOf(order.getStatus()).getValue());
        orderVo.setShippingId(order.getShipping().getId());
        orderVo.setReceiverName(order.getShipping().getReceiverName());
        orderVo.setShippingVo(assembleShippingVo(order.getShipping()));
        orderVo.setPaymentTime(DateTimeUtil.dateToStr(order.getPaymentTime()));
        orderVo.setSendTime(DateTimeUtil.dateToStr(order.getSendTime()));
        orderVo.setEndTime(DateTimeUtil.dateToStr(order.getEndTime()));
        orderVo.setCreateTime(DateTimeUtil.dateToStr(order.getCreateTime()));
        orderVo.setCloseTime(DateTimeUtil.dateToStr(order.getCloseTime()));
        orderVo.setImageHost(PropertiesUtil.getProperty("fileServer.prefix"));

        List<OrderItemVo> orderItemVoList=orderItemList.stream().map(this::assembleOrderItemVo).collect(Collectors.toList());

        orderVo.setOrderItemVoList(orderItemVoList);
        return orderVo;
    }

    private OrderItemVo assembleOrderItemVo(OrderItem orderItem){
        OrderItemVo orderItemVo=new OrderItemVo();
        orderItemVo.setOrderNo(orderItem.getOrderNo());
        orderItemVo.setProductId(orderItem.getProduct().getId());
        orderItemVo.setProductName(orderItem.getProduct().getName());
        orderItemVo.setProductImage(orderItem.getProduct().getMainImage());
        orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVo.setQuantity(orderItem.getQuantity());
        orderItemVo.setTotalPrice(orderItem.getTotalPrice());
        orderItemVo.setCreateTime(DateTimeUtil.dateToStr(orderItem.getCreateTime()));
        return orderItemVo;


    }

    private ShippingVo assembleShippingVo(Shipping shipping){
        ShippingVo shippingVo=new ShippingVo();
        shippingVo.setReceiverName(shipping.getReceiverName());
        shippingVo.setReceiverAddress(shipping.getReceiverAddress());
        shippingVo.setReceiverProvince(shipping.getReceiverProvince());
        shippingVo.setReceiverCity(shipping.getReceiverCity());
        shippingVo.setReceiverDistrict(shipping.getReceiverDistrict());
        shippingVo.setReceiverMobile(shipping.getReceiverMobile());
        shippingVo.setReceiverZip(shipping.getReceiverZip());
        shippingVo.setReceiverPhone(shipping.getReceiverPhone());
        return shippingVo;
    }


    private void cleanCart(List<Cart> carts){
        cartDao.deleteInBatch(carts);
    }

    @Transactional
    public void reduceProductStock(List<OrderItem> orderItemList){
        orderItemList.forEach(e->{
            Product product=e.getProduct();
            product.setStock(product.getStock()-e.getQuantity());
            productDao.save(product);
        });
    }

    private EsOrder assembleOrder(User user,Shipping shipping,Long payment){
        EsOrder order=new EsOrder();
        long orderNo=generateOrderNo();
        order.setOrderNo(orderNo);
        order.setStatus(Const.OrderStatusEnum.NO_PAY.getCode());
        order.setPostage(0l);
        order.setPaymentType(Const.PaymentTypeEnum.ONLINE_PAY.getCode());
        order.setPayment(payment);
        order.setUser(user);
        order.setShipping(shipping);
        //todo 发货时间
        //todo 付款时间
        EsOrder save=orderDao.save(order);
        if (save == null) {
            return null;
        }
        return save;
    }

    private long generateOrderNo(){
        long currentTime=System.currentTimeMillis();
        return currentTime+new Random().nextInt(100);
    }

    @Transactional
    public ServerResponse<List<OrderItem>> getCartOrderItem(User user,List<Cart> cartList){
        List<OrderItem> orderItemList=new ArrayList<>();
        if(CollectionUtils.isEmpty(cartList)){
            return ServerResponse.errorMessage("购物车为空");
        }
        //校验购物车的数据
        for (Cart cartItem : cartList){
            OrderItem orderItem=new OrderItem();
            Product product=cartItem.getProduct();
            if(Const.ProductStatusEnum.ON_SELL.getCode()!=product.getStatus()){
                return ServerResponse.errorMessage("产品"+product.getName()+"不是在线售卖状态");
            }
            if (cartItem.getQuantity()>product.getStock()){
                return ServerResponse.errorMessage("产品"+product.getName()+"库存不足");
            }
            orderItem.setUser(user);
            orderItem.setProduct(product);
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(product.getPrice()*cartItem.getQuantity());
            orderItemList.add(orderItem);
        }
        return ServerResponse.success(orderItemList);
    }
}
