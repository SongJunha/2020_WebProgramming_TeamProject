package com.webprogramming.project;
import java.sql.*;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DatabaseManager {
	
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private String dbURL = "";
	private String dbID = "";
	private String dbPW = "";
	
	public DatabaseManager() {
		dbURL = "jdbc:mysql://conative.myds.me:8888/HTC_Cafe?serverTimezone=UTC&characterEncoding=utf8";
		dbID = "root";
		dbPW = "qwerty12";
	}
	
	public String LoadFAQ() {
		
		JSONArray Jarray = new JSONArray();
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPW);
			pstmt = conn.prepareStatement("SELECT * FROM Faq");
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				JSONObject obj = new JSONObject();
				obj.put("question", rs.getString("question"));
				obj.put("answer", rs.getString("answer"));
				Jarray.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Jarray.toString();
	}
	
	public String LoadNotice() {
		JSONArray Jarray = new JSONArray();
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPW);
			pstmt = conn.prepareStatement("SELECT * FROM Notice");
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				JSONObject obj = new JSONObject();
				obj.put("nid", rs.getString("nid"));
				obj.put("title", rs.getString("title"));
				obj.put("content", rs.getString("content"));
				obj.put("photo", rs.getString("photo"));
				obj.put("date", rs.getString("date"));
				obj.put("views", rs.getString("views"));
				Jarray.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Jarray.toString();
	}
	public int login(String userID, String userPW) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPW);
			pstmt = conn.prepareStatement("SELECT userPw FROM Userinfo WHERE userId=?");
			pstmt.setString(1, userID);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				if(rs.getString(1).equals(userPW)) {
					return 1; //성공
				}else {
					return 0; //불일치
				}
			}else {
				return -1; //아이디x
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -2;
	}
	public int register(DB_DTO db_dto) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPW);
			pstmt = conn.prepareStatement("INSERT INTO Userinfo(userName,userId,userPw,userEmail,userPhone,rank,couponNum) VALUES (?,?,?,?,?,'bronze',0)");
			pstmt.setString(1, db_dto.getUserName());
			pstmt.setString(2, db_dto.getUserId());
			pstmt.setString(3, db_dto.getUserPw());
			pstmt.setString(4, db_dto.getUserEmail());
			pstmt.setString(5, db_dto.getUserPhone());
			PreparedStatement pstmt_2 = conn.prepareStatement("SELECT userId FROM Userinfo WHERE userId=?");
			pstmt_2.setString(1, db_dto.getUserId());
			rs=pstmt_2.executeQuery();
			if(rs.next()) {
				return -2;//id 존재
			}else {
				return pstmt.executeUpdate();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;//db오류
	}
	public String searchId(DB_DTO db_dto) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPW);
			pstmt = conn.prepareStatement("SELECT userId FROM Userinfo WHERE userEmail=?");
			pstmt.setString(1, db_dto.getUserEmail());
			rs=pstmt.executeQuery();
			String result=" | ";
			if(rs.next()) {
				result=result+rs.getString(1)+" | ";
				while(rs.next()) {
					result=result+rs.getString(1)+" | ";
				}
			}else {
				return "-2"; //email x
			}
			return result; //성공
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "-1";//db오류
	}
	public String searchPw(DB_DTO db_dto) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPW);
			pstmt = conn.prepareStatement("SELECT userPw FROM Userinfo WHERE userId=?");
			pstmt.setString(1, db_dto.getUserId());
			rs=pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1); //성공
			}else {
				return "-1"; //없는 Id
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "-2";//db오류
	}
}
