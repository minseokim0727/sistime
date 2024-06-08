<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
    <form name="createForm" method="post">
        <input type="text" name="pageName">
        <button type="button" onclick="sendData()">전송</button>
    </form>

    <script type="text/javascript">
        function sendData() {
            // 폼 요소 가져오기
            let form = document.forms["createForm"];
            // 입력된 값 가져오기
            let pageName = form["pageName"].value;
            // 페이지 이름이 비어있는지 확인
            if (!pageName.trim()) {
                alert("페이지 이름을 입력하세요.");
                return;
            }
            // 서버로 데이터 전송
            form.action = "${pageContext.request.contextPath}/admin/"+pageName;
            form.submit();
        }
    </script>
</body>
</html>
