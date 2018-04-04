package com.nonobank.group.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.group.entity.remote.TestCase;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by tangrubei on 2018/3/16.
 */
public class ObjectUtil {



    public static Object setValue(Object object, JSONObject jsonObject) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Field[] fields = object.getClass().getDeclaredFields();
        for(Field field:fields) {
            Object attValue = jsonObject.get(field.getName());
            String setMethodName = CharsUtil.createSetMethodName(CharsUtil.functionFirstLetterUp.apply(field.getName()));
            Method method = object.getClass().getDeclaredMethod(setMethodName, String.class);
            method.invoke(object, attValue);
        }
        return object;
    }

    public static void main(String[] args) {
        TestCase reqTestCase = new TestCase();
        reqTestCase.setName("aaa");
        reqTestCase.setId(1);
        reqTestCase.setDescription("bbbb");
        reqTestCase.setChecked(false);
        JSONObject jsonObject = (JSONObject) JSON.toJSON(reqTestCase);
        TestCase reqTestCase1 = jsonObject.toJavaObject(TestCase.class);
        System.out.println("ok");


    }



}
