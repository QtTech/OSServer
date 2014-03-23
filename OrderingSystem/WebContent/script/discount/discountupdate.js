function getDiscount(server) {
	var options = {};
	options.url = server + "/bill/getmaxdiscount";
	options.successCallback = function(data) {
		if (data.status == 0) {
			$("#discountValue").html(data.data.discount);
			$("#deductionValue").html(data.data.deduction);
		}
	};
	swan.ajax(options);
}

swan.onReady(function() {
	$(function() {
		$("#header").load("/OrderingSystem/page/layout/header.html",loadMenu);
		$("#footer").load("/OrderingSystem/page/layout/footer.html",resizeWindow);
		$(window).resize(resizeWindow);
	});
	
	var server = swan.getServerUrl("baseUrl");
	
	$("#currentDiscount").html(os_lang.current_max_discount);
	$("#currentDeduction").html(os_lang.current_max_deduction);

	getDiscount(server);
	
	$("#discountBt").click(function() {
		if($("#discount").val()) {
			var options = {};
			var data = {};
			data.discount = $("#discount").val();
			data.deduction = -1;
			options.url = server + "/bill/updatemaxdiscount";
			options.data = data;
			options.successCallback = function(data) {
				if (data.status == 0) {
					getDiscount(server);
				}
			};
			swan.ajax(options);
		} else {
			alert(os_lang.input_not_null);
		}
	});
	
	$("#deductionBt").click(function() {
		if($("#deduction").val()) {
			var options = {};
			var data = {};
			data.deduction = $("#deduction").val();
			data.discount = -1;
			
			options.url = server + "/bill/updatemaxdiscount";
			options.data = data;
			options.successCallback = function(data) {
				if (data.status == 0) {
					getDiscount(server);
				}
			};
			swan.ajax(options);
		} else {
			alert(os_lang.input_not_null);
		}
	});
	
});