function getSkinStyle(server) {
	var options = {};
	options.url = server + "/clientstyle/getskinstyle";
	options.successCallback = function(data) {
		if (data.status == 0) {
			switch(data.data) {
			case 1:
				$("#currentSkinValue").html(os_lang.skin_chinese);
				break;
			case 2:
				$("#currentSkinValue").html(os_lang.skin_korean);
				break;
			case 3:
				$("#currentSkinValue").html(os_lang.skin_western);
				break;
			}
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
	
	$("#currentSkin").html(os_lang.current_client_skin);
	$("#newSkin").html(os_lang.selectable_client_skin);
	$("#submitBt").html(os_lang.submit);
	$("#skin_chinese").html(os_lang.skin_chinese);
	$("#skin_korean").html(os_lang.skin_korean);
	$("#skin_western").html(os_lang.skin_western);
	getSkinStyle(server);
	
	$("#submitBt").click(function() {
		var options = {};
		var data = {};
		data.skin = $("#newSkinValue").val();
		options.url = server + "/clientstyle/setskinstyle";
		options.data = data;
		options.successCallback = function(data) {
			if (data.status == 0) {
				getSkinStyle(server);
			}
		};
		swan.ajax(options);
	});
	
});