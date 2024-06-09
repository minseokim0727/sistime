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
.body-main{
	margin-top : 10px;
}
.sub_box input {
    display: block;
    margin-bottom: 10px; /* 각 input 간의 간격 조정 */
    width: 150px; /* 입력 박스 너비를 100%로 설정 */
    padding: 10px; /* 입력 박스 내부 여백 추가 */
    box-sizing: border-box; /* 패딩과 보더를 포함하여 너비 계산 */
}
.readmytime{
	padding: 10px;
	margin-top: 10px;
	margin-bottom: 10px;
}
</style>

</head>
<body>

<header>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</header>
	
<main>
	<div class="container">
		<div class="body-container">	
			<div class="body-title">
				<h3><i class="bi bi-calculator"></i> 시간표 </h3>
			</div>
			
			<div class="body-main">
			<form action="${pageContext.request.contextPath}/timetable/newsublist" method="get">
				<div class="readinfo">
					<div class="col-auto p-1">
    					<input type="text" name="year" placeholder="년도" class="form-control">
					</div>
					<div class="col-auto p-1">
   					 	<select name="semester" class="form-select">
        					<option value="1">1학기</option>
       						<option value="2">2학기</option>
   						</select>
					</div>
					<div class="readmytime">
						<button type="submit">시간표 작성하기</button>
					</div>
				</div>
			</form>
			
			<div class="body-main">
		        <div class="row board-list-header">
		    
		            <div class="col-auto">&nbsp;</div>
		        </div>				
				
				<table class="table table-hover board-list">
					<thead class="table-light">
						<tr>
							<th class="num">번호</th>
							<th class="subject">년도</th>
							<th class="name">학기</th>
							<th> 시간표 작성 </th>

						</tr>
					</thead>
					
					<tbody>
						<c:forEach var="dto" items="${list}" varStatus="status">
					<form action="${pageContext.request.contextPath}/timetable/sublist" method="get">
							<tr>
							<td> <input type="hidden" name="sem_num" value="${dto.sem_num}" /> ${dto.sem_num}  </td>
							<td> <input type="hidden" name="year" value="${dto.sub_year}" /> ${dto.sub_year} 년도 </td>
							<td> <input type="hidden" name="semester" value="${dto.semester}" /> ${dto.semester} 학기 </td>
							<td> <button type="submit"> 이동 </button> </td>
							</tr>
						</form>
						</c:forEach>
					</tbody>
				</table>
				
				<div class="page-navigation">
					${dataCount == 0 ? "등록된 게시물이 없습니다." : paging}
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