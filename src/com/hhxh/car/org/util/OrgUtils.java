package com.hhxh.car.org.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.lang.StringUtils;

public class OrgUtils
{
	private final static String RT = "\r\n";
	
	public static void updateOrgLongName(Connection conn) throws Exception
	{
		StringBuffer sql = new StringBuffer("");
		
		sql.append("select orgid,curCode,orgCode,name from sys_org").append(RT);
		
		PreparedStatement pstmt = conn.prepareStatement(sql.toString());
		ResultSet rs = pstmt.executeQuery();
		int i = 0;
		conn.setAutoCommit(false);
		try
		{
			while (rs.next()) {
				String id = rs.getString("orgid");
				String longNumber = rs.getString("orgcode");
				if (StringUtils.isEmpty(longNumber))
				{
					continue;
				}
				
				longNumber = longNumber +"!";
				String longName = "";		
				while(longNumber.indexOf("!") > 0)
				{
					int startIndex = 0;
					int endIndex = longNumber.indexOf("!");
					String number = longNumber.substring(startIndex,endIndex);
					if (StringUtils.isNotEmpty(number))
					{
						String name = getNameByNumber(conn,number);
						if (StringUtils.isEmpty(longName))
						{
							longName = name;
						}
						else 
						{
							longName = longName +"/"+name;
						}
					}
					longNumber = longNumber.substring(endIndex + 1);
				}
				
				//01!04!04.03!033!033.803!033.803.002
				
				StringBuffer updateSql = new StringBuffer("");
				updateSql.append("update sys_org set orgCode ='").append(longName).append("' where orgid ='").append(id).append("'");
				PreparedStatement insertPstmt=null;
				try{				 
				insertPstmt = conn.prepareStatement(updateSql.toString());
				insertPstmt.executeUpdate();
				}catch(Exception e){
					e.printStackTrace();
				}
				conn.setAutoCommit(true);
				insertPstmt.close();
				i = i +1;
				System.out.println("dddd:"+i);
			}	
			conn.commit();
			conn.setAutoCommit(true);
		}
		catch(Exception e)
		{
			conn.setAutoCommit(false);			
		}
	}
	
	private static String getNameByNumber(Connection conn,String number) throws Exception
	{
		String name = "";
		StringBuffer sql = new StringBuffer("");		
		sql.append("select name from sys_org where curCode='").append(number).append("'").append(RT);
		
		PreparedStatement pstmt = conn.prepareStatement(sql.toString());
	    ResultSet rs = pstmt.executeQuery();
	    if (rs.next())
	    {
	    	name = rs.getString("name");
	    }
	    rs.close();
	    pstmt.close();	    
		
	    return name;
	}	
}
