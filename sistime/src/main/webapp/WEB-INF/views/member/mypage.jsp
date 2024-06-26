<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>마이 페이지</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
<style>
  .accordion-item {
    margin-bottom: 10px; /* 카드 간의 여백을 줄입니다 */
  }

  .accordion-button {
    padding: 10px 15px; /* 버튼의 패딩을 줄입니다 */
    text-align: center; /* 버튼 텍스트 가운데 정렬 */
  }

  .accordion-body {
    padding: 10px 15px; /* 콘텐츠의 패딩을 줄입니다 */
    
  }

  .card {
    
  }

  

  .form-control {
    height: calc(1.5em + .75rem + 2px); /* 입력 필드의 높이를 줄입니다 */
    padding: .25rem .5rem; /* 입력 필드의 패딩을 줄입니다 */
    font-size: .875rem; /* 입력 필드의 폰트 크기를 줄입니다 */
  }

  .btn {
    padding: .25rem .5rem; /* 버튼의 패딩을 줄입니다 */
    font-size: .875rem; /* 버튼의 폰트 크기를 줄입니다 */
  }
</style>
    <jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />

</head>
<header>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</header>
<body>
<div class="row justify-content-center">
    <div class="col-md-5">
		<c:if test="${not empty message}">
            <div class="alert alert-info" role="alert">
                ${message}
            </div>
        </c:if>
        <!-- 내 정보 -->
        <div class="accordion" id="accordionExample">
            <div class="accordion-item">
                <h2 class="accordion-header" id="headingOne">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="false" aria-controls="collapseOne">
                       현재 내 정보
                    </button>
                </h2>
                <div id="collapseOne" class="accordion-collapse collapse" aria-labelledby="headingOne">
                    <div class="accordion-body">
                        <p class="card-text">이메일 : ${dto.email}</p>
                        <p class="card-text">이름 : ${dto.userName}</p>
                        <p class="card-text">닉네임 : ${dto.nickname}</p>
                        <p class="card-text">생년월일 : ${dto.birth}</p>
                        <p class="card-text">전화번호 : ${dto.tel}</p>
                        <p class="card-text">주소 : ${dto.zip} ${dto.addr1} ${dto.addr2}</p>
                        <p class="card-text">가입 날짜 : ${dto.register_date}</p>
                    </div>
                </div>
            </div>

            <!-- 이름 변경 -->
            <div class="accordion-item">
                <h2 class="accordion-header" id="headingTwo">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                        이름 변경 
                    </button>
                </h2>
                <div id="collapseTwo" class="accordion-collapse collapse" aria-labelledby="headingTwo" >
                    <div class="accordion-body">
                        <p class="card-text">현재 이름: ${dto.userName}</p>
                        <form action="${pageContext.request.contextPath}/member/updateName" method="post">
                            <div class="mb-3">
                                <label for="newName" class="form-label">새로운 닉네임</label>
                                <input type="text" class="form-control" id="newName" name="newName" required>
                            </div>
                            <div class="d-flex flex-row-reverse">
                                <button type="submit" class="btn btn-primary p-2">변경 확인</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <!-- 닉네임 변경 -->
            <div class="accordion-item">
                <h2 class="accordion-header" id="headingThree">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
                        닉네임 변경 
                    </button>
                </h2>
                <div id="collapseThree" class="accordion-collapse collapse" aria-labelledby="headingThree">
                    <div class="accordion-body">
                        <p class="card-text">현재 닉네임: ${dto.nickname}</p>
                        <form action="${pageContext.request.contextPath}/member/updateNickname" method="post">
                            <div class="mb-3">
                                <label for="newNickname" class="form-label">새로운 닉네임</label>
                                <input type="text" class="form-control" id="newNickname" name="newNickname" required>
                            </div>
                            <div class="d-flex flex-row-reverse">
                                <button type="submit" class="btn btn-primary p-2">변경 확인</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <!-- 비밀번호 변경 -->
            <div class="accordion-item">
                <h2 class="accordion-header" id="headingFour">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseFour" aria-expanded="false" aria-controls="collapseFour">
                        비밀번호 변경 
                    </button>
                </h2>
                <div id="collapseFour" class="accordion-collapse collapse" aria-labelledby="headingFour" >
                    <div class="accordion-body">
                        <form action="${pageContext.request.contextPath}/member/updatePassword" method="post">
                            <input type="password" class="form-control mb-2" name="currentPassword" placeholder="현재 비밀번호" required>
                            <input type="password" class="form-control mb-2" name="newPassword" placeholder="새로운 비밀번호" required>
                            <input type="password" class="form-control mb-2" name="confirmPassword" placeholder="새로운 비밀번호 확인" required>
                            <div class="d-flex flex-row-reverse">
                                <button type="submit" class="btn btn-primary p-2">변경 확인</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <!-- 모든 정보 수정 -->
            <div class="accordion-item">
                <h2 class="accordion-header" id="headingFive">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseFive" aria-expanded="false" aria-controls="collapseFive">
                        내 정보 수정 
                    </button>
                </h2>
                <div id="collapseFive" class="accordion-collapse collapse" aria-labelledby="headingFive" >
                    <div class="accordion-body">
                        <form action="${pageContext.request.contextPath}/member/updateMember" method="post">
                            <div class="row mb-3">
                                <label class="col-sm-2 col-form-label" for="birth">생년월일</label>
                                <div class="col-sm-10">
                                    <input type="date" name="birth" id="birth" class="form-control" value="${dto.birth}" placeholder="생년월일">
                                    <small class="form-control-plaintext">생년월일은 yyyy-MM-dd 형식으로 입력합니다.</small>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <label class="col-sm-2 col-form-label" for="tel">전화번호</label>
                                <div class="col-sm-10 row">
                                    <div class="col-sm-10 pe-2">
                                        <input type="text" name="tel" id="tel" class="form-control" value="${dto.tel}">
                                    </div>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <label class="col-sm-2 col-form-label" for="zip">우편번호</label>
                                <div class="col-sm-5">
                                    <div class="input-group">
                                        <input type="text" name="zip" id="zip" class="form-control" placeholder="우편번호" value="${dto.zip}" readonly>
                                        <button class="btn btn-primary" type="button" style="margin-left: 3px;" onclick="daumPostcode();">우편번호 검색</button>
                                    </div>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <label class="col-sm-2 col-form-label" for="addr1">주소</label>
                                <div class="col-sm-10">
                                    <div>
                                        <input type="text" name="addr1" id="addr1" class="form-control" placeholder="기본 주소" value="${dto.addr1}" readonly>
                                    </div>
                                    <div style="margin-top: 5px;">
                                        <input type="text" name="addr2" id="addr2" class="form-control" placeholder="상세 주소" value="${dto.addr2}">
                                    </div>
                                </div>
                            </div>
                            <div class="d-flex flex-row-reverse">
                                <button type="submit" class="btn btn-primary p-2">정보 업데이트</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- 내가 쓴 글 -->
        <div class="card">
            <div class="card-body">
                <a href="${pageContext.request.contextPath}/member/myPageList">내가 쓴 글 : ${pagelistcount}개</a>
            </div>
        </div>

        <!-- 내가 쓴 댓글 -->
        <div class="card">
            <div class="card-body">
                <a href="${pageContext.request.contextPath}/member/myReplyList">내가 쓴 댓글 : ${replylistcount}개</a>
            </div>
        </div>

        <!-- 회원 탈퇴 -->
        
         <form id="deleteForm" action="${pageContext.request.contextPath}/member/deletemember" method="post">
         	<a href="${pageContext.request.contextPath}" class="btn btn-primary">홈 화면</a>
         	<button type="button" onclick="confirmDelete()" class="btn btn-danger" style="float: right;">회원탈퇴</button>
         </form>
            
    </div>
</div>

    <script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
		<script>
	    function daumPostcode() {
	        new daum.Postcode({
	            oncomplete: function(data) {
	                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
	
	                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
	                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
	                var fullAddr = ''; // 최종 주소 변수
	                var extraAddr = ''; // 조합형 주소 변수
	
	                // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
	                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
	                    fullAddr = data.roadAddress;
	
	                } else { // 사용자가 지번 주소를 선택했을 경우(J)
	                    fullAddr = data.jibunAddress;
	                }
	
	                // 사용자가 선택한 주소가 도로명 타입일때 조합한다.
	                if(data.userSelectedType === 'R'){
	                    //법정동명이 있을 경우 추가한다.
	                    if(data.bname !== ''){
	                        extraAddr += data.bname;
	                    }
	                    // 건물명이 있을 경우 추가한다.
	                    if(data.buildingName !== ''){
	                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
	                    }
	                    // 조합형주소의 유무에 따라 양쪽에 괄호를 추가하여 최종 주소를 만든다.
	                    fullAddr += (extraAddr !== '' ? ' ('+ extraAddr +')' : '');
	                }
	
	                // 우편번호와 주소 정보를 해당 필드에 넣는다.
	                document.getElementById('zip').value = data.zonecode; //5자리 새우편번호 사용
	                document.getElementById('addr1').value = fullAddr;
	
	                // 커서를 상세주소 필드로 이동한다.
	                document.getElementById('addr2').focus();
	            }
	        }).open();
	    }
	    
	    function confirmDelete() {
	        if (confirm("정말 회원 탈퇴를 진행하시겠습니까?")) {
	            document.getElementById("deleteForm").submit();
	        }
	    }

	</script>
    
<footer>
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</footer>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>
