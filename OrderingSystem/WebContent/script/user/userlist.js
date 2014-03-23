function searchByName() {
	var p = swan.pagination.getPagination('pagination1');
	var server = swan.getServerUrl("baseUrl");
	if (!$("#userNameLabel").val()) {
		p.remoteSource.url = server + "/user/query";
	} else {
		p.remoteSource.url = server + "/user/querybyname";
		p.remoteSource.data.username = $("#userNameLabel").val();
	}
	swan.pagination.freshCurrentPage('pagination1');
};

function goUserAdd(paraData) {
   	var width = 410;
   	var height = 220;
   	var top = (screen.height - height)/2;
   	var left = (screen.width - width)/2;
   	var para = "dialogWidth:" + width + "px; dialogHeight:" + height + "px; dialogTop:" + top + "px; dialogLeft:" + left +"px; resizable:no; center:yes; status:no; location:no; scroll:no";

	if (window.showModalDialog("/OrderingSystem/page/user/user_add.html", paraData, para)) {
		swan.pagination.freshCurrentPage('pagination1');
	}
};

function goUserChange(paraData) {
   	var width = 410;
   	var height = 220;
   	var top = (screen.height - height)/2;
   	var left = (screen.width - width)/2;
   	var para = "dialogWidth:" + width + "px; dialogHeight:" + height + "px; dialogTop:" + top + "px; dialogLeft:" + left +"px; resizable:no; center:yes; status:no; location:no; scroll:no";

	if (window.showModalDialog("/OrderingSystem/page/user/user_change.html", paraData, para)) {
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
	$("#userIdLabel").html(os_lang.user_name);
	$("#searchBtn").html(os_lang.search);
	$("#addBtn").html(os_lang.add);
	
	$("#addBtn").click(function() {
		goUserAdd();
	});
	
	var server = swan.getServerUrl("baseUrl");

	$("#searchBtn").click(function() {
		searchByName();
	});

	$("#userNameLabel").keypress(function() {
		if (event.keyCode == 13) {
			searchByName();
		}
	});
	//Create a pagination control
	swan.pagination.onReady(function() {
		//Remote Source
		var remoteData = {
			url : server + "/user/query",
			order : "desc",
			orderField : "id",
			currentPage : 1,
			type : "post",
			data : {}
		};
		//Create a pagination
		swan.pagination.init({
			template : "userlist_template",
			templatePath : "/style/user/",
			id : "pagination1",
			containerId : "userListContent",
			pager : true,
			pageFront : true,
			remoteSource : remoteData,
			pageSize : 10
		});
	}, {
		skin : "default",
		onDataLoadEnd : resizeWindow
	});

	$("#userListContent").on("click", ".viewchange", function() {
		console.log("change " + this.getAttribute("userid"));
		goUserChange(this.getAttribute("userid"));
	});
	
	$("#userListContent").on("click", ".viewremove", function() {
		var data = {};
		data.username = this.getAttribute("userid");
		
		var options = {};
		options.data = data;
		options.url = server + "/user/delete";
		options.successCallback = function(data) {
			if (data.status == 0) {
				swan.pagination.freshCurrentPage('pagination1');
			}
		};
		options.errorCallback = function() {
			
		};
		swan.ajax(options);
	});
});