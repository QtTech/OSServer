
swan.onReady(function() {
	$(function() {
		$("#header").load("/OrderingSystem/page/layout/header.html",loadMenu);
		$("#footer").load("/OrderingSystem/page/layout/footer.html",resizeWindow);
		$(window).resize(resizeWindow);
	});
	
	var server = swan.getServerUrl("baseUrl");
	
	$("#currentVersion").html(os_lang.current_version);
	$("#generateBtn").html(os_lang.execute);
	$("#versionUpdate").html(os_lang.version_upgrade);
	$("#generateBtn").click(function() {
		$("#corver").css("display", "");
       	$("#sending").css("display", "");
       	
		var options = {};
		options.url = server + "/menuitem/generatenewversion";
		options.successCallback = function(data) {
			$("#corver").css("display", "none");
           	$("#sending").css("display", "none");
           	
			if (data.status == 0) {
				$("#versionvalue").html(data.data);
			} else {
				alert(os_lang.version_gen_fail);
			}
		};
		swan.ajax(options);
	});
	
    $("#sending").attr("src", "/OrderingSystem/image/loading.gif");
	
	var options = {};
	options.url = server + "/upgrade/getversion";
	options.successCallback = function(data) {
		if (data.status == 0) {
			$("#versionvalue").html(data.data);
		}
	};
	swan.ajax(options);
	
});