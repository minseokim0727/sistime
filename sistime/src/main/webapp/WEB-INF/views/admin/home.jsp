<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 페이지</title>
<style>
    body {
        font-family: Arial, sans-serif;
        margin: 0;
        padding: 0;
    }
    .header {
        background-color: #333;
        color: white;
        text-align: center;
        padding: 20px 0;
    }
    ul {
        list-style-type: none;
        margin: 0;
        padding: 0;
        overflow: hidden;
        text-align: center;
    }
    li {
        display: inline;
        margin: 0 10px;
    }
    li a {
        display: block;
        color: black;
        text-align: center;
        padding: 20px 30px;
        text-decoration: none;
        font-size: 20px;
    }
    li a:hover {
        background-color: #f2f2f2;
        color: #333;
    }
</style>
</head>
<body>
    <div class="header">
        <h1>관리자 페이지</h1>
    </div>
    <ul>
        <li><a href="${pageContext.request.contextPath}/admin/write">게시판 만들기</a></li>
        <li><a href="<c:url value='/menu2.jsp'/>">메뉴 2</a></li>
        <li><a href="<c:url value='/menu3.jsp'/>">메뉴 3</a></li>
    </ul>
</body>
</html>
