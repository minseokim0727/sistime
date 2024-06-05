<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />

</head>
<header>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</header>
<body>

	<!-- 내가 쓴 댓글 -->
		<div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-body">
                        <h2 class="card-title">내가 쓴 댓글 ▼</h2>
                        <!-- 내가 쓴 댓글 목록 -->
                        <div class="border px-2">
							<c:forEach var="dto" items="${dto3}" varStatus="status">
								<div class="text-truncate px-2 subject-list">
									<p>${dto.reply_content}</p>
									
								</div>
							</c:forEach>
							
						</div>
						<div class="page-navigation">
							${dataCount == 0 ? "등록된 게시물이 없습니다." : paging}
						</div>
						<a href="${pageContext.request.contextPath}/member/mypage" class="btn btn-primary">이전 화면</a>
                    </div>
                </div>
             </div>
          </div>
<footer>
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</footer>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>