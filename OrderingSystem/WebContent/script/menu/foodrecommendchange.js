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
	$("#foodrecommendid").html(os_lang.food_name);
	$("#foodrecommendname").html(os_lang.food_recommond_name);
	
	$("#erroradd").css("display", "none");
	$("#btnEnvCancel").click(function(){
		window.close();
	});
	
	if (!sessionStorage.foodrecommendid) {
		sessionStorage.foodrecommendid = window.dialogArguments;
	}
	
	var options = {};
	options.url = serverurl + "/food/querysimplefoodlist";
	options.successCallback = function(data) {
		if (data.status == 0) {
			var simplefoodlist = data.data;
			var food = "";
			for (var i in simplefoodlist) {
				food += "<option value=\""+simplefoodlist[i].id+"\">"+simplefoodlist[i].name+"</option>";
			}
			$("#foodrecommendName").html(food);
		}
	};
	swan.ajax(options);
	
	var data = {};
	data.id = sessionStorage.foodrecommendid;
	var options = {};
	options.data = data;
	options.url = serverurl + "/food/queryfoodrecommendname";
	options.successCallback = function(data) {
		if (data.status == 0) {
			$("#foodrecommendId").html(data.data.foodname);
			$("#foodrecommendName").val(data.data.recommendid);
		}
	};
	swan.ajax(options);
	
	$("#btnEnvConfirm").click(function(){
		var foodrecommendname = $("#foodrecommendName").val();
		var data = {};
		data.id = sessionStorage.foodrecommendid;
		data.recommendid = foodrecommendname;
		
		var options = {};
		options.data = data;
		options.url = serverurl + "/menuitem/updatefoodrecommend";
		options.successCallback = function(data) {
			console.log(data);
			if (data.status == 0) {
				window.returnValue=true;
				window.close();
			} else if (data.status == 2) {
				$("#erroradd").html(os_lang.food_not_recommond_self);
				$("#erroradd").css("display", "");
			} else {
				$("#erroradd").html(os_lang.food_recommond_exists);
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