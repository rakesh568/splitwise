package com.rakesh.splitwise.dto;

import lombok.Data;

import java.util.List;

@Data
public class GroupRequest {
    private List<String> userMobiles;
    private String name;

}
