package com.yxy.oa.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxy.oa.entity.User;
import com.yxy.oa.entity.UserInfo;
import com.yxy.oa.entity.UserModifyRecord;
import com.yxy.oa.exception.BizException;
import com.yxy.oa.exception.CodeMsg;
import com.yxy.oa.repository.IRedisRepository;
import com.yxy.oa.service.IUserInfoService;
import com.yxy.oa.service.IUserModifyRecordService;
import com.yxy.oa.service.IUserService;
import com.yxy.oa.util.JacksonUtil;
import com.yxy.oa.util.ObjectUtil;
import com.yxy.oa.util.Toolkit;
import com.yxy.oa.vo.Page;
import com.yxy.oa.vo.UserVO;
import io.swagger.annotations.ApiOperation;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 用户名/手机号/邮箱认证表 前端控制器
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private String tempFolder = System.getProperty("java.io.tmpdir");

    @Value("${excel.export.maxnum:20000}")
    private int maxExportNum;

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IRedisRepository redisRepository;

    @Autowired
    private IUserModifyRecordService iUserModifyRecordService;


    public static final Integer exportNum = 10000;

    /**
     * 查询用户列表
     *
     * @param userVO
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    //@RequiresPermissions("user:list")
    public PageInfo<UserVO> getUserList(UserVO userVO, Page page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), true);
        List<UserVO> list = userService.getUserList(userVO);
        PageInfo<UserVO> userList = new PageInfo<>(list);
        return userList;
    }

    /**
     * 修改用户信息
     *
     * @param userVO
     * @return
     */
    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
    @ApiOperation(value = "更新会员基础信息表", notes = "更新会员基础信息表，需要主键Id，必填项不能为空", httpMethod = "POST", response = String.class)
    //@RequiresPermissions("user:update")
    public String updateUserInfo(UserVO userVO) {
        User user = userService.selectById(userVO.getId());
        if (ObjectUtil.isBlank(user) || StringUtils.isEmpty(user.getId())) {
            throw new BizException(CodeMsg.record_not_exist);
        }

        if (!ObjectUtil.isBlank(userVO.getPhone()) && !userVO.getPhone().equals(user.getPhone())) {
            List<UserVO> users = userService.selectByPhone(userVO.getPhone());
            if (users != null && !users.isEmpty()) {
                throw new BizException(CodeMsg.user_phone_exist);
            }
        }
        if (!ObjectUtil.isBlank(userVO.getEmail()) && !userVO.getEmail().equals(user.getEmail())) {
            List<UserVO> users = userService.selectByEmail(userVO.getEmail());
            if (users != null && !users.isEmpty()) {
                throw new BizException(CodeMsg.user_email_exist);
            }
        }
        //先清除缓存
        userService.clearCache(userVO);
        userService.clearCache(user);

        user.setId(user.getId());
        user.setPhone(userVO.getPhone());
        user.setEmail(userVO.getEmail());
        user.setUpdateUid(getCurUserId());
        user.setUpdateTime(Toolkit.getCurDate());
        userService.updateAllColumnById(user);

        UserInfo userInfo = userInfoService.selectById(user.getUserInfoId());
        if (ObjectUtil.isBlank(userInfo) || StringUtils.isEmpty(userInfo.getId())) {
            throw new BizException(CodeMsg.record_not_exist);
        }
        userInfo.setNickName(userVO.getNickName());
        userInfo.setSex(userVO.getSex());
        userInfo.setUpdateUid(getCurUserId());
        userInfo.setUpdateTime(Toolkit.getCurDate());
        userInfoService.updateAllColumnById(userInfo);
        redisRepository.del(user.getId().toString());

        // 记录修改记录
        UserModifyRecord modifyRecord = new UserModifyRecord();
        modifyRecord.setUserId(user.getId());
        modifyRecord.setSubsidiaryCode(user.getSubsidiaryCode());
        modifyRecord.setResourceContent(JacksonUtil.toJson(user));
        modifyRecord.setModifyContent(JacksonUtil.toJson(userVO));
        modifyRecord.setCreateTime(Toolkit.getCurDate());
        modifyRecord.setCreateUid(getCurUserId());
        modifyRecord.setUpdateTime(Toolkit.getCurDate());
        modifyRecord.setUpdateUid(getCurUserId());
        iUserModifyRecordService.insert(modifyRecord);
        return SUCCESS;
    }

    /**
     * 导出用户数据
     */
    @RequestMapping("/export")
    @RequiresPermissions("user:export")
    public void exportData(UserVO userVO, HttpServletResponse response) {
        int total = userService.selectTotal(userVO);
        //导出数量不允许超过配置的最大值
        if (total > maxExportNum) {
            throw new BizException("单次导出数据量不能超过" + maxExportNum + "条");
        }
        String excelName = "user.xlsx";
        InputStream in = null;
        OutputStream out = null;
        try {
            List<UserVO> userList = userService.getUserList(userVO);
            //设置响应
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + excelName);
            response.setContentType("application/vnd.ms-excel");
            Workbook workbook = toExcel(userList, in);
            out = response.getOutputStream();
            workbook.write(out);
            out.flush();
        } catch (InvalidFormatException e) {
            logger.error("导出excel格式错误", e);
            throw new BizException(CodeMsg.system_error);
        } catch (IOException e) {
            logger.error("导出excel io异常", e);
            throw new BizException(CodeMsg.system_error);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 先计算导出的数量
     */
    @RequestMapping("/exportNum")
    @RequiresPermissions("user:export")
    public int exportNum(UserVO userVO) {
        return userService.selectTotal(userVO);
    }

    /**
     * 重置密码
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    @RequiresPermissions("user:update")
    public int resetPassword(Long id) {
        return userService.resetPassword(id);
    }


    public void getZip(HttpServletResponse response) {
        OutputStream outputStream = null;
        try {
            String sourcePath = tempFolder + "/excelExport";
            String zipName = "user";
            String fileName = sourcePath + "/" + zipName + ".zip";
            fileToZip(sourcePath, sourcePath, zipName);
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=user.zip");
            response.setContentType("application/zip");
            //设置响应
            InputStream zipFileInputStream = new FileInputStream(fileName);
            outputStream = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int bufferSize = -1;
            while ((bufferSize = zipFileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bufferSize);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Workbook toExcel(List<UserVO> list, InputStream in) throws IOException, InvalidFormatException {
        ClassPathResource classPathResource = new ClassPathResource("jxls/template.xlsx");
        Map<String, Object> beans = new HashMap<String, Object>();
        beans.put("userList", list);
        XLSTransformer transformer = new XLSTransformer();
        in = new BufferedInputStream(classPathResource.getInputStream());
        Workbook workbook = transformer.transformXLS(in, beans);
        return workbook;
    }

    /**
     * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下
     *
     * @param sourceFilePath :待压缩的文件路径
     * @param zipFilePath    :压缩后存放路径
     * @param fileName       :压缩后文件的名称
     * @return
     */
    public static boolean fileToZip(String sourceFilePath, String zipFilePath, String fileName) {
        boolean flag = false;
        File sourceFile = new File(sourceFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        if (sourceFile.exists() == false) {
            System.out.println("待压缩的文件目录：" + sourceFilePath + "不存在.");
        } else {
            try {
                File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
                if (zipFile.exists()) {
                    System.out.println(zipFilePath + "目录下存在名字为:" + fileName + ".zip" + "打包文件.");
                } else {
                    File[] sourceFiles = sourceFile.listFiles();
                    if (null == sourceFiles || sourceFiles.length < 1) {
                        System.out.println("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");
                    } else {
                        fos = new FileOutputStream(zipFile);
                        zos = new ZipOutputStream(new BufferedOutputStream(fos));
                        byte[] bufs = new byte[1024 * 10];
                        for (int i = 0; i < sourceFiles.length; i++) {
                            //创建ZIP实体，并添加进压缩包
                            ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
                            zos.putNextEntry(zipEntry);
                            //读取待压缩的文件并写进压缩包里
                            fis = new FileInputStream(sourceFiles[i]);
                            bis = new BufferedInputStream(fis, 1024 * 10);
                            int read = 0;
                            while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                                zos.write(bufs, 0, read);
                            }
                        }
                        flag = true;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                //关闭流
                try {
                    if (null != bis) {
                        bis.close();
                    }
                    if (null != zos) {
                        zos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        return flag;
    }

}
