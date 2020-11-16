package POJO.KYCProfile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public class KYCProfileResult {

    private String activationDate;
    private String sim;
    private String simType;
    private String status;
    private String lineType;
    private String serviceCategory;
    private String segment;
    private String subSegment;
    private String serviceClass;
    private Boolean vip;
    private ArrayList<PUKPOJO> puk;
}
