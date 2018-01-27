package com.truthbean.code.excel4j.entity;

import com.truthbean.code.excel4j.annotation.Column;
import com.truthbean.code.excel4j.annotation.ColumnValue;
import com.truthbean.code.excel4j.annotation.Sheet;
import com.truthbean.code.excel4j.handler.transform.number.BigDecimalTransformHandler;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author TruthBean
 * @since 2018-01-14 20:13
 */
@Sheet(name = "测试SHEET", bigTitle = "hello excel4j")
public class CellEntityTest extends CommonEntityTest {

    @Column(name = "ShortNum", columnValue = @ColumnValue(type = CellValueType.NUMBER))
    private long shortNum;

    @Column(order = 3, name = "用户名", width = 9000)
    private String userName;

    @Column(order = 5, name = "时间", columnValue = @ColumnValue(type = CellValueType.DATE))
    private Date time;

    @Column(order = 4, name = "金额", columnValue = @ColumnValue(type = CellValueType.NUMBER,
            transformHandler = BigDecimalTransformHandler.class))
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
        return decimal.abs();
    }

    public void setDecimal(BigDecimal decimal) {
        this.decimal = decimal;
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

    @Override
    public String toString() {
        return "{" + "shortNum:" + shortNum + "," + "userName:\"" + userName + "\"" + "," + "time:" + time + ","
                + "decimal:" + decimal + "," + "exception:" + exception + "super:" + super.toString() + "}";
    }
}