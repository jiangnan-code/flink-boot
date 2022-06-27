package com.intsmaze.flink.client.entity.nursing;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class N001  implements Serializable {

    /**
     * 手术主记录ID
     */
    private String operationMasterId;

    private ByOperationMasterDomain operationMaster;

    private ByOperationPatientDomain operationPatient;

    private List<ByOperationStaffDomain> operationStaff;

    private List<ByOperationNameDomain> operationName;
}
