package com.analysys.automation.modules.app.dao;

import com.github.crab2died.annotation.ExcelField;

public class TestCaseExcel {
    @ExcelField(title = "用例编号")
    private String testCaseCode;

    @ExcelField(title = "项目")
    private String projectName;

    @ExcelField(title = "模块")
    private String moduleName;

    @ExcelField(title = "版本")
    private String version;

    @ExcelField(title = "URI")
    private String uri;

    @ExcelField(title = "请求方式")
    private String method;

    @ExcelField(title = "请求内容")
    private String data;

    @ExcelField(title = "期望结果")
    private String expectedResult;

    @ExcelField(title = "描述")
    private String description;

    @Override
    public String toString() {
        return "TestCaseExcel{" +
                "testCaseCode='" + testCaseCode + '\'' +
                ", projectName='" + projectName + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", version='" + version + '\'' +
                ", uri='" + uri + '\'' +
                ", method='" + method + '\'' +
                ", data='" + data + '\'' +
                ", expectedResult='" + expectedResult + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getTestCaseCode() {
        return testCaseCode;
    }

    public void setTestCaseCode(String testCaseCode) {
        this.testCaseCode = testCaseCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
