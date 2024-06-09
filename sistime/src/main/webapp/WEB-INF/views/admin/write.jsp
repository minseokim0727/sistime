<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>페이지 생성</title>
<style>
    body {
        font-family: Arial, sans-serif;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        margin: 0;
    }
    form {
        display: flex;
        flex-direction: column;
        align-items: center;
    }
    input[type="text"] {
        margin-bottom: 10px;
        padding: 8px;
        border-radius: 5px;
        border: 1px solid #ccc;
        font-size: 16px;
    }
    button {
        padding: 10px 20px;
        background-color: #007bff;
        color: white;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        font-size: 16px;
        transition: background-color 0.3s;
    }
    button:hover {
        background-color: #0056b3;
    }
</style>
</head>
<body>
    <form name="createForm" method="post">
        <input type="text" id="pageName" name="pageName" placeholder="페이지 이름 입력">
        <input type="text" id = "pageDescription" name="pageDescription" placeholder="페이지 설명 입력">
        <button type="button" onclick="sendData()">페이지 생성</button>

    </form>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

  <script type="text/javascript">
    function sendData() {
        let form = document.forms["createForm"];
        let pageName = form["pageName"].value;
        let pageDescription = form["pageDescription"].value;

        if (!pageName.trim()) {
            alert("페이지 이름을 입력하세요.");
            return;
        }
        
        let query = {
            pageName: pageName,
            pageDescription: pageDescription
        };
        
        $.ajax({
            type: "post",
            url: "${pageContext.request.contextPath}/admin/pageSend",
            data: query,
            dataType: "json",
            success: function(data) {
                // AJAX 요청이 성공한 경우에만 form을 서버로 제출합니다.
                form.action = "${pageContext.request.contextPath}/admin/write";
                form.submit();
            },
            error: function(e) {
                console.log(e.responseText);
            }
        });
    }
</script>

</body>
</html>
