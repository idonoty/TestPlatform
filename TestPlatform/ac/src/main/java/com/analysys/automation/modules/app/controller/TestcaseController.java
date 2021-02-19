package com.analysys.automation.modules.app.controller;

import com.analysys.automation.common.annotation.SysLog;
import com.analysys.automation.common.utils.Constant;
import com.analysys.automation.common.utils.PageUtils;
import com.analysys.automation.common.utils.PoiUtils;
import com.analysys.automation.common.utils.Ret;
import com.analysys.automation.modules.app.dao.TestCaseExcel;
import com.analysys.automation.modules.app.entity.TestcaseEntity;
import com.analysys.automation.modules.app.service.TestcaseService;
import com.analysys.automation.modules.sys.controller.AbstractController;
import com.analysys.automation.modules.sys.service.FileService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.crab2died.ExcelUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2018-10-10 14:36:34
 */
@RestController
@RequestMapping("app/testcase")
public class TestcaseController extends AbstractController {
    @Autowired
    private TestcaseService testcaseService;

    @Autowired
    private FileService fileService;

    @Value("${file.filepath.download}")
    private String downloadFilePath;

    @Value("${file.filepath.template}")
    private String templateFilePath;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("app:testcase:list")
    public Ret list(@RequestParam Map<String, Object> params) {
        PageUtils page = testcaseService.queryPage(params);

        return Ret.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{testcaseId}")
    @RequiresPermissions("app:testcase:info")
    public Ret info(@PathVariable("testcaseId") Integer testcaseId) {
        TestcaseEntity testcase = testcaseService.selectById(testcaseId);

        return Ret.ok().put("testcase", testcase);
    }

    /**
     * 保存
     */
    @SysLog("保存测试用例")
    @RequestMapping("/save")
    @RequiresPermissions("app:testcase:save")
    public Ret save(@RequestBody TestcaseEntity testcase) {
        testcase.setStatus(Constant.Status.NORMAL.getValue());
        testcaseService.insert(testcase);

        return Ret.ok();
    }

    /**
     * 修改
     */
    @SysLog("修改测试用例")
    @RequestMapping("/update")
    @RequiresPermissions("app:testcase:update")
    public Ret update(@RequestBody TestcaseEntity testcase) {
        testcase.setStatus(Constant.Status.NORMAL.getValue());
        testcaseService.updateById(testcase);

        return Ret.ok();
    }

    /**
     * 删除
     */
    @SysLog("删除测试用例")
    @RequestMapping("/delete")
    @RequiresPermissions("app:testcase:delete")
    public Ret delete(@RequestBody Integer[] testcaseIds) {
        testcaseService.deleteBatchIds(Arrays.asList(testcaseIds));
        return Ret.ok();
    }

    /**
     * 上传
     */
    @SysLog("批量导入测试用例")
    @RequestMapping("/upload")
    @RequiresPermissions("app:testcase:upload")
    public Ret upload(MultipartFile file) {
        int value = fileService.validate(file, "xlsx");
        if (value == 1) {
            return Ret.error("文件内容为空");
        } else if (value == 2) {
            return Ret.error("文件后缀名称不为xlsx");
        }

        String absolutePath = fileService.upload(file);
        if (absolutePath == null) {
            return Ret.error("上传文件保存失败");
        }

        Iterator<Sheet> it = null;
        try {
            it = PoiUtils.readExcel(absolutePath);
        } catch (Exception e) {
            e.printStackTrace();
            return Ret.error("解析文件失败");
        }
        List<TestcaseEntity> testcaseEntityList = new ArrayList<>();
        List<TestCaseExcel> list;
        try {
            list = ExcelUtils.getInstance().readExcel2Objects(absolutePath, TestCaseExcel.class);
            //List<ArrayList<Object>> list = PoiUtils.readSheet(it.next(), 0, 8);
            logger.info("导入文件列表大小为 " + list.size());
            //logger.info("列表每行有 [" + list.get(0).size() + "] 列");
            for (TestCaseExcel testCaseExcel : list) {
                TestCaseExcel testcase = new TestCaseExcel();
                BeanUtils.copyProperties(testcase, testCaseExcel);
                if (StringUtils.isNotBlank(testcase.getTestCaseCode())){
                    String testcaseCode = testcase.getTestCaseCode();
                    String projectName = testcase.getProjectName();
                    String moduleName = testcase.getModuleName();
                    String version = testcase.getVersion();
                    String uri = testcase.getUri();
                    String method = testcase.getMethod();
                    String data = testcase.getData();
                    String expectedResult = testcase.getExpectedResult();
                    String description = testcase.getDescription();
                    logger.info(testcase.toString());
                    TestcaseEntity testcaseEntity = testcaseService.selectOne(new EntityWrapper<TestcaseEntity>().eq("project_name", projectName).eq("module_name", moduleName).eq("version", version).eq("testcase_code", testcaseCode));
                    if (testcaseEntity == null)
                        testcaseEntity = new TestcaseEntity();
                    testcaseEntity.setProjectName(projectName);
                    testcaseEntity.setModuleName(moduleName);
                    testcaseEntity.setUri(uri);
                    testcaseEntity.setMethod(method);
                    testcaseEntity.setData(data);
                    testcaseEntity.setTestcaseCode(testcaseCode);
                    testcaseEntity.setExpectedResult(expectedResult);
                    testcaseEntity.setDescription(description);
                    testcaseEntity.setStatus(10);
                    testcaseEntity.setVersion(version);
                    testcaseEntityList.add(testcaseEntity);
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
            return Ret.error("excel文件读取失败");
        }

        if (testcaseService.insertOrUpdateBatch(testcaseEntityList)) {
            return Ret.ok();
        }
        return Ret.error("未知错误!");
    }

    /**
     * 下载
     */
    @SysLog("下载测试用例模板")
    @RequestMapping("/downloadtemplate")
    @RequiresPermissions("app:testcase:downloadtemplate")
    public void download(HttpServletResponse response) throws Exception {
        String fileName = "testcase_template.xlsx";// 设置文件名，根据业务需要替换成要下载的文件名
        List<TestCaseExcel> XRexcel = new ArrayList<TestCaseExcel>();
        TestCaseExcel testCaseExcel = new TestCaseExcel();
        XRexcel.add(testCaseExcel);
        ExcelUtils.getInstance().exportObjects2Excel(XRexcel,TestCaseExcel.class,fileName);
        this.download(response, this.templateFilePath, fileName);
    }

    /**
     * 下载
     */
    @SysLog("下载测试用例")
    @RequestMapping("/downloadtestcases")
    @RequiresPermissions("app:testcase:downloadtestcases")
    public void downloadTestcases(HttpServletResponse response, @RequestParam Map<String, Object> params) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String dateString = df.format(new Date());
        String filePath = downloadFilePath + dateString + "\\";
        String fileName = "testcases.xlsx";  // 设置文件名，根据业务需要替换成要下载的文件名
        List<ArrayList<Object>> rows = new ArrayList<>();
        ArrayList<Object> titleRow = new ArrayList<>();
        titleRow.add("用例编码");
        titleRow.add("项目名称");
        titleRow.add("模块名称");
        titleRow.add("用例版本");
        titleRow.add("URI");
        titleRow.add("请求方式");
        titleRow.add("请求数据");
        titleRow.add("期望结果");
        titleRow.add("用例描述");
        rows.add(titleRow);

        List<TestcaseEntity> list = testcaseService.queryList(params);
        for (TestcaseEntity testcaseEntity : list) {
            ArrayList<Object> tempRow = new ArrayList<>();
            tempRow.add(testcaseEntity.getTestcaseCode());
            tempRow.add(testcaseEntity.getProjectName());
            tempRow.add(testcaseEntity.getModuleName());
            tempRow.add(testcaseEntity.getVersion());
            tempRow.add(testcaseEntity.getUri());
            tempRow.add(testcaseEntity.getMethod());
            if(testcaseEntity.getData().length()>30000){
                tempRow.add("数据超长,取前2000字符： "+testcaseEntity.getData().substring(0,2000));
            }else {
                tempRow.add(testcaseEntity.getData());
            }
            if(testcaseEntity.getExpectedResult().length()>30000){
                tempRow.add("数据超长,取前2000字符： "+testcaseEntity.getExpectedResult().substring(0,2000));
            }else {
                tempRow.add(testcaseEntity.getExpectedResult());
            }
            tempRow.add(testcaseEntity.getDescription());
            rows.add(tempRow);
        }

        PoiUtils.writeExcel(filePath, fileName, "Sheet1", rows);

        this.download(response, filePath, fileName);
    }


    @RequestMapping("/queryprojects")
    @RequiresPermissions("app:testcase:pm")
    public Ret queryProjects() {
        return Ret.ok().put("list", testcaseService.queryAllProjects());
    }

    @RequestMapping("/querymodules")
    @RequiresPermissions("app:testcase:pm")
    public Ret queryModules(@RequestParam String projectName) {
        return Ret.ok().put("list", testcaseService.queryModuleNames(projectName));
    }

    @RequestMapping("/queryversions")
    @RequiresPermissions("app:testcase:pm")
    public Ret queryVersions(@RequestParam String projectName) {
        return Ret.ok().put("list", testcaseService.queryVersions(projectName));
    }
}
