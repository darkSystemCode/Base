package com.sy.base;

import com.sy.util.Utils;
import lombok.Data;

import java.util.Date;

/**
 * @Author: shuYan
 * @Date: 2023/5/2 19:58
 * @Descript: 基础实体类
 */
@Data
public class BaseEntity {
    private String id;
    private Date createTime;
    private Date updateTime;

}
