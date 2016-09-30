<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<jsp:include page="/jsp/top.jsp" />
<section class="main" ng-app="department">
	<div ng-view></div>
</section>
<jsp:include page="/jsp/userManagement/left.jsp" />
<jsp:include page="/jsp/footer.jsp" />

<script src="${ctx}/js/app/userManagement/department.js"></script>
<script>
	$(function() {
		if (window.location.href.indexOf('#') == -1)
			console.log(document.getElementById("departmentFirst"));
			document.getElementById("departmentFirst").click();
	})
</script>
</body>
</html>
