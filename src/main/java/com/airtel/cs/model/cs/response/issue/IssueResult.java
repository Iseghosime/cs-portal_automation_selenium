package com.airtel.cs.model.cs.response.issue;

import com.airtel.cs.model.cs.request.issue.IssueDetail;
import com.airtel.cs.model.cs.request.issue.TicketDetail;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IssueResult {
    private String interactionId;
    private IssueDetail issues;
    private TicketDetail ticket;
}
