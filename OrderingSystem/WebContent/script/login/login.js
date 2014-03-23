/**
 * 
 */

function showError(errorMsg) {
	var info_a='<div id="l_b_prompt"><img src="/OrderingSystem/image/login/icon_15.png" width="22" height="22" /><span id="loginError">';
	var info_b='</span></div>';
	$("#message").html(info_a + errorMsg + info_b);
};

function login(url) {
	
	var data = {};
	data.code = $("#codeVal").val();
	data.category = $("#usercategory").val();

	var option = {};
	option.url = url + "/user/login";
	option.data = data;
	option.successCallback = function(data) {
		if (data.status == 0) {
			sessionStorage.username = data.data.username;
			location.href="/OrderingSystem/page/menulist.html";
		} else {
			showError(os_lang.login_fail);
		}
	};
	option.errorCallback = function(data) {
		
	};
	os.ajax(option);

}

swan.onReady(function(){
	
	var serverurl = swan.getServerUrl("baseUrl");
	
	$("#usercode").html(os_lang.secret_code);
	$("#category").html(os_lang.user_role);
	$("#category5").html(os_lang.role_supervisor);
	$("#category5").val(6);
	$("#category6").html(os_lang.role_manager);
	$("#category6").val(7);
	
	$(document).keydown(function (event) {
		if (event.keyCode == 13) {
			login(serverurl);
		}
	});
	
	$("#submitBtn").click(function(){
		login(serverurl);
	});
});