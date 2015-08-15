/**
 * 提供了上传一张图片的功能。必须要和UploadOneImgService.js 结合起来使用
 * 使用方法 <div upload_one_img url="formData.photoUrl" name="formData.photoUrlName" label="photoUrl"></div>
 * @url 是需要上传到服务其中的数据，保存的是上传图片之后的地址
 * @name 是显示的名称，如果是从后台查询的数据，那么需要将查询的数据进行包装
 * 						$scope.formData.photoUrlName = resp.data.details.photoUrl?"请点击&nbsp;<a style='color: blue; text-decoration: underline;'  onClick='$(\"#photoUrl\").click();'>预览！</a>":undefined;
 * @label 是元素id 或者点击事件中的唯一标识符
 * @author zw
 */
app.directive("uploadOneImg",['uploadOneImgService',function(uploadOneImgService){
	return {
		restrict:"EA",
		replace:true,
		compile: function(ele, attr, transcludeFn){
				var newEle = angular.element("<div>" +
																	    "<div ng-if='"+attr.url+"'>"+
																			" <div class='col-sm-7'>"+
																			"<span ng-bind-html='"+attr.name+" | trustHtmlFilter' class='form-control'></span></div>"+
																			 "<div class='col-sm-2'>"+
																					"<button class='btn btn-success' id='"+attr.label+"' role='button' ng-click='previewImg(\""+attr.label+"\")'>预览</button>"+
	 	        																	"<button class='btn btn-danger' role='button' ng-click='deleteImg(\""+attr.label+"\")'>删除</button>"+
	 	        																"</div>"+
	 	        														"</div>"+
	 	        														 "<div ng-if='!"+attr.url+"'>"+
			       																"<div class='col-sm-7'>"+
			       																		"<input type='file' uploader='uploader'  nv-file-select='' class='form-control ' accept='image/*'  options='{name:\""+attr.label+"\"}'  />"+
			       																"</div>"+
																			       "<div class='col-sm-2'>"+
																			 	        "<button class='btn btn-success' role='button' ng-click='uploadImg(\""+attr.label+"\")'  id='"+attr.label+"Button' >上传</button>"+
																			      " </div>"+
																		    "</div>"+
																		 "</div>");
			
				ele.replaceWith(newEle);
			/**
			 * 返回链接函数提供使用
			 */
				return function(scope,ele,attr){
					uploadOneImgService.initUploadModule(scope);
				}
			}
	
	};
	//结束方法
}]);