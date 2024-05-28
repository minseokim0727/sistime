<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>spring</title>
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>

<style type="text/css">
.body-container {
	max-width: 800px;
}
</style>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/board2.css" type="text/css">

<script type="text/javascript">

</script>
</head>
<body>

<header>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</header>
	
<main>
	<div class="container">
		<div class="body-container">	
			<div class="body-title">
				<h3><i class="bi bi-clipboard"></i> 공지사항 </h3>
			</div>
			
			<div class="body-main">
				<form name="listForm" method="post">
			        <div class="row board-list-header">
			            <div class="col-auto me-auto">
							<p class="form-control-plaintext">
								1개(1/1 페이지)
							</p>
			            </div>
			            <div class="col-auto">

						</div>
			        </div>				
					
					<table class="table table-hover board-list">
						<thead class="table-light">
							<tr>
								<c:if test="${sessionScope.member.userId=='admin'}">
									<th class="chk">
										<input type="checkbox" class="form-check-input" name="chkAll" id="chkAll">        
									</th>
								</c:if>
								<th class="num">번호</th>
								<th class="subject">제목</th>
								<th class="name">작성자</th>
								<th class="date">작성일</th>
								<th class="hit">조회수</th>
							</tr>
						</thead>
						
						<tbody>
						
								<tr>
									<c:if test="${sessionScope.member.userId=='admin'}">
										<td>
											<input type="checkbox" class="form-check-input" name="nums" value="${dto.num}">
										</td>
									</c:if>
									<td><span class="badge bg-primary">공지</span></td>
									<td class="left">
										<span class="d-inline-block text-truncate align-middle" style="max-width: 390px;"><a href="" class="text-reset">제목 입니다.</a></span>
									</td>
									<td>홍길동</td>
									<td>2000-10-10</td>
									<td>1</td>
								</tr>
						
						</tbody>
					</table>
				</form>
				
				<div class="page-navigation">
					1 2 3
				</div>

				<div class="row board-list-footer">
					<div class="col">
						<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/';"><i class="bi bi-arrow-clockwise"></i></button>
					</div>
					<div class="col-6 text-center">
						<form class="row" name="searchForm" action="${pageContext.request.contextPath}/" method="post">
							<div class="col-auto p-1">
								<select name="schType" class="form-select">
									<option value="all" ${schType=="all"?"selected":""}>제목+내용</option>
									<option value="userName" ${schType=="userName"?"selected":""}>작성자</option>
									<option value="reg_date" ${schType=="reg_date"?"selected":""}>등록일</option>
									<option value="subject" ${schType=="subject"?"selected":""}>제목</option>
									<option value="content" ${schType=="content"?"selected":""}>내용</option>
								</select>
							</div>
							<div class="col-auto p-1">
								<input type="text" name="kwd" value="${kwd}" class="form-control">
							</div>
							<div class="col-auto p-1">
								<input type="hidden" name="size" value="${size}">
								<button type="button" class="btn btn-light" onclick="searchList()"> <i class="bi bi-search"></i> </button>
							</div>
						</form>
					</div>
					<div class="col text-end">
						<c:if test="${sessionScope.member.userId=='admin'}">
							<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/';">글올리기</button>
						</c:if>
					</div>
				</div>

			</div>
		</div>
	</div>
</main>

<footer>
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</footer>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>