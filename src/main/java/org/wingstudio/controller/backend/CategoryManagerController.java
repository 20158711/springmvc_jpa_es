package org.wingstudio.controller.backend;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wingstudio.common.ServerResponse;
import org.wingstudio.service.ICategoryService;
import org.wingstudio.service.IUserService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manager/category")
@Api(tags = "品类管理")
public class CategoryManagerController {

    @Autowired
    private IUserService userService;

    @Autowired
    private ICategoryService categoryService;



    @ApiOperation(value = "添加品类")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "categoryName",defaultValue = "testCategory"),
            @ApiImplicitParam(paramType = "query",name = "parentId",defaultValue = "0"),
            @ApiImplicitParam(paramType = "query",name = "session",defaultValue = "")
    })
    @RequestMapping(value = "add_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategory(HttpSession session,
                                      @RequestParam("categoryName") String categoryName,
                                      @RequestParam(value = "parentId",defaultValue = "0") Integer parentId){
        ServerResponse response = userService.checkAdmin(session);
        if (!response.isSuccess())
            return response;
        return categoryService.addCategory(categoryName,parentId);
    }

    @ApiOperation(value = "修改品类名")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "categoryId",defaultValue = "1"),
            @ApiImplicitParam(paramType = "query",name = "categoryName",defaultValue = "测试类型修改"),
            @ApiImplicitParam(paramType = "query",name = "session",defaultValue = "")
    })
    @RequestMapping(value = "set_category_name.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session,
                                          @RequestParam("categoryId") Integer categoryId,
                                          @RequestParam("categoryName") String categoryName){
        ServerResponse response = userService.checkAdmin(session);
        if (!response.isSuccess())
            return response;
        return categoryService.updateCategoryName(categoryId, categoryName);
    }

    @ApiOperation(value = "查询品类")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "categoryId",defaultValue = "1"),
            @ApiImplicitParam(paramType = "query",name = "session",defaultValue = "")
    })
    @RequestMapping(value = "get_category.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session,
                                                      @RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        ServerResponse response = userService.checkAdmin(session);
        if (!response.isSuccess())
            return response;
        ServerResponse childrenParallelCategory = categoryService.getChildrenParallelCategory(categoryId);
        return childrenParallelCategory;
    }

    @ApiOperation(value = "递归查询品类")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "categoryId",defaultValue = "1"),
            @ApiImplicitParam(paramType = "query",name = "session",defaultValue = "")
    })
    @RequestMapping(value = "get_deep_category.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,
                                                             @RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        ServerResponse response = userService.checkAdmin(session);
        if (!response.isSuccess())
            return response;
        return categoryService.getCategoryAndChildrenById(categoryId);
    }

}
