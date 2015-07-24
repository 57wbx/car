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
		
		sql.append("select FID,FNumber,FLongNumber,FName from t_org_admin").append(RT);
		
		PreparedStatement pstmt = conn.prepareStatement(sql.toString());
		ResultSet rs = pstmt.executeQuery();
		int i = 0;
		conn.setAutoCommit(false);
		try
		{
			while (rs.next()) {
				String id = rs.getString("FID");
				String longNumber = rs.getString("FLongNumber");
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
				updateSql.append("update t_org_admin set FLongName ='").append(longName).append("' where fid ='").append(id).append("'");
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
		sql.append("select FName from t_org_admin where fnumber='").append(number).append("'").append(RT);
		
		PreparedStatement pstmt = conn.prepareStatement(sql.toString());
	    ResultSet rs = pstmt.executeQuery();
	    if (rs.next())
	    {
	    	name = rs.getString("FName");
	    }
	    rs.close();
	    pstmt.close();	    
		
	    return name;
	}	
}
