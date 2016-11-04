<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<jsp:include page="/jsp/top.jsp" />

<section id="contract" class="main"
	style="min-height: 40px;">
	<div ng-view></div>
</section>
<section id="receipt" class="main"
	style="min-height: 40px;">
	<div ng-view></div>
</section>
<section id="invoice" class="main"
	style="min-height: 40px;">
	<div ng-view></div>
</section>
<section id="receiveMoney" class="main"
	style="min-height: 40px;">
	<div ng-view></div>
</section>




<jsp:include page="/jsp/left.jsp" />
<jsp:include page="/jsp/footer.jsp" />
<script src="${ctx}/js/app/billContract.js"></script>
<script src="${ctx}/js/app/receiptMgmt.js"></script>
<script src="${ctx}/js/app/invoiceMgmt.js"></script>
<script src="${ctx}/js/app/receiveMoneyMgmt.js"></script>
<script type="text/javascript">
	var permissionList;
	angular.element(document).ready(function() {
		$.get('/CIMS/login/getUserPermission.do', function(data) {
		permissionList = data;
		angular.bootstrap(document.getElementById("invoice"), [ "invoice" ]);
		angular.bootstrap(document.getElementById("receipt"), [ "receipt" ]);
		angular.bootstrap(document.getElementById("receiveMoney"), [ "receiveMoney" ]);
	});
	});
</script>
<script>
	$(function(){
		$('dd').find('ul').css("display","none");
		$('#bill-ul').css("display","block");
		var currentPage = sessionStorage.getItem("currentPage");
		if(currentPage){
			$("#"+currentPage).addClass("active");
		}
	})
</script>
</body>
</html>