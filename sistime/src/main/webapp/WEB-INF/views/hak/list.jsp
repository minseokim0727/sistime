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
<script type="text/javascript">
function sendOk(){
	const f = document.searchForm;
	f.submit();
}
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
				<h3><i class="bi bi-calculator"></i> 학점계산기 </h3>
			</div>
			
			<div class="body-main">
			<form action="Calcgrade" method="get">
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
						<button type="button" onclick="sendOk();">내 시간표</button>
					</div>
				</div>
			</form>
			
				
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