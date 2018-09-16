package org.wingstudio.service.impl;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.wingstudio.BaseTest;
import org.wingstudio.common.ServerResponse;
import org.wingstudio.dao.CategoryDao;
import org.wingstudio.service.ICategoryService;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class CategoryServiceImplTest extends BaseTest {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private CategoryDao categoryDao;

    @Test
    public void testA_addCategory() {
        ServerResponse serverResponse = categoryService.addCategory("test", 1);
        Assert.assertTrue(serverResponse.isSuccess());
    }

    @Test
    public void testB_updateCategoryName() {
        ServerResponse changed = categoryService.updateCategoryName(1, "changed");
        Assert.assertTrue(changed.isSuccess());
    }

    @Test
    public void testC_getChildrenParallelCategory() {
        ServerResponse response = categoryService.getChildrenParallelCategory(0);
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void testD_getCategoryAndChildrenById() {
        ServerResponse response = categoryService.getCategoryAndChildrenById(0);
        List<Integer> ids= (List<Integer>) response.getData();
        Assert.assertEquals(2,ids.size());
    }
}