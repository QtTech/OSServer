/**
 * 
 */

var os = {};
os.serverurl;
/*
$.ajax({
	url: "/OrderingSystem/webConfig.xml",
	type:"GET",
	async:false,
	success: function (data) {
		os.serverurl = data;
	},
	error: function () { 
		alert("error"); 
	}
});
*/
os.getServerUrl = function(key) {
	return $(os.serverurl).find("[key=" + key + "]").text();
};

os.ajax = function (option) {
	$.ajax({
		url:option.url,
		type:"post",
		dataType:"json",
		data:JSON.stringify(option.data),
		contentType:"application/json;charset=utf-8",
		success: function (data) {
			option.successCallback(data);
		},
        error: function (data) {
        	option.errorCallback(data);
        }
	});
};

function resizeWindow() {
	$('.page_ctn .footer').css('margin-top', 0);
	var _mg = $(window).height() - $('.page_ctn').outerHeight();
	$('.page_ctn .footer').css('margin-top', (_mg > 0 ? _mg : 0) + 'px');
}

//write cookies
function setCookie(name, value) {
	var Days = 30;
	var exp = new Date(); 
	exp.setTime(exp.getTime() + Days*24*60*60*1000);
	document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
}

//read cookies
function getCookie(name) {
	var arr, reg = new RegExp("(^| )"+name+"=([^;]*)(;|$)");
	if(arr = document.cookie.match(reg)) {
		return unescape(arr[2]);
	} else {
		return null;
	}
}
//remove cookies
function delCookie(name) {
	var exp = new Date();
	exp.setTime(exp.getTime() - 1);
	var cval=getCookie(name);
	if(cval != null) {
		document.cookie= name + "=" + cval + ";expires=" + exp.toGMTString();
	} 
}

var locateUrl = function(url){
//	sessionStorage.currentUrl = url;
	window.location.href=url;
};