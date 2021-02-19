package com.analysys.automation.modules.app.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TestcaseVO implements Serializable {
    private Integer testcaseCode;
    /**
     *
     */
    private String projectName;
    /**
     *
     */
    private String moduleName;

    private String version;
    /**
     *
     */
    private String uri;
    /**
     *
     */
    private String method;
    /**
     *
     */
    private String data;
    private String expectedResult;

    private String description;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getTestcaseCode()).append(", ").
                append(this.getProjectName()).append(", ").
                append(this.getModuleName()).append(", ").
                append(this.getVersion()).append(", ").
                append(this.getUri()).append(", ").
                append(this.getMethod()).append(", ").
                append(this.getData()).append(", ").
                append(this.getExpectedResult()).append(", ").
                append(this.getDescription());
        return sb.toString();
    }
}