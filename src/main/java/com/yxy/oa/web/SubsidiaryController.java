package com.yxy.oa.web;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxy.oa.entity.SubsidiaryInfo;
import com.yxy.oa.exception.BizException;
import com.yxy.oa.exception.CodeMsg;
import com.yxy.oa.repository.IRedisRepository;
import com.yxy.oa.service.SubsidiaryService;
import com.yxy.oa.util.StringUtil;
import com.yxy.oa.util.Toolkit;
import com.yxy.oa.vo.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 子公司信息管理 前端控制器
 */
@RestController
@RequestMapping("/subsidiary")
public class SubsidiaryController extends BaseController {

    /**
     * 日志记录
     */
    private Logger logger = LoggerFactory.getLogger(SysPermissionController.class);


    @Autowired
    private SubsidiaryService subsidiaryService;


    @Autowired
    private IRedisRepository redisRepository;

    /**
     * 查询公司配置表,带分页
     *
     * @param subsidiaryInfo 公司配置表对象
     * @return
     */
    @RequestMapping("/list")
    @ApiOperation(value = "查询公司配置表列表", notes = "根据条件查询公司配置表列表", httpMethod = "POST", response = PageInfo.class)
    @RequiresPermissions("subsidiary:list")
    public PageInfo<SubsidiaryInfo> getList(SubsidiaryInfo subsidiaryInfo, Page page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), true);
        List<SubsidiaryInfo> list = subsidiaryService.selectList(new EntityWrapper<>(subsidiaryInfo));
        PageInfo<SubsidiaryInfo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    /**
     * 查询公司配置表,带分页
     *
     * @param subsidiaryInfo 公司配置表对象
     * @return
     */
    @RequestMapping("/listByNo")
    @ApiOperation(value = "查询公司配置表列表", notes = "根据条件查询公司配置表列表", httpMethod = "POST", response = PageInfo.class)
    public List<Map> listByNo(SubsidiaryInfo subsidiaryInfo) {
        return subsidiaryService.selectSubsidiaryNo();
    }

    /**
     * 新增公司配置表
     *
     * @param subsidiaryInfo 公司配置表对象
     */
    @RequestMapping("/insert")
    @ApiOperation(value = "新增公司配置表", notes = "新增公司配置表，必填项不能为空", httpMethod = "POST", response = String.class)
    @RequiresPermissions("subsidiary:insert")
    public String insert(SubsidiaryInfo subsidiaryInfo) {
        //检验参数不能为空
        if (StringUtil.hasNull(subsidiaryInfo.getSubsidiaryName(), subsidiaryInfo.getSubsidiaryCode(), subsidiaryInfo.getState())) {
            throw new BizException(CodeMsg.param_note_blank);
        }
        //检验参数不能重复
        if (subsidiaryService.checkCodeAndName(subsidiaryInfo)) {
            throw new BizException(CodeMsg.subsidiary_find_name_code);
        }
        //新增
        subsidiaryInfo.setApiKey(Toolkit.randomUUID());
        subsidiaryInfo.setState(subsidiaryInfo.getState());
        subsidiaryInfo.setApiSecret(Toolkit.randomUUID());
        subsidiaryInfo.setCreateTime(Toolkit.getCurDate());
        subsidiaryInfo.setCreateUid(getCurUserId());
        subsidiaryInfo.setUpdateTime(Toolkit.getCurDate());
        subsidiaryInfo.setUpdateUid(getCurUserId());
        subsidiaryService.insert(subsidiaryInfo);
        return SUCCESS;
    }


    /**
     * 修改公司配置表
     *
     * @param subsidiaryInfo 公司配置表对象
     */
    @RequestMapping("/update")
    @ApiOperation(value = "更新公司配置表", notes = "更新公司配置表，需要主键Id，必填项不能为空", httpMethod = "POST", response = String.class)
    @RequiresPermissions("subsidiary:update")
    public String update(SubsidiaryInfo subsidiaryInfo) {
        //检验参数不能为空
        if (StringUtil.hasNull(subsidiaryInfo.getId(), subsidiaryInfo.getSubsidiaryName(), subsidiaryInfo.getSubsidiaryCode(), subsidiaryInfo.getState())) {
            throw new BizException(CodeMsg.param_note_blank);
        }
        SubsidiaryInfo newSubsidiaryInfo = subsidiaryService.selectById(subsidiaryInfo.getId());
        //检验数据是否存在
        if (newSubsidiaryInfo == null) {
            throw new BizException(CodeMsg.resource_not_found);
        }
        //检验参数不能重复
        if (subsidiaryService.checkCodeAndNameExcludeThis(subsidiaryInfo)) {
            throw new BizException(CodeMsg.subsidiary_find_name_code);
        }

        //修改
        newSubsidiaryInfo.setUpdateUid(getCurUserId());
        newSubsidiaryInfo.setState(subsidiaryInfo.getState());
        newSubsidiaryInfo.setUpdateTime(Toolkit.getCurDate());
        newSubsidiaryInfo.setSubsidiaryCode(subsidiaryInfo.getSubsidiaryCode());
        newSubsidiaryInfo.setSubsidiaryName(subsidiaryInfo.getSubsidiaryName());
        subsidiaryService.updateById(newSubsidiaryInfo);
        //清除缓存
        redisRepository.del(newSubsidiaryInfo.getApiKey());

        return SUCCESS;
    }

    /**
     * 根据Id查询公司配置表
     *
     * @param id 主键id
     */
    @RequestMapping("/get")
    @ApiOperation(value = "获取公司配置表对象", notes = "根据主键Id获取公司配置表对象", httpMethod = "POST", response = String.class)
    @RequiresPermissions("subsidiary:update")
    public SubsidiaryInfo getById(Long id) {
        SubsidiaryInfo subsidiaryInfo = subsidiaryService.selectById(id);
        if (subsidiaryInfo == null) {
            throw new BizException(CodeMsg.resource_not_found);
        }
        return subsidiaryInfo;
    }

    /**
     * 删除公司配置表
     *
     * @param id 主键id
     */
    @RequestMapping("/delete")
    @ApiOperation(value = "删除公司配置表对象", notes = "根据主键Id删除公司配置表对象", httpMethod = "POST", response = String.class)
    @RequiresPermissions("subsidiary:delete")
    public String deleteById(Long id) {
        //检验参数不能为空
        if (id == null) {
            throw new BizException(CodeMsg.id_param_blank);
        }
        //检验数据是否存在
        if (subsidiaryService.selectById(id) == null) {
            throw new BizException(CodeMsg.resource_not_found);
        }
        //删除
        subsidiaryService.deleteById(id);
        return SUCCESS;
    }

}
