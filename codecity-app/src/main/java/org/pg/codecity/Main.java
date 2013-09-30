package org.pg.codecity;

public class Main {
    public static void main(String[] args) throws Exception {
        String trackingUtilDir = "c:\\Projects\\HPS\\Development\\11-HPR\\20 Track and Trace\\TrackingAll\\tracking-utils\\src\\main\\java\\";
        String fileName = trackingUtilDir + "net\\hellmann\\tracking\\trackingutils\\util\\StringUtils.java";
        JavaClass clazz = JavaClass.parseCommonTree(fileName);
        clazz.dump();
    }

}
