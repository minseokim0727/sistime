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
</style>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/board2.css"
	type="text/css">

<c:if test="${sessionScope.member.email=='admin'}">
	<script type="text/javascript">
		function deleteNotice() {
			if (!confirm('게시글을 삭제하시겠습니까? ')) {
				return;
			}
			let q = '${query}&num=${dto.notice_num}';
			let url = '${pageContext.request.contextPath}/notice/delete';

			location.href = url + '?' + q;

		}

		function complain() {

			location.href = '${pageContext.request.contextPath}/notice/complain?num=${dto.notice_num}'
		}
	</script>
</c:if>

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
						<i class="bi bi-clipboard"></i> 공지사항
					</h3>
				</div>

				<div class="body-main">

					<table class="table">
						<thead>
							<tr>
								<td colspan="2" align="center">${dto.title}</td>
							</tr>
						</thead>

						<tbody>
							<tr>
								<td width="50%">이름 : ${dto.email}</td>
								<td align="right">${dto.reg_date}</td>
							</tr>

							<tr>
								<td colspan="2" valign="top" height="200"
									style="border-bottom: none;">${dto.content}</td>
							</tr>

							<tr>
								<td colspan="2"><c:forEach var="vo" items="${listFile}"
										varStatus="status">
										<p class="border text-secondary mb-1 p-2">
											<i class="bi bi-folder2-open"></i> <a
												href="${pageContext.request.contextPath}/notice/download?noticefile_num=${vo.noticefile_num}">${vo.originalFilename}</a>
										</p>
									</c:forEach></td>
							</tr>

							<tr>
								<td colspan="2">이전글 : <c:if test="${not empty prevDto}">
										<a
											href="${pageContext.request.contextPath}/notice/article?${query}&num=${prevDto.notice_num}">${prevDto.title}</a>
									</c:if>
								</td>
							</tr>
							<tr>
								<td colspan="2">다음글 : <c:if test="${not empty nextDto}">
										<a
											href="${pageContext.request.contextPath}/notice/article?${query}&num=${nextDto.notice_num}">${nextDto.title}</a>
									</c:if>
								</td>
							</tr>

						</tbody>
					</table>

					<table class="table table-borderless">
						<tr>
							<td width="50%"><c:if
									test="${sessionScope.member.email == 'admin'}">
									<button type="button" class="btn btn-light"
										onclick="deleteNotice();">삭제</button>
									<button type="button" class="btn btn-light"
										onclick="location.href='${pageContext.request.contextPath}/notice/update?num=${dto.notice_num}&page=${page}&size=${size}';">수정</button>
								</c:if></td>
							<td class="text-end">

								<button type="button" class="btn btn-light"
									onclick="location.href='${pageContext.request.contextPath}/notice/list?${query}';">리스트</button>
								
								<form action ="${pageContext.request.contextPath}/notice/complain">
								<select name = "comp_reason">
								<option value="욕설"> 욕설 </option>
								<option value="불법컨텐츠"> 불법컨텐츠 </option>
								<option value="광고"> 광고 </option>
								</select>
								<button type="submit" class="btn btn-light"
									onclick="">신고</button>
								<input type="hidden" name="board_name" value="notice"> 
								<input type="hidden" name="page" value="${page}">
								<input type="hidden" name="num" value="${dto.notice_num}">
								<input type="hidden" name="email" value="${dto.email}">
								<input type="hidden" name="size" value="${size}">
								</form>
			

							</td>

						</tr>
					</table>

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