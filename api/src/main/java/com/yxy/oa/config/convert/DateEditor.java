package com.yxy.oa.config.convert;


import org.apache.commons.lang3.time.DateUtils;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期转换类，支持多种的日期格式
 **/
public class DateEditor extends PropertyEditorSupport {

    private boolean emptyAsNull;
    private String dateFormat = "yyyy-MM-dd HH:mm:ss";
    public static final String[] DATE_PATTERNS = {"yyyy", "yyyy-MM", "yyyy-MM-dd", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss"};

    public DateEditor(boolean emptyAsNull) {
        this.emptyAsNull = emptyAsNull;
    }

    public DateEditor(boolean emptyAsNull, String dateFormat) {
        this.emptyAsNull = emptyAsNull;
        this.dateFormat = dateFormat;
    }

    @Override
    public String getAsText() {
        Date date = (Date) getValue();
        return date != null ? new SimpleDateFormat(this.dateFormat).format(date) : "";
    }

    @Override
    public void setAsText(String text) {
        if (text == null) {
            setValue(null);
        } else {
            String str = text.trim();
            if ((this.emptyAsNull) && ("".equals(str))) {
                setValue(null);
            } else {
                try {
                    setValue(DateUtils.parseDate(str, DATE_PATTERNS));
                } catch (ParseException e) {
                    setValue(null);
                }
            }
        }
    }
}
