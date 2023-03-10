package com.airtel.cs.model.cs.response.kycprofile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultGsmKyc {
    private String name;
    private String dob;
    private String identificationType;
    private String identificationNo;
    private String gsm;
}
