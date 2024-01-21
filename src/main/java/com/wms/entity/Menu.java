package com.wms.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
//import io.swagger.v3.oas.annotations.media.Schema;

//有时小驼峰命名会被识别成下划线分隔，这时候需要导入@Table一系列来辅助

/**
 * <p>
 * 
 * </p>
 *
 * @author nty
 * @since 2024-01-12
 */
@ApiModel(value = "Menu", description = "$!{table.comment}")
@TableName("menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId
    private Integer id;

    @ApiModelProperty(value = "菜单编码")
    @TableField("menuCode")
    private String menuCode;

    @ApiModelProperty(value = "菜单名字")
    @TableField("menuName")
    private String menuName;

    @ApiModelProperty(value = "菜单级别")
    @TableField("menuLevel")
    private String menuLevel;

    @ApiModelProperty(value = "菜单的父code")
    @TableField("menuParentCode")
    private String menuParentCode;

    @ApiModelProperty(value = "点击触发的函数")
    @TableField("menuClick")
    private String menuClick;

    @ApiModelProperty(value = "权限 0超级管理员，1表示管理员，2表示普通用户，可以用逗号组合使用")
    @TableField("menuRight")
    private String menuRight;

    @TableField("menuComponent")
    private String menuComponent;

    @TableField("menuIcon")
    private String menuIcon;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuLevel() {
        return menuLevel;
    }

    public void setMenuLevel(String menuLevel) {
        this.menuLevel = menuLevel;
    }

    public String getMenuParentCode() {
        return menuParentCode;
    }

    public void setMenuParentCode(String menuParentCode) {
        this.menuParentCode = menuParentCode;
    }

    public String getMenuClick() {
        return menuClick;
    }

    public void setMenuClick(String menuClick) {
        this.menuClick = menuClick;
    }

    public String getMenuRight() {
        return menuRight;
    }

    public void setMenuRight(String menuRight) {
        this.menuRight = menuRight;
    }

    public String getMenuComponent() {
        return menuComponent;
    }

    public void setMenuComponent(String menuComponent) {
        this.menuComponent = menuComponent;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    @Override
    public String toString() {
        return "Menu{" +
            "id = " + id +
            ", menuCode = " + menuCode +
            ", menuName = " + menuName +
            ", menuLevel = " + menuLevel +
            ", menuParentCode = " + menuParentCode +
            ", menuClick = " + menuClick +
            ", menuRight = " + menuRight +
            ", menuComponent = " + menuComponent +
            ", menuIcon = " + menuIcon +
        "}";
    }
}
