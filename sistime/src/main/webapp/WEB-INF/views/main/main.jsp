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
<style>
        .body {
            font-family: 'Inter', sans-serif;
            margin: 0;
            display: flex;
            height: 100vh;
        }
           
        .main {
            flex: 1;
            display: flex;
            flex-direction: column;
            padding: 20px;
            box-sizing: border-box;
        }
		/* justify-content 건드리면 중간 공백 사라짐 */
        .content-wrapper {
            display: flex;
            flex-direction: column;
            flex: 1;
            justify-content: flex-end;
        }
        .content {
            display: flex;
            gap: 20px;
        }
        .card {
            background: #fff;
            border-radius: 10px;
            padding: 20px;
            flex: 1;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
		/* 이미지 크기 */
        .carousel-inner img {
            width: 50%;
            margin: 0 auto;
        }
		/* 버튼 크기 */
        .carousel-control-prev,
        .carousel-control-next {
            width: 5%;
        }
		/* 버튼 위치 */
        .carousel-control-prev {
            left: 25%;
        }
		/* 버튼 위치 */
        .carousel-control-next {
            right: 25%;
        }
        
    	.border {
    		margin-top: 10px;
    		padding: 10px;
    		border: 1px solid #ddd;
    		border-radius: 5px;
    		background-color: #f8f9fa;
		}
		
		
}
        
    </style>
</head>
<body>
<header>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</header>
<div class="body">
    <div class="main">
        
<div id="carouselExample" class="carousel slide">
      		
  <div class="carousel-inner">
    <div class="carousel-item active">
      <img src="${pageContext.request.contextPath}/resources/images/bg.png"  class="d-block" alt="...">
    </div>
   
  </div>
  <button class="carousel-control-prev" type="button" data-bs-target="#carouselExample" data-bs-slide="prev">
    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
    <span class="visually-hidden">Previous</span>
  </button>
  <button class="carousel-control-next" type="button" data-bs-target="#carouselExample" data-bs-slide="next">
    <span class="carousel-control-next-icon" aria-hidden="true"></span>
    <span class="visually-hidden">Next</span>
  </button>
  
</div>
     
      
        <div class="content-wrapper">
            <div class="content">
                <div class="card">
                    <h3><a href="${pageContext.request.contextPath}/notice/list">공지사항</a></h3>
						<div class="border px-2">
							<c:forEach var="dto" items="${listNotice}">
								<div class="text-truncate pax-2 subject-list">
									<a href="${pageContext.request.contextPath}/notice/article?page=1&size=10&num=${dto.notice_num}">${dto.title}</a>
								</div>
							</c:forEach>
							<c:forEach var="n" begin="${listNotice.size() +1}" end="5">
								<div class="text-truncate pax-2 subject-list">
								&nbsp;
								</div>
							</c:forEach>
						</div>	
                </div>
                <div class="card">
                    <h3><a href="${pageContext.request.contextPath}/board/list">자유 게시판</a></h3>
                    
                    	<div class="border px-2">
							<c:forEach var="dto" items="${listboard}">
								<div class="text-truncate pax-2 subject-list">
									<a href="${pageContext.request.contextPath}/board/article?page=1&size=10&num=${dto.board_num}">${dto.title}</a>
								</div>
							</c:forEach>
							<c:forEach var="n" begin="${listboard.size() +1}" end="5">
								<div class="text-truncate pax-2 subject-list">
								&nbsp;
								</div>
							</c:forEach>
						</div>	
							
                </div>
                <div class="card">
                    <h3><a href="${pageContext.request.contextPath}/event/list">이벤트 페이지</a></h3>
                    	<div class="border px-2">
							<c:forEach var="dto" items="${listEvent}">
								<div class="text-truncate pax-2 subject-list">
									<a href="${pageContext.request.contextPath}/event/article?page=1&size=10&num=${dto.eventpage_num}">${dto.event_title}</a>
								</div>
							</c:forEach>
							<c:forEach var="n" begin="${listEvent.size() +1}" end="5">
								<div class="text-truncate pax-2 subject-list">
								&nbsp;
								</div>
							</c:forEach>
						</div>	
                </div>
            </div>
        </div>
    </div>
   
    </div>
    <footer>
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</footer>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>