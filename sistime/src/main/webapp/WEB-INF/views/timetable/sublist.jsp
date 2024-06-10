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

.highlight {
    background-color: #f0f0f0; /* 강조할 색상 설정 */
}


.body-container {
    display: flex;
    flex-wrap: nowrap;
    gap: 20px;
}

.table-container {
    flex: 1;
}

.time-table-container {
    flex: 1;
    display: flex;
    flex-direction: column;
}

.time-table {
    width: 100%;
    border-collapse: collapse;
}

.time-table th, .time-table td {
    border: 1px solid black;
    width: 150px;
    height: 100px;
    padding: 10px;
    text-align: center;
    vertical-align: middle;
}

.board-list {
    width: 100%;
    border-collapse: collapse;
}

.board-list th, .board-list td {
    border: 1px solid black;
    padding: 10px;
    text-align: center;
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
            <div class="table-container">
                <table class="table table-hover board-list">
                    <thead class="table-light">
                        <tr>
                            <th class="num">학수번호</th>
                            <th class="subject">과목이름</th>
                            <th class="name">담당교수</th>
                            <th>년도</th>
                            <th>학기</th>
                            <th>학점</th>
                            <th>일정</th>
                            <th>등록/삭제</th>
                        </tr>
                    </thead>
                    <tbody>
                            <c:forEach var="dto" items="${list}" varStatus="status">
                        <form name="subForm" method="post">
                                <tr>
                                    <td> <input type="hidden" name="sub_num" value="${dto.sub_num}" /> ${dto.sub_num} </td>
                                    <td> <input type="hidden" name="sub_name" value="${dto.sub_name}" /> ${dto.sub_name} </td>
                                    <td> <input type="hidden" name="sub_pro" value="${dto.sub_pro}" /> ${dto.sub_pro} </td>
                                    <td> <input type="hidden" name="sub_year" value="${dto.sub_year}" /> ${dto.sub_year} </td>
                                    <td> <input type="hidden" name="semester" value="${dto.semester}" /> ${dto.semester} </td>
                                    <td> <input type="hidden" name="sub_grade" value="${dto.sub_grade}" /> ${dto.sub_grade} </td>
                                    <td> <input type="hidden" name="sub_time" value="${dto.sub_time}" /> ${dto.sub_time} </td>
                                    <td>
                                        <button type="button" onclick="submitForm('/sistime/timetable/insert?year=${dto.sub_year}&semester=${dto.semester}&sem_num=${sem_num}&sub_num=${dto.sub_num}')"> 등록 </button>
                                        <button type="button" onclick="submitForm('/sistime/timetable/delete?year=${dto.sub_year}&semester=${dto.semester}&sem_num=${sem_num}&sub_num=${dto.sub_num}')"> 삭제 </button>
                                    </td>
                                </tr>
                        </form>
                            </c:forEach>
                         
                    </tbody>
                </table>
            </div>

            <div class="time-table-container">
                <table class="time-table" id="timetable">
                    <thead>
                        <tr>
                            <th>시간</th>
                            <th>월</th>
                            <th>화</th>
                            <th>수</th>
                            <th>목</th>
                            <th>금</th>
                        </tr>
                    </thead>
                    <tbody>
         <c:forEach var="hour"  begin="9" end="17">
    <tr>
        <td id="time_${hour}">${hour}:00-${hour+1}:00</td>
        <td id="mon_${hour}">
        <c:forEach var="dto" items="${subList}" varStatus="status">
        <c:choose>
       <c:when test="${dto.sub_time == ('월 (' += (hour-1) += '-' += (hour+1)) += ')'}">
           ${dto.sub_name}
           ${dto.sub_pro}
       </c:when>
       <c:when test="${dto.sub_time == ('월 (' += (hour-1) += '-' += (hour)) += ')'}">
           ${dto.sub_name}
           ${dto.sub_pro}
       </c:when>
       <c:when test="${dto.sub_time == ('월 (' += (hour) += '-' += (hour+1)) += ')'}">
           ${dto.sub_name}
           ${dto.sub_pro}
       </c:when>
       <c:when test="${dto.sub_time == ('월 (' += (hour) += '-' += (hour+2)) += ')'}">
           ${dto.sub_name}
           ${dto.sub_pro}
       </c:when>
   </c:choose>
        </c:forEach>
        </td>
        <td id="tue_${hour}">
         <c:forEach var="dto" items="${subList}" varStatus="status">
        <c:choose>
       <c:when test="${dto.sub_time == ('화 (' += (hour-1) += '-' += (hour+1)) += ')'}">
           ${dto.sub_name}
           ${dto.sub_pro}
       </c:when>
       <c:when test="${dto.sub_time == ('화 (' += (hour-1) += '-' += (hour)) += ')'}">
           ${dto.sub_name}
           ${dto.sub_pro}
       </c:when>
       <c:when test="${dto.sub_time == ('화 (' += (hour) += '-' += (hour+1)) += ')'}">
           ${dto.sub_name}
           ${dto.sub_pro}
       </c:when>
       <c:when test="${dto.sub_time == ('화 (' += (hour) += '-' += (hour+2)) += ')'}">
           ${dto.sub_name}
           ${dto.sub_pro}
       </c:when>
   </c:choose>
        </c:forEach>
        </td>
        <td id="wed_${hour}">
         <c:forEach var="dto" items="${subList}" varStatus="status">
        <c:choose>
       <c:when test="${dto.sub_time == ('수 (' += (hour-1) += '-' += (hour+1)) += ')'}">
           ${dto.sub_name}
           ${dto.sub_pro}
       </c:when>
       <c:when test="${dto.sub_time == ('수 (' += (hour-1) += '-' += (hour)) += ')'}">
           ${dto.sub_name}
           ${dto.sub_pro}
       </c:when>
       <c:when test="${dto.sub_time == ('수 (' += (hour) += '-' += (hour+1)) += ')'}">
           ${dto.sub_name}
           ${dto.sub_pro}
       </c:when>
       <c:when test="${dto.sub_time == ('수 (' += (hour) += '-' += (hour+2)) += ')'}">
           ${dto.sub_name}
           ${dto.sub_pro}
       </c:when>
   </c:choose>
        </c:forEach>
        </td>
        <td id="thu_${hour}">
         <c:forEach var="dto" items="${subList}" varStatus="status">
        <c:choose>
       <c:when test="${dto.sub_time == ('목 (' += (hour-1) += '-' += (hour+1)) += ')'}">
           ${dto.sub_name}
           ${dto.sub_pro}
       </c:when>
       <c:when test="${dto.sub_time == ('목 (' += (hour-1) += '-' += (hour)) += ')'}">
           ${dto.sub_name}
           ${dto.sub_pro}
       </c:when>
       <c:when test="${dto.sub_time == ('목 (' += (hour) += '-' += (hour+1)) += ')'}">
           ${dto.sub_name}
           ${dto.sub_pro}
       </c:when>
       <c:when test="${dto.sub_time == ('목 (' += (hour) += '-' += (hour+2)) += ')'}">
           ${dto.sub_name}
           ${dto.sub_pro}
       </c:when>
   </c:choose>
        </c:forEach>
        </td>
        <td id="fri_${hour}">
         <c:forEach var="dto" items="${subList}" varStatus="status">
        <c:choose>
       <c:when test="${dto.sub_time == ('금 (' += (hour-1) += '-' += (hour+1)) += ')'}">
           ${dto.sub_name}
           ${dto.sub_pro}
       </c:when>
       <c:when test="${dto.sub_time == ('금 (' += (hour-1) += '-' += (hour)) += ')'}">
           ${dto.sub_name}
           ${dto.sub_pro}
       </c:when>
       <c:when test="${dto.sub_time == ('금 (' += (hour) += '-' += (hour+1)) += ')'}">
           ${dto.sub_name}
           ${dto.sub_pro}
       </c:when>
       <c:when test="${dto.sub_time == ('금 (' += (hour) += '-' += (hour+2)) += ')'}">
           ${dto.sub_name}
           ${dto.sub_pro}
       </c:when>
   </c:choose>
        </c:forEach>
        </td>
    </tr>
</c:forEach>

</tbody>
</table>
</div>
</div>

<div class="page-navigation">
${dataCount == 0 ? "등록된 게시물이 없습니다." : paging}
</div>
</div>
</main>

<footer>
<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</footer>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>


<script type="text/javascript">
function submitForm(actionUrl) {
    const form = document.forms['subForm'];
    form.action = actionUrl;
    form.submit();
}



</script>
</body>
</html>
                                
