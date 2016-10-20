<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<jsp:include page="/jsp/top.jsp" />
<section id="user" class="main" ng-app="user" style="min-height: 40px;">
	<div ng-view></div>
</section>
<section id="alarm" class="main" ng-app="alarm"
	style="min-height: 40px;">
	<div ng-view></div>
</section>
<jsp:include page="/jsp/left.jsp" />
<jsp:include page="/jsp/footer.jsp" />

<script src="${ctx}/js/app/user.js"></script>
<script src="${ctx}/js/app/alarmSet.js"></script>
<script type="text/javascript">
	angular.element(document).ready(function() {
		angular.bootstrap(document.getElementById("alarm"), [ "alarm" ]);
	});
</script>
</body>
</html>

