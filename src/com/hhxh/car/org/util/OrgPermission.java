package com.hhxh.car.org.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hhxh.car.common.service.BaseService;

public class OrgPermission {
	private final static String RT = "\r\n";
	
	public static String insertOrgPerm(Connection conn,String userIDs) throws Exception {
		long start = System.currentTimeMillis();
		String table =  insertDataTable(conn,userIDs);
		
		return table;
	}
				
	public static String insertDataTable(Connection conn,String userIDs) throws Exception
	{
		String tempTableName = "T_Org_PermOrg";
		ResultSet resultRS = null;		
		try {
			conn.setAutoCommit(false);		
			
			StringBuffer deleteSql = new StringBuffer("");
			deleteSql.append("delete from T_Org_PermOrg");
			if (StringUtils.isNotEmpty(userIDs))
			{
				deleteSql.append(" where FUserID in ").append(userIDs);
			}						
			PreparedStatement pstmt = conn.prepareStatement(deleteSql.toString());
			pstmt.execute();
			pstmt.close();
			
			String orgSql = getPermissionOrgSQL(userIDs);
			pstmt = conn.prepareStatement(orgSql.toString());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String orgUnitID = rs.getString("FOrgUnitID");
				String orgUnitNumber = rs.getString("FOrgUnitNumber");
				String parentOrgUnitID = rs.getString("FParentOrgUnitID");
				String everyUserID = rs.getString("FUserID");	
				
				StringBuffer insertSql = new StringBuffer("");
				insertSql.append("insert into ").append(tempTableName).append("(FUSerID,FOrgUnitID,FOrgUnitName,FParentOrgUnitID,FNumber,FLongNumber,FLevel,FIsLeaf,FHasPermission)").append(RT);
				insertSql.append("select '").append(everyUserID).append("', FID,FName,FParent,FNumber,FLongNumber,FLevel,FIsLeaf,1  from T_Org_Admin where FLongNumber like '").append(orgUnitNumber).append("%'").append(RT);
				insertSql.append(" and CONCAT('").append(everyUserID).append("',fid) not in (select CONCAT(FUserID,FOrgUnitID) from ").append(tempTableName).append(")").append(RT);
								 
				PreparedStatement insertPstmt = conn.prepareStatement(insertSql.toString());
				insertPstmt.execute();
				insertPstmt.close();

				addParentOrgUnit(conn, tempTableName, parentOrgUnitID,everyUserID);
			}
			rs.close();
			pstmt.close();
				
			StringBuffer updateSql = new StringBuffer("");
			updateSql.append(" update ").append(tempTableName).append(" orgUnitTbl ").append(RT);
			updateSql.append(" set  FParentLongNumber = ").append(RT);
			updateSql.append(" (select orgAdmin.FLongNumber from T_Org_Admin orgAdmin where orgUnitTbl.FParentOrgUnitID = orgAdmin.FID )").append(RT);
			
			PreparedStatement updatePstmt = conn.prepareStatement(updateSql.toString());
			updatePstmt.executeUpdate();
			updatePstmt.close();
				
			conn.commit();	
			conn.setAutoCommit(true);		
		} catch (Exception e) 
		{		
			try 
			{
				conn.rollback();
				conn.setAutoCommit(true);		
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} 
		
		return tempTableName;
	}
	
	private static String getPermissionOrgSQL(String userIDs) {
		StringBuffer sql = new StringBuffer("");
		sql.append("select ").append(RT);
		sql.append("pmUser.FID FUserID, ").append(RT);		
		sql.append("pmUser.FDefOrgUnitID FOrgUnitID,").append(RT);
		sql.append("orgAdmin.FLongNumber FOrgUnitNumber,").append(RT);
		sql.append("orgAdmin.Fparent FParentOrgUnitID").append(RT);
		sql.append("from T_PM_User pmUser").append(RT);
		sql.append("inner join T_PM_USERROLEORG userRole").append(RT);
		sql.append("on pmUser.FID = userRole.FUserID").append(RT);
		sql.append("inner join T_PM_Role pmRole").append(RT);
		sql.append("on userRole.FRoleID = pmRole.FID").append(RT);
		sql.append("inner join T_ORG_Admin orgAdmin").append(RT);
		sql.append("on pmUser.FDefOrgUnitID = orgAdmin.FID").append(RT);
		sql.append("where pmRole.FIsExtendOrg = 1").append(RT);
		if (StringUtils.isNotEmpty(userIDs))
		{
			sql.append("and pmUser.FID in ").append(userIDs).append(RT);
		}

		sql.append("union").append(RT);

		sql.append("select ").append(RT);
		sql.append("orgRange.FUserID FUserID, ").append(RT);		
		sql.append("ForgID FOrgUnitID,").append(RT);
		sql.append("orgAdmin.FLongNumber FOrgUnitNumber,").append(RT);
		sql.append("orgAdmin.Fparent FParentOrgUnitID").append(RT);
		sql.append("from T_PM_ORGRANGEINCLUDESUBORG orgRange").append(RT);
		sql.append("inner join T_ORG_Admin orgAdmin").append(RT);
		sql.append("on orgRange.ForgID = orgAdmin.FID ").append(RT);
		if (StringUtils.isNotEmpty(userIDs))
		{
			sql.append("where orgRange.FUserID in ").append(userIDs).append(RT);
		}

		return sql.toString();
	}

	private static void addParentOrgUnit(Connection conn, String tempTableName, String parentOrgUnitID,String userID) throws Exception {
		if (StringUtils.isEmpty(parentOrgUnitID)) {
			return;
		}
		
		StringBuffer insertSql = new StringBuffer("");
		insertSql.append("insert into ").append(tempTableName).append("(FUserID,FOrgUnitID,FOrgUnitName,FParentOrgUnitID,FNumber,FLongNumber,FLevel,FIsLeaf,FHasPermission)").append(RT);
		insertSql.append("select '").append(userID).append("', FID,FName,FParent,FNumber,FLongNumber,FLevel,FIsLeaf,0  from T_Org_Admin where FID =  '").append(parentOrgUnitID).append("'").append(RT);
		insertSql.append(" and CONCAT('").append(userID).append("',fid) not in (select CONCAT(FUserID,FOrgUnitID) from ").append(tempTableName).append(")");

		PreparedStatement pstmt = conn.prepareStatement(insertSql.toString());
		pstmt.execute();
		pstmt.close();

		StringBuffer sql = new StringBuffer("");
		sql.append("select FParent from T_Org_Admin where FID = '").append(parentOrgUnitID).append("'").append(RT);
		pstmt = conn.prepareStatement(sql.toString());
		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
			String parentOrgID = rs.getString("FParent");
			addParentOrgUnit(conn, tempTableName, parentOrgID,userID);
		}
		rs.close();
		pstmt.close();
	}

	private static String buildTempTableNew() {
		Date nowDate = new Date();

		DateFormat df = new SimpleDateFormat("yyMMddHHmmsssssssss");

		return "tbl_org" + df.format(nowDate);
	}


	public static void dropTable(BaseService baseservice, String tableName) {
		System.out.println("删除表session"+tableName);
		StringBuffer existTable = new StringBuffer("");
		existTable.append("SELECT * FROM  User_Tables WHERE table_name = '").append(tableName).append("'");
		List list = baseservice.querySql(existTable.toString());
		if (list.size() >0)
		{
			baseservice.executeSqlUpdate("drop table " + tableName);
		}
	}
}
