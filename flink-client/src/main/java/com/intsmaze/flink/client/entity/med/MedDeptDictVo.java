package com.intsmaze.flink.client.entity.med;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 科室字典视图对象 med_dept_dict
 *
 * @author jiangnan
 * @date 2022-06-09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedDeptDictVo {

    private static final long serialVersionUID = 1L;

   
    private Long serialNo;

   
    private String deptCode;

   
    private String deptName;

   
    private String inputCode;

   
    private String deptAlias;


}
