package org.wingstudio.service;

import org.wingstudio.common.ServerResponse;
import org.wingstudio.po.Category;

import java.util.List;

public interface ICategoryService {
    ServerResponse addCategory(String categoryName, Integer parentId);

    ServerResponse updateCategoryName(Integer categoryId, String categoryName);

    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    ServerResponse<List<Integer>> getCategoryAndChildrenById(Integer categoryId);
}
