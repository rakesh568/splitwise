package com.rakesh.splitwise.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ExpenseRequest {
    private UUID groupId;
    private String expenseName;
    private double totalAmount;
    private String splitType;
    private UUID paidBy;
    private List<Share> splitBreakup;

    @Data
    public static class Share {
        private UUID userUuid;
        private double exactShare;
    }
}
