package org.wingstudio.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public class UpdateUtil {

    public static void copyNotNullProperties(Object src,Object dest,String...exIncludes){
        BeanUtils.copyProperties(src, dest,getNullProperties(src,exIncludes));
    }

    private static String[] getNullProperties(Object src,String[] exIncludes){
        BeanWrapper srcBean=new BeanWrapperImpl(src);
        PropertyDescriptor[] descriptors = srcBean.getPropertyDescriptors();
        Set<String> emptyName=new HashSet<>();
        for (PropertyDescriptor descriptor : descriptors) {
            Object value = srcBean.getPropertyValue(descriptor.getName());
            if (value == null) {
                emptyName.add(descriptor.getName());
            }
        }
        for (String exInclude : exIncludes) {
            emptyName.add(exInclude);
        }
        String[] result=new String[emptyName.size()];
        return emptyName.toArray(result);
    }

}
