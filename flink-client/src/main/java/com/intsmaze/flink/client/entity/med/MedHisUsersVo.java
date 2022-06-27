package com.intsmaze.flink.client.entity.med;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 人员字典视图对象 med_his_users
 *
 * @author ruoyi
 * @date 2022-06-08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedHisUsersVo {

    private static final long serialVersionUID = 1L;

    /**
     * N
     */
    private String userId;

    /**
     * N
     */
    private String userName;

    /**
     * N
     */
    private String userDept;

    /**
     * N
     */
    private String inputCode;

    /**
     * N
     */
    private String userJob;

    /**
     * N
     */
    private String reserved01;

    /**
     * N
     */
    private Date createDate;


}
