package com.hhxh.car.sys.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.util.ConvertObjectMapUtil;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.hhxh.car.permission.domain.MainMenuItem;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 菜单的管理action 实现菜单的维护
 * 
 * @author zw
 * @date 2015年8月19日 下午3:56:31
 *
 */
public class MenuAction extends BaseAction implements ModelDriven<MainMenuItem>
{
	private MainMenuItem menu;

	/**
	 * 列出所有的菜单。格式为tree形式的
	 */
	public void listMenu()
	{
		try
		{
			List<MainMenuItem> menus = this.baseService.gets(MainMenuItem.class);
			List<Map<String, Object>> menusMapList = new ArrayList<Map<String, Object>>();
			for (MainMenuItem menu : menus)
			{
				Map<String, Object> m = ConvertObjectMapUtil.convertObjectToMap(menu, JsonValueFilterConfig.MENU_ONLY_MENU);
				m.put("parentId", menu.getParent() == null ? "" : menu.getParent().getId());
				menusMapList.add(m);
			}
			this.jsonObject.accumulate("data", menusMapList, this.getJsonConfig(JsonValueFilterConfig.MENU_ONLY_MENU));
			this.putJson();
		} catch (Exception e)
		{
			log.error("获取所有的菜单失败", e);
			this.putJson(false, this.getMessageFromConfig("menu_error"));
		}
	}

	/**
	 * 获取一个菜单详细信息
	 */
	public void detailsMenu()
	{
		try
		{
			if (isNotEmpty(this.menu.getId()))
			{
				menu = this.baseService.get(MainMenuItem.class, this.menu.getId());
				if (menu != null)
				{
					this.jsonObject.accumulate("details", menu, this.getJsonConfig(JsonValueFilterConfig.MENU_ONLY_MENU));
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
			log.error("查询一个菜单的详细信息失败", e);
			this.putJson(false, this.getMessageFromConfig("menu_error"));
		}
	}

	@Override
	public MainMenuItem getModel()
	{
		this.menu = new MainMenuItem();
		return this.menu;
	}

}
