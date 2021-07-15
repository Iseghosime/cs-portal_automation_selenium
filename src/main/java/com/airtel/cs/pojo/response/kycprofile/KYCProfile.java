package com.airtel.cs.pojo.response.kycprofile;

import com.airtel.cs.pojo.response.APIErrors;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KYCProfile {
    private String status;
    private Integer statusCode;
    private KYCProfileResult result;
    private APIErrors apiErrors;
}
