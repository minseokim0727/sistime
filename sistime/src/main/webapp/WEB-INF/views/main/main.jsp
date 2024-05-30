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
      <img src="${pageContext.request.contextPath}/resources/images/bg.png"  class="d-block w-100" alt="...">
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
                    <h3>To Do</h3>
                    <p>Task 1</p>
                    <p>Task 2</p>
                </div>
                <div class="card">
                    <h3>In Progress</h3>
                    <p>Task 3</p>
                    <p>Task 4</p>
                </div>
                <div class="card">
                    <h3>Done</h3>
                    <p>Task 5</p>
                    <p>Task 6</p>
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