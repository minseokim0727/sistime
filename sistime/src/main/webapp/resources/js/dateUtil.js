// 날짜 형식 검사 정규표현식
function isValidDate(data){
    let regexp = /^[12][0-9]{3}[\.|\-|\/]?[0-9]{2}[\.|\-|\/]?[0-9]{2}$/;
    if(! regexp.test(data)) {
        return false;
	}

    regexp = /(\.)|(\-)|(\/)/g;
    data = data.replace(regexp, '');
    
	let y = parseInt(data.substring(0, 4));
    let m = parseInt(data.substring(4, 6));
    if(m < 1 || m > 12) {
    	return false;
    }
    let d = parseInt(data.substring(6));
    let lastDay = (new Date(y, m, 0)).getDate();
    if(d < 1 || d > lastDay) {
    	return false;
   	}

	return true;
}

// 월의 마지막 날짜 반환
function lastDayOfMonth(year, month) {
	// let date = new Date(year, month, 1-1);
	let date = new Date(year, month, 0);
	
	return date.getDate();
}

// 년도가 윤년인지 판별
function isLeapYear(year) {
	let lastDay = lastDayOfMonth(year, 2);
	
	return lastDay === 29;
}

// 날짜를 문자열로
function dateToString(date) {
	if( ! (date instanceof Date) ) {
		throw 'Date 객체가 아닙니다.';
	}
	
    let y = date.getFullYear();
    let m = date.getMonth() + 1;
    if(m < 10) m = '0' + m;
    let d = date.getDate();
    if(d < 10) d = '0' + d;
    
    return `${y}-${m}-${d}`;
}

// 문자열을 날짜로
function stringToDate(value) {
    if(! isValidDate(value)) {
    	throw '날짜 형식이 올바르지 않습니다.';
    }
		
    let format = /(\.)|(\-)|(\/)/g;
    value = value.replace(format, '');
    
    let y = parseInt(value.substring(0, 4));
    let m = parseInt(value.substring(4, 6));
    let d = parseInt(value.substring(6));
    
    return new Date(y, m-1, d);
}

// 기준일부터 몇일 후
function daysLater(startDate, days) {
    if(! isValidDate(startDate)) {
    	throw '날짜 형식이 올바르지 않습니다.';
    }
	
    let y, m, d;
    let date = new Date();
    let regexp = /(\.)|(\-)|(\/)/g;
    startDate = startDate.replace(regexp, '');
    
    y = parseInt(startDate.substring(0, 4));
    m = parseInt(startDate.substring(4, 6));
    d = parseInt(startDate.substring(6)) + parseInt(days);
    // d = parseInt(startDate.substring(6)) + parseInt(days) - 1; // 기준일 포함

    date.setFullYear(y, m-1, d);

    return dateToString(date);
}

// 두 날짜간의 간격 계산
function diffDays(startDate, endDate) {
	if(! isValidDate(startDate) || ! isValidDate(endDate)) {
		throw '날짜 형식이 올바르지 않습니다.';
	}
	
    let regexp = /(\.)|(\-)|(\/)/g;
    startDate = startDate.replace(regexp, '');
    endDate = endDate.replace(regexp, '');
    
    let sy = parseInt(startDate.substring(0, 4));
    let sm = parseInt(startDate.substring(4, 6));
    let sd = parseInt(startDate.substring(6));
    
    let ey = parseInt(endDate.substring(0, 4));
    let em = parseInt(endDate.substring(4, 6));
    let ed = parseInt(endDate.substring(6));

    let date1 = new Date(sy, sm-1, sd);
    let date2 = new Date(ey, em-1, ed);
    
    let sn = date1.getTime();
    let en = date2.getTime();
    let dif = en-sn;
    let day = Math.floor(dif/(24*3600*1000));
    
    return day;
    // return day + 1; // 시작일 포함
}

// 나이 계산
function toAge(data) {
	if(! isValidDate(data)) {
		throw '날짜 형식이 올바르지 않습니다.';
	}
	
    let regexp=/(\.)|(\-)|(\/)/g;
    data = data.replace(regexp, '');
    
    let by = parseInt(data.substring(0, 4));
    let bm = parseInt(data.substring(4, 6));
    let bd = parseInt(data.substring(6));
    let bdate = new Date(by, bm-1, bd);
    let now = new Date();
    
    let age = now.getFullYear() - bdate.getFullYear();
    if((bdate.getMonth() > now.getMonth()) ||
    		((bdate.getMonth() === now.getMonth()) && bdate.getDate() > now.getDate())) {
        age--;
    }
    
    return age;
}

// 주민등록 번호 검사
function isValidResidentNO(ssn1, ssn2) {
	const days = [31, 0, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
	const check = [2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5];
        
	let ssn = [];
	let temp, year, month, day, tot, chkNum;

	if((ssn1.length !== 6) || (ssn2.length !== 7)) {
		return false;
	}

	for(let i = 0; i < 13; i++) {
		ssn[i] = i < 6 ? parseInt(ssn1.charAt(i)) : parseInt(ssn2.charAt(i-6)); 
	}

	temp = ssn1.substring(0, 2);
	if(temp.charAt(0) === '0') {
		temp = temp.charAt(1);
	}
	year = parseInt(temp);

	if(ssn[6] === 1 || ssn[6] === 2 || ssn[6] === 5 || ssn[6] === 6) {
		year = year + 1900;
	} else {
		year = year + 2000;
	}

	temp = ssn1.substring(2, 4);
	if(temp.charAt(0) === '0') {
		temp = temp.charAt(1);
	}
	month = parseInt(temp);

	if(ssn[6] < 1 || ssn[6] > 4) {
		return false;
	}
   
	temp = ssn1.substring(4, 6);
	if(temp.charAt(0) === '0') {
		temp = temp.charAt(1);
	}
	day = parseInt(temp);

	days[1] = year%4 === 0 && year%100 !== 0 || year%400 === 0 ? 29 : 28; 

	if(month < 1 || month > 12) {
		return false;
	}

	if(day > days[month-1] || day < 1) {
		return false;
	}

	tot = 0;
	for(i = 0; i < 12; i++) {
		tot = tot + ssn[i] * check[i];
	}
	chkNum = 11 - tot % 11;
	chkNum = chkNum % 10;
  
	if(chkNum !== ssn[12]) {
		return false;
	}
        
	return true;
}
