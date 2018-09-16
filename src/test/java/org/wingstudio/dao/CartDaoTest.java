package org.wingstudio.dao;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wingstudio.BaseTest;
import org.wingstudio.common.Const;
import org.wingstudio.po.Cart;
import org.wingstudio.po.Product;
import org.wingstudio.po.User;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CartDaoTest extends BaseTest {

    @Autowired
    private CartDao cartDao;

//    @Test
    public void init(){
        Cart cart=new Cart();
        cart.setChecked(Const.Cart.CHECKED);
        cart.setQuantity(10);
        cart.setUser(new User(1L));
        cart.setProduct(new Product(1L));
        Cart save=cartDao.save(cart);
        System.out.println(cart.getId());
        Assert.assertNotNull(save);
    }

    @Test
    public void testDelete(){
    }

    @Test
    public void testUpdate(){
        cartDao.updateCheckedByUser(new User(1L),Const.Cart.UN_CHECKED);
    }
}