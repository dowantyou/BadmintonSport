package com.badmintonsport.common.result;

import lombok.Data;

import java.io.Serializable;

@Data
public class Resultreturnresult<T> implements Serializable {
    private T result;
    private String msg;
    private boolean success;
    public static <T> Resultreturnresult<T> success(T object) {
        Resultreturnresult<T> res = new Resultreturnresult<T>();
        res.success = true;
        res.msg = "查询成功";
        res.result = object;
        return res;
    }

    public static <T> Resultreturnresult<T> error(String msg) {
        Resultreturnresult res= new Resultreturnresult();
        res.msg = msg;
        res.success = false;
        return res;
    }
}
