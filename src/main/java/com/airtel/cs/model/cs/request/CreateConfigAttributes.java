package com.airtel.cs.model.cs.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateConfigAttributes {
    private String opco;
    private String key;
    private String value;
}
