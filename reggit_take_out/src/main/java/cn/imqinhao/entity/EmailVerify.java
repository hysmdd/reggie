package cn.imqinhao.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qinhao
 * @version 1.0
 */
@Data
public class EmailVerify implements Serializable {
    private String email;
    private String code;
}
