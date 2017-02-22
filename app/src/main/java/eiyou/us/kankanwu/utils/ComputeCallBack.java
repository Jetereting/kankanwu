package eiyou.us.kankanwu.utils;

/**
 * 非主线程完成后执行什么
 */
public abstract class ComputeCallBack {
    public abstract void onComputeEnd(Object o);
    public  void onFailure(){

    };
}
