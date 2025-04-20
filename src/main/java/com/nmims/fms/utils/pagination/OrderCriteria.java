package com.nmims.fms.utils.pagination;

import lombok.Data;

@Data
public class OrderCriteria {
    
    private String logicalName; // The name of the field to order by
    private String orderType;   // The type of order (e.g., "asc" for ascending, "desc" for descending)
    private String table;        // The type of the field (e.g., "String", "Integer", etc.)
    private String column;
}
