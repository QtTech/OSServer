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
	$("#attachname").html(os_lang.attach_name);
	$("#attachprice").html(os_lang.attach_price);
	$("#attachtype").html(os_lang.attach_type);
	$("#attachTypeTable").html(os_lang.attach_per_table);
	$("#attachTypePerson").html(os_lang.attach_per_person);
	
	$("#erroradd").css("display", "none");
	$("#btnEnvCancel").click(function(){
		window.close();
	});
	
	$("#btnEnvConfirm").click(function(){
		var attachname = $("#attachnameVal").val();
		var attachprice = $("#attachpriceVal").val();
		var attachtype = $("#attachtypeVal").val();
		var data = {};
		data.id = sessionStorage.attachid;
		data.price = attachprice;
		data.name = attachname;
		data.type = attachtype;
		
		var options = {};
		options.data = data;
		options.url = serverurl + "/attachment/updateattachment";
		options.successCallback = function(data) {
			console.log(data);
			if (data.status == 0) {
				window.returnValue=true;
				window.close();
			} else {
				$("#erroradd").html(os_lang.update_fail);
				$("#erroradd").css("display", "");
			}
		};
		options.errorCallback = function() {
			$("#erroradd").html(os_lang.update_fail);
			$("#erroradd").css("display", "");
		};
		swan.ajax(options);
	});
	
	if (!sessionStorage.attachid) {
		sessionStorage.attachid = window.dialogArguments;
	}
	
	var data = {};
	data.id = sessionStorage.attachid;
	var options = {};
	options.data = data;
	options.url = serverurl + "/attachment/queryattachmentbyid";
	options.successCallback = function(data) {
		if (data.status == 0) {
			$("#attachnameVal").val(data.data.name);
			$("#attachpriceVal").val(data.data.price);
			$("#attachtypeVal").val(data.data.type);
		}
	};
	swan.ajax(options);
	
});