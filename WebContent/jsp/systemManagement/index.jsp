<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<jsp:include page="/jsp/top.jsp" />
<section id="user" class="main" style="min-height: 40px;">
	<div ng-view></div>
</section>
<section id="alarm" class="main"
	style="min-height: 40px;">
	<div ng-view></div>
</section>
<jsp:include page="/jsp/left.jsp" />
<jsp:include page="/jsp/footer.jsp" />

<script src="${ctx}/js/app/user.js"></script>
<script src="${ctx}/js/app/alarmSet.js"></script>
<script type="text/javascript">
    var permissionList; 
	angular.element(document).ready(function() {
		$.get('/CIMS/login/getUserPermission.do', function(data) { 
			  permissionList = data; // 
			  //console.log("身份是：" + permissionList);
			  angular.bootstrap(document.getElementById("alarm"), [ "alarm" ]);
	});
	});
</script>
<script>
	$(function(){
		$('dd').find('ul').css("display","none");
		$('#system-ul').css("display","block");
		var currentPage = sessionStorage.getItem("currentPage");
		if(currentPage){
			$("#"+currentPage).addClass("active");
		}
	})
</script>
</body>
</html>

