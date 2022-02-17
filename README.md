# Simple Posts Board

Spring Data JPA와 Spring Boot를 사용하여 간단한 게시판 기능을 구현하였습니다.

## ERD

![image](https://user-images.githubusercontent.com/53790137/154380004-4727f431-6f8c-4e58-a193-75bc4fee8445.png)

## API 문서

### Authenticate(인증 토큰)

**CreateAuthenticationToken**
---
  토큰을 발급합니다.

* **URL**

  /authenticate

* **Method:**

  `POST`
  
* **Payload**

```
    {
        "username": "admin",
        "password": "1q2w3e4r"
    }
```

* **Success Response:**

  * **Code:** 200 
  * **Content:** </br>
  ```
    {
        "jwtToken": "token"
    }
  ```
 
* **Error Response:**

  * **Code:** 401 UNAUTHORIZED <br />
  * **Content:** </br>
  ```
    {
        "statusCode": 401,
        "statusName": "UNAUTHORIZED",
        "message": "인증에 실패했습니다."
    }
  ```

---

### Posts(게시글)

**GetAllPosts**
---
  모든 게시글 정보를 가져옵니다. 인증 토큰과 함께 요청하면 사용자의 차단 리스트에 포함된 사용자의 게시글을 제외하고 가져옵니다. 

* **URL**

  /api/v1/posts

* **Method:**

  `GET`
  
*  **URL Params**

   **Required:**
 
   `orderBase[=String]` (DATE_DESC, VIEW_DESC)

* **Success Response:**

  * **Code:** 200 
  * **Content:** </br>
  ```
    {
        "itemCount": 0,
        "itemList": [
            {
            "content": "string",
            "createdDate": "yyyy-MM-dd HH:mm:ss",
            "modifiedDate": "yyyy-MM-dd HH:mm:ss",
            "postsId": 0,
            "title": "string",
            "viewCount": 0,
            "writerId": 0,
            "writerNickname": "string"
            }
        ]
    }
  ```
    
 
* **Error Response:**

  * **Code:** 401 UNAUTHORIZED <br />
  * **Content:** </br>
  ```
    {
        "statusCode": 401,
        "statusName": "UNAUTHORIZED",
        "message": "인증에 실패했습니다."
    }
  ```

**GetPosts**
---
  한 사용자의 게시글을 가져옵니다.

* **URL**

  /api/v1/posts/:postsId

* **Method:**

  `GET`
  
* **Success Response:**

  * **Code:** 200 
  * **Content:** </br>
  ```
    {
        "content": "string",
        "createdDate": "yyyy-MM-dd HH:mm:ss",
        "modifiedDate": "yyyy-MM-dd HH:mm:ss",
        "postsId": 0,
        "title": "string",
        "viewCount": 0,
        "writerId": 0,
        "writerNickname": "string"
    }
  ```
    
 
* **Error Response:**

  * **Code:** 401 UNAUTHORIZED <br />
  * **Content:** </br>
  ```
    {
        "statusCode": 401,
        "statusName": "UNAUTHORIZED",
        "message": "인증에 실패했습니다."
    }
  ```

**RegisterPosts**
---
  게시글을 등록합니다.  

* **URL**

  /api/v1/posts

* **Method:**

  `POST`

* **Auth**

  인증된 사용자를 작성자로하여 게시글을 등록하기 때문에 인증이 필요합니다. 헤더에 Token을 넣어서 인증 가능합니다.
  
*  **Payload**

   ```
    {
        "title": "string"
        "content": "string"
    }
   ```

* **Success Response:**

  * **Code:** 200 
  * **Content:** </br>
  ```
    1 // 생성된 Posts의 Id
  ```
    
 
* **Error Response:**

  * **Code:** 401 UNAUTHORIZED <br />
  * **Content:** </br>
  ```
    {
        "statusCode": 401,
        "statusName": "UNAUTHORIZED",
        "message": "인증에 실패했습니다."
    }
  ```
---

### Comments(댓글)

**GetAllCommentsInPosts**
---
  게시글에 달린 댓글을 모두 가져옵니다. 인증된 요청일 경우 사용자의 차단 리스트에 등록된 사용자의 댓글을 제외하고 가져옵니다.

* **URL**

  /api/v1/posts/:postsId/comments

* **Method:**

  `GET`
  
* **Success Response:**

  * **Code:** 200 
  * **Content:** </br>
  ```
    {
        "itemCount": 0,
        "itemList": [
            {
            "commentsId": 0,
            "content": "string",
            "createdDate": "yyyy-MM-dd HH:mm:ss",
            "modifiedDate": "yyyy-MM-dd HH:mm:ss",
            "writerId": 0,
            "writerNickname": "string"
            }
        ]
    }
  ```
 
* **Error Response:**

  * **Code:** 401 UNAUTHORIZED <br />
  * **Content:** </br>
  ```
    {
        "statusCode": 401,
        "statusName": "UNAUTHORIZED",
        "message": "인증에 실패했습니다."
    }
  ```

**RegisterComments**
---
  게시글에 댓글을 등록합니다.

* **URL**

  /api/v1/posts/:postsId/comments

* **Method:**

  `POST`
  
* **Auth**

    인증된 사용자를 작성자로하여 등록 되기 때문에 인증이 필요합니다. 헤더에 Token을 넣어서 인증 가능합니다.

* **Payload**

```
    {
        "content": "string"
    }
```

* **Success Response:**

  * **Code:** 200 
  * **Content:** </br>
  ```
    1   // 등록된 댓글의 id
  ```
    
 
* **Error Response:**

  * **Code:** 401 UNAUTHORIZED <br />
  * **Content:** </br>
  ```
    {
        "statusCode": 401,
        "statusName": "UNAUTHORIZED",
        "message": "인증에 실패했습니다."
    }
  ```

---

### Blocks(차단)

**GetAllBlockedAccountsList**
---
  차단된 사용자들 목록을 가져옵니다.

* **URL**

  /api/v1/accounts/:accountsId/blocks

* **Method:**

  `GET`

* **Auth**

    인증된 사용자의 차단 리스트를 가져오기 때문에 인증이 필요합니다. 헤더에 Token을 넣어서 인증 가능합니다.
  
* **Success Response:**

  * **Code:** 200 
  * **Content:** </br>
  ```
    {
        "fromAccountsId": 0,
        "fromAccountsNickname": "string",
        "itemCount": 0,
        "itemList": [
            {
            "blockedAccountsId": 0,
            "blockedAccountsNickname": "string",
            "blocksId": 0,
            "createdDate": "yyyy-MM-dd HH:mm:ss",
            "modifiedDate": "yyyy-MM-dd HH:mm:ss"
            }
        ]
    }
  ```
 
* **Error Response:**

  * **Code:** 401 UNAUTHORIZED <br />
  * **Content:** </br>
  ```
    {
        "statusCode": 401,
        "statusName": "UNAUTHORIZED",
        "message": "인증에 실패했습니다."
    }
  ```

**CreateBlockRelation**
---
  차단관계를 등록합니다.

* **URL**

  /api/v1/accounts/:toBlockAccountsId/block

* **Method:**

  `POST`

* **Auth**

    인증된 사용자를 주체로 차단을 하기 때문에 인증이 필요합니다. 헤더에 Token을 넣어서 인증 가능합니다.
  
* **Success Response:**

  * **Code:** 200 
  * **Content:** </br>
  ```
    1   // 생성된 차단리스트의 id
  ```
 
* **Error Response:**

  * **Code:** 401 UNAUTHORIZED <br />
  * **Content:** </br>
  ```
    {
        "statusCode": 401,
        "statusName": "UNAUTHORIZED",
        "message": "인증에 실패했습니다."
    }
  ```

**CreateUnblockRelation**
---
  차단관계를 해제합니다. 차단관계가 있는 경우에만 동작합니다. 내부적으로 삭제가 아니라 `NOT_BLOCKED` 상태로 바꿉니다.

* **URL**

  /api/v1/accounts/:toUnblockAccountsId/unblock

* **Method:**

  `POST`

* **Auth**

    인증된 사용자를 주체로 차단 해제를 하기 때문에 인증이 필요합니다. 헤더에 Token을 넣어서 인증 가능합니다.
  
* **Success Response:**

  * **Code:** 200 
  * **Content:** </br>
  ```
    1   // NOT_BLOCKED 상태가 된 차단 리스트의 id
  ```
 
* **Error Response:**

  * **Code:** 401 UNAUTHORIZED <br />
  * **Content:** </br>
  ```
    {
        "statusCode": 401,
        "statusName": "UNAUTHORIZED",
        "message": "인증에 실패했습니다."
    }
  ```

