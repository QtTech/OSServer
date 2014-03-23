/*
 *
 */
var serverurl;

swan.onReady(function() {
	$(function() {
		$(window).resize(resizeWindow);
	});
	
	serverurl = swan.getServerUrl("baseUrl");

	$("#btnEnvConfirm").html(os_lang.confirm);
	$("#btnEnvCancel").html(os_lang.cancel);
	$("#categoryid").html(os_lang.category_id);
	$("#categoryname").html(os_lang.category_name);
	
	$("#erroradd").css("display", "none");
	$("#btnEnvCancel").click(function(){
		window.close();
	});
	
	$("#btnEnvConfirm").click(function(){
		var categoryid = $("#categoryId").val();
		var categoryname = $("#categoryName").val();
		console.log("id:" + categoryid + ", name:" + categoryname);
		var data = {};
		data.id = categoryid;
		data.name = categoryname;
		
		var options = {};
		options.data = data;
		options.url = serverurl + "/menuitem/insertcategory";
		options.successCallback = function(data) {
			console.log(data);
			if (data.status == 0) {
				window.returnValue=true;
				window.close();
			} else {
				$("#erroradd").html(os_lang.category_exists);
				$("#erroradd").css("display", "");
			}
		};
		options.errorCallback = function() {
			$("#erroradd").html(os_lang.add_fail);
			$("#erroradd").css("display", "");
		};
		swan.ajax(options);
	});
	
});