package org.wingstudio.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wingstudio.common.ResponseCode;
import org.wingstudio.common.ServerResponse;
import org.wingstudio.dao.CategoryDao;
import org.wingstudio.po.Category;
import org.wingstudio.service.ICategoryService;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.errorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT);
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParent(new Category(parentId));
        category.setStatus(true);

        Category save = categoryDao.save(category);
        if (save != null) {
            return ServerResponse.success("添加成功");
        }
        return ServerResponse.errorMessage("添加失败");
    }

    @Override
    public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.errorMessage("更新品类参数错误");
        }
        Category one = categoryDao.findOne(categoryId);
        one.setName(categoryName);
        Category save = categoryDao.save(one);
        if (save != null) {
            return ServerResponse.success("更新成功");
        }
        return ServerResponse.errorMessage("更新失败");
    }

    @Override
    @Transactional
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
        Category one = categoryDao.findOne(categoryId);
        List<Category> children = new ArrayList<>();
        Set<Category> childrenSet = one.getChildren();
        children.addAll(childrenSet);
        return ServerResponse.success(children);
    }

    @Override
    @Transactional
    public ServerResponse<List<Integer>> getCategoryAndChildrenById(Integer categoryId) {
        Category one = categoryDao.findOne(categoryId);
        Set<Category> categorySet=new HashSet<>();
        List<Integer> ids=new ArrayList<>();
        ArrayDeque<Category> deque = new ArrayDeque<Category>();
        deque.push(one);
        while (!deque.isEmpty()){
            Category pop = deque.pop();
            Set<Category> children = pop.getChildren();
            categorySet.addAll(children);
            children.forEach(deque::push);
        }
        categorySet.forEach(e->ids.add(e.getId()));
        return ServerResponse.success(ids);
    }
}
