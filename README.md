# API Documentation

## Overview
This API allows for the management of users, groups, and expenses. It includes endpoints to create and retrieve users, groups, and expenses, as well as to retrieve transactions associated with a group for a specific user.


---

## API Documentation
### Base URL
http://localhost:8080/api

### 1. **User Management**

#### 1.1 Create a New User
- **Endpoint**: `/users`
- **Method**: `POST`
- **Description**: Creates a new user in the system.
- **Request Body**:
  ```json
  {
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phone": "1234567890"
  }
  ```
- **Response**:
    - Status: 201 Created
    - Body
  ```json
    {
      "userId": "c2df8e22-01ce-44df-b5ed-7feb749e579c"
    }
    ```

#### 1.2 Get a User by UUID

- Endpoint: `/users/{uuid}`
- Method: GET
- Description: Retrieves user details by their UUID.
- Path Parameters:
    - uuid: The UUID of the user.
- Response:
    - Status: 200 OK
    - Body:
      ```json
      {
        "name": "John Doe",
        "email": "john.doe@example.com",
        "phone": "1234567890",
      }
      ```

### 2. **Group Management**

#### 2.1 Create a New Group

- Endpoint: `/groups`
- Method: POST
- Description: Creates a new group with the provided name and users.
- Request Body:
  ```json
  {
    "name": "Friends Group",
    "userMobiles": ["1234567890", "0987654321"]
  }
  ```
- Response:
    - Status: 200 OK
    - Body:
      ```json
      {
        "uuid": "0168c3a5-c95f-4a1f-b9e8-86944762387e"
      }
      ```

#### 2.2 Get a Group by UUID

- Endpoint: `/groups/{uuid}`
- Method: GET
- Description: Retrieves group details by its UUID.
- Path Parameters:
    - uuid: The UUID of the group.
- Response:
    - Status: 200 OK
    - Body:
      ```json
      {
        "name": "Friends Group",
        "members": ["0168c3a5-c95f-4a1f-b9e8-86944762387e", "2138c5a5-c96f-3a1f-c9e8-76944362387b"]
      }
      ```

#### 2.3 Get Transactions for a Group and User

- Endpoint: `/groups/{groupId}/transactions?userUuid=<uuid>&startDate=<yyyy-mm-dd>&endDate=<yyyy-mm-dd>`
- Method: GET
- Description: Retrieves transactions for a specific group and user within a date range.
- Path Parameters:
    - groupId: The UUID of the group.
- Query Parameters:
    - userUuid: The UUID of the user.
    - startDate: The start date for filtering transactions (ISO format: YYYY-MM-DD).
    - endDate: The end date for filtering transactions (ISO format: YYYY-MM-DD).
- Response:
    - Status: 200 OK
    - Body:
      ```json
      [
        {
          "date": "2024-06-10",
          "group": "Office",
          "expense": "Breakfast @nisarga",
          "totalAmount": 100.00,
          "partShare": "-25.00"
        },
        {
          "date": "2024-06-11",
          "group": "College",
          "expense": "Dinner",
          "totalAmount": 500.00,
          "partShare": "+400.00"
        }
      ]
      ```

### 3. **Expense Management**

#### 3.1 Create a New Expense

- Endpoint: `/expense`
- Method: POST
- Description: Creates a new expense within a group.
- Request Body:
  ```json
  {
    "groupId": "0168c3a5-c95f-4a1f-b9e8-86944762387e",
    "expenseName": "Dinner",
    "totalAmount": 120.00,
    "splitType": "EQUAL",
    "paidBy": "c2df8e22-01ce-44df-b5ed-7feb749e579c",
    "splitBreakup": [
      {
        "userUuid": "c2df8e22-01ce-44df-b5ed-7feb749e579c"
      },
      {
        "userUuid": "3b7d7f29-8d0b-4b9b-a254-3414d6783cb8"
      }
    ]
  }
  ```
- Response:
    - Status: 200 OK
    - Body:
      ```json
      {
        "uuid": "5e89c2e9-92f8-4d17-9d59-01d1245d7c72"
      }
      ```

#### 3.2 Get an Expense by UUID

- Endpoint: `/expense/{uuid}`
- Method: GET
- Description: Retrieves expense details by its UUID.
- Path Parameters:
    - uuid: The UUID of the expense.
- Response:
    - Status: 200 OK
    - Body:
      ```json
      {
        "uuid": "5e89c2e9-92f8-4d17-9d59-01d1245d7c72",
        "expenseName": "Dinner"
      }
      ```

## Class Diagram
```mermaid
classDiagram
  class UserController {
    -UserService userService
    +createUser(user: User): ResponseEntity<UUID>
    +getUserByUuid(uuid: UUID): Optional<User>
  }

  class GroupController {
    -GroupService groupService
    +createGroup(groupRequest: GroupRequest): UUID
    +getGroup(uuid: UUID): Optional<Group>
    +getTransactions(groupId: UUID, userUuid: UUID, startDate: LocalDate, endDate: LocalDate): ResponseEntity<List<TransactionResponse>>
  }

  class ExpenseController {
    -ExpenseService expenseService
    +createExpense(expenseRequest: ExpenseRequest): UUID
    +getExpense(uuid: UUID): Expense
  }

  class UserService {
    -UserRepository userRepository
    +createUser(user: User): User
    +getUserByUuid(uuid: UUID): Optional<User>
  }

  class GroupService {
    -GroupRepository groupRepository
    -UserRepository userRepository
    -TransactionRepository transactionRepository
    +createGroup(name: String, userMobiles: List<String>): Group
    +getGroupByUuid(uuid: UUID): Optional<Group>
    +getTransactionsForUserAndGroup(userUuid: UUID, groupId: UUID, startDate: LocalDate, endDate: LocalDate): List<TransactionResponse>
    +settleTransactionsBetweenUsers(groupUuid: UUID, user1Uuid: UUID, user2Uuid: UUID): void
  }

  class ExpenseService {
    -ExpenseRepository expenseRepository
    -TransactionRepository transactionRepository
    -UserRepository userRepository
    +createExpense(expenseRequest: ExpenseRequest): Expense
    +getExpenseByUuid(uuid: UUID): Expense
  }

  class User {
    +UUID uuid
    +String name
    +String email
    +String phone
  }

  class Group {
    +UUID uuid
    +String name
    +List<User> users
  }

  class Expense {
    +UUID uuid
    +String expenseName
    +double amount
    +Group group
    +User paidBy
  }

  class Transaction {
    +UUID uuid
    +Expense expense
    +User user
    +TransactionType transactionType
    +double amount
    +boolean isSettled
  }

  class UserRepository {
    +Optional<User> findByUuid(UUID uuid)
  }

  class GroupRepository {
    +Optional<Group> findByUuid(UUID uuid)
  }

  class ExpenseRepository {
    +Optional<Expense> findByUuid(UUID uuid)
  }

  class TransactionRepository {
    +List<Transaction> findByUserAndGroupAndDateRange(UUID userUuid, UUID groupId, LocalDateTime startDate, LocalDateTime endDate)
    +List<Transaction> findByGroupAndPaidByAndUserAndUnSettled(UUID groupId, Long paidById, Long userId)
  }

%% Relationships between classes
  UserController --> UserService
  GroupController --> GroupService
  ExpenseController --> ExpenseService

  UserService --> UserRepository
  GroupService --> GroupRepository
  GroupService --> UserRepository
  GroupService --> TransactionRepository
  ExpenseService --> ExpenseRepository
  ExpenseService --> TransactionRepository
  ExpenseService --> UserRepository

  Group --> User
  Expense --> Group
  Expense --> User
  Transaction --> Expense
  Transaction --> User
```