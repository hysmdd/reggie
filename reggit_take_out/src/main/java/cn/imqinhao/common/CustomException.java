package cn.imqinhao.common;

/**
 * 自定义业务异常
 * @author qinhao
 * @version 1.0
 */
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }

}
