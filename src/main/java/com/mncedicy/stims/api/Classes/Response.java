package com.mncedicy.stims.api.Classes;

@SuppressWarnings("ALL")
public class Response {
    private String message;
    private String status;
    private Object data;
    private Object data1;
    private String extra;
    private Long id;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }
    public Object getData1() {
        return data1;
    }

    public void setData(Object data) {
        this.data = data;
    }
    public void setData1(Object data1) {
        this.data1 = data1;
    }
    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
