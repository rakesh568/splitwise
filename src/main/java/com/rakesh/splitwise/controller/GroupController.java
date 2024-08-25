package com.rakesh.splitwise.controller;

import com.rakesh.splitwise.dto.ExpenseRequest;
import com.rakesh.splitwise.dto.GroupRequest;
import com.rakesh.splitwise.dto.SettlementRequest;
import com.rakesh.splitwise.dto.TransactionResponse;
import com.rakesh.splitwise.model.Group;
import com.rakesh.splitwise.model.User;
import com.rakesh.splitwise.service.GroupService;
import com.rakesh.splitwise.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UUID> createGroup(@RequestBody GroupRequest groupRequest) {
        // Create the group with the provided name and users
        Group group = groupService.createGroup(groupRequest.getName(), groupRequest.getUserMobiles());

        // Return the UUID of the created group
        return ResponseEntity.ok(group.getUuid());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Optional<Group>> getGroup(@PathVariable UUID uuid) {
        // Retrieve the group by its UUID
        return ResponseEntity.ok(groupService.getGroupByUuid(uuid));
    }

    @GetMapping("/{groupId}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactions(
            @PathVariable UUID groupId,
            @RequestParam UUID userUuid,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<TransactionResponse> transactions = groupService.getTransactionsForUserAndGroup(userUuid, groupId, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/{groupId}/settle")
    public ResponseEntity<Void> settleTransactionsBetweenUsers(@RequestBody SettlementRequest settlementRequest, @PathVariable UUID groupId) {

        groupService.settleTransactionsBetweenUsers(groupId, settlementRequest.getUser1Uuid(), settlementRequest.getUser2Uuid());
        return ResponseEntity.ok().build();
    }
}
