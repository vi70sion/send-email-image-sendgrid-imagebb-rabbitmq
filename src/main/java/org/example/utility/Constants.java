package org.example.utility;

public class Constants {

    public final static String URL = "jdbc:mysql://localhost:3306/work_queue";
    public final static String USERNAME = "root";
    public final static String PASSWORD = "*";
    public static final String SENDGRIDAPIKEY = "*";
    public static final String SENDEREMAIL = "*";
    public static final String IMAGEBBAPIKEY = "*";

    private Constants() {
        throw new AssertionError("Cannot instantiate the Constants class");
    }

}
