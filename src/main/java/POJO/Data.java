package POJO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public class Data {
    String balance;
    long expireTime;
    ArrayList<Accounts> accounts;
}
