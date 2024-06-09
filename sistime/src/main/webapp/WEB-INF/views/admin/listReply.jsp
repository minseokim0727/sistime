<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class='reply-info'>
	<span class='reply-count'>댓글 ${replyCount}개</span>
	<span>[목록, ${pageNo}/${total_page} 페이지]</span>
</div>

<table class='table table-borderless reply-list'>
	<c:forEach var="dto" items="${listReply}">
		<tr class='list-header'>
			<td width='50%'>
				<span class='bold'>${dto.userName}</span>
			</td>
			<td width='50%' align='right'>
				<span></span> |
				
				<c:choose>
					<c:when test="${sessionScope.member.email==dto.email || sessionScope.member.email == 'admin'}">
						<span class='deleteReply' data-replyNum='${dto.replynum}' data-pageNo='${pageNo}'>삭제</span>
					</c:when>
					<c:otherwise>
						<span class='notifyReply'>신고</span>
					</c:otherwise>
				</c:choose>
				
			</td>
		</tr>
		<tr>
			<td colspan='2' valign='top'>${dto.replycontent}</td>
		</tr>

	</c:forEach>
</table>

<div class="page-navigation">
	${paging}
</div>							
