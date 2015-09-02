package com.hhxh.car.base.car.domain;

// Generated 2015-8-27 10:19:34 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * BaseCar generated by hbm2java
 */
@Entity
@Table(name="base_car")
public class Car 
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	@Column(name="FIRST_LETTER")
	private String firstLetter;
	@Column(name="MAKE_NAME")
	private String makeName;
	@Column(name="MODEL_SERIES")
	private String modelSeries;
	@Column(name="MODEL_NAME")
	private String modelName;
	@Column(name="TYPE_SERIES")
	private String typeSeries;
	@Column(name="TYPE_NAME")
	private String typeName;
	@Column
	private String country;
	@Column
	private String technology;
	@Column(name="VEHICLE_CLASS")
	private String vehicleClass;
	@Column(name="ENGINE_CAPACITY")
	private String engineCapacity;
	@Column
	private String transmission;
	@Column
	private Date updateTime;

	public Car()
	{
	}

	public Car(int id)
	{
		this.id = id;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getFirstLetter()
	{
		return this.firstLetter;
	}

	public void setFirstLetter(String firstLetter)
	{
		this.firstLetter = firstLetter;
	}

	public String getMakeName()
	{
		return this.makeName;
	}

	public void setMakeName(String makeName)
	{
		this.makeName = makeName;
	}

	public String getModelSeries()
	{
		return this.modelSeries;
	}

	public void setModelSeries(String modelSeries)
	{
		this.modelSeries = modelSeries;
	}

	public String getModelName()
	{
		return this.modelName;
	}

	public void setModelName(String modelName)
	{
		this.modelName = modelName;
	}

	public String getTypeSeries()
	{
		return this.typeSeries;
	}

	public void setTypeSeries(String typeSeries)
	{
		this.typeSeries = typeSeries;
	}

	public String getTypeName()
	{
		return this.typeName;
	}

	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}

	public String getCountry()
	{
		return this.country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public String getTechnology()
	{
		return this.technology;
	}

	public void setTechnology(String technology)
	{
		this.technology = technology;
	}

	public String getVehicleClass()
	{
		return this.vehicleClass;
	}

	public void setVehicleClass(String vehicleClass)
	{
		this.vehicleClass = vehicleClass;
	}

	public String getEngineCapacity()
	{
		return this.engineCapacity;
	}

	public void setEngineCapacity(String engineCapacity)
	{
		this.engineCapacity = engineCapacity;
	}

	public String getTransmission()
	{
		return this.transmission;
	}

	public void setTransmission(String transmission)
	{
		this.transmission = transmission;
	}

	public Date getUpdateTime()
	{
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}

	@Override
	public String toString()
	{
		return "Car [id=" + id + ", firstLetter=" + firstLetter + ", makeName=" + makeName + ", modelSeries=" + modelSeries + ", modelName=" + modelName + ", typeSeries=" + typeSeries + ", typeName="
				+ typeName + ", country=" + country + ", technology=" + technology + ", vehicleClass=" + vehicleClass + ", engineCapacity=" + engineCapacity + ", transmission=" + transmission
				+ ", updateTime=" + updateTime + "]";
	}
	
}