package org.wingstudio.service.impl;

import com.google.common.base.Splitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.wingstudio.common.Const;
import org.wingstudio.common.ResponseCode;
import org.wingstudio.common.ServerResponse;
import org.wingstudio.dao.CartDao;
import org.wingstudio.dao.ProductDao;
import org.wingstudio.po.Cart;
import org.wingstudio.po.Product;
import org.wingstudio.po.User;
import org.wingstudio.service.ICartService;
import org.wingstudio.util.PropertiesUtil;
import org.wingstudio.vo.CartProductVo;
import org.wingstudio.vo.CartVo;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartDao cartDao;

    @Autowired
    private ProductDao productDao;

    @Override
    public ServerResponse<CartVo> add(User user, Long productId, Integer count) {
        if (productId==null || count==null){
            return ServerResponse.errorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT);
        }
        Product product=productDao.findOne(productId);
        Cart cart = cartDao.findByUserAndProduct(user, product);
        Cart save=null;
        if (cart == null) {
            //新增
            Cart cartItem=new Cart();
            cartItem.setQuantity(count);
            cartItem.setChecked(Const.Cart.CHECKED);
            cartItem.setProduct(product);
            cartItem.setUser(user);
            save = cartDao.save(cartItem);
        }else {
            //存在，数量相加
            cart.setQuantity(count+cart.getQuantity());
            save=cartDao.save(cart);
        }
        if (save != null) {
            return list(user.getId());
        }
        return ServerResponse.error();
    }

    @Override
    public ServerResponse<CartVo> update(Long userId, Long productId, Integer count) {
        if (productId==null || count==null){
            return ServerResponse.errorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT);
        }
        Cart cart=cartDao.findByUserAndProduct(new User(userId),new Product(productId));
        if(cart!=null){
            cart.setQuantity(count);
            cartDao.save(cart);
        }
        return this.list(userId);
    }

    @Override
    public ServerResponse<CartVo> deleteProduct(Long userId, String productIds) {
        List<String> productList=Splitter.on(",").splitToList(productIds);
        if(CollectionUtils.isEmpty(productList)) {
            return ServerResponse.errorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT);
        }
        List<Product> longs=productList.stream().map(e->new Product(Long.parseLong(e))).collect(Collectors.toList());
        cartDao.deleteByIds(longs);
        return this.list(userId);
    }

    @Override
    public ServerResponse<CartVo> list(Long userId) {
        CartVo cartVo=this.getCartVoLimit(userId);
        return ServerResponse.success(cartVo);
    }

    @Override
    public ServerResponse<CartVo> selectOrUnSelectAll(Long userId, byte checked) {
        cartDao.updateCheckedByUser(new User(userId),checked);
        return this.list(userId);
    }

    @Override
    public ServerResponse<CartVo> selectOrUnSelectProduct(Long userId, Long productId, byte checked) {
        cartDao.updateCheckByUserAndProduct(new User(userId),new Product(productId),checked);
        return list(userId);
    }

    @Override
    public ServerResponse<Long> getCartProductCount(Long userId) {
        if (userId==null)
            return ServerResponse.errorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT);
        long result=cartDao.count();
        return ServerResponse.success(result);
    }

    public CartVo getCartVoLimit(Long userId){
        CartVo cartVo=new CartVo();
        List<Cart> cartList = cartDao.findByUser(new User(userId));
        List<CartProductVo> cartProductVoList=new ArrayList<>();
        long cartTotalPrice=0L;

        if (!CollectionUtils.isEmpty(cartList)){
            for (Cart cart : cartList) {
                CartProductVo cartProductVo=new CartProductVo();
                cartProductVo.setId(cart.getId());
                cartProductVo.setUserId(cart.getUser().getId());
                cartProductVo.setProductId(cart.getProduct().getId());

                Product product=cart.getProduct();
                if (product != null) {
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubTitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());
                    //判断库存
                    int buyLimitCount=0;
                    if(product.getStock()>=cart.getQuantity()){
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                        buyLimitCount=cart.getQuantity();
                    }else {
                       buyLimitCount=product.getStock();
                       cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                       //购物车中更新有效库存
                        Cart old=cartDao.findOne(cart.getId());
                        old.setQuantity(buyLimitCount);
                        cartDao.save(old);
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    //计算单项总价
                    cartProductVo.setProductTotalPrice(product.getPrice()*cartProductVo.getQuantity());
                    cartProductVo.setProductChecked(cart.getChecked());
                }
                if (cart.getChecked()==Const.Cart.CHECKED){
                    cartTotalPrice+=cartProductVo.getProductTotalPrice();
                }
                cartProductVoList.add(cartProductVo);
            }
        }
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setAllChecked(getAllCheckedStatus(new User(userId)));
        cartVo.setImageHost(PropertiesUtil.getProperty("fileServer.prefix","http://127.0.0.1:8080/es/upload/"));

        return cartVo;
    }

    private boolean getAllCheckedStatus(User user){
        if (user==null){
            return false;
        }
        return cartDao.countByUserAndChecked(user,Const.Cart.UN_CHECKED)==0;
    }
}
