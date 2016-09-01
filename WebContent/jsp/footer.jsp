<footer> </footer>
<script type="text/javascript" src="../../js/lib/jquery-1.9.1.min.js"></script>
<!-- AngularJS dependences -->
<script type="text/javascript" src="../../js/lib/angular/angular.js"></script>
<script type="text/javascript" src="../../js/lib/angular/angular-route.js"></script>


<script type="text/javascript">
	$(function() {
		//顶部导航切换
		$(".nav li a").click(function() {
			$(".nav li a.selected").removeClass("selected");
			$(this).addClass("selected");
		});
	});

	$(function() {
		//导航切换
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
