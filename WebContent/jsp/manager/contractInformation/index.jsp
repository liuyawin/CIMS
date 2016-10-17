<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<jsp:include page="/jsp/top.jsp" />
<section class="main" ng-app="contract">
	<div ng-view></div>
</section>
<jsp:include page="/jsp/left.jsp" />
<jsp:include page="/jsp/footer.jsp" />
<script src="${ctx}/js/app/manager/contractMgmt.js"></script>
<script>
	$(function(){
		/* alert(window.location.href.indexOf('#')); */
		if(window.location.href.indexOf('#')==-1) 
		document.getElementById("mContractFirst").click();
	})
</script>
</body>
</html>