package com.yuri.tam.core.rx;

/**
 * 事件实体类
 *
 * @param <T> 泛型参数
 */
public class Event<T> {

    //退出
    public static final int EVENT_FINISH = 10000;

    private int code = -1;
    private T data;

    public Event(int code) {
        this(code, null);
    }

    public Event(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

}
