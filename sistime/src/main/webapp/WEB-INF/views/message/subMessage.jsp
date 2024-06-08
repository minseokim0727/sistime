<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
    <title>Message Details</title>
    <style>
        .chat-messages {
            width: 100%;
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        .received-message, .sent-message {
            padding: 10px;
            margin-bottom: 10px;
            border-radius: 5px;
        }
        .received-message {
            background-color: #f1f1f1;
            text-align: left;
        }
        .sent-message {
            background-color: #e1ffc7;
            text-align: right;
        }
        .page-navigation {
            text-align: center;
            margin-top: 20px;
        }
    </style>
</head>
<body>
   
        <c:forEach var="dto" items="${list}">
            <c:choose>
                <c:when test="${sessionScope.member.nickname eq dto.send_email}">
                    <div class="received-message">
                        <p>${dto.content}</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="sent-message">
                        <p>${dto.content}</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </c:forEach>
   
    <div class="page-navigation">
        ${paging}
    </div>
</body>
</html>
