/*
 *
 */
var server;

swan.onReady(function() {
	$(function() {
		$(window).resize(resizeWindow);
	});
	
	server = swan.getServerUrl("baseUrl");

	$("#menuID").html("菜单编号");
	$("#menuOrdered").html("是否可点");
	$("#menuName").html("菜单名称");
	$("#menuNumber").html("快捷号");
	$("#menuPrice").html("菜单单价");
	$("#menuDefaultCount").html("默认数量");
	$("#menuCookedBy").html("菜单种类");
	$("#menuCategory").html("菜品分类");
	$("#menuDescription").html("菜单描述");
	$("#menuDetails").html("菜单详情");
	$("#btnMenuCancel").html("返回");
	
	$("#btnMenuCancel").click(function(){
		window.close();
	});
	
	var options = {};
	options.url = server + "/food/querycategorylist";
	options.successCallback = function(data) {
		if (data.status == 0) {
			var categorylist = data.data;
			var food = "";
			for (var i in categorylist) {
				food += "<option value=\"" + categorylist[i].id + "\">" +
				categorylist[i].name + "</option>";
			}
			
			$("#menuCategoryvalue").html(food);
			$("#menuCategoryvalue").multiSelect({
				selectableHeader: "<div class='custom-header'>类别列表</div>",
				selectionHeader: "<div class='custom-header'>选中类别</div>",
				keepOrder: true
			});
		}
	};
	swan.ajax(options);
	
	var options = {};
	var data = {};
	var menuIDvalue = window.dialogArguments;
	if (!sessionStorage.menuIDvalue) {
		sessionStorage.menuIDvalue = menuIDvalue;
	} else {
		menuIDvalue = sessionStorage.menuIDvalue;
	}
	
	data.id = menuIDvalue;
	options.data = data;
	options.url = server + "/food/queryfoodallinfobyid";
	options.successCallback = function(data) {
		if (data.status == 0) {
			$("#menuIDvalue").val(data.data.id);
			$("#menuNamevalue").val(data.data.name);
			$("#menuNumbervalue").val(data.data.number);
			$("#menuPricevalue").val(data.data.price);
			$("#menuDefaultCountvalue").val(data.data.default_count);
			$("#menuCookedByvalue").val(data.data.cookby);
			$("#menuDescriptionvalue").val(data.data.description);
			$("#menuDetailsvalue").val(data.data.details);
			$("#menuOrderedvalue").val(data.data.orderable);
			$("#menuCategoryvalue").multiSelect("select", data.data.categorylist);
		}
	};
	options.errorCallback = function() {
		window.close();
	};
	swan.ajax(options);

});
