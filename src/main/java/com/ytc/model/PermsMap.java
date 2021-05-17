/**
 * @Copyright (C), 2015-2021, XXX有限公司
 * @FileName: PermsMap
 * @Author: qintianyu
 * @Date: 2021/4/30 15:52
 * @Description:
 * @Version: 1.0
 */
package com.ytc.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Description
 *
 *
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-configuration-processor</artifactId>
        <optional>true</optional>
    </dependency>
 * @author qintianyu
 * @create 2021/4/30
 * @since 1.0.0
 */
@ConfigurationProperties(prefix="permissions")
@Component
public class PermsMap {

    private List<Map<String,String>> perms;

    public List<Map<String, String>> getPerms() {
        return perms;
    }

    public void setPerms(List<Map<String, String>> perms) {
        this.perms = perms;
    }


}
