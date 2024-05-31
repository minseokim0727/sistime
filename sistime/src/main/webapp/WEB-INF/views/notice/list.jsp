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
function changeList() {
	const f = document.listForm;
	f.page.value = "1";
	f.action = "${pageContext.request.contextPath}/notice/list";
	f.submit();
}

function searchList() {
	const f = document.searchForm;
	f.submit();
}
</script>

<c:if test="${sessionScope.member.email == 'admin'}">
	<script type="text/javascript">
		$(function(){
			$('#chkAll').click(function(){
				$('input[name=nums]').prop('checked',$(this).is(':checked'));
			});
			$('#btnDeleteList').click(function(){
				let cnt = $('input[name=nums]:checked').length;
				if(cnt === 0){
					alert('삭제할 게시물을 선택하세요. ');
					return;
				}
				
				if(confirm('게시물을 삭제하시겠습니까 ? ')){
					const f = document.listForm;
					f.action = '${pageContext.request.contextPath}/notice/deleteList';
					f.submit();
				}
				
			});
		});
	</script>
</c:if>

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
								<c:if test="${sessionScope.member.email != 'admin'}">
								${dataCount}개(${page} / ${total_page} 페이지)
								</c:if>
								<c:if test="${sessionScope.member.email == 'admin'}">
									<button type="button" class="btn btn-light" id="btnDeleteList" title = "삭제"><i class="bi bi-trash"></i></button>
								</c:if>
							</p>
			            </div>
			            <div class="col-auto">
							<c:if test="${dataCount != 0}">
								<select name="size" class="form-select" onchange="changeList();">
									<option value="5" ${size==5 ? "selected":"" }>5개 출력</option>
									<option value="10" ${size==10 ? "selected":"" }>10개 출력</option>
									<option value="20" ${size==20 ? "selected":"" }>20개 출력</option>
									<option value="30" ${size==30 ? "selected":"" }>30개 출력</option>
									<option value="50" ${size==50 ? "selected":"" }>50개 출력</option>
								</select>
							</c:if>
							
							<input type="hidden" name="page" value="${page}">
							<input type="hidden" name="schType" value="${schType}">
							<input type="hidden" name="kwd" value="${kwd}">
							
						</div>
			        </div>				
					
					<table class="table table-hover board-list">
						<thead class="table-light">
							<tr>
								<c:if test="${sessionScope.member.email=='admin'}">
									<th class="chk">
										<input type="checkbox" class="form-check-input" name="chkAll" id="chkAll">        
									</th>
								</c:if>
								<th class="num">번호</th>
								<th class="title">제목</th>
								<th class="name">작성자</th>
								<th class="date">작성일</th>
							</tr>
						</thead>
						
						<tbody>
							<c:forEach var="dto" items="${listNotice}">
								<tr>
									<c:if test="${sessionScope.member.email=='admin'}">
										<td>
											<input type="checkbox" class="form-check-input" name="nums" value="${dto.notice_num}">
										</td>
									</c:if>
									<td><span class="badge bg-primary">공지</span></td>
									<td class="left">
										<span class="d-inline-block text-truncate align-middle" style="max-width: 390px;"><a href="${articleUrl}&num=${dto.notice_num}" class="text-reset">${dto.title}</a></span>
									</td>
									<td>${dto.email}</td>
									<td>${dto.reg_date}</td>
								</tr>
							</c:forEach>

							<c:forEach var="dto" items="${list}" varStatus="status">
								<tr>
									<c:if test="${sessionScope.member.email=='admin'}">
										<td>
											<input type="checkbox" class="form-check-input" name="nums" value="${dto.notice}">
										</td>
									</c:if>
									<td>${dataCount-(page-1)*size-status.index}</td>
									<td class="left">
										<span class="d-inline-block text-truncate align-middle" style="max-width: 390px;"><a href="${articleUrl}&num=${dto.notice_num}" class="text-reset">${dto.title}</a></span>
										<c:if test="${dto.gap < 1}"><img class="align-middle" src="${pageContext.request.contextPath}/resources/images/new.gif"></c:if>
									</td>
									<td>${dto.email}</td>
									<td>${dto.reg_date}</td>
									
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</form>
				
				<div class="page-navigation">
					${dataCount==0 ? "등록된 게시글이 없습니다." : paging}
				</div>

				<div class="row board-list-footer">
					<div class="col">
						<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/notice/list';"><i class="bi bi-arrow-clockwise"></i></button>
					</div>
					<div class="col-6 text-center">
						<form class="row" name="searchForm" action="${pageContext.request.contextPath}/notice/list" method="post">
							<div class="col-auto p-1">
								<select name="schType" class="form-select">
									<option value="all" ${schType=="all"?"selected":""}>제목+내용</option>
									<option value="email" ${schType=="email"?"selected":""}>작성자</option>
									<option value="reg_date" ${schType=="reg_date"?"selected":""}>등록일</option>
									<option value="title" ${schType=="title"?"selected":""}>제목</option>
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
						<c:if test="${sessionScope.member.email=='admin'}">
							<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/notice/write';">글올리기</button>
						</c:if>
					</div>
				</div>

			</div>
		</div>
	</div>
</main>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
<footer>
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</footer>


</body>
</html>