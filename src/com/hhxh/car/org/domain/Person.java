package com.hhxh.car.org.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.hhxh.car.permission.domain.User;

/***
 * Copyright (C), 2015-2025 Hhxh Tech. Co., Ltd
 * 
 * 功能描述：职员类
 * 
 * Version： 1.0
 * 
 * date： 2015-06-11
 * 
 * @author：jiangdw
 *
 */
@Entity
@Table(name="T_BD_Person")
public class Person 
{
	@Id
	@GenericGenerator(name = "sasGenerator", strategy = "assigned")
	@GeneratedValue(generator = "sasGenerator")
	@Column(name="FID",length=44)
	private String id;
	
	//编码
	@Column(name="FNumber")
	private String number;
	
	//名称
	@Column(name="FName")
	private String name;
	
	//简称
	@Column(name="FSimpleName")
	private String simpleName;
	
	//描述
	@Column(name="FDescription")
	private String description;

	//性别 1="男"，2="女"//胡该
	@Column(name="FGender")
	private Integer gender=1;
	
	//职位
	@ManyToOne
	@JoinColumn(name="FPositionID")
	private Position position;
	
	//办公电话
	@Column(name="FOfficePhone")
	private String officePhone;
	
	//手机
	@Column(name="FCell")
	private String cell;
	
	//Email
	@Column(name="FEmail")
	private String email;
	
	//qq
	@Column(name="FQQ")
	private String qq;
	
	//家庭住址
	@Column(name="FAddress")
	private String address;
	
	//出生日期
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FBirthday",nullable=true)
	private Date birthday;
	
	//身份证号
	@Column(name="FIdCardNO")
	private String idCardNO;
	
	//护照号码
	@Column(name="FPassportNO")
	private String passportNO;
	
	//曾用名
	@Column(name="FOldName")
	private String oldName;
	/**
	 * 用户简称拼音
	 */
	@Column(name="FNAME_JIANPIN",length=80)
	private String name_jianpin;
	
	//血型  10=A,20=B,30=AB,40=O,90=其他,0=未知
	@Column(name="FBloodType")
	private Integer bloodType;
	
	//员工状态  
	@Column(name="FEmployeeTypeID")
	private String employeeTypeID;
	
	@Column(name="FEmployeeTypeName")
	private String employeeTypeName;
	
	//身高
	@Column(name="FHeight")
	private Integer height;
	
	//籍贯
	@Column(name="FNativePlace")
	private String nativePlace;
	
	@Column(name="FEmployeeClassifyID")
	private String employeeClassifyID;
	
	//人员类型
	@Column(name="FEmployeeClassifyName")
	private String employeeClassifyName;
	
	//审核状态 0未审核，1已审核
	@Column(name="FState")
	private String state = "0";
	
	//国籍
	@Column(name="FNationalityID")
	private String nationalityID;
	
	@Column(name="FNationalityName")
	private String nationalityName;
	
	//健康状况
	@Column(name="FHealthID")
	private String healthID;
	
	@Column(name="FHealthName")
	private String healthName;
	
	//家庭出身
	@Column(name="FBirthID")
	private String birthID;
	
	@Column(name="FBirthName")
	private String birthName;
	
	//婚姻状况
	@Column(name="FWedID")
	private String wedID;
	
	@Column(name="FWedName")
	private String wedName;
	
	//个人成分
	@Column(name="FStandingID")
	private String standingID;
	
	@Column(name="FStandingName")
	private String standingName;
	
	//政治面貌
	@Column(name="FPoliticalFaceID")
	private String politicalFaceID;
	
	@Column(name="FPoliticalFaceName")
	private String politicalFaceName;
	
	//民族
	@Column(name="FFolkID")
	private String folkID;
	
	@Column(name="FFolkName")
	private String folkName;
	
	//最高学历
	@Column(name="FHighestDegreeID")
	private String highestDegreeID;
	
	@Column(name="FHighestDegreeName")
	private String highestDegreeName;
	
	//户口所在地
	@Column(name="FHjAddress")
	private String hjAddress;
	
	//邮编
	@Column(name="FPostalcode")
	private String postalcode;
	
	//通信地址
	@Column(name="FAddressTX")
	private String addressTX;
	
	//创建人
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="FCreatorID")
	private User creator;
	
	//创建时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FCreateTime",nullable=true)
	private Date creatTime;
	
	//修改人
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="FLastUpdateUserID")
	private User updateUser;
	
	//修改时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FLastUpdateTime",nullable=true)
	private Date updateTime;
	
	@Column(name="FISFROMEAS")
	private Integer isFromEas;
	
	public Integer getIsFromEas() {
		return isFromEas;
	}

	public void setIsFromEas(Integer isFromEas) {
		this.isFromEas = isFromEas;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

//	public Position getPosition() {
//		return position;
//	}
//
//	public void setPosition(Position position) {
//		this.position = position;
//	}

	public String getOfficePhone() {
		return officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public User getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(User updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getIdCardNO() {
		return idCardNO;
	}

	public void setIdCardNO(String idCardNO) {
		this.idCardNO = idCardNO;
	}

	public String getPassportNO() {
		return passportNO;
	}

	public void setPassportNO(String passportNO) {
		this.passportNO = passportNO;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public Integer getBloodType() {
		return bloodType;
	}

	public void setBloodType(Integer bloodType) {
		this.bloodType = bloodType;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getEmployeeTypeID() {
		return employeeTypeID;
	}

	public void setEmployeeTypeID(String employeeTypeID) {
		this.employeeTypeID = employeeTypeID;
	}

	public String getEmployeeTypeName() {
		return employeeTypeName;
	}

	public void setEmployeeTypeName(String employeeTypeName) {
		this.employeeTypeName = employeeTypeName;
	}

	public String getEmployeeClassifyID() {
		return employeeClassifyID;
	}

	public void setEmployeeClassifyID(String employeeClassifyID) {
		this.employeeClassifyID = employeeClassifyID;
	}

	public String getEmployeeClassifyName() {
		return employeeClassifyName;
	}

	public void setEmployeeClassifyName(String employeeClassifyName) {
		this.employeeClassifyName = employeeClassifyName;
	}

	public String getNationalityID() {
		return nationalityID;
	}

	public void setNationalityID(String nationalityID) {
		this.nationalityID = nationalityID;
	}

	public String getNationalityName() {
		return nationalityName;
	}

	public void setNationalityName(String nationalityName) {
		this.nationalityName = nationalityName;
	}

	public String getHealthID() {
		return healthID;
	}

	public void setHealthID(String healthID) {
		this.healthID = healthID;
	}

	public String getHealthName() {
		return healthName;
	}

	public void setHealthName(String healthName) {
		this.healthName = healthName;
	}

	public String getBirthID() {
		return birthID;
	}

	public void setBirthID(String birthID) {
		this.birthID = birthID;
	}

	public String getBirthName() {
		return birthName;
	}

	public void setBirthName(String birthName) {
		this.birthName = birthName;
	}

	public String getWedID() {
		return wedID;
	}

	public void setWedID(String wedID) {
		this.wedID = wedID;
	}

	public String getWedName() {
		return wedName;
	}

	public void setWedName(String wedName) {
		this.wedName = wedName;
	}

	public String getStandingID() {
		return standingID;
	}

	public void setStandingID(String standingID) {
		this.standingID = standingID;
	}

	public String getStandingName() {
		return standingName;
	}

	public void setStandingName(String standingName) {
		this.standingName = standingName;
	}

	public String getPoliticalFaceID() {
		return politicalFaceID;
	}

	public void setPoliticalFaceID(String politicalFaceID) {
		this.politicalFaceID = politicalFaceID;
	}

	public String getPoliticalFaceName() {
		return politicalFaceName;
	}

	public void setPoliticalFaceName(String politicalFaceName) {
		this.politicalFaceName = politicalFaceName;
	}

	public String getFolkID() {
		return folkID;
	}

	public void setFolkID(String folkID) {
		this.folkID = folkID;
	}

	public String getFolkName() {
		return folkName;
	}

	public void setFolkName(String folkName) {
		this.folkName = folkName;
	}

	public String getHighestDegreeID() {
		return highestDegreeID;
	}

	public void setHighestDegreeID(String highestDegreeID) {
		this.highestDegreeID = highestDegreeID;
	}

	public String getHighestDegreeName() {
		return highestDegreeName;
	}

	public void setHighestDegreeName(String highestDegreeName) {
		this.highestDegreeName = highestDegreeName;
	}


	public String getHjAddress() {
		return hjAddress;
	}

	public void setHjAddress(String hjAddress) {
		this.hjAddress = hjAddress;
	}

	public String getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	public String getAddressTX() {
		return addressTX;
	}

	public void setAddressTX(String addressTX) {
		this.addressTX = addressTX;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public String getName_jianpin() {
		return name_jianpin;
	}

	public void setName_jianpin(String name_jianpin) {
		this.name_jianpin = name_jianpin;
	}
	
}
