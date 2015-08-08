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
	 *  baseArea 的需要在json中排除的数据  ，提供一个只有baseArea的对象
	 */
	public static final String[] BASEAREA_ONLY_BASEAREA = new String[]{"baseCity","baseAreas","baseAreaParent"};
	
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
	
}
