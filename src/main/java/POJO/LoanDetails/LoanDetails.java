package POJO.LoanDetails;

import POJO.Vendors.HeaderList;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.ArrayList;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoanDetails {
    private ArrayList<HeaderList> headerList;
    private ArrayList<LoanDetailList> loanDetailList;
}
