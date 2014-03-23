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
	
	var options = {};
	options.url = serverurl + "/food/querysimplefoodlist";
	options.successCallback = function(data) {
		if (data.status == 0) {
			var simplefoodlist = data.data;
			var food = "";
			for (var i in simplefoodlist) {
				food += "<option value=\""+simplefoodlist[i].id+"\">"+simplefoodlist[i].name+"</option>";
			}
			$("#foodrecommendId").html(food);
			$("#foodrecommendName").html(food);
		}
	};
	swan.ajax(options);
	
	$("#btnEnvConfirm").click(function(){
		var foodrecommendid = $("#foodrecommendId").val();
		var foodrecommendname = $("#foodrecommendName").val();
		console.log("id:" + foodrecommendid + ", name:" + foodrecommendname);
		var data = {};
		data.foodid = foodrecommendid;
		data.recommendid = foodrecommendname;
		
		var options = {};
		options.data = data;
		options.url = serverurl + "/menuitem/insertfoodrecommend";
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
			$("#erroradd").html(os_lang.add_fail);
			$("#erroradd").css("display", "");
		};
		swan.ajax(options);
	});
	
});