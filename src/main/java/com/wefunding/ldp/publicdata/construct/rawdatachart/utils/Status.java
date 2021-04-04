package com.wefunding.ldp.publicdata.construct.rawdatachart.utils;

/**
 * Created by yes on 2020/10/12
 */
//재정의한 StatusCode
public class Status {
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int NO_CONTENT = 204;
    public static final int BAD_REQUEST =  400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int INTERNAL_SERVER_ERROR = 500;

    public static final int DB_ERROR = 600;
}
