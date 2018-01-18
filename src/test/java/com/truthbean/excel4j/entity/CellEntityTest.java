package com.truthbean.excel4j.entity;

import com.truthbean.excel4j.annotation.Column;
import com.truthbean.excel4j.annotation.Sheet;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author TruthBean
 * @since 2018-01-14 20:13
 */
@Sheet(name = "测试SHEET", bigTitle = "测试....")
public class CellEntityTest {

    @Column(order = 2, column = "ID", valueClass = CellEntityValueClass.DOUBLE, columnWidth = 2864)
    private int id;

    @Column(order = 1, column = "ShortNum", valueClass = CellEntityValueClass.DOUBLE)
    private long shortNum;

    @Column(order = 3, column = "用户名", valueClass = CellEntityValueClass.TEXT, columnWidth = 9000)
    private String userName;

    @Column(order = 5, column = "时间", valueClass = CellEntityValueClass.DATE)
    private Date time;

    @Column(order = 4, column = "金额", valueClass = CellEntityValueClass.DOUBLE)
    private BigDecimal decimal;

    private Exception exception;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public BigDecimal getDecimal() {
        return decimal;
    }

    public void setDecimal(BigDecimal decimal) {
        this.decimal = decimal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getShortNum() {
        return shortNum;
    }

    public void setShortNum(long shortNum) {
        this.shortNum = shortNum;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}