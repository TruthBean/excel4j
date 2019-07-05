package com.truthbean.excel4j.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 反射Utils 函数集合 提供访问私有变量, 获取泛型类型 Class, 提取集合中元素属性等 Utils 函数
 *
 * @author TruthBean
 * @since 0.0.1
 */
public final class ReflectionUtils {

    /**
     * slf4j logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtils.class);

    private ReflectionUtils() {
    }

    /**
     * 通过反射, 获得定义 Class 时声明的父类的泛型参数的类型
     *
     * @param clazz class
     * @param index index
     * @return class
     */
    public static Class<?> getSuperClassGenricType(Class<?> clazz, int index) {
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }

        if (!(params[index] instanceof Class)) {
            return Object.class;
        }

        return (Class<?>) params[index];
    }

    /**
     * 通过反射, 获得 Class 定义中声明的父类的泛型参数类型
     *
     * @param <T>
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getSuperGenericType(Class<?> clazz) {
        return (Class<T>) getSuperClassGenricType(clazz, 0);
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod
     *
     * @param object
     * @param methodName
     * @param parameterTypes
     * @return
     */
    public static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {

        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                //Method 不在当前类, 继续向上转型
            }
            //..
        }

        return null;
    }

    /**
     * make field public
     *
     * @param field field
     */
    public static void makeAccessible(Field field) {
        if (!Modifier.isPublic(field.getModifiers())) {
            field.setAccessible(true);
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField
     *
     * @param object
     * @param filedName
     * @return
     */
    public static Field getDeclaredField(Object object, String filedName) {

        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(filedName);
            } catch (NoSuchFieldException e) {
                //Field 不在当前类, 继续向上转型
            }
        }
        return null;
    }

    public static List<Field> getDeclaredFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            fields.addAll(Arrays.asList(superClass.getDeclaredFields()));
        }
        return fields;
    }

    /**
     * 直接调用对象方法, 而忽略修饰符(private, protected)
     *
     * @param object
     * @param methodName
     * @param parameterTypes
     * @param parameters
     * @return
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes,
                                      Object[] parameters) throws InvocationTargetException {

        Method method = getDeclaredMethod(object, methodName, parameterTypes);

        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");
        }

        method.setAccessible(true);

        try {
            return method.invoke(object, parameters);
        } catch (IllegalAccessException e) {
            System.out.println("不可能抛出的异常");
        }

        return null;
    }

    /**
     * 直接设置对象属性, 忽略 private/protected 修饰符, 也不经过 setter
     *
     * @param object
     * @param fieldName
     * @param value
     */
    public static Object setFieldValue(Object object, String fieldName, Object value) {
        Field field = getDeclaredField(object, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }

        makeAccessible(field);

        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            System.out.println("不可能抛出的异常");
        }

        return object;
    }

    /**
     * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter
     *
     * @param object
     * @param fieldName
     * @return
     */
    public static Object getFieldValue(Object object, String fieldName) {
        Field field = getDeclaredField(object, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }

        makeAccessible(field);

        Object result = null;

        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
            LOGGER.error("不可能抛出的异常");
        }

        return result;
    }

    /**
     * invoke set method
     *
     * @param target         target object
     * @param fieldName      field name
     * @param arg            arg
     * @param parameterTypes arg type
     * @return invoke method result
     */
    public static Object invokeSetMethod(Object target, String fieldName, Object arg, Class<?>... parameterTypes) {
        String methodName = "set" + handleFieldName(fieldName);
        try {
            Method method = target.getClass().getMethod(methodName, parameterTypes);
            return method.invoke(target, arg);
        } catch (NoSuchMethodException e) {
            LOGGER.error(fieldName + " set method not found", e);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * invoke set method
     *
     * @param target         target object
     * @param fieldName      field name
     * @param arg            arg
     * @param parameterTypes arg type
     * @return invoke method result
     */
    public static Object setPropertyValue(Object target, String fieldName, Object arg, Class<?>... parameterTypes) {
        Object o = invokeSetMethod(target, fieldName, arg, parameterTypes);
        if (o == null) {
            o = setFieldValue(target, fieldName, arg);
        }
        return 0;
    }

    /**
     * invoke get method
     *
     * @param target    target object
     * @param fieldName field name
     * @return invoke method result
     */
    public static Object invokeGetMethod(Object target, String fieldName) {
        String methodName = "get" + handleFieldName(fieldName);
        try {
            Method method = getDeclaredMethod(target, methodName, null);
            if (method != null) {
                method.setAccessible(true);
                return method.invoke(target);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.warn(fieldName + " invoke error", e);
        }
        return null;
    }

    public static Object getPropertyValue(Object target, String fieldName) {
        Object o = invokeGetMethod(target, fieldName);
        if (o == null) {
            o = getFieldValue(target, fieldName);
        }
        return o;
    }

    public static Object getPropertyValue(Object target, Field field) {
        return getPropertyValue(target, field.getName());
    }

    /**
     * make char of field name upper
     *
     * @param fieldName filed name
     * @return string
     */
    private static String handleFieldName(String fieldName) {
        String string = fieldName.substring(0, 1).toUpperCase();
        if (fieldName.length() > 1) {
            string += fieldName.substring(1);
        }
        return string;
    }

    /**
     * invoke get method
     *
     * @param target target object
     * @param field  field
     * @return invoke method result
     */
    public static Object invokeGetMethod(Object target, Field field) {
        String fieldName = field.getName();
        return invokeGetMethod(target, fieldName);
    }

}
