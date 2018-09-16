package org.wingstudio.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.wingstudio.common.Const;
import org.wingstudio.common.ResponseCode;
import org.wingstudio.common.ServerResponse;
import org.wingstudio.dao.CategoryDao;
import org.wingstudio.dao.ProductDao;
import org.wingstudio.po.Category;
import org.wingstudio.po.Product;
import org.wingstudio.service.IProductService;
import org.wingstudio.util.PageUtil;
import org.wingstudio.util.PropertiesUtil;
import org.wingstudio.util.UpdateUtil;
import org.wingstudio.vo.ProductDetailVo;
import org.wingstudio.vo.ProductListVo;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private CategoryDao categoryDao;


    @Override
    public ServerResponse save(Product product, Integer categoryId) {
        if (product == null)
            return ServerResponse.errorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT);
        if (StringUtils.isNotBlank(product.getSubImages())) {
            String[] subImageArr = product.getSubImages().split(",");
            if (subImageArr.length > 0) {
                product.setMainImage(subImageArr[0]);
            }
            if (product.getId() != null) {//更新
                Product old = productDao.findOne(product.getId());
                if (old == null) {
                    return ServerResponse.errorMessage("要更新的产品不存在");
                }
                UpdateUtil.copyNotNullProperties(product, old);
                Product save = productDao.save(old);
                if (save != null) {
                    return ServerResponse.successMessage("更新产品成功");
                } else {
                    return ServerResponse.errorMessage("更新产品失败");
                }
            } else {
                Product save = productDao.save(product);
                if (save != null) {
                    return ServerResponse.successMessage("新增产品成功");
                } else {
                    return ServerResponse.errorMessage("新增产品失败");
                }
            }
        }
        return ServerResponse.errorMessage("新增或更新产品参数不正确");
    }

    @Override
    public ServerResponse setSaleStatus(Long productId, Byte status) {
        if (productId == null || status == null) {
            return ServerResponse.errorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT);
        }
        Product old = productDao.findOne(productId);
        old.setStatus(status);
        Product save = productDao.save(old);
        if (save != null) {
            return ServerResponse.successMessage("修改产品销售状态成功");
        }
        return ServerResponse.errorMessage("修改产品状态失败");
    }

    @Override
    public ServerResponse<ProductDetailVo> managerProductDetail(Long productId) {
        if (productId == null) {
            return ServerResponse.errorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT);
        }
        Product one = productDao.findOne(productId);
        if (one == null) {
            return ServerResponse.errorMessage("产品已经下架或删除");
        }
        ProductDetailVo vo = assembleProductDetailVo(one);
        return ServerResponse.success(vo);
    }

    @Override
    public ServerResponse<Page<ProductListVo>> getProductList(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageUtil.getPageable(pageNum, pageSize);
        Page<Product> productPage = productDao.findAll(pageable);
        return getPageListVo(pageable, productPage);
    }

    @Override
    public ServerResponse<Page<ProductListVo>> searchProductList(
            String productName, Long productId, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageUtil.getPageable(pageNum, pageSize);
        Page<Product> productPage = productDao.findAllByNameContains(productName==null?"":productName, pageable);
        return getPageListVo(pageable, productPage);
    }

    @Override
    public ServerResponse<ProductDetailVo> getProductDetail(Long productId) {
        if (productId == null) {
            return ServerResponse.errorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT);
        }
        Product one = productDao.findOne(productId);
        if (one == null || one.getStatus()!= Const.ProductStatusEnum.ON_SELL.getCode()) {
            return ServerResponse.errorMessage("产品已经下架或删除");
        }

        ProductDetailVo vo = assembleProductDetailVo(one);
        return ServerResponse.success(vo);
    }

    @Override
    @Transactional
    public ServerResponse<Page<ProductListVo>> searchProductDetail(
            String keyword, Integer categoryId,
            Integer pageNum, Integer pageSize,
            String orderBy) {
        if (StringUtils.isBlank(keyword) && categoryId==null){
            return ServerResponse.errorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT);
        }
        Page<Product> productPage=null;
        Pageable pageable = PageUtil.getPageable(pageNum, pageSize, orderBy);
        if (categoryId!=null){
            Category category=new Category(categoryId);
            productPage = productDao.findAllByNameContainsAndCategoryAndStatus(keyword==null?"":keyword, category, Const.ProductStatusEnum.ON_SELL.getCode(),pageable);
        }else {
            productPage=productDao.findAllByNameContainsAndStatus(keyword==null?"":keyword,Const.ProductStatusEnum.ON_SELL.getCode(), pageable);
        }
        return getPageListVo(pageable,productPage);
    }

    //Page<Product> --> Page<ProductListVo>
    private ServerResponse<Page<ProductListVo>> getPageListVo(Pageable pageable, Page<Product> productPage) {
        List<Product> content = productPage.getContent();
        List<ProductListVo> listVos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(content)) {
            content.forEach(e -> listVos.add(this.assembleProductListVo(e)));
        }
        Page<ProductListVo> voPage = new PageImpl<>(listVos, pageable, productPage.getTotalElements());
        return ServerResponse.success(voPage);
    }

    @Transactional
    public ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo vo = new ProductDetailVo();
        BeanUtils.copyProperties(product, vo);

        Category category = product.getCategory();
        if (category == null)
            category = categoryDao.findOne(0);
        product.setCategory(category);

        Category parentCategory = category.getParent();
        if (parentCategory != null)
            vo.setParentCategoryId(parentCategory.getId());


        vo.setCategoryId(product.getCategory().getId());
        vo.setImageHost(PropertiesUtil.getProperty("fileServer.prefix", "http://127.0.0.1:8080/es/upload/"));

        return vo;
    }

    @Transactional
    public ProductListVo assembleProductListVo(Product product) {
        ProductListVo vo = new ProductListVo();
        BeanUtils.copyProperties(product, vo);
        if (product.getCategory() == null || product.getCategory().getId() == null)
            vo.setCategoryId(0);
        else
            vo.setCategoryId(product.getCategory().getId());
        vo.setImageHost(PropertiesUtil.getProperty("fileServer.prefix", "http://127.0.0.1:8080/es/upload/"));
        Category category = product.getCategory();
        if (category == null)
            category = new Category(0);
        product.setCategory(category);
        return vo;
    }
}