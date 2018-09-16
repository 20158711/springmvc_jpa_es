package org.wingstudio.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.wingstudio.common.ServerResponse;
import org.wingstudio.dao.ShippingDao;
import org.wingstudio.po.Shipping;
import org.wingstudio.po.User;
import org.wingstudio.service.IShippingService;
import org.wingstudio.util.PageUtil;
import org.wingstudio.util.UpdateUtil;
import java.util.HashMap;
import java.util.Map;

@Service
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingDao shippingDao;

    @Override
    public ServerResponse add(User user, Shipping shipping) {
        shipping.setUser(user);
        Shipping save=shippingDao.save(shipping);
        if(save!=null){
            Map result=new HashMap();
            result.put("shippingId",shipping.getId());
            return ServerResponse.success("新建地址成功",result);
        }
        return ServerResponse.errorMessage("新建地址失败");
    }

    @Override
    public ServerResponse<String> del(User user, Long shippingId) {
        int result=shippingDao.deleteByUserAndId(user,shippingId);
        if(result>0){
            return ServerResponse.success("删除地址成功");
        }
        return ServerResponse.errorMessage("删除地址失败");
    }

    @Override
    public ServerResponse<String> update(User user, Shipping shipping) {
        Shipping old=shippingDao.findByUserAndId(user,shipping.getId());
        if (old!=null){
            UpdateUtil.copyNotNullProperties(shipping,old);
            Shipping save=shippingDao.save(old);
            if(save!=null){
                return ServerResponse.success("更新地址成功");
            }
        }
        return ServerResponse.errorMessage("更新地址失败");
    }

    @Override
    public ServerResponse<Shipping> select(User user, Long shippingId) {
        Shipping shipping=shippingDao.findByUserAndId(user,shippingId);
        if (shipping == null) {
            return ServerResponse.errorMessage("无法查询到地址");
        }
        return ServerResponse.success("更新地址成功",shipping);
    }

    @Override
    public ServerResponse<Page<Shipping>> select(User user, int pageNum, int pageSize) {
        Pageable pageable=PageUtil.getPageable(pageNum,pageSize);
        Page<Shipping> page=shippingDao.findByUser(user,pageable);
        return ServerResponse.success(page);
    }
}
