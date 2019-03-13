package com.truthbean.code.excel4j.util;

import com.truthbean.code.excel4j.entity.CellEntityTest;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author TruthBean
 * @since 2018-01-26 16:46
 */
public class ReflectionUtilsTest {

    @Test
    public void getSuperClassGenricType() {
    }

    @Test
    public void getSuperGenericType() {
        List<CellEntityTest> list = new ArrayList<>();
        Class<CellEntityTest> superGenericType = ReflectionUtils.getSuperGenericType(list.getClass());
        System.out.println(superGenericType);
    }

    @Test
    public void getDeclaredMethod() {
    }

    @Test
    public void makeAccessible() {
    }

    @Test
    public void getDeclaredField() {
    }

    @Test
    public void invokeMethod() {
    }

    @Test
    public void setFieldValue() {
    }

    @Test
    public void getFieldValue() {
    }

    @Test
    public void invokeSetMethod() {
        CellEntityTest test = new CellEntityTest();
        Object id = ReflectionUtils.invokeSetMethod(test, "id", 666, int.class);
        System.out.println(test);
    }

    @Test
    public void invokeGetMethod() {
        CellEntityTest test = new CellEntityTest();
        test.setId(666);
        Object id = ReflectionUtils.getPropertyValue(test, "id");
        System.out.println(id);
    }

    @Test
    public void getFields() {
        CellEntityTest test = new CellEntityTest();
        test.setId(666);
        List<Field> fields = ReflectionUtils.getDeclaredFields(CellEntityTest.class);
        System.out.println(fields);
    }
}