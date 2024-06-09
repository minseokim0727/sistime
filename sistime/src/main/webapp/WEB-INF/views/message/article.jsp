<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title></title>
<style>
body {
	font-family: Arial, sans-serif;
	background-color: #f0f0f0;
	margin: 0;
	padding: 0;
}

.chat-container {
	max-width: 1000px;
	margin: 50px auto;
	background-color: #ffffff;
	border-radius: 10px;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.chat-partner {
	background-color: #696969;
	color: #ffffff;
	padding: 10px;
	border-top-left-radius: 10px;
	border-top-right-radius: 10px;
}

.chat-messages {
	padding: 20px;
	overflow-y: auto;
	max-height: 300px;
}

.chat-messages p {
	margin: 10px 0;
}

.chat-messages .sent-message {
	background-color: #778899;
	color: #ffffff;
	padding: 10px;
	border-radius: 5px;
	margin-bottom: 10px;
	max-width: 70%;
	align-self: flex-end;
	text-align: right;
	margin-left: auto;
}

.chat-messages .received-message {
	background-color: 	#D3D3D3;
	color: #333333;
	padding: 10px;
	border-radius: 5px;
	margin-bottom: 10px;
	max-width: 70%;
	align-self: flex-start;
	text-align: left;
}

.chat-input {
	display: flex;
	padding: 10px;
	justify-content: flex-end;
}

.chat-input input {
	flex: 1;
	padding: 8px;
	border: none;
	border-radius: 5px;
	outline: none;
}

.chat-input button {
	padding: 8px 15px;
	background-color: #778899;
	color: #ffffff;
	border: none;
	border-radius: 5px;
	cursor: pointer;
	margin-left: 10px;
}

.chat-input button:hover {
	background-color: #e87442;
}

.page-navigation {
	text-align: center;
	margin-top: 10px;
}
</style>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
	<div class="chat-container">
		<div class="chat-partner">
			<p id="partner-name">${read_nickname}</p>
		</div>
		<div class="chat-messages" id="chat-messages"></div>


		<div class="chat-input">
			<input type="text" id="message" class="message"
				placeholder="메시지를 입력하세요...">
			<button type="button" onclick="sendOk();">전송</button>
		</div>
		<div style="text-align: center; margin-top: 10px;">
			<a href="${pageContext.request.contextPath}/message/list"
				style="text-decoration: none;">
				<button type="button"
					style="padding: 8px 15px; background-color: #E0E0E0; color:black ; border: none; border-radius: 5px; cursor: pointer;">
					메시지 목록으로 돌아가기</button>
			</a>
		</div>
	</div>

	<script type="text/javascript">
		function ajaxFun(url, method, formData, dataType, fn) {
			const settings = {
				type : method,
				data : formData,
				dataType : dataType,
				success : function(data) {
					fn(data);
				},
				beforeSend : function(jqXHR) {
					jqXHR.setRequestHeader('AJAX', true);
				},
				complete : function() {
				},
				error : function(jqXHR) {
					let errorMessage = "알 수 없는 오류가 발생했습니다.";
					let errorDetails = "";

					if (jqXHR.status) {
						errorMessage += " (코드: " + jqXHR.status + ")";
					}

					if (jqXHR.responseText) {
						errorDetails = " - " + jqXHR.responseText;
					}

					console.error("AJAX 오류:", errorMessage, errorDetails);

					// 추가적인 처리 (로그 기록, 사용자 알림 등)
				}

			};
			$.ajax(url, settings);
		}

		function sendOk() {
			let message = $("#message").val();
			let read_name = "${read_nickname}";
			let send_name = "${sessionScope.member.nickname}";
			let url = "${pageContext.request.contextPath}/message/messageSend";
			let query = {
				message : message,
				send_name : send_name,
				read_name : read_name
			};

			$.ajax({
				type : "post",
				url : url,
				data : query,
				dataType : "json",
				success : function(data) {
					let passed = data.passed;
					if (passed === "true") {
						let s = "<div class='sent-message'><p>" + message
								+ "</p></div>";
						$("#chat-messages").append(s);
						$("#message").val('');
					}
				},
				error : function(e) {
					console.log(e.responseText);
				}
			});
		}

		function listPage() {
			let url = "${pageContext.request.contextPath}/message/messageGet";
			let send_nickname = "${yourname}";
			let read_nickname = "${send_nickname}";
			let msg_num = "${dto.msg_num}";
			let selector = "#chat-messages";
			let query = {
				page : 1,
				msg_num : msg_num,
				send_nickname : send_nickname,
				read_nickname : read_nickname
			};

			const fn = function(data) {
				$(selector).html(data);
			}

			// AJAX - JSON
			ajaxFun(url, "get", query, "text", fn);
		}

		// 페이지 로드 시 listPage() 함수를 호출하여 메시지를 가져오도록 설정합니다.
		$(document).ready(function() {
			listPage();
			setInterval(listPage, 5000); // 5초마다 새로운 메시지를 가져옴
		});
	</script>

</body>
</html>
