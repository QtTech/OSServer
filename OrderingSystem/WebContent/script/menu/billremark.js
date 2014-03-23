function searchByName() {
	var p = swan.pagination.getPagination('pagination1');
	var server = swan.getServerUrl("baseUrl");
	if (!$("#billmarkNameLabel").val()) {
		p.remoteSource.url = server + "/food/querybillmarklist";
	} else {
		p.remoteSource.url = server + "/food/querybillmarklistbyid";
		p.remoteSource.data.id = $("#billmarkNameLabel").val();
	}
	swan.pagination.freshCurrentPage('pagination1');
};

function goMenuAdd(paraData) {
   	var width = 400;
   	var height = 140;
   	var top = (screen.height - height)/2;
   	var left = (screen.width - width)/2;
   	var para = "dialogWidth:" + width + "px; dialogHeight:" + height + "px; dialogTop:" + top + "px; dialogLeft:" + left +"px; resizable:no; center:yes; status:no; location:no; scroll:no";

	if (window.showModalDialog("/OrderingSystem/page/menu/billmark_add.html", paraData, para)) {
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
	$("#billmarkIdLabel").html(os_lang.billremark_id);
	$("#searchBtn").html(os_lang.search);
	$("#addBtn").html(os_lang.add);
	
	$("#addBtn").click(function() {
		goMenuAdd();
	});
	
	var server = swan.getServerUrl("baseUrl");

	$("#searchBtn").click(function() {
		searchByName();
	});

	$("#billmarkNameLabel").keypress(function() {
		if (event.keyCode == 13) {
			searchByName();
		}
	});
	//Create a pagination control
	swan.pagination.onReady(function() {
		//Remote Source
		var remoteData = {
			url : server + "/food/querybillmarklist",
			order : "desc",
			orderField : "id",
			currentPage : 1,
			type : "post",
			data : {}
		};
		//Create a pagination
		swan.pagination.init({
			template : "billmarklist_template",
			templatePath : "/style/menu/",
			id : "pagination1",
			containerId : "billmarkListContent",
			pager : true,
			pageFront : true,
			remoteSource : remoteData,
			pageSize : 10
		});
	}, {
		skin : "default",
		onDataLoadEnd : resizeWindow
	});

	$("#billmarkListContent").on("click", ".view", function() {
		var data = {};
		data.id = this.getAttribute("billmarkid");
		
		var options = {};
		options.data = data;
		options.url = server + "/menuitem/deletebillremark";
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

});