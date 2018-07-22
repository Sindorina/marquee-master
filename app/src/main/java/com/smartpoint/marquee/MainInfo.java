package com.smartpoint.marquee;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/4/9.
 */

public class MainInfo implements Serializable {
    private List<String> columns;
    private List<String> rows;
    private List<List<List<String>>> data;

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<String> getRows() {
        return rows;
    }

    public void setRows(List<String> rows) {
        this.rows = rows;
    }

    public List<List<List<String>>> getData() {
        return data;
    }

    public void setData(List<List<List<String>>> data) {
        this.data = data;
    }
}

