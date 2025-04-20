package com.nmims.fms.utils.pagination;

import lombok.Data;

@Data
public class FilterCriteria {

    private String logicalName; // The name of the field to filter on
    private String operator;   // The operator for the filter (e.g., "=", ">", "<", etc.)
    private Object value;      // The value to compare against the field
    private String type;       // The type of the field (e.g., "String", "Integer", etc.)
    private String column;     // The column name in the database
    private String table;      // The table name in the database
    
}
