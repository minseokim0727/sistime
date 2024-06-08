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
	max-width: 850px;
}
</style>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/board2.css"
	type="text/css">

<script type="text/javascript">
		function check() {
		    const f = document.eventForm;
		    const startDate = f.eventStartDate.value;
		    
		    if (!startDate.trim()) {
		        alert("이벤트 시작 날짜를 입력하세요.");
		        return false; // 폼 제출을 막음
		    }
		    
		    f.action = "${pageContext.request.contextPath}/event/${mode}";
		    return true;
		}


	<c:if test="${mode=='update'}">
	function deleteFile(num) {
		if (!confirm("파일을 삭제하시겠습니까 ?")) {
			return;
		}
		let url = "${pageContext.request.contextPath}/event/deleteFile?num="
				+ num + "&page=${page}";
		location.href = url;
	}
	</c:if>
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
						<i class="bi bi-book-half"></i> 이벤트
					</h3>
				</div>

				<div class="body-main">
					<form name="eventForm" method="post" enctype="multipart/form-data">
						<table class="table write-form mt-5">
							<tr>
								<td class="bg-light col-sm-2" scope="row">제 목</td>
								<td><input type="text" name="title" class="form-control"
									value="${dto.event_title}"></td>
							</tr>


							<tr>
								<td class="bg-light col-sm-2" scope="row">작성자명</td>
								<td>
									<p class="form-control-plaintext">${sessionScope.member.nickname}</p>
								</td>
							</tr>
							<c:if test="${sessionScope.member.email == 'admin'}">
								<tr>
									<td class="bg-light col-sm-2" scope="row">이벤트 시작 날짜</td>
									<td><input type="date" name="eventStartDate"
										class="form-control"></td>
								</tr>
							</c:if>
							
							<tr>
								<td class="bg-light col-sm-2" scope="row">내 용</td>
								<td><textarea name="content" id="ir1" class="form-control"
										style="width: 95%; height: 270px;">${dto.event_content}</textarea></td>
							</tr>

							<tr>
								<td class="bg-light col-sm-2">첨&nbsp;&nbsp;&nbsp;&nbsp;부</td>
								<td><input type="file" name="selectFile"
									class="form-control"></td>
							</tr>
							<c:if test="${mode=='update'}">
								<tr>
									<td class="bg-light col-sm-2" scope="row">첨부된파일</td>
									<td>
										<p class="form-control-plaintext">
											<c:if test="${not empty dto.saveFilename}">
												<a href="javascript:deleteFile('${dto.eventpage_num}');"><i
													class="bi bi-trash"></i></a>
											${dto.originalFilename}
										</c:if>
											&nbsp;
										</p>
									</td>
								</tr>
							</c:if>

						</table>

						<table class="table table-borderless">
							<tr>
								<td class="text-center">
									<button type="button" class="btn btn-dark"
										onclick="submitContents(this.form);">${mode=='update'?'수정완료':'등록하기'}&nbsp;<i
											class="bi bi-check2"></i>
									</button>
									<button type="reset" class="btn btn-light">다시입력</button>
									<button type="button" class="btn btn-light"
										onclick="location.href='${pageContext.request.contextPath}/event/list';">${mode=='update'?'수정취소':'등록취소'}&nbsp;<i
											class="bi bi-x"></i>
									</button> <c:if test="${mode=='update'}">
										<input type="hidden" name="num" value="${dto.eventpage_num}">
										<input type="hidden" name="page" value="${page}">
										<input type="hidden" name="size" value="${size}">
										<input type="hidden" name="fileSize" value="${dto.filesize}">
										<input type="hidden" name="saveFilename"
											value="${dto.saveFilename}">
										<input type="hidden" name="originalFilename"
											value="${dto.originalFilename}">
									</c:if>
								</td>
							</tr>
						</table>
					</form>
				</div>
			</div>
		</div>
	</main>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/se2/js/service/HuskyEZCreator.js"
		charset="utf-8"></script>

	<script type="text/javascript">
		var oEditors = [];
		nhn.husky.EZCreator
				.createInIFrame({
					oAppRef : oEditors,
					elPlaceHolder : "ir1",
					sSkinURI : "${pageContext.request.contextPath}/resources/se2/SmartEditor2Skin.html",
					fCreator : "createSEditor2"
				});

		function submitContents(elClickedObj) {
			oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD", []);
			console.log(document.eventForm.content.value);
			try {
				if (!check()) {
					return;
				}

				elClickedObj.submit();
			} catch (e) {
			}
		}

		function setDefaultFont() {
			var sDefaultFont = '돋움';
			var nFontSize = 12;
			oEditors.getById["ir1"].setDefaultFont(sDefaultFont, nFontSize);
		}
	</script>
	<footer>
		<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	</footer>

	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
</body>
</html>