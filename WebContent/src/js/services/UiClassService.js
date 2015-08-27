app.factory("uiClassService",[function(){
	var imgArray = [{"name":"时钟","class":"fa-clock-o"},{"name":"人物","class":"fa-child"}];
	var colorArray = [{"name":"红色","class":"-danger"},{"name":"蓝色","class":"info"}];
	return {
		getImgArray:getImgArray,
		getColorArray:getColorArray
	};
	function getImgArray(){
		return imgArray ;
	} 
	function getColorArray(){
		return colorArray ;
	}
}]);