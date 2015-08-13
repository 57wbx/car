/**
* 所有的表的状态，或者需要装换的数据，都将从该文件中找相关的表服务
* 表服务的命名规则为：表名+StateService  例如：orderStateService
*/

/**
 * 该服务提供了所有订单表中，有关的状态信息。
 * 
 */
app.factory("orderStateService",[function(){
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
}]);