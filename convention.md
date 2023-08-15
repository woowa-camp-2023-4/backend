# [Guideline for Convention]

# 컨트리뷰션 가이드

# PR 가이드

- PR 형식은 아래와 같습니다

<예시>

---

### Description

설명

### Changes

변경사항

#### Test Checklist

테스트 항목

---
<br>

## branch 전략

기본적으로 master-develop branch가 존재하며 PR merge를 요청하는 branch는 **develop**입니다<br>
dev에서 분기한 branch의 이름은 **feat/이슈넘버 or xxx**, **fix/이슈넘버 or xxx** , **refactor/xxx** 로 지정해주세요
<br>

- PR을 Merge 하기 위해선 반드시 1명 이상의 **Approve**가 필요합니다.
- PR은 **Sqush and merge** 옵션으로 merge합니다
- master <- develop으로 Merge 시에는 **Create a merge commit** 옵션으로 Merge합니다
- PR 제목은 아래와 같이 지어주세요. (issue가 없다면 생략해주세요)

```
  (#issue-number) 내용 요약
```

- master <- develop PR 생성 시 develop에 merge된 pr 목록을 내용에 적어주세요.
- master, develop branch에는 commit할 수 없습니다.
- master는 prod에 배포하는 브랜치이기 떄문에 주의해주세요.
  <br>

## 코드 리뷰 규칙

[코드 리뷰 참고 글](https://tech.kakao.com/2022/03/17/2022-newkrew-onboarding-codereview/)을 읽어주세요

- 왜 개선이 필요한지 이유를 **충분한 설명**해 주세요.
- 답을 알려주기보다는 스스로 고민하고 개선 방법을 선택할 수 있게 해주세요.
- **코드를 클린** 하게 유지하고, 일관되게 구현하도록 안내해 주세요.
- 리뷰 과정이 숙제검사가 아닌 학습과정으로 느낄 수 있게 리뷰해 주세요.
- 리뷰를 위한 리뷰를 하지 마세요. 피드백 할 게 없으면 **칭찬**해 주세요.

리뷰어의 책임이 50%라는 마음으로 리뷰를 부탁드립니다
<br><br>

# commit 가이드

Commit 양식은 [해당 사이트](https://www.conventionalcommits.org/en/v1.0.0/#summary)를 참고해주세요

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

- 위의 양식을 준수하며 제목과 본문은 **한글**로 작성해주세요 <br>
- type은 **영어**로 작성해주세요

예시 )

```
feat: 우아킷 쿠폰 발행 기능 추가

//본문은 한줄 띄워주세요
xxx 쿠폰 발행을 만족하기 위해....
```

- 커밋 로그를 보고 흐름을 이해할 수 있도록 작성 부탁드려요
- 명확한 제목과 자세한 본문 작성을 부탁드려요

<br><br>

# 컨벤션 가이드

네이버 핵데이 컨벤션을 따라주세요

https://naver.github.io/hackday-conventions-java/#line-wrapping-position

클린 코드를 함께 고민보아요!

### 우아킷 세부 컨벤션

파라미터에는 final을 붙혀주세요

**120글자가 넘을 때 파라미터 개행 규칙**

```java
// 최지원 우승!!
public OrderService(
final OrderRepository orderRepository, // Tab 2번
final CartItemRepository cartItemRepository,
final MemberRepository memberRepository,
final ExchangeRateProvider exchangeRateProvider
	){
	this.orderRepository=orderRepository;
	this.cartItemRepository=cartItemRepository;
	this.memberRepository=memberRepository;
	this.exchangeRateProvider=exchangeRateProvider;
	}

```

```java
return OrderResponse.of(
	order.getId(),  // Tab 2번
	order.getOrderPrice(),
	order.getExchangeRate(),
	orderItemResponses
	);

```

**stream 개행 규칙**

stream(). 이후에 줄바꿈을 하며 줄바꿈 시 탭 2번을 한다

```java
//최준영 우승!
@Transactional(readOnly = true)
public OrderResponses readOrders(final Long memberId){
final Member member=getMemberById(memberId);
final List<OrderResponse> response=orderRepository.findAllByMember(member).stream()
	.map(order->readOrder(order.getId()))
	.collect(toList());

	return OrderResponses.from(response);
	}

```

**클래스 선언 괄호 다음에 한 줄 띄우고 필드 명시**

```java

@Service
public class OrderService {
	//한 줄 띄우기
	private OrderRepository orderRepository;
	private OrderValidator orderValidator;
```

**클래스 메서드 순서**

상수 - 엔터 - 필드 - 생성자 - 스태틱 메서드 - 비즈니스 메서드 - 게터 - 이퀄스/해시코드 - 투스트링

참조되는 method는 참조하는 메서드 바로 아래에 둔다

많이 참조되는 메서드는 제일 아래로 내린다

domain,vo는 정적 팩터리 메서드 사용 권장

```java

public static CommentResponse from(CommentResult commentResult){
	return CommentResponse.builder()
	.commentId(commentResult.commentId())
	.content(commentResult.content())
	.nickname(commentResult.nickname())
	.articleId(commentResult.articleId())
	.createdAt(commentResult.createdAt())
	.isWritten(commentResult.isWritten())
	.build();
	}
```

필드가 4개 이상부터 빌더를 추가한다

생성자는 public으로는 설정하지 않는다(new 방지)

static import를 하지않는다 (assertJ 라이브러리 제외)

### 테스트 컨벤션

테스트 픽스쳐 사용을 적극적으로 할 것 (builder 형식으로)
헬퍼 메서드는 given,when,then에 상관없이 래핑한다

테스트 메서드의 DisplayName는 자연스러운 네이밍으로 가져가되 구체성을 드러낸다

메서드 네이밍은 간단하게 가져가되 예외상황일때 좀 더 자세하게 적는다 (시간 많이 쓰지 않기)

ex)"장바구니에 담긴 상품을 주문하면 주문이 등록되고 장바구니가 비워진다"

```java

@Test
@DisplayName("장바구니에 담긴 상품을 주문하면 주문이 등록되고 장바구니가 비워진다")
    void order(){
```
  

