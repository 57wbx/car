package com.hhxh.car.sys.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.annotation.AuthCheck;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.hhxh.car.permission.domain.MainMenuItem;
import com.hhxh.car.permission.domain.PermItem;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 权限子项的维护action
 * @author zw
 * @date 2015年8月28日 上午10:03:53
 *
 */
public class PermItemAction extends BaseAction implements ModelDriven<PermItem>
{
	private PermItem permItem ;

	/**
	 * 按钮级的菜单路径条件，列如uiClass=app.carowner.list 或者 app.carower 或者app.carower.edit
	 * uiClass
	 */
	
	/**
	 * 获取一个权限子项的详细信息
	 */
	@AuthCheck
	public void detailsPermItem(){
		try
		{
			if (isNotEmpty(this.permItem.getId()))
			{
				permItem = this.baseService.get(PermItem.class,this.permItem.getId());
				if (permItem != null)
				{
					jsonObject.accumulate("details", permItem,this.getJsonConfig(JsonValueFilterConfig.Sys.Menu.PERMITEM_ONLY_PERMITEM));
					this.putJson();
				} else
				{
					this.putJson(false, this.getMessageFromConfig("menu_errorId"));
				}
			} else
			{
				this.putJson(false, this.getMessageFromConfig("menu_needId"));
			}
		} catch (Exception e)
		{
			log.error("查询一个权限子项的详细信息失败", e);
			this.putJson(false, this.getMessageFromConfig("menu_error"));
		}
	}
	
	/**
	 * 获取一个菜单下，一个用户所在的权限所具有的全部按钮权限
	 * 用户级按钮权限就在这个方法下面进行
	 */
	@AuthCheck
	public void listRoleBtn(){
		try{
			if(isNotEmpty(this.permItem.getUiClass())){
				String sql = "SELECT DISTINCT b.FNUMBER FROM sys_role_menu a "
										+ "LEFT JOIN sys_menu_permitem b ON a.permitemID = b.fid "
										+ "LEFT JOIN sys_menu c ON b.FPARENTID = c.menuID "
										+ "WHERE c.uiClassName LIKE :uiClass "
										+ " AND a.roleID = :roleId AND b.useState <> :useState";
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("uiClass", this.permItem.getUiClass()+"%");
				paramMap.put("roleId", this.getLoginUser().getRole().getId());
				paramMap.put("useState", PermItemState.USESTATE_STOP);
				List results = this.baseService.querySql(sql, paramMap);
				Map<String,Object> resultMap  = null ;
				if(results!=null && results.size()>0){
					resultMap = new HashMap<String,Object>();
					for(Object str : results ){
						resultMap.put(str.toString(), true);
					}
				}
				jsonObject.put("data", resultMap);
				this.putJson();
			}else{
				this.putJson(false, this.getMessageFromConfig("btn_needUiClass"));
			}
			
		}catch(Exception e){
			log.error("查询一个用户在一个菜单下具有的按钮权限出错", e);
			this.putJson(false, this.getMessageFromConfig("menu_error"));
		}
	}
	
	@Override
	public PermItem getModel()
	{
		this.permItem = new PermItem();
		return this.permItem;
	}

}
