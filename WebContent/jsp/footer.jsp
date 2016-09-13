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
		//é¡¶é¨å¯¼èªåæ¢
		$(".nav li a").click(function() {
			$(".nav li a.selected").removeClass("selected");
			$(this).addClass("selected");
		});
	});
	
	$(function() {
		//å¯¼èªåæ¢
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

	/* $(function() {
		//é¡¶é¨å¯¼èªåæ¢
		var $pages = $(".tcdPageCode");
		var $tablelist = $(".tablelist");
		console.log($pages.length);
		console.log($tablelist.length);
		if($pages.length != 0){
			$(".tcdPageCode").createPage({
		        pageCount:10,
		        current:1,
		        backFn:function(p){
		            console.log("分页！");
		        }
		    });
		}
	}); */
	
</script>
