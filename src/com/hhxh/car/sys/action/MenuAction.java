package com.hhxh.car.sys.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.util.ConvertObjectMapUtil;
import com.hhxh.car.common.util.CopyObjectUtil;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.hhxh.car.common.util.TypeTranslate;
import com.hhxh.car.permission.domain.MainMenuItem;
import com.hhxh.car.permission.domain.PermItem;
import com.hhxh.car.sys.service.MenuService;
import com.hhxh.car.sys.state.MenuState;
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
	 * 用来接受从页面端上传的action级菜单
	 */
	private String permItemJsonStr;
	/**
	 * 上级菜单
	 */
	private String parentId;

	@Resource
	private MenuService menuService;

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
				Map<String, Object> m = ConvertObjectMapUtil.convertObjectToMap(menu, JsonValueFilterConfig.Sys.Menu.MENU_ONLY_MENU);
				m.put("parentId", menu.getParent() == null ? "" : menu.getParent().getId());
				menusMapList.add(m);
			}
			this.jsonObject.accumulate("data", menusMapList, this.getJsonConfig(JsonValueFilterConfig.Sys.Menu.MENU_ONLY_MENU));
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
					this.jsonObject.accumulate("details", menu, this.getJsonConfig(JsonValueFilterConfig.Sys.Menu.MENU_ONLY_MENU));
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

	public void listPermItemByMenuId()
	{
		try
		{
			if (isNotEmpty(this.menu.getId()))
			{
				menu = this.baseService.get(MainMenuItem.class, this.menu.getId());
				if (menu != null)
				{
					this.jsonObject.accumulate("data", menu.getPermItems(), this.getJsonConfig(JsonValueFilterConfig.Sys.Menu.PERMITEM_ONLY_PERMITEM));
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
			log.error("查询一个菜单的操作子项失败", e);
			this.putJson(false, this.getMessageFromConfig("menu_error"));
		}
	}

	/**
	 * 新增或者修改一个菜单信息
	 */
	public void addOrUpdateMenu()
	{
		try
		{
			if (isNotEmpty(this.menu.getId()))
			{
				MainMenuItem needUpdateMenu = this.baseService.get(MainMenuItem.class, this.menu.getId());
				if (needUpdateMenu != null)
				{
					CopyObjectUtil.copyValueToObject(menu, needUpdateMenu, MenuState.DONT_NEED_UPDATE_PROPERTISE);
					this.menuService.addOrUpdateMenuWithPermItems(needUpdateMenu, this.parsePermItemJsonStrToList(permItemJsonStr));
					this.putJson();
				} else
				{
					this.putJson(false, this.getMessageFromConfig("menu_errorId"));
				}
			} else
			{
				if (isNotEmpty(this.parentId))
				{
					// 新增权限的菜单
					MainMenuItem newMenu = new MainMenuItem();
					CopyObjectUtil.copyValueToObject(menu, newMenu, MenuState.DONT_NEED_UPDATE_PROPERTISE);
					newMenu.setParent(new MainMenuItem(parentId));
					this.menuService.addOrUpdateMenuWithPermItems(newMenu, this.parsePermItemJsonStrToList(permItemJsonStr));
					this.putJson();
				} else
				{
					this.putJson(false, this.getMessageFromConfig("menu_needId"));
				}
			}
		} catch (Exception e)
		{
			log.error("新增或修改一个菜单失败", e);
			this.putJson(false, this.getMessageFromConfig("menu_error"));
		}
	}

	private List<PermItem> parsePermItemJsonStrToList(String str)
	{
		if (isNotEmpty(str))
		{
			JSONArray permItemArray = JSONArray.fromObject(str);
			if (permItemArray != null && permItemArray.size() > 0)
			{
				// 返回的list数据
				List<PermItem> permItems = new ArrayList<PermItem>();

				for (int i = 0; i < permItemArray.size(); i++)
				{
					JSONObject permItemJsonObject = permItemArray.getJSONObject(i);
					if (permItemJsonObject != null)
					{
						PermItem action = new PermItem();
						action.setId((String)permItemJsonObject.get("id"));
						action.setAction((String)permItemJsonObject.get("action"));
						action.setfType(TypeTranslate.getObjectInteger(permItemJsonObject.getInt("fType")));
						action.setName((String)permItemJsonObject.get("name"));
						action.setNumber((String)permItemJsonObject.get("number"));
						action.setUiClass((String)permItemJsonObject.get("uiClass"));
						action.setUseState(TypeTranslate.getObjectInteger(permItemJsonObject.getInt("useState")));

						permItems.add(action);
					}
				}
				return permItems;
			}
		}
		return null;
	}

	@Override
	public MainMenuItem getModel()
	{
		this.menu = new MainMenuItem();
		return this.menu;
	}

	public String getPermItemJsonStr()
	{
		return permItemJsonStr;
	}

	public void setPermItemJsonStr(String permItemJsonStr)
	{
		this.permItemJsonStr = permItemJsonStr;
	}

	public String getParentId()
	{
		return parentId;
	}

	public void setParentId(String parentId)
	{
		this.parentId = parentId;
	}
}
