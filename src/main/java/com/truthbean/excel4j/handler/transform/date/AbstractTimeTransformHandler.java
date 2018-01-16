package com.truthbean.excel4j.handler.transform.date;

import com.truthbean.excel4j.handler.transform.CellEntityValueHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author TruthBean
 * @since 2018-01-15 16:52
 */
public abstract class AbstractTimeTransformHandler implements CellEntityValueHandler<Date, String> {

    private String dateTimeFormat;

    private SimpleDateFormat dateFormat;

    public void setFormat(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
        this.dateFormat = new SimpleDateFormat(dateTimeFormat);
        // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
        this.dateFormat.setLenient(false);
    }

    @Override
    public String format(Date date) {
        if (date == null || dateTimeFormat == null) {
            return null;
        }
        return dateFormat.format(date);
    }

    @Override
    public Date transform(String result) {
        try {
            return dateFormat.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
