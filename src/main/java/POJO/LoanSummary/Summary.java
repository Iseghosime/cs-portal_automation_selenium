package POJO.LoanSummary;

import POJO.Vendors.ApiErrors;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Summary {
    private String status;
    private String statusCode;
    private Details result;
    private ApiErrors apiErrors;
    private String message;
}
