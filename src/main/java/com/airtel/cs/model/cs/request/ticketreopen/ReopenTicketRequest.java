package com.airtel.cs.model.cs.request.ticketreopen;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReopenTicketRequest {
    private String status;
    private Integer statusCode;
    private String message;
    private ReopenResult result;
}
