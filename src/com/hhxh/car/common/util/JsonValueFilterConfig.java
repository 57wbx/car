package com.hhxh.car.common.util;

/**
 * 提供一系列的静态对象，给jsonconfig中的string数组使用。
 * 排除无限循环的意外。
 * 目的：因为domain可能会经常变化，其中的关系可能一会发生变化，如果在每个action中都定义一个String数组，维护起来比较麻烦，
 * 所以将string数组都放在该config类中提供一个修改的的地方。
 * 注意：之前有些模块已经做完了，并没有使用该规则
 * @author zw
 * @date 2015年8月3日 上午11:54:01
 *
 */
public class JsonValueFilterConfig {
	private JsonValueFilterConfig(){};//私有化构造方法
	
	/**
	 * hibernate懒加载需要过滤的数据
	 */
	public static final String HIBERNATE_LAZY_PROPERTISE_HANDLER = "handler";
	public static final String HIBERNATE_LAZY_PROPERTISE_LAZYINITIALIZER = "hibernateLazyInitializer";
	
	/**
	 *  baseArea 的需要在json中排除的数据  ，提供一个只有baseArea的对象
	 */
	public static final String[] BASEAREA_ONLY_BASEAREA = new String[]{"baseCity","baseAreas","baseAreaParent"};
	
	/**
	 * basecity  城市中 中需要排除的数据
	 */
	public static final String[] BASECITY_ONLY_BASECITY = new String[]{"baseProvince","baseAreas"};
	
	/**
	 * busitem 服务项需要排除的数据
	 */
	public static final String[] BASEITEM_ONLY_BASEITEM = new String[]{"busPackages","busAtoms","busItem","busItemImgs"};
	public static final String[] BASEITEM_HAS_BASEITEMIMG = new String[]{"busPackages","busAtoms","busItem","user"};
	
	/**
	 * busitemImg 服务项图片需要排除的数据
	 */
	public static final String[] BASEITEMIMG_ONLY_BASEITEMIMG = new String[]{"busItem","user"};
	
	/**
	 * buspackage 平台套餐需要过滤的数据
	 */
	public static final String[] BASEPACKAGE_HAS_BUSITEMS = new String[]{"busPackages","busAtoms","busItemImgs"};
	
	/**
	 * busatom 平台服务子项需要过滤的数据
	 */
	public static final String[] BASEATOM_HAS_BUSITEM = new String[]{"busPackages","busAtoms","busItemImgs"};
	
	/**
	 * shopitem 商家服务项需要过滤的数据
	 */
	public static final String[] SHOPITEM_ONLY_SHOPITEM = new String[]{"shopAtoms","shopItem","carShop","shopPackages","shopItemImgs"};
	public static final String[] SHOPITEM_HAS_SHOPITEMIMG = new String[]{"shopAtoms","shopItem","carShop","shopPackages","user"};

	/**
	 * shopItemImg 商家服务项图片需要过滤的数据
	 */
	public static final String[] SHOPITEMIMG_ONLY_SHOPITEMIMG = new String[]{"shopItem","user"};
	
	/**
	 * shopAtom 商家服务子项需要过滤的数据
	 */
	public static final String[] SHOPATOM_ONLY_SHOPATOM = new String[]{"autoPart","shopItem","carShop"};
	public static final String[] SHOPATOM_HAS_AUTOPART = new String[]{"shopItem","carShop"};
	
	/**
	 * shopPackage 商家套餐
	 */
	public static final String[] SHOPPACKAGE_ONLY_SHOPPACKAGE = new String[]{"shopItems","carShop","shopPackageImgs"};
	public static final String[] SHOPPACKAGE_HAS_SHOPITEM = new String[]{"carShop","shopPackages","shopItemImgs","shopAtoms","shopPackageImgs"};
	public static final String[] SHOPPACKAGE_HAS_SHOPPACKAGEIMG = new String[]{"carShop","shopPackages","shopItemImgs","shopItems","shopAtoms","user"};
	
	/**
	 * shopPackageImg 商家套餐需要过滤的数据
	 */
	public static final String[] SHOPPACKAGEIMG_ONLY_SHOPPACKAGEIMG = new String[]{"shopPackage","user"};
	
	/**
	 * advertisement 广告需要过滤的数据
	 */
	public static final String[] ADVERTISEMENT_ONLY_ADVERTISEMENT = new String[]{"user"};
	
	/**
	 * carshop  网店需要过滤的数据
	 */
	public static final String[] CARSHOP_ONLY_CARSHOP = new String[]{"org","user","merbers","orders","shopBlackList"};
	public static final String[] CARSHOP_HAS_ORG = new String[]{"user", "createUser", "lastUpdateUser", "parent","merbers","orders","shopBlackList"};
	//门店黑名单
	public static final String[] SHOPBLACKLIST_ONLY_BLACKLIST = new String[]{"carShop","createUser"}; 
	
	/**
	 * Member 会员或者师傅 需要过滤的信息
	 */
	public static final String[] MEMBER_ONLY_MEMBER = new String[]{"carShop","orders","workerBlackList"};
	//师傅 黑名单
	public static final String[] WORKERBLACKLIST_ONLY_WORKERBLACKLIST = new String[]{"worker","createUser"};
	
	/**
	 * Order 订单需要过滤的信息
	 */
	public static final String[] ORDER_HAS_TIGUSERS_HAS_WORKER = new String[]{"carShop","orders","order","orderTracks","workerBlackList",HIBERNATE_LAZY_PROPERTISE_HANDLER,HIBERNATE_LAZY_PROPERTISE_LAZYINITIALIZER};
	
	/**
	 * OrderTrack 订单跟踪状态需要过滤的信息
	 */
	public static final String[] ORDERTRACK_ONLY_ORDERTRACK = new String[]{"order"};
	
	/**
	 * AppCase 需要过滤的信息   相当于只有用户信息
	 */
	public static final String[] APPCASE_HAS_USER = new String[]{"adminOrgUnit","createUser","lastModifyUser","person","role","rootOrgUnit","carShop","password",HIBERNATE_LAZY_PROPERTISE_HANDLER,HIBERNATE_LAZY_PROPERTISE_LAZYINITIALIZER};
	public static final String[] APPCASE_ONLY_APPCASE = new String[]{"user",HIBERNATE_LAZY_PROPERTISE_HANDLER,HIBERNATE_LAZY_PROPERTISE_LAZYINITIALIZER};
	
	/**
	 * Complain 投诉表需要过滤的数据
	 */
	public static final String[] COMPLAINS_HAS_COMPLAINUSER_HAS_DEALUSER = new String[]{"createUser","lastModifyUser","person","role","rootOrgUnit","carShop","password","orders","user_pw",HIBERNATE_LAZY_PROPERTISE_HANDLER,HIBERNATE_LAZY_PROPERTISE_LAZYINITIALIZER};
	public static final String[] COMPLAIN_ONLY_COMPLAIN = new String[]{"dealUser","complainUser"};
	
	
	/**
	 * menu 菜单表需要处理的数据
	 */
	public static final String[] MENU_ONLY_MENU = new String[]{"parent","permItems",HIBERNATE_LAZY_PROPERTISE_HANDLER,HIBERNATE_LAZY_PROPERTISE_LAZYINITIALIZER};
	
	public static final String[] PERMITEM_ONLY_PERMITEM = new String[]{"mainMenuItem","roles",HIBERNATE_LAZY_PROPERTISE_HANDLER,HIBERNATE_LAZY_PROPERTISE_LAZYINITIALIZER};
	
	/**
	 * car 车型需要处理的过滤数据
	 */
	public static final String[] CAR_ONLY_CAR = new String[]{HIBERNATE_LAZY_PROPERTISE_HANDLER,HIBERNATE_LAZY_PROPERTISE_LAZYINITIALIZER};
	/**
	 * carName 车品牌需要过滤的数据
	 */
	public static final String[] CARNAME_ONLY_CARNAME = new String[]{HIBERNATE_LAZY_PROPERTISE_HANDLER,HIBERNATE_LAZY_PROPERTISE_LAZYINITIALIZER};
	
	/**
	 * hotword 热门词汇需要过滤的数据
	 */
	public static final String[] HOTWORD_ONLY_HOTWORD = new String[]{HIBERNATE_LAZY_PROPERTISE_HANDLER,HIBERNATE_LAZY_PROPERTISE_LAZYINITIALIZER};
	
}
