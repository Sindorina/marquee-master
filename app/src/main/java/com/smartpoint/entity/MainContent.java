package com.smartpoint.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2018/10/17
 * 邮箱 18780569202@163.com
 */
public class MainContent extends DataSupport {
    private String document;

    public MainContent() {

    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }
}
