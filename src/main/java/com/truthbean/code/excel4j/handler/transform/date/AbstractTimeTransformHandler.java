package com.truthbean.code.excel4j.handler.transform.date;

import com.truthbean.code.excel4j.handler.transform.CellEntityValueHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author TruthBean
 * @since 0.0.1
 */
public abstract class AbstractTimeTransformHandler implements CellEntityValueHandler<Date, String> {

    /**
     * date time format, eg 'YYYY-MM-DD HH:mm:SS'
     */
    private String dateTimeFormat;

    /**
     * dateFormat class
     */
    private SimpleDateFormat dateFormat;

    /**
     * set dateTimeFormat
     * @param dateTimeFormat dateTimeFormat
     */
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
