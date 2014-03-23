function searchByName() {
	var p = swan.pagination.getPagination('pagination1');
	var server = swan.getServerUrl("baseUrl");
	if (!$("#attachIdInput").val()) {
		p.remoteSource.url = server + "/attachment/queryattachments";
	} else {
		p.remoteSource.url = server + "/attachment/queryattachmentsbyname";
		p.remoteSource.data.name = $("#attachIdInput").val();
	}
	swan.pagination.freshCurrentPage('pagination1');
};

function goAttachAdd(paraData) {
   	var width = 400;
   	var height = 140;
   	var top = (screen.height - height)/2;
   	var left = (screen.width - width)/2;
   	var para = "dialogWidth:" + width + "px; dialogHeight:" + height + "px; dialogTop:" + top + "px; dialogLeft:" + left +"px; resizable:no; center:yes; status:no; location:no; scroll:no";

	if (window.showModalDialog("/OrderingSystem/page/attach/attachsetting_add.html", paraData, para)) {
		swan.pagination.freshCurrentPage('pagination1');
	}
};

function goAttachChange(paraData) {
   	var width = 400;
   	var height = 140;
   	var top = (screen.height - height)/2;
   	var left = (screen.width - width)/2;
   	var para = "dialogWidth:" + width + "px; dialogHeight:" + height + "px; dialogTop:" + top + "px; dialogLeft:" + left +"px; resizable:no; center:yes; status:no; location:no; scroll:no";

	if (window.showModalDialog("/OrderingSystem/page/attach/attachsetting_change.html", paraData, para)) {
		swan.pagination.freshCurrentPage('pagination1');
	}
};

swan.onReady(function() {
	$(function() {
		$("#header").load("/OrderingSystem/page/layout/header.html",loadMenu);
		$("#footer").load("/OrderingSystem/page/layout/footer.html",resizeWindow);
		$(window).resize(resizeWindow);
	});

		
	//language
	$("#attachIdLabel").html(os_lang.attach_name);
	$("#searchBtn").html(os_lang.search);
	$("#addBtn").html(os_lang.add);
	
	$("#addBtn").click(function() {
		goAttachAdd();
	});
	
	var server = swan.getServerUrl("baseUrl");

	$("#searchBtn").click(function() {
		searchByName();
	});

	$("#attachIdInput").keypress(function() {
		if (event.keyCode == 13) {
			searchByName();
		}
	});
	//Create a pagination control
	swan.pagination.onReady(function() {
		//Remote Source
		var remoteData = {
			url : server + "/attachment/queryattachments",
			order : "desc",
			orderField : "id",
			currentPage : 1,
			type : "post",
			data : {}
		};
		//Create a pagination
		swan.pagination.init({
			template : "attachsetting_template",
			templatePath : "/style/attach/",
			id : "pagination1",
			containerId : "attachSettingContent",
			pager : true,
			pageFront : true,
			remoteSource : remoteData,
			pageSize : 10
		});
	}, {
		skin : "default",
		onDataLoadEnd : resizeWindow
	});

	$("#attachSettingContent").on("click", ".viewremove", function() {

		var data = {};
		data.id = this.getAttribute("attachid");
		
		var options = {};
		options.data = data;
		options.url = server + "/attachment/deleteattachment";
		options.successCallback = function(data) {
			console.log(data);
			if (data.status == 0) {
				swan.pagination.freshCurrentPage('pagination1');
			} else {
				
			}
		};
		options.errorCallback = function() {
			
		};
		swan.ajax(options);
	
	
	});
	
	$("#attachSettingContent").on("click", ".viewchange", function() {
		goAttachChange(this.getAttribute("attachid"));
	});
});