$(document).ready(function() {
	$("#newTask").on('click',function() {
		$(".tip").fadeIn(200);
		return false;
	});

	$(".tiptop a").click(function() {
		$(".tip").fadeOut(200);
	});

	$(".sure").click(function() {
		$(".tip").fadeOut(100);
	});

	$(".cancel").click(function() {
		$(".tip").fadeOut(100);
	});
});