package com.shuiwangzhijia.wuliu.event;



public class RefreshDataEvent {
    private String aim;
    private String methodName;

    public RefreshDataEvent(String aim, String methodName) {
        this.aim = aim;
        this.methodName = methodName;
    }

    public String getAim() {
        return aim;
    }

    public void setAim(String aim) {
        this.aim = aim;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
