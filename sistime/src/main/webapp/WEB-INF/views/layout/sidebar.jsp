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
  <h2>SISTIME</h2>
    <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
    
  </div>
  
  <div class="offcanvas-body">
     <div class="sidebar">
        
        <div class="dropdown">
  <a data-bs-toggle="dropdown" aria-expanded="false">
    SoTongHaeYo&nbsp; <i class="bi bi-caret-down downicon"></i>
  </a>
  <ul class="dropdown-menu">
    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/test/template">&nbsp;&nbsp;Freedom</a></li>
    <li><a class="dropdown-item" href="#">&nbsp;&nbsp;Secret</a></li>
    <li><a class="dropdown-item" href="#">&nbsp;&nbsp;Notice</a></li>
    <li><a class="dropdown-item" href="#">&nbsp;&nbsp;Event</a></li>
    <li><a class="dropdown-item" href="#">&nbsp;&nbsp;BeryBeryHot</a></li>
  </ul>
</div>
        <a href="#">커뮤니티1</a>
        <a href="#">커뮤니티1</a>
        <a href="#">커뮤니티1</a>
        <a href="#">커뮤니티1</a>
    </div>
  </div>
</div>
