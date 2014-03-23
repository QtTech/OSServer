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
	$("#foodmarkid").html(os_lang.food_name);
	$("#foodmarkname").html(os_lang.food_remark);
	
	$("#erroradd").css("display", "none");
	$("#btnEnvCancel").click(function(){
		window.close();
	});
	
	if (!sessionStorage.foodremarkid) {
		sessionStorage.foodremarkid = window.dialogArguments;
	}
	
	var data = {};
	data.id = sessionStorage.foodremarkid;
	var options = {};
	options.data = data;
	options.url = serverurl + "/food/queryfoodremarkname";
	options.successCallback = function(data) {
		if (data.status == 0) {
			$("#foodMarkId").html(data.data.foodname);
			$("#foodMarkName").val(data.data.name);
		}
	};
	swan.ajax(options);
	
	$("#btnEnvConfirm").click(function(){
		var foodmarkname = $("#foodMarkName").val();
		var data = {};
		data.id = sessionStorage.foodremarkid;
		data.name = foodmarkname;
		
		var options = {};
		options.data = data;
		options.url = serverurl + "/menuitem/updatefoodremark";
		options.successCallback = function(data) {
			console.log(data);
			if (data.status == 0) {
				window.returnValue=true;
				window.close();
			} else {
				$("#erroradd").html(os_lang.food_remark_exists);
				$("#erroradd").css("display", "");
			}
		};
		options.errorCallback = function() {
			$("#erroradd").html(os_lang.update_fail);
			$("#erroradd").css("display", "");
		};
		swan.ajax(options);
	});
	
});