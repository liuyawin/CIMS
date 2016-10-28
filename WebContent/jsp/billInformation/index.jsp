<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<jsp:include page="/jsp/top.jsp" />

<section id="contract" class="main" ng-app="contract"
	style="min-height: 40px;">
	<div ng-view></div>
</section>
<section id="receipt" class="main" ng-app="receipt"
	style="min-height: 40px;">
	<div ng-view></div>
</section>
<section id="invoice" class="main" ng-app="invoice"
	style="min-height: 40px;">
	<div ng-view></div>
</section>
<section id="receiveMoney" class="main" ng-app="receiveMoney"
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
	angular.element(document).ready(function() {
		//angular.bootstrap(document.getElementById("contract"), [ "contract" ]);
		angular.bootstrap(document.getElementById("invoice"), [ "invoice" ]);
		angular.bootstrap(document.getElementById("receipt"), [ "receipt" ]);
		angular.bootstrap(document.getElementById("receiveMoney"), [ "receiveMoney" ]);
	});
</script>
</body>
</html>