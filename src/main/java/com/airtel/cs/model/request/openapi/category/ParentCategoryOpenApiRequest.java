package com.airtel.cs.model.request.openapi.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParentCategoryOpenApiRequest {
    private String status;
    private Integer statusCode;
    private String message;
    private Map<String, ArrayList<CategoryHierarchyOpenApi>> result;
}
