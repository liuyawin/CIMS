<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<jsp:include page="/jsp/top.jsp" />
<section id="ng-section" class="main">
	<div ng-view></div>
</section>
<jsp:include page="/jsp/left.jsp" />
<jsp:include page="/jsp/footer.jsp" />
<script src="${ctx}/js/app/contractMgmt.js"></script>
<script src="${ctx}/js/lib/angular-file-upload.min.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/distpicker/bootstrap.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/distpicker/distpicker.data.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/distpicker/distpicker.js"></script>
<script>
	$(function(){
		$('dd').find('ul').css("display","none");
		$('#contract-ul').css("display","block");
		var currentPage = sessionStorage.getItem("currentPage");
		if(currentPage){
			$("#"+currentPage).addClass("active");
		}
	})
</script>
</body>
</html>