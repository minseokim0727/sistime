<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>spring</title>

<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />

<style type="text/css">
.body-container {
   max-width: 800px;
}
.small{
   font-size: 0.5rem;
   text-align: center;
   margin-bottom: -20px;
   padding-left: 60px;
}
.loginimage{
	width: 60px;
	height: 60px;
}
</style>

<script type="text/javascript">
   function sendLogin() {
      const f = document.loginForm;
      let str;

      str = f.userId.value;
      if (!str) {
         f.userId.focus();
         return;
      }

      str = f.userPwd.value;
      if (!str) {
         f.userPwd.focus();
         return;
      }

      f.action = "${pageContext.request.contextPath}/member/login";
      f.submit();
   }
</script>
</head>
<body>

   <main>
      <div class="container">
         <div class="body-container">

            <div class="row">
               <div class="col-md-6 offset-md-3">
                  <div class="border-none mt-5 p-4">
                     <form name="loginForm" action="" method="post" class="row g-3"
                        >
                        <i class="small">누가 요즘 에타하냐~</i>
                        <h3 class="text-center">
                           
                           <img class="loginimage" alt="" src="resources/images/login.jpg"> 
                           <i style="justify-content:flex-end; ">쌍용타임</i> 
                        </h3>
                        <div class="col-12">
                           <input type="text" name="userId" class="form-control"
                              placeholder="아이디">
                        </div>
                        <div class="col-12">
                           <input type="password" name="userPwd" class="form-control"
                              autocomplete="off" placeholder="패스워드">
                        </div>

                        <div class="col-12">
                           <button type="button" class="form-control"
                              onclick="sendLogin();" style="background: #F53D1D;">
                              &nbsp;쌍용타임 로그인&nbsp;
                           </button>
                        </div>

                        <div
                           class="col-12 d-flex align-items-center justify-content-between">
                           <div class="form-check">
                              <input class="form-check-input" type="checkbox"
                                 id="rememberMe"> <label class="form-check-label"
                                 for="rememberMe"> 아이디 저장</label>
                           </div>
                           <a href="#" class="text-decoration-none">아이디/비밀번호 찾기</a>
                        </div>


                     </form>
                     <p class="mt-4"></p>
                     <div class="col-12">
                        <p class="text-center mb-0">
                           <a href="${pageContext.request.contextPath}/member/member"
                              class="text-decoration-none">회원가입</a>
                        </p>
                     </div>
                  </div>

                  <div class="d-grid">
                     <p class="form-control-plaintext text-center text-primary">${message}</p>
                  </div>

               </div>
            </div>

         </div>
      </div>
   </main>

   <footer>
      <jsp:include page="/WEB-INF/views/layout/footer.jsp" />
   </footer>

   <jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />

</body>
</html>