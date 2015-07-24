<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>咨询管理</title>
<script type="text/javascript" src="resource/js/jquery.min.js"></script>
<script type="text/javascript">
$(function(){
	test();
});
function test(){
	$.post(
		'test/testAction!loadList.action',
		{"id":'12'},
		function(data){
			alert(1);
		},'json'
	);
}
</script>

</head>

11
</html>