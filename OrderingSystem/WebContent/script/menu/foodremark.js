function searchByName() {
	var p = swan.pagination.getPagination('pagination1');
	var server = swan.getServerUrl("baseUrl");
	if (!$("#foodIdInput").val()) {
		p.remoteSource.url = server + "/food/queryfoodmarklist";
	} else {
		p.remoteSource.url = server + "/food/queryfoodmarklistbyid";
		p.remoteSource.data.foodid = $("#foodIdInput").val();
	}
	swan.pagination.freshCurrentPage('pagination1');

};

function goMenuAdd(paraData) {
   	var width = 400;
   	var height = 140;
   	var top = (screen.height - height)/2;
   	var left = (screen.width - width)/2;
   	var para = "dialogWidth:" + width + "px; dialogHeight:" + height + "px; dialogTop:" + top + "px; dialogLeft:" + left +"px; resizable:no; center:yes; status:no; location:no; scroll:no";

	if (window.showModalDialog("/OrderingSystem/page/menu/foodmark_add.html", paraData, para)) {
		swan.pagination.freshCurrentPage('pagination1');
	}
};

function goMenuChange(paraData) {
   	var width = 400;
   	var height = 140;
   	var top = (screen.height - height)/2;
   	var left = (screen.width - width)/2;
   	var para = "dialogWidth:" + width + "px; dialogHeight:" + height + "px; dialogTop:" + top + "px; dialogLeft:" + left +"px; resizable:no; center:yes; status:no; location:no; scroll:no";

	if (window.showModalDialog("/OrderingSystem/page/menu/foodmark_change.html", paraData, para)) {
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
	$("#foodIdLabel").html(os_lang.food_name);
	$("#searchBtn").html(os_lang.search);
	$("#addBtn").html(os_lang.add);
	
	$("#addBtn").click(function() {
		goMenuAdd();
	});
	
	var server = swan.getServerUrl("baseUrl");

	$("#searchBtn").click(function() {
		searchByName();
	});

	$("#foodIdInput").keypress(function() {
		if (event.keyCode == 13) {
			searchByName();
		}
	});
	//Create a pagination control
	swan.pagination.onReady(function() {
		//Remote Source
		var remoteData = {
			url : server + "/food/queryfoodmarklist",
			order : "desc",
			orderField : "id",
			currentPage : 1,
			type : "post",
			data : {}
		};
		//Create a pagination
		swan.pagination.init({
			template : "foodmarklist_template",
			templatePath : "/style/menu/",
			id : "pagination1",
			containerId : "foodremarkListContent",
			pager : true,
			pageFront : true,
			remoteSource : remoteData,
			pageSize : 10
		});
	}, {
		skin : "default",
		onDataLoadEnd : resizeWindow
	});

	$("#foodremarkListContent").on("click", ".viewremove", function() {

		var data = {};
		data.id = this.getAttribute("foodmarkid");
		
		var options = {};
		options.data = data;
		options.url = server + "/menuitem/deletefoodremark";
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
	
	$("#foodremarkListContent").on("click", ".viewchange", function() {
		goMenuChange(this.getAttribute("foodmarkid"));
	});
});