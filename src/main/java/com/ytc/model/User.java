/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: User
 * Author:   15065
 * Date:     2020/9/5 9:26
 * Description:
 * History:
 * qty               <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.ytc.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 15065
 * @create 2020/9/5
 * @since 1.0.0
 */
@ConfigurationProperties(prefix="user")
@Component
@Data
@Table(name = "t_user")
public class User {
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Id
    Integer userId;
    String userName;
    String userPwd;
    String perms;
}
