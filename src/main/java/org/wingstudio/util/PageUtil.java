package org.wingstudio.util;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.wingstudio.common.Const;

public class PageUtil {
    public static Pageable getPageable(int pageNumber,int pageSize){
        Pageable pageable=new PageRequest(pageNumber-1,pageSize);
        return pageable;
    }

    public static Pageable getPageable(Integer pageNum, Integer pageSize, String orderBy) {
        Sort sort=null;
        if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy.toLowerCase())){
            String[] orderArr=orderBy.split("_");
            sort=new Sort("asc".equals(orderArr[1].toLowerCase())? Sort.Direction.ASC: Sort.Direction.DESC,orderArr[0]);
        }
        return new PageRequest(pageNum-1,pageSize,sort);
    }
}
