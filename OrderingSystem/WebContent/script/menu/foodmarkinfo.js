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
	
	var options = {};
	options.url = serverurl + "/food/querysimplefoodlist";
	options.successCallback = function(data) {
		if (data.status == 0) {
			var simplefoodlist = data.data;
			var food = "";
			for (var i in simplefoodlist) {
				food += "<option value=\""+simplefoodlist[i].id+"\">"+simplefoodlist[i].name+"</option>";
			}
			$("#foodMarkId").html(food);
		}
	};
	swan.ajax(options);
	
	$("#btnEnvConfirm").click(function(){
		var foodmarkid = $("#foodMarkId").val();
		var foodmarkname = $("#foodMarkName").val();
		console.log("id:" + foodmarkid + ", name:" + foodmarkname);
		var data = {};
		data.foodid = foodmarkid;
		data.name = foodmarkname;
		
		var options = {};
		options.data = data;
		options.url = serverurl + "/menuitem/insertfoodremark";
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
			$("#erroradd").html(os_lang.add_fail);
			$("#erroradd").css("display", "");
		};
		swan.ajax(options);
	});
	
});