package com.sist.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/*
- 오라클 서버에 접속하기 위해 필요한 사항
 1) 호스트이름 : 오라클 서버가 설치된 서버 IP 주소
    localhost 또는 127.0.0.1 : 자기자신의 컴퓨터에 오라클이 설치된 경우
 2) 포트번호 : 기본값으로 설치한 경우 1521
 3) SID
    Express Edition 기본 SID : XE
    Enterprise Edition 기본 SID : ORCL
 4) 사용자 이름 및 비밀번호 
*/
public class DBConn {
	private static Connection conn = null;

	private DBConn() {
	}
	
	public static Connection getConnection() {
		// String url = "jdbc:oracle:thin:@127.0.0.1:1521:ORCL";  // 11g
		String url = "jdbc:oracle:thin:@//127.0.0.1:1521/XE"; // 12C 이상
		String user = "sistime";
		String pwd = "java$!";
		
		if(conn == null) {
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				conn = DriverManager.getConnection(url, user, pwd);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return conn;
	}

	public static Connection getConnection(String url, String user, String pwd) {
		if(conn == null) {
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				conn = DriverManager.getConnection(url, user, pwd);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return conn;
	}

	public static Connection getConnection(String url, String user, String pwd, String internal_logon) {
		if(conn == null) {
			try {
				Properties info = new Properties();
				info.put("user", user);
				info.put("password", pwd);
				info.put("internal_logon", internal_logon);  // sysdba, Normal 등 roll
				Class.forName("oracle.jdbc.driver.OracleDriver");
				conn = DriverManager.getConnection(url, info);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return conn;
	}
	
	public static void close() {
		if(conn == null) {
			return;
		}
		
		// isClosed() : close() 메소드를 호출한 경우에만 true
		try {
			if(! conn.isClosed()) {
				conn.close();
			}
		} catch (Exception e) {
		}
		
		conn = null;
	}
}
