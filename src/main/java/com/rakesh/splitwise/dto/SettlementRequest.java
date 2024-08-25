package com.rakesh.splitwise.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class SettlementRequest {
    private UUID user1Uuid;
    private UUID user2Uuid;
}
