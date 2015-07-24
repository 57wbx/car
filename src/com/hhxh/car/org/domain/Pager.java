package com.hhxh.car.org.domain;

import java.io.Serializable;

/***
 * Copyright (C), 2015-2025 Hhxh Tech. Co., Ltd
 * 
 * 功能描述：dataTables 分页类
 * 
 * Version： 1.0
 * 
 * date： 2015-07-11
 * 
 * @author：蒋大伟
 *
 */
public class Pager implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer pageSize=10;
	private Integer currentPage=1;
	
	public Integer getStart(){
		if(pageSize==null||currentPage==null||currentPage==0){
			return 0;
		}
		return (currentPage-1)*pageSize;
	}
	
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	
	
}
