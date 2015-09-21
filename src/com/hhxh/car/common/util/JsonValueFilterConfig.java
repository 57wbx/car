package com.hhxh.car.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;



/**
 * 提供一系列的静态对象，给jsonconfig中的string数组使用。 排除无限循环的意外。
 * 目的：因为domain可能会经常变化，其中的关系可能一会发生变化，如果在每个action中都定义一个String数组，维护起来比较麻烦，
 * 所以将string数组都放在该config类中提供一个修改的的地方。 注意：之前有些模块已经做完了，并没有使用该规则
 * 
 * @author zw
 * @date 2015年8月3日 上午11:54:01
 *
 */
public class JsonValueFilterConfig
{
	private JsonValueFilterConfig()
	{
	};// 私有化构造方法

	/**
	 * 将多个字符串数组相结合，组合成一个数组，相同的字符数组可以同时存在
	 * 
	 * @param args
	 * @return 如果没有数据将返回 null
	 */
	private static String[] CombineStringArrayHasSame(String[]... args)
	{
		if (args == null || args.length <= 0)
		{// 没有参数时返回null
			return null;
		}
		List<String> list = new ArrayList<String>();
		for (String[] strs : args)
		{
			if (strs != null && strs.length > 0)
			{
				// 遍历子数组
				for (String str : strs)
				{
					list.add(str);
				}
			}
		}

		if (list.size() > 0)
		{
			return list.toArray(new String[]{});
		}
		return null;
	}

	/**
	 * 将多个字符串数组相结合，组合成一个数组，相同的字符数组可以不可以同时存在
	 * 
	 * @param args
	 * @return 如果没有数据将返回 null
	 */
	private static String[] CombineStringArrayNoSame(String[]... args)
	{
		String[] hasSameStringArray = CombineStringArrayHasSame(args);
		return (String[]) new HashSet<String>(Arrays.asList(hasSameStringArray)).toArray(new String[]{});
	}

	/**
	 * hibernate懒加载需要过滤的数据
	 */
	public static final String HIBERNATE_LAZY_PROPERTISE_HANDLER = "handler";
	public static final String HIBERNATE_LAZY_PROPERTISE_LAZYINITIALIZER = "hibernateLazyInitializer";

	public static class Base
	{
		private Base()
		{
		}

		public static class BusPackage
		{
			private BusPackage()
			{
			};

			/**
			 * buspackageImg 平台套餐需要过滤的数据
			 */
			public static final String[] BUSPACKAGEIMG_ONLY_BUSPACKAGEIMG = new String[] { "busPackage", "user" };

			/**
			 * buspackage 平台套餐需要过滤的数据
			 */
			public static final String[] BASEPACKAGE_HAS_BUSPACKAGEIMG = new String[] { "busItems", "user" };

			/**
			 * buspackage 平台套餐需要过滤的数据
			 */
			public static final String[] BASEPACKAGE_HAS_BUSITEMS = new String[] { "busPackages", "busAtoms", "busItemImgs", "busPackageImgs" };

		}

		public static class BusItem
		{
			private BusItem()
			{
			}

			/**
			 * busitem 服务项需要排除的数据
			 */
			public static final String[] BASEITEM_ONLY_BASEITEM = new String[] { "busPackages", "busAtoms", "busItem", "busItemImgs" };
			public static final String[] BASEITEM_HAS_BASEITEMIMG = new String[] { "busPackages", "busAtoms", "busItem", "user" };

			/**
			 * busitemImg 服务项图片需要排除的数据
			 */
			public static final String[] BASEITEMIMG_ONLY_BASEITEMIMG = new String[] { "busItem", "user" };

		}

		public static class District
		{
			private District()
			{
			}

			/**
			 * baseArea 的需要在json中排除的数据 ，提供一个只有baseArea的对象
			 */
			public static final String[] BASEAREA_ONLY_BASEAREA = new String[] { "baseCity", "baseAreas", "baseAreaParent" };

			/**
			 * basecity 城市中 中需要排除的数据
			 */
			public static final String[] BASECITY_ONLY_BASECITY = new String[] { "baseProvince", "baseAreas" };

		}

		public static class BusAtom
		{
			private BusAtom()
			{
			}

			/**
			 * busatom 平台服务子项需要过滤的数据
			 */
			public static final String[] BASEATOM_HAS_BUSITEM = new String[] { "busPackages", "busAtoms", "busItemImgs" };

		}

		public static class CarShop
		{
			private CarShop()
			{
			}

			/**
			 * carshop 网店需要过滤的数据
			 */
			public static final String[] CARSHOP_ONLY_CARSHOP = new String[] { "org", "user", "merbers", "orders", "shopBlackList" };
			public static final String[] CARSHOP_HAS_ORG = new String[] { "user", "createUser", "lastUpdateUser", "parent", "merbers", "orders", "shopBlackList" };
			// 门店黑名单
			public static final String[] SHOPBLACKLIST_ONLY_BLACKLIST = new String[] { "carShop", "createUser" };
		}

		public static class Member
		{
			private Member()
			{
			}

			/**
			 * 师傅或者会员只能有小部分数据显示到其他的模块
			 */
			public static final String[] MEMBER_ONLY_PRIMARY_PROPERTIESE = new String[] { "MCARDURL", "registerDate", "IDCARDNO", "IDCARDURL", "auditState", "useState", "VIPtime", "VIPlevel",
					"integration", "email", "qq", "weixin", "address", "birthday", "folk", "gender", "merriageState", "updateTime", "memo", "carShop", "orders", "workerBlackList" };
			/**
			 * Member 会员或者师傅 需要过滤的信息
			 */
			public static final String[] MEMBER_ONLY_MEMBER = new String[] { "carShop", "orders", "workerBlackList" };
			// 师傅 黑名单
			public static final String[] WORKERBLACKLIST_ONLY_WORKERBLACKLIST = new String[] { "worker", "createUser" };

		}

		public static class Car
		{
			private Car()
			{
			}

			/**
			 * car 车型需要处理的过滤数据
			 */
			public static final String[] CAR_ONLY_CAR = new String[] { HIBERNATE_LAZY_PROPERTISE_HANDLER, HIBERNATE_LAZY_PROPERTISE_LAZYINITIALIZER };

		}

		public static class CarName
		{
			private CarName()
			{
			}

			/**
			 * carName 车品牌需要过滤的数据
			 */
			public static final String[] CARNAME_ONLY_CARNAME = new String[] { HIBERNATE_LAZY_PROPERTISE_HANDLER, HIBERNATE_LAZY_PROPERTISE_LAZYINITIALIZER };

		}

	}

	public static class Opr
	{
		private Opr()
		{
		}

		public static class Complain
		{
			private Complain()
			{
			}

			/**
			 * Complain 投诉表需要过滤的数据
			 */
			public static final String[] COMPLAINS_HAS_COMPLAINUSER_HAS_DEALUSER = new String[] { "createUser", "lastModifyUser", "person", "role", "rootOrgUnit", "carShop", "password", "orders",
					"user_pw", HIBERNATE_LAZY_PROPERTISE_HANDLER, HIBERNATE_LAZY_PROPERTISE_LAZYINITIALIZER };
			public static final String[] COMPLAIN_ONLY_COMPLAIN = new String[] { "dealUser", "complainUser" };

		}

		public static class HotWord
		{
			private HotWord()
			{
			}

			/**
			 * hotword 热门词汇需要过滤的数据
			 */
			public static final String[] HOTWORD_ONLY_HOTWORD = new String[] { HIBERNATE_LAZY_PROPERTISE_HANDLER, HIBERNATE_LAZY_PROPERTISE_LAZYINITIALIZER };

		}

		public static class Order
		{
			private Order()
			{
			}

			/**
			 * Order 订单需要过滤的信息
			 */
			public static final String[] ORDER_HAS_TIGUSERS_HAS_WORKER = (String[]) CombineStringArrayNoSame(new String[] { "carShop", "orders", "order", "orderTracks", "workerBlackList", "user_pw",
					"user_account", HIBERNATE_LAZY_PROPERTISE_HANDLER, HIBERNATE_LAZY_PROPERTISE_LAZYINITIALIZER }, Permission.User.USER_ONLY_PRIMARY_PROPERTIESE,Base.Member.MEMBER_ONLY_PRIMARY_PROPERTIESE);

			/**
			 * OrderTrack 订单跟踪状态需要过滤的信息
			 */
			public static final String[] ORDERTRACK_ONLY_ORDERTRACK = new String[] { "order" };

		}

	}

	public class Org
	{
		private Org()
		{
		}
	}

	public static class Permission
	{
		private Permission()
		{
		}

		public static class User
		{
			private User()
			{
			}

			/**
			 * 一般输出去给用户看的只有名字、电话、登陆账号 不重要的信息可以输出去，这里将排除重要的信息字段，例如：密码、身份证等一些信息
			 */
			public static final String[] USER_ONLY_PRIMARY_PROPERTIESE = new String[] { "userType", "adminOrgUnit", "password", "isEnable", "isLock", "description", "createTime", "lastModifyTime",
					"createUser", "lastModifyUser", "person", "role", "rootOrgUnit", "isAdministrator", "isOprUser", "carShop" };
		}
	}

	public static class Shop
	{
		private Shop()
		{
		}

		public static class ShopPackage
		{
			private ShopPackage()
			{
			}

			/**
			 * shopPackage 商家套餐
			 */
			public static final String[] SHOPPACKAGE_ONLY_SHOPPACKAGE = new String[] { "shopItems", "carShop", "shopPackageImgs" };
			public static final String[] SHOPPACKAGE_HAS_SHOPITEM = new String[] { "carShop", "shopPackages", "shopItemImgs", "shopAtoms", "shopPackageImgs" };
			public static final String[] SHOPPACKAGE_HAS_SHOPPACKAGEIMG = new String[] { "carShop", "shopPackages", "shopItemImgs", "shopItems", "shopAtoms", "user" };

			/**
			 * shopPackageImg 商家套餐需要过滤的数据
			 */
			public static final String[] SHOPPACKAGEIMG_ONLY_SHOPPACKAGEIMG = new String[] { "shopPackage", "user" };

		}

		public static class ShopItem
		{
			private ShopItem()
			{
			}

			/**
			 * shopitem 商家服务项需要过滤的数据
			 */
			public static final String[] SHOPITEM_ONLY_SHOPITEM = new String[] { "shopAtoms", "shopItem", "carShop", "shopPackages", "shopItemImgs" };
			public static final String[] SHOPITEM_HAS_SHOPITEMIMG = new String[] { "shopAtoms", "shopItem", "carShop", "shopPackages", "user" };

			/**
			 * shopItemImg 商家服务项图片需要过滤的数据
			 */
			public static final String[] SHOPITEMIMG_ONLY_SHOPITEMIMG = new String[] { "shopItem", "user" };
		}

		public static class ShopAtom
		{
			private ShopAtom()
			{
			}

			/**
			 * shopAtom 商家服务子项需要过滤的数据
			 */
			public static final String[] SHOPATOM_ONLY_SHOPATOM = new String[] { "autoPart", "shopItem", "carShop" };
			public static final String[] SHOPATOM_HAS_AUTOPART = new String[] { "shopItem", "carShop" };
		}

	}

	public static class Sys
	{
		private Sys()
		{
		}

		public static class Menu
		{
			private Menu()
			{
			}

			/**
			 * menu 菜单表需要处理的数据
			 */
			public static final String[] MENU_ONLY_MENU = new String[] { "parent", "permItems", HIBERNATE_LAZY_PROPERTISE_HANDLER, HIBERNATE_LAZY_PROPERTISE_LAZYINITIALIZER };

			public static final String[] PERMITEM_ONLY_PERMITEM = new String[] { "mainMenuItem", "roles", HIBERNATE_LAZY_PROPERTISE_HANDLER, HIBERNATE_LAZY_PROPERTISE_LAZYINITIALIZER };

		}

		public static class Log
		{
			private Log()
			{
			}

			/**
			 * LoginLog 登陆日志需要过滤的数据
			 */
			public static final String[] LOGINLOG_ONLY_LOGINLOG = new String[] { "user", HIBERNATE_LAZY_PROPERTISE_HANDLER, HIBERNATE_LAZY_PROPERTISE_LAZYINITIALIZER };
			/**
			 * operatorLog 操作日志需要过滤的数据
			 */
			public static final String[] OPERATORLOG_ONLY_OPERATORLOG = new String[] { "operator", HIBERNATE_LAZY_PROPERTISE_HANDLER, HIBERNATE_LAZY_PROPERTISE_LAZYINITIALIZER };

		}
	}

	public static class Tig
	{
		private Tig()
		{
		}

		public static class Advertisement
		{
			private Advertisement()
			{
			}

			/**
			 * advertisement 广告需要过滤的数据
			 */
			public static final String[] ADVERTISEMENT_ONLY_ADVERTISEMENT = new String[] { "user" };
		}

		public static class AppCase
		{
			private AppCase()
			{
			}

			/**
			 * AppCase 需要过滤的信息 相当于只有用户信息
			 */
			public static final String[] APPCASE_HAS_USER = new String[] { "adminOrgUnit", "createUser", "lastModifyUser", "person", "role", "rootOrgUnit", "carShop", "password",
					HIBERNATE_LAZY_PROPERTISE_HANDLER, HIBERNATE_LAZY_PROPERTISE_LAZYINITIALIZER };
			public static final String[] APPCASE_ONLY_APPCASE = new String[] { "user", HIBERNATE_LAZY_PROPERTISE_HANDLER, HIBERNATE_LAZY_PROPERTISE_LAZYINITIALIZER };
		}

		public static class PushMessage
		{
			private PushMessage()
			{
			}

			/**
			 * pushMessage 推送消息需要过滤掉的数据
			 */
			public static final String[] PUSHMESSAGE_ONLY_PUSHMESSAGE = new String[] { "pushMessageParts", "createUser", HIBERNATE_LAZY_PROPERTISE_HANDLER, HIBERNATE_LAZY_PROPERTISE_LAZYINITIALIZER };
			/**
			 * pushMessage 推送消息需要过滤掉的数据
			 */
			public static final String[] PUSHMESSAGE_HAS_PUSHMESSAGEPART = new String[] { "pushMessage", "createUser", HIBERNATE_LAZY_PROPERTISE_HANDLER, HIBERNATE_LAZY_PROPERTISE_LAZYINITIALIZER };

		}
	}

}
