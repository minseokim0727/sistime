<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>내 학점 계산</title>

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
				<h3><i class="bi bi-calculator"></i> 학점계산기 </h3>
			</div>
			
			<div class="body-main">
			<table border="1">
        <tr>
            <th>Subject Name</th>
            <th>Grade</th>
        </tr>
        <c:forEach var="grade" items="${grades}">
            <tr>
                <td>${grade.sub_name}</td>
                <td>${grade.sub_grade}</td>
            </tr>
        </c:forEach>
    </table>
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