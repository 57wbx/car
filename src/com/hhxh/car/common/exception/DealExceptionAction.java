package com.hhxh.car.common.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.sys.domain.ErrorLog;
import com.opensymphony.xwork2.ActionContext;

/**
 * struts的异常处理机制，获取异常之后，将转发到该action中进行输出内容操作
 * @author zw
 * @date 2015年9月12日 上午10:36:34
 *
 */
public class DealExceptionAction  extends BaseAction
{
	
	public DealExceptionAction(){
	}
	/**
	 * 处理异常，并将错误信息保存在数据库中
	 */
	public void handler(){
		try{
			log.info("struts机制获取异常，在这里进行输出错误信息操作,并记录日志");
			Exception ex = (Exception) ActionContext.getContext() .getValueStack().findValue("exception"); 
			addErrorLog(ex);
			if(ex instanceof ErrorMessageException){
				this.putJson(false, ex.getMessage());
			}else{
				this.putJson(false, this.getMessageFromConfig("common_deflaut_message"));
			}
		}catch(Exception e){
			log.error("处理错误信息时失败",e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存错误信息到数据库中
	 * @throws Exception 
	 */
	private void addErrorLog(Exception e) throws Exception{
		ErrorLog errorLog = new ErrorLog();
		errorLog.setExceptionMessage(e.getMessage());
		errorLog.setActionPath(this.getRequest().getRequestURI());
		
		StringWriter sw = new StringWriter(); 
        e.printStackTrace(new PrintWriter(sw, true)); 
        String trace = sw.toString(); 
        
        errorLog.setExceptionStackTrace(trace);
        
		if(this.getLoginUser()!=null){
			errorLog.setOperator(this.getLoginUser());
			errorLog.setOperatorName(this.getLoginUser().getName());
		}
		errorLog.setOperationTime(new Date());
		this.baseService.saveObject(errorLog);
	}
	
}
