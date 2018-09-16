package org.wingstudio.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.wingstudio.BaseTest;
import org.wingstudio.po.Shipping;
import org.wingstudio.po.User;
import org.wingstudio.util.PageUtil;

import static org.junit.Assert.*;

public class ShippingDaoTest extends BaseTest {

    @Autowired
    private ShippingDao shippingDao;

    @Test
    public void deleteByUserAndId() {
    }

    @Test
    public void findByUserAndId() {
        Shipping shipping=shippingDao.findByUserAndId(new User(1l),1l);
        System.out.println(shipping.getReceiverAddress());
    }

    @Test
    public void findByUser() {
        Page<Shipping> page=shippingDao.findByUser(new User(1l),PageUtil.getPageable(1,10));
        System.out.println(page.getTotalElements());
    }
}