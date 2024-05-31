<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>spring</title>

<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />

<style type="text/css">
.body-container {
	max-width: 800px;
}

input[type="checkbox"].form-check-input {
	background-color: #f56020; /* 배경색 지정 */
}
</style>

<script type="text/javascript">

function memberOk() {
	const f = document.memberForm;
	let str;
	str = f.email.value;
	if( !/^[a-zA-Z0-9+-\_.]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/.test(str) ) { 
		alert("이메일을 다시 입력 하세요1. ");
		f.email.focus();
		return;
	}
	
	let mode = "${mode}";
	if(mode === "member" && f.emailValid.value === "false") {
		str = "이메일 중복 검사가 실행되지 않았습니다.";
		$("#email").closest(".email-box").find(".help-block").html(str);
		f.email.focus();
		return;
	}
	
	str = f.userPwd.value;
	if( !/^(?=.*[a-z])(?=.*[!@#$%^*+=-]|.*[0-9]).{5,10}$/i.test(str) ) { 
		alert("패스워드를 다시 입력 하세요. ");
		f.userPwd.focus();
		return;
	}

	if( str !== f.userPwd2.value ) {
        alert("패스워드가 일치하지 않습니다. ");
        f.userPwd.focus();
        return;
	}
	
    str = f.userName.value;
    if( !/^[가-힣]{2,5}$/.test(str) ) {
        alert("이름을 다시 입력하세요. ");
        f.userName.focus();
        return;
    }

    str = f.birth.value;
    if( !str ) {
        alert("생년월일를 입력하세요. ");
        f.birth.focus();
        return;
    }
    
    str = f.tel1.value;
    if( !str ) {
        alert("전화번호를 입력하세요. ");
        f.tel1.focus();
        return;
    }

    str = f.tel2.value;
    if( !/^\d{3,4}$/.test(str) ) {
        alert("숫자만 가능합니다. ");
        f.tel2.focus();
        return;
    }

    str = f.tel3.value;
    if( !/^\d{4}$/.test(str) ) {
    	alert("숫자만 가능합니다. ");
        f.tel3.focus();
        return;
    }


    str = f.univ_num.value;
    alert(str);
   	f.action = "${pageContext.request.contextPath}/member/${mode}";
    f.submit();
    
    
}


function emailCheck() {
	// 이메일 중복 검사
	let email = $("#email").val();
	if( ! /^[a-zA-Z0-9+-\_.]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/.test(email) ) { 
		let str = "이메일형식을 맞춰 주세요2.";
		$("#email").focus();
		$("#email").closest(".email-box").find(".help-block").html(str);
		return;
	}
	
	let url = "${pageContext.request.contextPath}/member/emailCheck";
	let query = "email=" + email;
	
	$.ajax({
		type:"post",
		url:url,
		data:query,
		dataType:"json",
		success:function(data) {
			let passed = data.passed;

			if(passed === "true") {
				let s = "<span style='color:blue; font-weight:700;'>" + email + "</span> 이메일은 사용가능 합니다.";
				$(".email-box").find(".help-block").html(s);
				$("#emailValid").val("true");
			} else {
				let s = "<span style='color:red; font-weight:700;'>" + email + "</span> 이메일은 사용할 수 없습니다.";
				$(".email-box").find(".help-block").html(s);
				$("#emailValid").val("false");
			}
			
		},
		error:function(e) {
			console.log(e.responseText);
		}
	});

}

window.addEventListener('load', () => {
	const el = document.querySelector('form input[name=birth]');
	el.addEventListener('keydown', e => e.preventDefault());
});
</script>
</head>
<body>

	<header>
		<jsp:include page="/WEB-INF/views/layout/header.jsp" />
	</header>

	<main>
		<div class="container">
			<div class="body-container">
				<div class="body-title" style="border-bottom: 1px solid orange;">
					<h3 style="border-bottom: 3px solid orange;">
						<i class="bi bi-person-square"></i> ${title}
					</h3>
				</div>

				<div class="alert alert-info" role="alert"
					style="background: #f56020;">
					<i class="bi bi-person-check-fill" style="color: white;"></i><i
						style="color: white;">빠르게 가입하고 같이 놀아주시죠</i>
				</div>

				<div class="body-main">

					<form name="memberForm" method="post">
						<div class="row mb-3">
							<label class="col-sm-2 col-form-label" for="email">이메일</label>
							<div class="col-sm-10 email-box">
								<div class="row">
									<div class="col-7 pe-1">
										<input type="text" name="email" id="email"
											class="form-control" value="${dto.email}"
											${mode=="update" ? "readonly ":""} placeholder="이메일">
									</div>
									<div class="col-3 ps-1">
										<c:if test="${mode=='member'}">
											<button type="button" class="btn btn-light"
												onclick="emailCheck();"
												style="background: #f56020; color: white">아이디중복검사</button>
										</c:if>
									</div>
								</div>
								<c:if test="${mode=='member'}">
									<small class="form-control-plaintext help-block">이메일
										양식을 맞춰주십시요, 첫글자는 영문자로 시작해야 합니다.</small>
								</c:if>
							</div>
						</div>

						<div class="row mb-3">
							<label class="col-sm-2 col-form-label" for="userPwd">패스워드</label>
							<div class="col-sm-10">
								<input type="password" name="userPwd" id="userPwd"
									class="form-control" autocomplete="off" placeholder="패스워드">
								<small class="form-control-plaintext">패스워드는 5~10자이며 하나
									이상의 숫자나 특수문자가 포함되어야 합니다.</small>
							</div>
						</div>

						<div class="row mb-3">
							<label class="col-sm-2 col-form-label" for="userPwd2">패스워드
								확인</label>
							<div class="col-sm-10">
								<input type="password" name="userPwd2" id="userPwd2"
									class="form-control" autocomplete="off" placeholder="패스워드 확인">
								<small class="form-control-plaintext">패스워드를 한번 더 입력해주세요.</small>
							</div>
						</div>

						<div class="row mb-3">
							<label class="col-sm-2 col-form-label" for="userName">이름</label>
							<div class="col-sm-3">
								<input type="text" name="userName" id="userName"
									class="form-control" value="${dto.userName}"
									${mode=="update" ? "readonly ":""} placeholder="이름">
							</div>
						</div>

						<div class="row mb-3">
							<label class="col-sm-2 col-form-label" for="nickname">닉네임</label>
							<div class="col-sm-3">
								<input type="text" name="nickname" id="nickname"
									class="form-control" value="${dto.nickname}"
									${mode=="update" ? "readonly ":""} placeholder="닉네임">
							</div>
						</div>

						<div class="row mb-3">
							<label class="col-sm-2 col-form-label" for="selectUniv">대학교</label>
							<div class="col-sm-10 row">
								<div class="col-3 pe-0">
									<select name="univ_num" id="univ_num" class="form-select">
										<option value="">선 택</option>
										<option value=1>쌍용대학교</option>
										<option value=2>안양대학교</option>
										<option value=3>호서대학교</option>
										<option value=4>익산대학교</option>
									</select>

								</div>
							</div>

						</div>

						<div class="row mb-3">
							<label class="col-sm-2 col-form-label" for="birth">생년월일</label>
							<div class="col-sm-10">
								<input type="date" name="birth" id="birth" class="form-control"
									value="${dto.birth}" placeholder="생년월일"> <small
									class="form-control-plaintext">생년월일은 yyyy-MM-dd 형식으로 입력
									합니다.</small>
							</div>
						</div>

						<div class="row mb-3">
							<label class="col-sm-2 col-form-label" for="tel1">전화번호</label>
							<div class="col-sm-10 row">
								<div class="col-sm-3 pe-2">
									<input type="text" name="tel1" id="tel1" class="form-control"
										value="${dto.tel1}" maxlength="3">
								</div>
								<div class="col-sm-1 px-1" style="width: 2%;">
									<p class="form-control-plaintext text-center">-</p>
								</div>
								<div class="col-sm-3 px-1">
									<input type="text" name="tel2" id="tel2" class="form-control"
										value="${dto.tel2}" maxlength="4">
								</div>
								<div class="col-sm-1 px-1" style="width: 2%;">
									<p class="form-control-plaintext text-center">-</p>
								</div>
								<div class="col-sm-3 ps-1">
									<input type="text" name="tel3" id="tel3" class="form-control"
										value="${dto.tel3}" maxlength="4">
								</div>
							</div>
						</div>

						<div class="row mb-3">
							<label class="col-sm-2 col-form-label" for="zip">우편번호</label>
							<div class="col-sm-5">
								<div class="input-group">
									<input type="text" name="zip" id="zip" class="form-control"
										placeholder="우편번호" value="${dto.zip}" readonly>
									<button class="btn btn-light" type="button"
										style="margin-left: 3px; background: #f56020; color: white;"
										onclick="daumPostcode();">우편번호 검색</button>
								</div>
							</div>
						</div>

						<div class="row mb-3">
							<label class="col-sm-2 col-form-label" for="addr1">주소</label>
							<div class="col-sm-10">
								<div>
									<input type="text" name="addr1" id="addr1" class="form-control"
										placeholder="기본 주소" value="${dto.addr1}" readonly>
								</div>
								<div style="margin-top: 5px;">
									<input type="text" name="addr2" id="addr2" class="form-control"
										placeholder="상세 주소" value="${dto.addr2}">
								</div>
							</div>
						</div>

						<c:if test="${mode == 'member'}">
							<div class="row mb-3">
								<label class="col-sm-2 col-form-label" for="agree">약관 동의</label>
								<div class="col-sm-8" style="padding-top: 5px;">
									<input type="checkbox" id="agree" name="agree"
										class="form-check-input" checked style="margin-left: 0;"
										onchange="form.sendButton.disabled = !checked"> <label
										class="form-check-label"> <a href="#"
										class="text-decoration-none">이용약관</a>에 동의합니다.
									</label>
								</div>
							</div>
						</c:if>

						<div class="row mb-3">
							<div class="text-center">
								<button type="button" name="sendButton" class="btn btn-primary"
									onclick="memberOk();" style="background: #f56020;">
									${mode=="member"?"회원가입":"정보수정"} <i class="bi bi-check2"></i>
								</button>

								<button type="button" class="btn btn-danger"
									style="background: #DA1515;"
									onclick="location.href='${pageContext.request.contextPath}/';">
									${mode=="member"?"가입취소":"수정취소"} <i class="bi bi-x"></i>
								</button>
								<input type="hidden" name="emailValid" id="emailValid"
									value="false">
							</div>
						</div>

						<div class="row">
							<p class="form-control-plaintext text-center">${message}</p>
						</div>
					</form>

				</div>
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
	</script>

	</main>

	<footer>
		<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	</footer>

	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
</body>
</html>