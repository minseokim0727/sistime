<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:forEach var="dto" items="${listReplyAnswer}">
	<div class='answer-article'>
		<div class='answer-article-header'>
			<div class='answer-left'>└</div>
			<div class='answer-right'>
				<div><span class='bold'>${dto.userName}</span></div>
				<div>
					<span>${dto.reg_date}</span> |
					<span class='deleteReplyAnswer' data-replyNum='${dto.replyNum}' data-answer='${dto.answer}'>삭제</span>
				</div>
			</div>
		</div>
		<div class='answer-article-body'>
			${dto.content}
		</div>
	</div>
</c:forEach>
