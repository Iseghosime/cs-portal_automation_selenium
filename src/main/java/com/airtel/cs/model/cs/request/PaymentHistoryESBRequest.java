package com.airtel.cs.model.cs.request;

import com.beust.jcommander.internal.Nullable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentHistoryESBRequest {

  private String accountNo;

  @Nullable
  private String fromDate;

  @Nullable
  private String toDate;

  @Nullable
  private String limit;

  @Nullable
  private String offset;
}

