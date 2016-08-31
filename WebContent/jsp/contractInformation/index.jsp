<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<jsp:include page="/jsp/top.jsp" />
<jsp:include page="/jsp/left.jsp" />
<section class="main" ng-app="contract">
	<div ng-view></div>
</section>
<jsp:include page="/jsp/footer.jsp" />
<script src="../../js/app/contractMgmt.js"></script>
</body>
</html>