<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<style>
.downicon {
	font-size: 14px;
}

.sidebar {
	width: 100%;
	height: 100%;
	background: #f5f5f5;
	box-sizing: border-box;
	display: flex;
	flex-direction: column;
}

.sidebar a {
	display: flex;
	align-items: center;
	padding: 10px 0;
	text-decoration: none;
	color: #787486;
	font-weight: 500;
}

.sidebar img {
	margin-right: 10px;
}
</style>
<div class="offcanvas offcanvas-start" data-bs-scroll="true"
	data-bs-backdrop="false" tabindex="-1" id="offcanvasScrolling"
	aria-labelledby="offcanvasScrollingLabel"
	style="background-color: #f5f5f5">

	<div class="offcanvas-header">
		<h1>SISTIME</h1>
		<button type="button" class="btn-close" data-bs-dismiss="offcanvas"
			aria-label="Close"></button>

	</div>

	<div class="offcanvas-body">
		<div class="sidebar">

			<div class="dropdown">
				<a class="h5" data-bs-toggle="dropdown" aria-expanded="false">
					화개장터&nbsp; <i class="bi bi-caret-down downicon"></i>
				</a>
				<ul class="dropdown-menu">
					<li><a class="dropdown-item"
						href="${pageContext.request.contextPath}/board/list">&nbsp;&nbsp;
							자유 </a></li>
					<li><a class="dropdown-item" href="${pageContext.request.contextPath}/anonymous/list">&nbsp;&nbsp; 비밀 </a></li>
					<li><a class="dropdown-item"
						href="${pageContext.request.contextPath}/notice/list">&nbsp;&nbsp;
							공지사항 </a></li>
					<li><a class="dropdown-item"
						href="${pageContext.request.contextPath}/event/list">&nbsp;&nbsp;
							이벤트 </a></li>
					<li><a class="dropdown-item" href="${pageContext.request.contextPath}/bestboard/list">&nbsp;&nbsp; 베스트 </a></li>
				</ul>
			</div>
			<a class="h5"
				href="${pageContext.request.contextPath}/timetable/list"> 시간표 </a> <a
				class="h5" href="${pageContext.request.contextPath}/test/template">
				학점계산기 </a>

			<div class="dropdown">
				<a class="h5" data-bs-toggle="dropdown" aria-expanded="false">
					고객센터&nbsp; <i class="bi bi-caret-down downicon"></i>
				</a>
				<ul class="dropdown-menu">
					<li><a class="dropdown-item"
						href="${pageContext.request.contextPath}/qna/list">&nbsp;&nbsp;
							1대1 문의 </a></li>
					<li><a class="dropdown-item"
						href="${pageContext.request.contextPath}/requestboard/list">&nbsp;&nbsp;
							화개장터 요청 </a></li>
				</ul>
			</div>
			
			<div class="dropdown">
				<a class="h5" data-bs-toggle="dropdown" aria-expanded="false" href="${pageContext.request.contextPath}/layout/sidebar">
					기타 장터&nbsp; <i class="bi bi-caret-down downicon"></i>
				</a>
				<ul class="dropdown-menu">
					<c:forEach var="dto" items="${listcreate}">
						<li><a class="dropdown-item"
							href="${pageContext.request.contextPath}/admin/list?board_name=${dto.BOARD_NAME}">&nbsp;&nbsp;
								${dto.BOARD_NAME} </a></li>
					</c:forEach>
				</ul>
			</div>

		</div>
	</div>
</div>
