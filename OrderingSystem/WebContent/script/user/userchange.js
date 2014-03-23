/*
 *
 */
var server;

swan.onReady(function() {
	$(function() {
		$(window).resize(resizeWindow);
	});
	
	server = swan.getServerUrl("baseUrl");

	$("#userName").html(os_lang.user_name);
	$("#userCode").html(os_lang.secret_code);
	$("#userCodeConfirm").html(os_lang.confirm_secret_code);
	$("#userCategory").html(os_lang.user_role);

	$("#role_waiter").html(os_lang.role_waiter);
	$("#role_cook").html(os_lang.role_cook);
	$("#role_bartender").html(os_lang.role_bartender);
	$("#role_cashier").html(os_lang.role_cashier);
	$("#role_supervisor").html(os_lang.role_supervisor);
	$("#role_manager").html(os_lang.role_manager);
	
	$("#btnUserConfirm").html(os_lang.confirm);
	$("#btnUserCancel").html(os_lang.cancel);
	$("#btnUserCancel").click(function(){
		window.close();
	});

	$("#btnUserConfirm").click(function(){
		var userNamevalue = $("#userNamevalue").val();
		var userCodevalue = $("#userCodevalue").val();
		var userCodeConfirmvalue = $("#userCodeConfirmvalue").val();
		var userCategory = $("#userCategoryvalue").val();
		
		if (userNamevalue != '' && userCodevalue != '' && userCodeConfirmvalue != '') {
			if (userCodevalue == userCodeConfirmvalue) {
				var data = {};
				data.username = userNamevalue;
				data.code = userCodeConfirmvalue;
				data.category = userCategory;
				
				var options = {};
				options.data = data;
				options.url = server + "/user/update";
				options.successCallback = function(data) {
					if (data.status == 0) {
						window.returnValue=true;
						window.close();
					}
				};
				options.errorCallback = function() {
					
				};
				swan.ajax(options);
			} else {
				alert(os_lang.code_not_same);
			}
		} else {
			alert(os_lang.input_not_null);
		}
		
	});
	
	var userChangeName = window.dialogArguments;
	if (!sessionStorage.userChangeName) {
		sessionStorage.userChangeName = userChangeName;
	}

	var data = {};
	data.username = sessionStorage.userChangeName;
	var options = {};
	options.data = data;
	options.url = server + "/user/querybyid";
	options.successCallback = function(data) {
		if (data.status == 0) {
			$("#userNamevalue").val(data.data.username);
			$("#userCodevalue").val(data.data.code);
			$("#userCodeConfirmvalue").val(data.data.code);
			$("#userCategoryvalue").val(data.data.category);
		}
	};
	options.errorCallback = function() {
		
	};
	swan.ajax(options);
	
});
