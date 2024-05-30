<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<style>
.downicon{
	font-size: 14px;
}
</style>
<div class="offcanvas offcanvas-start" data-bs-scroll="true" data-bs-backdrop="false" tabindex="-1" id="offcanvasScrolling" aria-labelledby="offcanvasScrollingLabel" style="background-color: #f5f5f5">
  
  <div class="offcanvas-header">   
  <h1>SISTIME</h1>
    <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
    
  </div>
  
  <div class="offcanvas-body">
     <div class="sidebar">
        
        <div class="dropdown">
  <a class = "h5" data-bs-toggle="dropdown" aria-expanded="false">
    화개장터&nbsp; <i class="bi bi-caret-down downicon"></i>
  </a>
  <ul class="dropdown-menu">
    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/test/template">&nbsp;&nbsp; 자유 </a></li>
    <li><a class="dropdown-item" href="#">&nbsp;&nbsp; 비밀 </a></li>
    <li><a class="dropdown-item" href="#">&nbsp;&nbsp; 공지사항 </a></li>
    <li><a class="dropdown-item" href="#">&nbsp;&nbsp; 이벤트 </a></li>
    <li><a class="dropdown-item" href="#">&nbsp;&nbsp; 베스트 </a></li>
  </ul>
</div>
        <a class = "h5" href="#"> 시간표 </a>
        <a class = "h5" href="#"> 학점계산기 </a>
        
         <div class="dropdown">
  <a class = "h5" data-bs-toggle="dropdown" aria-expanded="false">
   고객센터&nbsp; <i class="bi bi-caret-down downicon"></i>
  </a>
  <ul class="dropdown-menu">
    <li><a class="dropdown-item" href="#">&nbsp;&nbsp; 1대1 문의 </a></li>
    <li><a class="dropdown-item" href="#">&nbsp;&nbsp; 화개장터 요청 </a></li>
  </ul>
</div>
        <a class = "h5" href="#">관리자만 보이는 버튼 하기</a>
    </div>
  </div>
</div>
