function searchByName() {
	var p = swan.pagination.getPagination('pagination1');
	var server = swan.getServerUrl("baseUrl");
	p.remoteSource.data.id = $("#curCategoryVal").val();
	p.remoteSource.url = server + "/food/querymenulistbycid";
	if (!$("#menuNameLabel").val()) {
		p.remoteSource.data.name = null;
	} else {
		p.remoteSource.data.name = $("#menuNameLabel").val();
	}
	swan.pagination.freshCurrentPage('pagination1');
};

function goMenuAdd(paraData) {
   	var width = 810;
   	var height = 560;
   	var top = (screen.height - height)/2;
   	var left = (screen.width - width)/2;
   	var para = "dialogWidth:" + width + "px; dialogHeight:" + height + "px; dialogTop:" + top + "px; dialogLeft:" + left +"px; resizable:no; center:yes; status:no; location:no; scroll:no";

	if (window.showModalDialog("/OrderingSystem/page/menu/menu_add.html", paraData, para)) {
		swan.pagination.freshCurrentPage('pagination1');
	}
};

function goMenuDeleteSelected(paraData) {
	console.log("goMenuDeleteSelected");
	
	var data = new Array();
	$("#menuListContent .viewselect").each(function() {
		if (this.checked) {
			data.push(this.getAttribute("menuid"));
		}
	});
	
	if (data.length > 0) {
		var server = swan.getServerUrl("baseUrl");
		var sendData = {};
		sendData.dishes = data;
		sendData.orgcategoryid = $("#curCategoryVal").val();
		sendData.categoryid = $("#dstCategoryVal").val();
		
		var options = {};
		options.data = sendData;
		options.url = server + "/menuitem/deleteSelectedDishes";
		options.successCallback = function(data) {
			swan.pagination.freshCurrentPage('pagination1');
		};
		options.errorCallback = function() {
			
		};
		swan.ajax(options);
	} else {
		alert("Nothing was selected!");
	}
};

function copyOrMoveSelected(paraData) {
	console.log("copyOrMoveSelected");
	
	if ($("#dstCategoryVal").val() == $("#curCategoryVal").val()) {
		alert("Cannot move or copy to the same category");
		return;
	}
	
	var data = new Array();
	$("#menuListContent .viewselect").each(function() {
		if (this.checked) {
			data.push(this.getAttribute("menuid"));
		}
	});
	
	if (data.length > 0) {
		if ($("#dstCategory").val() == 0) {
			console.log("Start to Copy...");
		} else {
			console.log("Start to Move...");
		}
		
		var server = swan.getServerUrl("baseUrl");
		
		var sendData = {};
		sendData.categoryid = $("#dstCategoryVal").val();
		sendData.orgcategoryid = $("#curCategoryVal").val();
		sendData.type = $("#dstCategory").val();
		sendData.dishes = data;

		var options = {};
		options.data = sendData;
		options.url = server + "/menuitem/updateDishesCategory";
		options.successCallback = function(data) {
			swan.pagination.freshCurrentPage('pagination1');
		};
		options.errorCallback = function() {
			
		};
		swan.ajax(options);
	} else {
		alert("Nothing was selected!");
	}
	
};

function goMenuSearch(paraData) {
   	var width = 810;
   	var height = 560;
   	var top = (screen.height - height)/2;
   	var left = (screen.width - width)/2;
   	var para = "dialogWidth:" + width + "px; dialogHeight:" + height + "px; dialogTop:" + top + "px; dialogLeft:" + left +"px; resizable:no; center:yes; status:no; location:no; scroll:no";

	if (window.showModalDialog("/OrderingSystem/page/menu/menu_search.html", paraData, para)) {
		swan.pagination.freshCurrentPage('pagination1');
	}
};

function goMenuChange(paraData) {
   	var width = 810;
   	var height = 560;
   	var top = (screen.height - height)/2;
   	var left = (screen.width - width)/2;
   	var para = "dialogWidth:" + width + "px; dialogHeight:" + height + "px; dialogTop:" + top + "px; dialogLeft:" + left +"px; resizable:no; center:yes; status:no; location:no; scroll:no";

	if (window.showModalDialog("/OrderingSystem/page/menu/menu_change.html", paraData, para)) {
		swan.pagination.freshCurrentPage('pagination1');
	}
};

function goMenuSort(paraData) {
	console.log("goMenuSort");
   	var width = 400;
   	var height = 560;
   	var top = (screen.height - height)/2;
   	var left = (screen.width - width)/2;
   	var para = "dialogWidth:" + width + "px; dialogHeight:" + height + "px; dialogTop:" + top + "px; dialogLeft:" + left +"px; resizable:no; center:yes; status:no; location:no; scroll:no";

   	paraData = $("#curCategoryVal").val();
	if (window.showModalDialog("/OrderingSystem/page/menu/menu_sort.html", paraData, para)) {
		swan.pagination.freshCurrentPage('pagination1');
	}
}

swan.onReady(function() {
	$(function() {
		$("#header").load("/OrderingSystem/page/layout/header.html",loadMenu);
		$("#footer").load("/OrderingSystem/page/layout/footer.html",resizeWindow);
		$(window).resize(resizeWindow);
	});

		
	//language
	$("#menuIdLabel").html(os_lang.food_name);
	$("#searchBtn").html(os_lang.search);
	$("#addBtn").html(os_lang.add);
	$("#delBtn").html(os_lang.delete_selected);
	$("#curCategory").html(os_lang.food_category);
	$("#cofirmBtn").html(os_lang.confirm);
	$("#copyCategory").html(os_lang.copy_to);
	$("#moveCategory").html(os_lang.move_to);
	$("#changeSequenceBtn").html(os_lang.food_sort);
	
	$("#delBtn").click(function() {
		goMenuDeleteSelected();
	});
	
	$("#addBtn").click(function() {
		goMenuAdd();
	});
	
	var server = swan.getServerUrl("baseUrl");

	$("#searchBtn").click(function() {
		searchByName();
	});

	$("#menuNameLabel").keypress(function() {
		if (event.keyCode == 13) {
			searchByName();
		}
	});
	
	$("#cofirmBtn").click(function() {
		copyOrMoveSelected();
	});
	
	$("#changeSequenceBtn").click(function() {
		goMenuSort();
	});
	
	var options = {};
	options.url = server + "/food/querycategorylist";
	options.successCallback = function(data) {
		if (data.status == 0) {
			var categorylist = data.data;
			var category = "";
			for (var i in categorylist) {
				category += "<option value=\""+categorylist[i].id+"\">"+categorylist[i].name+"</option>";
			}
			$("#curCategoryVal").html(category);
			$("#dstCategoryVal").html(category);
			
			//Create a pagination control
			swan.pagination.onReady(function() {
				//Remote Source
				var data = {};
				data.id = $("#curCategoryVal").val();
				var remoteData = {
					url : server + "/food/querymenulistbycid",
					order : "desc",
					orderField : "id",
					currentPage : 1,
					type : "post",
					data : data
				};
				//Create a pagination
				swan.pagination.init({
					template : "menulist_template",
					templatePath : "/style/menu/",
					id : "pagination1",
					containerId : "menuListContent",
					pager : true,
					pageFront : true,
					remoteSource : remoteData,
					pageSize : 10
				});
			}, {
				skin : "default",
				onDataLoadEnd : resizeWindow
			});
		}
	};
	swan.ajax(options);
	
	$("#menuListContent").on("click", "#selectAllMenu",function() {
		var checked = this.checked;
		
		$("#menuListContent .viewselect").each(function() {
			$(this).prop("checked", checked);
		});
	});
	
	$("#curCategoryVal").on("change", function() {
		console.log($("#curCategoryVal").val());
		searchByName();
	});
	
	$("#menuListContent").on("click", ".viewselect", function() {
		console.log("select " + this.getAttribute("menuid") + " checked : " + this.checked);
	});
	
	$("#menuListContent").on("click", ".viewchange", function() {
		console.log("change " + this.getAttribute("menuid"));
		goMenuChange(this.getAttribute("menuid"));
	});
	
	$("#menuListContent").on("click", ".viewremove", function() {
		console.log("remove " + this.getAttribute("menuid"));

		var data = {};
		var clist = new Array();
		clist.push($("#curCategoryVal").val());
		
		data.id = this.getAttribute("menuid");
		data.categorylist = clist;
		
		var options = {};
		options.data = data;
		options.url = server + "/menuitem/deletefood";
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