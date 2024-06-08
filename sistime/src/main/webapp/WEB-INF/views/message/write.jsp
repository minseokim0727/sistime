<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
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
</style>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/board.css" type="text/css">

<script type="text/javascript">
	function sendOk() {
		const f = document.MessageForm;
		let str = f.content.value.trim();
		if (!str) {
			alert("내용을 입력하세요. ");
			f.content.focus();
			return;
		}

		f.action = "${pageContext.request.contextPath}/message/write";
		f.submit();
	}

	function nicknameCheck() {
		let nickname = $("#nickname").val();
		

		let url = "${pageContext.request.contextPath}/message/nicknameCheck";
		let query = "nickname=" + nickname;

		$.ajax({
			type: "post",
			url: url,
			data: query,
			dataType: "json",
			success: function(data) {
				let passed = data.passed;

				if (passed === "true") {
					let s = "<span style='color:blue; font-weight:700;'>" + nickname + "</span> 보낼 수 있는 닉네임입니다.";
					$(".nickname-box").find(".help-block").html(s);
				} else {
					let s = "<span style='color:red; font-weight:700;'>" + nickname + "</span> 존재하지 않는 닉네임입니다.";
					$(".nickname-box").find(".help-block").html(s);

				}
			},
			error: function(e) {
				console.log(e.responseText);
			}
		});
	}
</script>
</head>
<body>
	<header>
		<jsp:include page="/WEB-INF/views/layout/header.jsp" />
	</header>

	<main>
		<div class="container">
			<div class="body-container">
				<div class="body-title">
					<h3>
						<i class="bi bi-app"></i> 쪽지함
					</h3>
				</div>

				<div class="body-main">
					<form name="MessageForm" method="post">
						<table class="table write-form mt-5">
							<tr class="nickname-box">
								<td class="bg-light col-sm-2" scope="row">닉네임</td>
								<td>
									<div class="row">
										<div class="col-9">
											<input type="text" name="nickname" id="nickname" class="form-control" value="${dto.nickname}" placeholder="닉네임">
										</div>
										<div class="col-3 ps-1">
											<button type="button" class="btn btn-light" onclick="nicknameCheck();" style="background: gray; color: white">닉네임 검사</button>
										</div>
									</div>
									<small class="form-control-plaintext help-block">이거 눌러라</small>
								</td>
							</tr>
							<tr class="content-row">
								<td class="bg-light col-sm-2" scope="row">내 용</td>
								<td><textarea name="content" id="content" class="form-control">${dto.content}</textarea></td>
							</tr>
						</table>

						<table class="table table-borderless">
							<tr>
								<td class="text-center">
									<button type="button" class="btn btn-dark" onclick="sendOk();">
										전송하기&nbsp;<i class="bi bi-check2"></i>
									</button>
									<button type="reset" class="btn btn-light">다시입력</button>
									<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/message/list';">
										전송취소&nbsp;<i class="bi bi-x"></i>
									</button>
								</td>
							</tr>
						</table>
					</form>
				</div>
			</div>
		</div>
	</main>

	<footer>
		<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	</footer>

	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
</body>
</html>
