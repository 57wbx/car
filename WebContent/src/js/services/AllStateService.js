/**
* 所有的表的状态，或者需要装换的数据，都将从该文件中找相关的表服务
* 表服务的命名规则为：表名+StateService  例如：orderStateService
*/


app.factory("commonGetStateUtilService",[function(){
	return {
		/**key为需要装换的值，列如：1，Value对象为保存可能出现的状态 列如：{1：“hello”,2:"helloworld"}
		 * 当value不存在时返回空，当value中没有这个属性值时也返回空*/
		get:function(key,ValueObj){
			if(!key){
				return " ";
			}
			if(ValueObj){
				var value = ValueObj[key];
				if(value){
					return value ;
				}else{
					return " ";
				}
			}else{
				return " ";
			}
		}
	}
}]).
/**
 * 该服务提供了所有订单表中，有关的状态信息。
 * 
 */
factory("orderStateService",[function(){
	/**
	 * 服务的详细实现 
	 */
	//服务类型的中文信息
	var getServiceType = function(param){
		//1=标准型（默认）、2=经济型、3=高效型、4=原厂型 
		switch(param){
		case 1: return "标准型";break ;
		case 2: return "经济型";break ;
		case 3: return "高效型";break ;
		case 4: return "原厂型";break ;
		default:return ""; break ;
		}
	};
	//orderState for Order 0=初始、1=下单、2=接单、3=作业中、4=交付、5=结算、9=退单
	var getOrderState = function(param){
		switch(param){
    	//0=初始、1=下单、2=接单、3=作业中、4=交付、5=结算、9=退单
    	case 0 : return "初始"; break;
    	case 1 : return "下单";break;
    	case 2 : return "接单";break;
    	case 3 : return "作业中";break;
    	case 4 : return "交付";break;
    	case 5 : return "结算";break;
    	case 9 : return "退单";break;
    	default:return ""; break ;
    	}
	};
	//payType 支付方式 1=支付宝、2=微信、3=银联、4=财付通、5=线下
	var getPayType = function(param){
		switch(param){
		case 1:return "支付宝"; break ;
		case 2:return "微信"; break ;
		case 3:return "银联"; break ;
		case 4:return "财付通"; break ;
		case 5:return "线下"; break ;
		default:return ""; break ;
		}
	};
	//payState 0=未支付、1=已支付  支付状态
	var getPayState = function(param){
		switch(param){
		case 0:return "未支付";break ;
		case 1:return "已支付";break ;
		default:return ""; break;
		}
	};
	//busType 业务类型 0=送车、1=上门取车、2=现场
	var getBusType = function(param){
		switch(param){
		case 0: return "送车"; break ;
		case 1: return "上门取车"; break ;
		case 2: return "现场"; break ;
		default: return ""; break ;
		}
	};
	//dealType 订单处理类型 1=免预约、2=返积分、3=随时退、4=过期退
	var getDealType = function(param){
		switch(param){
		case 1:return "免预约";break;
		case 2:return "返积分";break;
		case 3:return "随时退";break;
		case 4:return "过期退";break;
		default:return "";break ;
		}
	}
	/**
	 * 返回使用对象服务
	 */
	return {
		getServiceType:getServiceType ,
		getOrderState:getOrderState,
		getPayType:getPayType,
		getPayState:getPayState,
		getBusType:getBusType,
		getDealType:getDealType
	};
	//结束
}]).factory("memberStateService",[function(){
	
	var getUseType = function(param){
		switch(param){
		case 0 : return "车主";break ;
		case 1 : return "技工";break ;
		case 2 : return "两者都是"; break ;
		default : return"";break;
		}
	};
	//0=初始（保存）、1=待审核、2=审核通过、3=退回
	var getAuditState = function(param){
		switch(param){
		case 0: return "初始"; break;
		case 1: return "待审核"; break;
		case 2: return "审核通过"; break;
		case 3: return "退回"; break;
		default : return "";break ;
		}
	};
	//1=正常、2=停用、3=注销（黑名单）
	var getUseState = function(param){
		switch(param){
		case 1: return "正常";break ;
		case 2: return "停用";break ;
		case 3: return "注销（黑名单）";break ;
		default : return""; break ;
		}
	};
	//0=一般用户、1=普通会员、2=VIP会员、3=白金VI、4=钻石VIP
	var getVIPLevel = function(param){
		switch(param){
		case 0 : return "一般用户" ; break ;
		case 1 : return "普通会员" ; break ;
		case 2 : return "VIP会员" ; break ;
		case 3 : return "白金会员" ; break ;
		case 4 : return "钻石VIP" ; break ;
		default : return "" ; break ;
		}
	};
	//0=男,1=女,2=保密
	var getGender = function(param){
		switch(param){
		case 0: return "男"; break ;
		case 1: return "女"; break ;
		case 2: return "保密"; break ;
		default : return "";break ;
		}
	};
	//0保密，1=未婚，2=已婚，3=再婚，4=离婚，5=丧偶，6其他
	var getMerriageState = function(param){
		switch(param){
		case 0:return "保密"; break ;
		case 1:return "未婚"; break ;
		case 2:return "已婚"; break ;
		case 3:return "再婚"; break ;
		case 4:return "离婚"; break ;
		case 5:return "丧偶"; break ;
		case 6:return "其他"; break ;
		default :return ""; break ;
		}
	}
	
	return {
		getUseType:getUseType,
		getAuditState:getAuditState,
		getUseState:getUseState,
		getVIPLevel:getVIPLevel,
		getGender:getGender,
		getMerriageState:getMerriageState
	};
	//结束
}]).factory("appCaseStateService",function(){
	return {
		getAppLevel:getAppLevel,
		getIsUse:getIsUse
	};
	
//		1=1星、2=2星、3=3星、4=4星、5=5星（默认0）
	function getAppLevel(param){
		switch(param){
		case 1: return "1星" ;break ;
		case 2: return "2星" ;break ;
		case 3: return "3星" ;break ;
		case 4: return "4星" ;break ;
		case 5: return "5星" ;break ;
		default : return ""; break ;
		}
	}
	//0=启用（默认）、1=停用
	function getIsUse(param){
		switch(param){
		case 0: return "启用"; break ;
		case 1: return "停用"; break ;
		default: return ""; break ;
		}
	}
	
}).factory("complainStateService",[function(){
	return {
		getObjType:getObjType,
		getDealState:getDealState,
		getDealStateWithStyle:getDealStateWithStyle
	};
	function getObjType(param){
		switch(param){
		//1=门店、2=技工、3=（服务）、4=套餐，5=其他
		case 1: return "门店" ; break ;
		case 2: return "技工" ; break ;
		case 3: return "服务" ; break ;
		case 4: return "套餐" ; break ;
		case 5: return "其他" ; break ;
		default :return ""; break ;
		}
	}
	function getDealState(param){
		switch(param){
		//0=未处理；1=已处理
		case 0 : return "未处理" ;break ;
		case 1 : return "已处理" ;break ;
		default : return "未处理"; break ;
		}
	}
	function getDealStateWithStyle(param){
		switch(param){
		//0=未处理；1=已处理
		case 0 : return "<span style='color:red ;'>未处理</span>" ;break ;
		case 1 : return "已处理" ;break ;
		default : return "<span style='color:red ;'>未处理</span>"; break ;
		}
	}
}]).factory("carShopStateService",['commonGetStateUtilService',function(commonGetStateUtilService){
	//useState 1=正常、2=停用、3=注销（黑名单）
	function getUseState(param){
		switch(param){
		case 1 : return "正常"; break ;
		case 2 : return "停用"; break ;
		case 3 : return "注销"; break ;
		case 4 : return "黑名单"; break ;
		default : return ""; break ;
		}
	};
	function getUseStateWithStyle(param){
		switch(param){
		case 1 : return "正常"; break ;
		case 2 : return "停用"; break ;
		case 3 : return "注销"; break ;
		case 4 : return "<span style='color:red;'>黑名单<span>"; break ;
		default : return ""; break ;
		}
	};
	//0=加盟店、1=合作店、3=直营店、4=中心店（区域旗舰店）
	function getShopType(param){
		var shopType = {
				0:"加盟店",1:"合作店",3:"直营店",4:"中心店(区域旗舰店)",
		};
		return commonGetStateUtilService.get(param,shopType);
	};

	function getVIPLevel(param){
		//0=0星、1=1星、2=2星、3=3星、4=4星、5=5星
		var VIPLevlel={
				0:"0星",1:"1星",2:"2星",3:"3星",4:"4星",5:"5星"
		};
		return commonGetStateUtilService.get(param,VIPLevlel);
	};
	
	return {
		getUseState:getUseState,
		getUseStateWithStyle:getUseStateWithStyle,
		getVIPLevel:getVIPLevel
	};
}]).factory("hotWordStateService",['commonGetStateUtilService',function(commonGetStateUtilService){
	function getUseState(param){
		//1=正常、2=停用、3=注销（以后无此业务）
		var useState = {
				1:"正常",2:"停用",3:"注销"
		};
		return commonGetStateUtilService.get(param,useState);
	}
	function getObjType(param){
		//1=门店、2=技工、3=（服务项）、4=套餐，5=其他
		var objType = {
				1:"门店",2:"技工",3:"服务",4:"套餐",5:"其他"
		};
		return commonGetStateUtilService.get(param,objType);
	}
	
	return {
		getUseState:getUseState,
		getObjType:getObjType
	};
	
}]);