package com.airtel.cs.model.cs.response.voucher;

import com.airtel.cs.model.cs.response.apierror.APIErrors;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoucherSearch {
    private String status;
    private Integer statusCode;
    private VoucherDetail result;
    private String message;
    private APIErrors apiErrors;
}
