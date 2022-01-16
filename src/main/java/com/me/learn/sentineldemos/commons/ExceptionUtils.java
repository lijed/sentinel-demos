/*
 * Copyright 2022 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.me.learn.sentineldemos.commons;

/**
 * Description:
 *
 * @Author: Jed Li
 * Created: 2022/1/16
 **/
public class ExceptionUtils {

    public static String handleException(Exception exception) {
        return exception.getMessage();
    }
}
