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
   margin-top: 150px;
}
.small{
   font-size: 0.5rem;
}

.ssang-container {
   display: flex;
   align-items: flex-end;
   justify-content: center;
   margin-top: 30px;
}

.loginimage {
   width: 90px;
   height: 90px;
   margin-right: 10px; 
}

.ssang {
   margin: 0;
   font-weight: bold;
   font-size: 30px;
}
</style>

<script type="text/javascript">
   function sendLogin() {
      const f = document.loginForm;
      let str;

      str = f.email.value;
      if (!str) {
         f.email.focus();
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
      <div class="body-container" style="justify-content: center;">

         <div class="row mt-12">
            <div class="col-md-6 offset-md-3">
               <div class="border-none mt-5 p-4">
                  <form name="loginForm" action="" method="post" class="row g-1">
                     
                     <div class="ssang-container">
                        <img class="loginimage" alt="" src="${pageContext.request.contextPath}/resources/images/login.jpg">
                        <div style="display: flex; flex-direction: column;">
                           <i class="small">누가 요즘 에타하냐~</i>
                           <i class="ssang">쌍용타임</i>
                        </div>
                     </div>
                    
                     
                     <div class="col-12">
                        <input type="text" name="email" class="form-control" placeholder="이메일">
                     </div>
                     <div class="col-12" style="font-weight: bold;">
                        <input type="password" name="userPwd" class="form-control" autocomplete="off" placeholder="패스워드">
                     </div>

                     <div class="col-12">
                        <button type="button" class="form-control" onclick="sendLogin();" style="background: #F53D1D; color: white">
                           &nbsp;쌍용타임 로그인&nbsp;
                        </button>
                     </div>

                     <div class="col-12 d-flex align-items-center justify-content-between">
                        <div class="form-check">
                           <input class="form-check-input" type="checkbox" id="rememberMe"> 
                           <label class="form-check-label" for="rememberMe"> 아이디 저장</label>
                        </div>
                        <a href="#" class="text-decoration-none">아이디/비밀번호 찾기</a>
                     </div>

                  </form>
                  <p class="mt-4"></p>
                  <div class="col-12">
                     <p class="text-center mb-0">
                        <a href="${pageContext.request.contextPath}/member/member" class="text-decoration-none">회원가입</a>
                     </p>
                  </div>
               </div>

               <div class="d-grid">
                  <p class="form-control-plaintext text-center text-primary">${message}</p>
                  <p class="form-control-plaintext text-center text-primary">${banmsg}</p>
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
