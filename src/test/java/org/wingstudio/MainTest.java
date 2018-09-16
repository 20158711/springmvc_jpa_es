package org.wingstudio;

import org.junit.Test;
import org.wingstudio.util.MD5Util;
import org.wingstudio.util.PropertiesUtil;

public class MainTest extends BaseTest {
    @Test
    public void test(){
        System.out.println(MD5Util.MD5EncodeUtf8("admin"));
        System.out.println(MD5Util.MD5EncodeUtf8("123456"));
    }
}
