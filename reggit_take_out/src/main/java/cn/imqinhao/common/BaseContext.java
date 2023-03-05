package cn.imqinhao.common;

/**
 * 基于ThreadLocal封装工具类，用户保存和获取当前登录用户id
 * @author qinhao
 * @version 1.0
 */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置值
     * @param id 用户id
     */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    /**
     * 获取值
     * @return 当前ThreadLocal存放的用户id
     */
    public static Long getCurrentId() {
        return threadLocal.get();
    }

}
