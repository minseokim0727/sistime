// 요소가 보이지 않으면 true를 반환
const isHidden = elem => {
	 const styles = window.getComputedStyle(elem);
	 return styles.display === 'none' || styles.visibility === 'hidden';
};

// 이미지 파일인지 검사
function isImageFile(filename){
    let format = /(\.gif|\.jpg|\.jpeg|\.png)$/i;
    return format.test(filename);
}

// 파일 용량 구하기
function fileSize(file) {
	let size = 0;
	
	for(let f of file.files) {
		size += f.size;
	}
	
	return size;
}

// 이메일 형식 검사
function isValidEmail(data){
    let format = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/;
    return format.test(data); // true : 올바른 포맷 형식
}

// SQL 문 존재 여부 검사
function isValidSQL(data){
    let format = /(SELECT|INSERT|UPDATE|DELETE|CREATE|ALTER|DROP|EXEC|UNION|FETCH|DECLARE|TRUNCATE)/gi;
    return format.test(data);
}

// -------------------------------------------------
// 전체화면
const fullscreen = elem => {
    if ( elem.requestFullscreen ) return elem.requestFullscreen();
    if ( elem.webkitRequestFullscreen ) return elem.webkitRequestFullscreen();
    if ( elem.mozRequestFullScreen ) return elem.mozRequestFullScreen();
    if ( elem.msRequestFullscreen ) return elem.msRequestFullscreen();
};

// 전체화면 취소
const cancelFullScreen = () => {
    if ( document.exitFullscreen ) return document.exitFullscreen();
    if ( document.webkitCancelFullscreen ) return document.webkitCancelFullscreen();
    if ( document.mozCancelFullScreen ) return document.mozCancelFullScreen();
    if ( document.msExitFullscreen ) return document.msExitFullscreen();
};

// 전체 화면 토글
function toggleFullScreen(elem) {
    if ( ! document.fullscreenElement ) {
        fullscreen(elem);
    } else {
        cancelFullScreen();
    }
}

// -------------------------------------------------
// 자바스크립트 xss 방지 HTML 특수문자 변환
// Cross-site Scripting (XSS)
//   : SQL injection과 함께 웹 상에서 가장 기초적인 취약점 공격 방법의 일종
function symbolHtml(content) {
    if (! content) return content;

    content = content.replace(/</g, '&lt;');
    content = content.replace(/>/g, '&gt;');
    content = content.replace(/\"/g, '&quot;');
    content = content.replace(/\'/g, '&#39;');
    content = content.replace(/\(/g, '&#40;');
    content = content.replace(/\)/g, '&#41;');

    return content;
}

// 기호를 특수문자로
function restoreHtml(content) {
    if (! content) return content;
    
    content = content.replace(/\&lt;/gi, '<');
    content = content.replace(/\&gt;/gi, '>');
    content = content.replace(/\&#40;/gi, '(');
    content = content.replace(/\&#41;/gi, ')');
    content = content.replace(/\&#39;/gi, "'");
    content = content.replace(/\&quot;/gi, "\"");
    
    return content;
}


// 문자열에 특수문자(",  ',  <,  >, (, ) ) 검사
function isValidSpecialChar(data) {
    let format = /[\",\',<,>,\(,\)]/g;
    return format.test(data);
}

// -------------------------------------------------
// 이벤트 등록
/* 
    // 사용 예
    let func= function() { alert('예제'); }
    addEvent(window, load, func);
*/
function addEvent(elem, evType, fn) {
    if (elem.addEventListener) {
        elem.addEventListener(evType, fn, false);
        return true;
    } else if (elem.attachEvent) {
        let r = elem.attachEvent('on' + evType, fn);
        return r;
    } else {
        elem['on' + evType] = fn;
    }
}

// -------------------------------------------------
// 팝업 윈도우즈
function winOpen(url, windowName, windowFeatures) {
	if(! theURL) return;
	if(! windowName) windowName = '';
	
	let flag = windowFeatures;
    if(flag === undefined) {
		flag = 'left=10, ';
		flag += 'top=10, ';
		flag += 'width=372, ';
		flag += 'height=466, ';
		flag += 'toolbar=no, ';
		flag += 'menubar=no, ';
		flag += 'status=no, ';
		flag += 'scrollbars=no, ';
		flag += 'resizable=no';
		// flag = 'scrollbars=no,resizable=no,width=220,height=230';
	}
	
    window.open(url, windowName, flag);
}

// -------------------------------------------------
// 기타 형식 검사
// 영문, 숫자 인지 확인
 function isValidEngNum(str) {
    for(let i = 0; i < str.length; i++) {
        achar = str.charCodeAt(i);                 
        if( achar > 255 ) {
            return false;
        }
    }
    return true; 
}

// 전화번호 형식(숫자-숫자-숫자)인지 체크
function isValidPhone(data) {
    // let format = /^(\d+)-(\d+)-(\d+)$/;
    let format = /^(010)-[0-9]{4}-[0-9]{4}$/g;
    return format.test(data);
}
