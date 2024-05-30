<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<style>
a {
	padding: 7px;
}

.search {
	margin: 0 auto;
}

.searchicon {
	font-size: 22px;
	line-height: 22px;
}
.header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin : 20px 20px 0 20px;
            padding: 20px;
            background-color: #E0E0E0;
        }
        .header input {
            padding: 5px;
            width: 200px;
        }
</style>

<div class="header">
	<jsp:include page="/WEB-INF/views/layout/sidebar.jsp" />
	<button class="btn" type="button" data-bs-toggle="offcanvas"
		data-bs-target="#offcanvasScrolling"
		aria-controls="offcanvasScrolling">
		<h2>
			<i class="bi bi-list"></i>
		</h2>
	</button>

	<h2>SISTIME</h2>
	<div class="search">

		<input type="text" placeholder="Search for anything..."
			style="width: 200px;"> <i class="bi bi-search searchicon"></i>
	</div>
	<div>
		<h2>
			<c:if test="${empty sessionScope.member}">
				<a href="${pageContext.request.contextPath}/member/login" title="로그인"><i class="bi bi-unlock"></i></a>
				<a><i class="bi bi-person-circle"></i></a>
				<a><i class="bi bi-envelope"></i></a>
			</c:if>
			
			<c:if test="${not empty sessionScope.member}">
				<a href="${pageContext.request.contextPath}/member/logout"title="로그아웃"><i class="bi bi-lock"></i></a>
				<a><i class="bi bi-person-circle"></i></a>
				<a><i class="bi bi-envelope"></i></a>
			</c:if>

		</h2>
	</div>
</div>