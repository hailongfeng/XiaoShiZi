package edu.children.xiaoshizi.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import zuo.biao.library.util.MD5Util;

public class Tools {
    public static String createSign( SortedMap<Object,Object> parameters){
        StringBuffer sb = new StringBuffer();
        StringBuffer sbkey = new StringBuffer();
        Set es = parameters.entrySet();  //所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            //空值不传递，不参与签名组串
            if(null != v && !"".equals(v)) {
                sb.append(k + "=" + v + "&");
                sbkey.append(k + "=" + v + "&");
            }
        }
//       String param=sbkey.substring(0,sbkey.length()-1);
        //System.out.println("字符串:"+sb.toString());
        sbkey=sbkey.append("key="+Constant.ENCRYPT_KEY);
        System.out.println("字符串:"+sbkey.toString());
        //MD5加密,结果转换为大写字符
        String sign = MD5Util.MD5(sbkey.toString(),Constant.ENCRYPT_KEY);
        System.out.println("MD5加密值:"+sign);
        return sign;
    }
}
