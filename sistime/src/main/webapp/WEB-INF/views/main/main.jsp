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
    	.imageslide{
    		border : 1px solid black;
    		margin : 0 auto;
    		box-shadow: 0 0 10px rgba(0, 0, 0, 0.6);
    		
    	}
        .body {
        	border : 1px solid black;
            font-family: 'Inter', sans-serif;
            margin: 0;
            display: flex;
            height: 100vh;
        }
        .sidebar {
            width: 100%;
            height: 100%;
            background: #f5f5f5;  
            box-sizing: border-box;
            display: flex;
            flex-direction: column;
        }
        .sidebar a {     
            display: flex;
            align-items: center;
            padding: 10px 0;
            text-decoration: none;
            color: #787486;
            font-weight: 500;
        }
        .sidebar img {
       
            margin-right: 10px;
        }
        
        .main {
            flex: 1;
            display: flex;
            flex-direction: column;
            padding: 20px;
            box-sizing: border-box;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin : 20px 20px 0 20px;
            padding: 20px;
            background-color: #E0E0E0;
        }
        .header input {
            padding: 5px;
            width: 200px;
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
        @media (max-width: 768px) {
            .sidebar {
                width: 150px;
            }
            .header input {
                width: 150px;
            }           
        }
        @media (max-width: 480px) {
            .sidebar {
                display: none;
            }
            .header {
                flex-direction: column;
                align-items: flex-start;
            }
            .content {
                flex-direction: column;
            }
        }
        
        .pi{
        	font-size: 100px;
    		line-height: 100px;
    		margin : 20px;
        }
        .myinfo{
        	border : 1px solid black;
        }
    </style>
</head>
<body>
<header>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</header>
<div class = "myinfo" style="float: right;">
	<div class = "info-box">
		<div class = "profileimage">
			<i class="bi bi-person-square pi"></i>
		</div>
			<h5> 프로필 </h5>
		<div class = "infobutton">
			<button type="button" class="btn btn-outline-secondary"> 내정보 </button>
			<button type="button" class="btn btn-outline-secondary"> 프로필 변경</button>
		</div>
	</div>
</div>
<div class="body">
    <div class="main">
        
<div id="carouselExample" class="carousel slide imageslide">
  <div class="carousel-inner">
  
    <div class="carousel-item active">
      <img src="${pageContext.request.contextPath}/resources/images/bg.png" alt="...">
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