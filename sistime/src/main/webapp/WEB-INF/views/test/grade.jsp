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
p{
	color : tomato;
}
.resultHak{
	margin : 5px;
	
}
.hakbutton{
	margin : 5px;
	border-radius: 30px;
	margin-left: 140px;
	width : 100px;
	
	
}

</style>
<script type="text/javascript">
function calchak(){
    const table = document.getElementById('table');
    const rows = table.getElementsByTagName('tr');
    let totalScore = 0;
    let totalCredit = 0;

    // 각 행을 반복하면서 계산
    for(let i = 1; i < rows.length; i++) { // 첫 번째 행은 헤더이므로 건너뜁니다.
        const row = rows[i];
        const credit = parseFloat(row.cells[1].textContent); // 두 번째 열이 학점입니다.
        const gradeSelect = row.cells[2].getElementsByTagName('select')[0]; // 세 번째 열에 있는 select 요소 가져오기
        const selectedGrade = parseFloat(gradeSelect.value); // 선택한 학점 값 가져오기

        totalScore += credit * selectedGrade; // 각 과목 학점과 선택한 학점을 곱해서 총 평점에 더합니다.
        totalCredit += credit; // 총 학점에 현재 과목의 학점을 더합니다.
    }

    // 평균 계산
    const averageScore = totalScore / totalCredit;

    // 결과 출력
    const averageScoreOutput = document.getElementById('averageScoreOutput');
    averageScoreOutput.innerText = "평균 학점 : " + averageScore.toFixed(2);
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
	<form name="myhakform" class="mainbox">
	<table class="myvalue" id="table">
        <tr>
            <th>과목 이름</th>
            <th>학점</th>
            <th>&nbsp;&nbsp; 나의 학점</th>
        </tr>
        
        <c:forEach var="dto" items="${list}">
            <tr>
            
                <td>${dto.sub_name}</td>
                <td id="dto_subgrade">${dto.sub_grade}</td>
                
                
                <td>&nbsp;&nbsp;
                	<select name="gradevalue" class="form-select">
						<option value="4.5">A+</option>
						<option value="4.0">A</option>
						<option value="3.5">B+</option>
						<option value="3.0">B</option>
						<option value="2.5">C+</option>
						<option value="2.0">C</option>
						<option value="1.5">D+</option>
						<option value="1.0">D</option>
					</select>
				</td>
            </tr>
        </c:forEach>
        
    </table>
    <button type="button" class="hakbutton" onclick = "calchak();"><i class="bi bi-calculator"></i>학점 계산</button>
    </form>
			</div>
			
			<div class="resultHak">
			<p id="averageScoreOutput">평균 학점 : </p>
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