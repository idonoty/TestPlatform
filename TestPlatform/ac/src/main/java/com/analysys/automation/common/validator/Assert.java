package com.analysys.automation.common.validator;

import com.analysys.automation.common.exception.AutomationException;
import org.apache.commons.lang.StringUtils;


public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new AutomationException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new AutomationException(message);
        }
    }
}
