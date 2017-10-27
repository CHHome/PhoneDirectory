package com.phone.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class DbController {
	private String url = "jdbc:mysql://localhost:3306/phone"; 
	private String username = "root";
	private String password = "kissme95838aa";
	private Connection conn = null;
	private Statement sqlStm = null;
	
	public DbController() throws SQLException{
		try {
			conn = java.sql.DriverManager.getConnection(url, username, password);
			System.out.println("连接数据库成功");
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("连接数据库失败");
			e.printStackTrace();
			
		}
	}
	public ResultSet select(String col, String value){
		String sql;
		if(col == null)
		     sql = "select * from contacts order by name";
		else
			sql =  "select * from contacts where "+col+"='"+value+"' order by name";
		try {
			sqlStm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet sqlRet = sqlStm.executeQuery(sql);
			return sqlRet;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return null;
	}
	public Boolean delete(String id){
		String sql = "delete from contacts where id="+id;
		try {
			sqlStm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			sqlStm.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	public Boolean update(String id, String name, String description, String mobilenumber, String homenumber,String address){
		String sql = "update contacts set name='"+name+"',description='"+description+
				"',mobilenumber='"+mobilenumber+"',homenumber='"+homenumber+
				"',address='"+address+"' where id="+id;
		try {
			sqlStm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			sqlStm.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public Boolean insert(String name, String description, String mobilenumber, String homenumber,String address){
		String sql = "insert into contacts (name,description,mobilenumber,homenumber,address)"
				+" values ('"+name+"','"+description+"','"+mobilenumber+"','"+homenumber+"','"+address+"')";
		try {
			sqlStm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			sqlStm.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
