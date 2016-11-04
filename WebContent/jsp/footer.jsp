</section>
<!-- <footer> </footer> -->
<script type="text/javascript" src="${ctx}/js/lib/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/pageTurn.js"></script>   
<script type="text/javascript" src="${ctx}/js/lib/jquery.json-2.2.min.js"></script>   
<!-- AngularJS dependences -->
<script type="text/javascript" src="${ctx}/js/lib/angular/angular.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/angular/angular-route.js"></script>
<script type="text/javascript">
	$(function() {
		//Ã©Â¡Â¶Ã©ÂÂ¨Ã¥Â¯Â¼Ã¨ÂÂªÃ¥ÂÂÃ¦ÂÂ¢
		$(".nav li a").click(function() {
			$(".nav li a.selected").removeClass("selected");
			$(this).addClass("selected");
		});
	});
	
	$(function() {
		//Ã¥Â¯Â¼Ã¨ÂÂªÃ¥ÂÂÃ¦ÂÂ¢
		$(".menuson li").click(function() {
			$(".menuson li.active").removeClass("active");
			$(this).addClass("active");
		});

		$('.title').click(function() {
			var $ul = $(this).next('ul');
			$('dd').find('ul').slideUp();
			if ($ul.is(':visible')) {
				$(this).next('ul').slideUp();
			} else {
				$(this).next('ul').slideDown();
			}
		});
	});
</script>
