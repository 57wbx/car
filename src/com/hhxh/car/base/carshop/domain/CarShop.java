package com.hhxh.car.base.carshop.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.hhxh.car.base.member.domain.Member;
import com.hhxh.car.opr.domain.Order;
import com.hhxh.car.org.domain.AdminOrgUnit;
import com.hhxh.car.permission.domain.User;
import com.hhxh.car.shop.domain.ShopItem;

@Entity
@Table(name = "base_car_shop")
public class CarShop implements Serializable
{

	/**
	 * 主键id
	 */
	@Id
	@GenericGenerator(name = "sasGenerator", strategy = "assigned")
	@GeneratedValue(generator = "sasGenerator")
	@Column(name = "id", length = 44)
	private String id;

	/**
	 * 店面唯一编号 ，参考【编码规则】
	 */
	@Column(name = "shopCode", length = 50, unique = true)
	private String shopCode;
	/**
	 * 店面名称
	 */
	@Column(name = "shopName", length = 100)
	private String shopName;

	/**
	 * 店面简称
	 */
	@Column(name = "simpleName", length = 50)
	private String simpleName;

	/**
	 * 店面性质
	 */
	@Column(name = "shopType")
	private Integer shopType;

	/**
	 * 营业执照
	 */
	@Column(name = "busCardNo", length = 20)
	private String busCard;

	/**
	 * 营业证相片URL
	 */
	@Column(name = "busCardUrl", length = 100)
	private String busCardUrl;

	/**
	 * 法人姓名
	 */
	@Column(name = "legalNAME", length = 50)
	private String legalNAME;

	/**
	 * 法人手机号
	 */
	@Column(name = "legalCELL", length = 20)
	private String legalCELL;

	/**
	 * 法人身份证号
	 */
	@Column(name = "IDCARDNO", length = 20)
	private String IDCARDNO;

	/**
	 * 法人身份证url
	 */
	@Column(name = "IDCARDURL", length = 255)
	private String IDCARDURL;

	/**
	 * 固定电话
	 */
	@Column(name = "telephone", length = 20)
	private String telephone;

	/**
	 * 注册时间
	 */
	@Column(name = "registerDate")
	private Date registerDate;

	/**
	 * 省
	 */
	@Column(name = "province", length = 10)
	private String province;

	/**
	 * 城市
	 */
	@Column(name = "city", length = 10)
	private String city;

	/**
	 * 区
	 */
	@Column(name = "area", length = 10)
	private String area;

	/**
	 * 详细地址
	 */
	@Column(name = "address", length = 255)
	private String address;

	/**
	 * 坐标x
	 */
	@Column(name = "mapX")
	private Double mapX;

	/**
	 * 坐标y
	 */
	@Column(name = "mapY")
	private Double mapY;

	/**
	 * 审核状态
	 */
	@Column(name = "auditState")
	private Integer auditState;

	/**
	 * 使用状态
	 */
	@Column(name = "useState")
	private Integer useState;

	/**
	 * 店面等级
	 */
	@Column(name = "VIPLevel")
	private Integer VIPLevel;

	/**
	 * 总体评价等级
	 */
	@Column(name = "commentLevel")
	private Integer commentLevel;

	/**
	 * 店面员工数
	 */
	@Column(name = "employeeNum")
	private Integer employeeNum;

	/**
	 * 企业邮箱
	 */
	@Column(name = "email", length = 60)
	private String email;

	/**
	 * qq
	 */
	@Column(name = "qq", length = 20)
	private String qq;

	/**
	 * 微信公众号
	 */
	@Column(name = "weixin", length = 20)
	private String weixin;

	/**
	 * 更新时间
	 */
	@Column(name = "updateTime")
	private Date updateTime;

	/**
	 * 店面描述
	 */
	@Column(name = "description", length = 500)
	private String description;

	/**
	 * 营业开始时间
	 */
	@Column(name = "busStartTime")
	private String busStartTime;

	/**
	 * 营业结束时间
	 */
	@Column(name = "busEndTime")
	private String busEndTime;

	/**
	 * 开卡银行
	 */
	@Column(name = "bankName", length = 100)
	private String bankName;

	/**
	 * 银行卡号
	 */
	@Column(name = "bankCardNo")
	private String bankCardNo;

	/**
	 * 银行卡照片
	 */
	@Column(name = "bankCardUrl", length = 100)
	private String bankCardUrl;

	/**
	 * 备注
	 */
	@Column(name = "Memo", length = 255)
	private String Memo;

	/**
	 * 该id可能是组织id
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "orgid")
	private AdminOrgUnit org;

	@OneToOne(mappedBy = "carShop", fetch = FetchType.LAZY)
	private User user;
	
	@OneToMany(mappedBy="carShop",fetch=FetchType.LAZY)
	private Set<Member> merbers = new HashSet<Member>(); ;
	
	@OneToMany(mappedBy="carShop",fetch=FetchType.LAZY)
	private Set<Order> orders = new HashSet<Order>();
	
	@OneToOne(fetch=FetchType.LAZY,mappedBy="carShop")
	private ShopBlackList shopBlackList ;
	

	@Column
	private String branks;

	@Column
	private String workerDes;

	@Column
	private String photoUrl;

	public CarShop()
	{
	}

	public CarShop(String id)
	{
		this.id = id;
	}

	/**
	 * @return
	 */
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getShopCode()
	{
		return shopCode;
	}

	public void setShopCode(String shopCode)
	{
		this.shopCode = shopCode;
	}

	public String getShopName()
	{
		return shopName;
	}

	public void setShopName(String shopName)
	{
		this.shopName = shopName;
	}

	public String getSimpleName()
	{
		return simpleName;
	}

	public void setSimpleName(String simpleName)
	{
		this.simpleName = simpleName;
	}

	public Integer getShopType()
	{
		return shopType;
	}

	public void setShopType(Integer shopType)
	{
		this.shopType = shopType;
	}

	public String getBusCard()
	{
		return busCard;
	}

	public void setBusCard(String busCard)
	{
		this.busCard = busCard;
	}

	public String getBusCardUrl()
	{
		return busCardUrl;
	}

	public void setBusCardUrl(String busCardUrl)
	{
		this.busCardUrl = busCardUrl;
	}

	public String getLegalNAME()
	{
		return legalNAME;
	}

	public void setLegalNAME(String legalNAME)
	{
		this.legalNAME = legalNAME;
	}

	public String getLegalCELL()
	{
		return legalCELL;
	}

	public void setLegalCELL(String legalCELL)
	{
		this.legalCELL = legalCELL;
	}

	public String getIDCARDNO()
	{
		return IDCARDNO;
	}

	public void setIDCARDNO(String iDCARDNO)
	{
		IDCARDNO = iDCARDNO;
	}

	public String getIDCARDURL()
	{
		return IDCARDURL;
	}

	public void setIDCARDURL(String iDCARDURL)
	{
		IDCARDURL = iDCARDURL;
	}

	public String getTelephone()
	{
		return telephone;
	}

	public void setTelephone(String telephone)
	{
		this.telephone = telephone;
	}

	public Date getRegisterDate()
	{
		return registerDate;
	}

	public void setRegisterDate(Date registerDate)
	{
		this.registerDate = registerDate;
	}

	public String getProvince()
	{
		return province;
	}

	public void setProvince(String province)
	{
		this.province = province;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getArea()
	{
		return area;
	}

	public void setArea(String area)
	{
		this.area = area;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public Double getMapX()
	{
		return mapX;
	}

	public void setMapX(Double mapX)
	{
		this.mapX = mapX;
	}

	public Double getMapY()
	{
		return mapY;
	}

	public void setMapY(Double mapY)
	{
		this.mapY = mapY;
	}

	public Integer getAuditState()
	{
		return auditState;
	}

	public void setAuditState(Integer auditState)
	{
		this.auditState = auditState;
	}

	public Integer getUseState()
	{
		return useState;
	}

	public void setUseState(Integer useState)
	{
		this.useState = useState;
	}

	public Integer getVIPLevel()
	{
		return VIPLevel;
	}

	public void setVIPLevel(Integer vIPLevel)
	{
		VIPLevel = vIPLevel;
	}

	public Integer getCommentLevel()
	{
		return commentLevel;
	}

	public void setCommentLevel(Integer commentLevel)
	{
		this.commentLevel = commentLevel;
	}

	public Integer getEmployeeNum()
	{
		return employeeNum;
	}

	public void setEmployeeNum(Integer employeeNum)
	{
		this.employeeNum = employeeNum;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getQq()
	{
		return qq;
	}

	public void setQq(String qq)
	{
		this.qq = qq;
	}

	public String getWeixin()
	{
		return weixin;
	}

	public void setWeixin(String weixin)
	{
		this.weixin = weixin;
	}

	public Date getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getBusStartTime()
	{
		return busStartTime;
	}

	public void setBusStartTime(String busStartTime)
	{
		this.busStartTime = busStartTime;
	}

	public String getBusEndTime()
	{
		return busEndTime;
	}

	public void setBusEndTime(String busEndTime)
	{
		this.busEndTime = busEndTime;
	}

	public String getBankName()
	{
		return bankName;
	}

	public void setBankName(String bankName)
	{
		this.bankName = bankName;
	}

	public String getBankCardNo()
	{
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo)
	{
		this.bankCardNo = bankCardNo;
	}

	public String getBankCardUrl()
	{
		return bankCardUrl;
	}

	public void setBankCardUrl(String bankCardUrl)
	{
		this.bankCardUrl = bankCardUrl;
	}

	public String getMemo()
	{
		return Memo;
	}

	public void setMemo(String memo)
	{
		Memo = memo;
	}

	public AdminOrgUnit getOrg()
	{
		return org;
	}

	public void setOrg(AdminOrgUnit org)
	{
		this.org = org;
	}

	@Override
	public String toString()
	{
		return "CarShop [id=" + id + ", shopCode=" + shopCode + ", shopName=" + shopName + ", simpleName=" + simpleName + ", shopType=" + shopType + ", busCard=" + busCard + ", busCardUrl=" + busCardUrl + ", legalNAME=" + legalNAME + ", legalCELL=" + legalCELL + ", IDCARDNO=" + IDCARDNO + ", IDCARDURL=" + IDCARDURL + ", telephone=" + telephone + ", registerDate=" + registerDate + ", province=" + province + ", city=" + city + ", area=" + area + ", address=" + address + ", mapX=" + mapX + ", mapY=" + mapY + ", auditState=" + auditState + ", useState=" + useState + ", VIPLevel=" + VIPLevel + ", commentLevel=" + commentLevel + ", employeeNum=" + employeeNum + ", email=" + email + ", qq=" + qq + ", weixin=" + weixin + ", updateTime=" + updateTime + ", description=" + description + ", busStartTime=" + busStartTime + ", busEndTime=" + busEndTime + ", bankName=" + bankName + ", bankCardNo=" + bankCardNo + ", bankCardUrl=" + bankCardUrl + ", Memo=" + Memo + "]";
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public String getBranks()
	{
		return branks;
	}

	public void setBranks(String branks)
	{
		this.branks = branks;
	}

	public String getWorkerDes()
	{
		return workerDes;
	}

	public void setWorkerDes(String workerDes)
	{
		this.workerDes = workerDes;
	}

	public String getPhotoUrl()
	{
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl)
	{
		this.photoUrl = photoUrl;
	}

	public Set<Member> getMerbers()
	{
		return merbers;
	}

	public void setMerbers(Set<Member> merbers)
	{
		this.merbers = merbers;
	}

	public Set<Order> getOrders()
	{
		return orders;
	}

	public void setOrders(Set<Order> orders)
	{
		this.orders = orders;
	}

	public ShopBlackList getShopBlackList()
	{
		return shopBlackList;
	}

	public void setShopBlackList(ShopBlackList shopBlackList)
	{
		this.shopBlackList = shopBlackList;
	}
	
}
