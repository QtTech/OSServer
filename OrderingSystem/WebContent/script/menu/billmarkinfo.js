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
	$("#billmarkid").html(os_lang.billremark_id);
	$("#billmarkname").html(os_lang.billremark_name);
	
	$("#erroradd").css("display", "none");
	$("#btnEnvCancel").click(function(){
		window.close();
	});
	
	$("#btnEnvConfirm").click(function(){
		var billmarkid = $("#billMarkId").val();
		var billmarkname = $("#billMarkName").val();
		console.log("id:" + billmarkid + ", name:" + billmarkname);
		var data = {};
		data.id = billmarkid;
		data.name = billmarkname;
		
		var options = {};
		options.data = data;
		options.url = serverurl + "/menuitem/insertbillremark";
		options.successCallback = function(data) {
			console.log(data);
			if (data.status == 0) {
				window.returnValue=true;
				window.close();
			} else {
				$("#erroradd").html(os_lang.billremark_exists);
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