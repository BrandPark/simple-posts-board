# Simple Posts Board

Spring Data JPA와 Spring Boot를 사용하여 간단한 게시판을 만들었습니다.

Spring MVC 구조로 페이지를 렌더링 해서 보내줍니다.

기능들은 모두 API로 구현하여 클라이언트(브라우저)의 요청을 받아 수행합니다.

## 기능
- 게시글 기능
  1. 게시글 올리기
  2. 게시글 조회하기
  3. 게시글을 조회하면 조회수 증가시키기
  4. 모든 게시글 목록 조회하기
- 차단 기능
  5. 사용자 차단하기 (차단된 사용자의 글과 댓글은 볼 수 없으며 상대방에게도 내 글과 댓글이 보이지 않는다.)
  6. 차단 목록 조회하기
  7. 차단 해제하기
- 댓글 기능
  8. 댓글 달기
  9. 게시글의 모든 댓글 조회하기

## 요구 사항
1. 사용자는 커뮤니티에 글을 올릴 때 (제목, 내용)은 필수로 올린다.
2. 사용자는 댓글을 여러개 달 수 있다.
3. 사용자는 다른 사용자를 차단할 수 있다.
4. 내가 차단한 사용자의 글은 볼 수 없다. 
5. 나를 차단한 사용자의 글도 볼 수 없다.
6. 내가 차단한 사용자의 댓글을 볼 수 없다. 
7. 나를 차단한 사용자의 댓글도 볼 수 없다. 
8. 사용자가 글의 전체보기를 요청할 때 날짜순과 조회수 순으로 정렬할 수 있어야 한다.
9. 글을 보면 조회수가 증가해야한다.

## ERD

![image](https://user-images.githubusercontent.com/53790137/154380004-4727f431-6f8c-4e58-a193-75bc4fee8445.png)

- 인덱스는 읽기 작업이 훨씬 많을 것으로 예상되는 `Blocks` 테이블만 생성했습니다. 
- `Blocks` 테이블 컬럼 중 `BlockState`는 `BLOCKED`와 `NOT_BLOCKED`가 있습니다.

## 화면 캡쳐

### 1. 메인화면

> 로그인 전 화면입니다.

<img src="https://user-images.githubusercontent.com/53790137/154879886-aebea550-a099-4eed-ace0-63130e284313.png" width=800 height=500>

> 로그인 후 화면입니다.

<img src="https://user-images.githubusercontent.com/53790137/154879938-fdd80276-ba7c-416b-bbfe-e4658bafaf59.png" width=800 height=500>

### 2. 글 쓰기 페이지

<img src="https://user-images.githubusercontent.com/53790137/154880007-93ca8b1a-eed9-4742-9ac8-c31edbc19035.png" width=700 height=500>

### 3. 전체 글 목록 조회

<img src="https://user-images.githubusercontent.com/53790137/154880650-6bda4a52-7a27-4c4e-b817-89dbb614f07f.png" width=800 height=500>

### 4. 등록 된 글 세부 내용 조회

> 세부 조회를 하면 조회수가 증가합니다.

<img src="https://user-images.githubusercontent.com/53790137/154880760-2701f024-5dd9-4a97-900c-3e19457cfd90.png" width=700 height=500>

### 5. 댓글 작성하기

<img src="https://user-images.githubusercontent.com/53790137/154880940-383d849e-aea9-41ac-ba86-bad596202116.png" width=700 height=500>

### 6. 사용자 차단하기

> 자신의 계정이 아닐 경우에만 차단할 수 있습니다.

<img src="https://user-images.githubusercontent.com/53790137/154881037-04bfc1a0-1182-42af-91d7-c3c9e15245e3.png" width=800 height=500>

> 차단된 사람의 게시글과 댓글은 볼 수 없습니다.

<img src="https://user-images.githubusercontent.com/53790137/154881092-9bafe2c3-bde5-4622-88a2-c1c2e2bdd0ff.png" width=800 height=500>

### 7. 차단 목록 조회하기

> 차단을 해제하면 게시글과 댓글을 다시 볼 수 있습니다.

<img src="https://user-images.githubusercontent.com/53790137/154882600-47e3061f-0192-4f5d-b088-0a504b70f70f.png" width=800 height=500>

---

# API Docs

### Auth(인증)

**CreateAuthenticationToken**
---

  토큰을 발급합니다.

* **URL**

  `/authenticate`

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

  `/api/v1/posts`

* **Method:**

  `GET`
  
*  **URL Params**

   **Required:**
 
   `orderBase[=String]` ("DATE_DESC" or "VIEW_DESC")

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

**GetPosts**
---
  한 사용자의 게시글을 가져옵니다. 가져온 게시글의 viewCount(조회수)는 1 증가합니다.

* **URL**

  `/api/v1/posts/{:postsId}`

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

**RegisterPosts**
---
  게시글을 등록합니다.  

* **URL**

  `/api/v1/posts`

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

  `/api/v1/posts/{:postsId}/comments`

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

**RegisterComments**
---
  게시글에 댓글을 등록합니다.

* **URL**

  `/api/v1/posts/{:postsId}/comments`

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

  `/api/v1/accounts/{:accountsId}/blocks`

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

  `/api/v1/accounts/{:toBlockAccountsId}/block`

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

  `/api/v1/accounts/{:toUnblockAccountsId}/unblock`

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

