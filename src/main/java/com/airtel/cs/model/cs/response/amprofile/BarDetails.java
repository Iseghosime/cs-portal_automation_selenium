package com.airtel.cs.model.cs.response.amprofile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BarDetails implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -2617541611656576041L;

    private String bar_reason;
    private String remarks;
    private String barred_by;
    private String barred_on;
    private String bar_type;
}

