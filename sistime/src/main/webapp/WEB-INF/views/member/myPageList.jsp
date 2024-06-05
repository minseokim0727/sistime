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

	<!-- 내가 쓴 글 -->
		 
	<div class="container">
		<div class="body-container">	
			<div class="body-title">
				<h3><i class="bi bi-app"></i> 내가 쓴 글 </h3>
			</div>
			
			<div class="body-main">
		        <div class="row board-list-header">
		            
		            <div class="col-auto">&nbsp;</div>
		        </div>				
				
				<table class="table table-hover board-list">
					<thead class="table-light">
						<tr>
							<th class="num">번호</th>
							<th class="subject">내용</th>
							<th class="name">작성일</th>

						</tr>
					</thead>
					
					<tbody>
						<c:forEach var="dto" items="${dto2}" varStatus="status">
							<tr>
								<td>${dataCount - (page-1) * size - status.index}</td>
								<td class="left">
									<a href="${pageContext.request.contextPath}/${dto.board_name}/article?num=${dto.board_num}&size=10&page=1" class="text-reset">${dto.title}</a>
								</td>
								<td>${dto.reg_date}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				
				<div class="page-navigation">
					${dataCount == 0 ? "등록된 게시물이 없습니다." : paging}
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