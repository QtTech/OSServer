/*
 *
 */
var server;
var categoryArr = new Array();

swan.onReady(function() {
	$(function() {
		$(window).resize(resizeWindow);
	});
	
	server = swan.getServerUrl("baseUrl");

	$("#menuOrdered").html(os_lang.orderable);
	$("#menuName").html(os_lang.food_name);
	$("#menuNumber").html(os_lang.food_number);
	$("#menuPrice").html(os_lang.food_single_price);
	$("#menuDefaultCount").html(os_lang.food_choose_count);
	$("#menuCookedBy").html(os_lang.food_cookby);
	$("#menuCategory").html(os_lang.food_category);
	$("#menuDescription").html(os_lang.food_description);
	$("#menuDetails").html(os_lang.food_details);
	$("#btnMenuConfirm").html(os_lang.confirm);
	$("#btnMenuCancel").html(os_lang.cancel);
	$("#menuOrderedvalue1").html(os_lang.yes);
	$("#menuOrderedvalue0").html(os_lang.no);
	$("#menuDefaultCountvalue0").html(os_lang.yes);
	$("#menuDefaultCountvalue1").html(os_lang.no);
	$("#menuCookedByvalue0").html(os_lang.food);
	$("#menuCookedByvalue1").html(os_lang.drink);
	
	$("#btnMenuCancel").click(function(){
		window.close();
	});
	
	$("#btnMenuConfirm").click(function(){
		var menuOrderedvalue = $("#menuOrderedvalue").val();
		var menuNamevalue = $("#menuNamevalue").val();
		var menuNumbervalue = $("#menuNumbervalue").val();
		var menuPricevalue = $("#menuPricevalue").val();
		var menuDefaultCountvalue = $("#menuDefaultCountvalue").val();
		var menuCookedByvalue = $("#menuCookedByvalue").val();
		var menuDescriptionvalue = $("#menuDescriptionvalue").val();
		var menuDetailsvalue = $("#menuDetailsvalue").val();
		
		var data = {};
		data.name = menuNamevalue;
		data.number = menuNumbervalue;
		data.price = menuPricevalue;
		data.default_count = menuDefaultCountvalue;
		data.cookby = menuCookedByvalue;
		data.description = menuDescriptionvalue;
		data.details = menuDetailsvalue;
		data.orderable = menuOrderedvalue;
		data.categorylist = categoryArr;
		
		var options = {};
		options.data = data;
		options.url = server + "/menuitem/insertfood";
		options.successCallback = function(data) {
			console.log(data);
			if (data.status == 0) {
				window.returnValue=true;
				window.close();
			} else {
				alert(os_lang.add_fail);
			}
		};
		options.errorCallback = function() {
			
		};
		swan.ajax(options);
	
	});
	
	var options = {};
	options.url = server + "/food/queryfoodnumbers";
	options.successCallback = function(data) {
		if (data.status == 0) {
			$("#menuNumbervalue").val(data.data);
		}
	};
	swan.ajax(options);
	
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
				selectableHeader: "<div class='custom-header'>"+os_lang.category_list+"</div>",
				selectionHeader: "<div class='custom-header'>"+os_lang.selected_category+"</div>",
				keepOrder: true,
				afterSelect: function(values) {
					categoryArr.push(values.toString());
				},
				afterDeselect: function(values) {
					for (var i = 0; i < categoryArr.length; i++) {
						if (categoryArr[i].toString() === values.toString()) {
							categoryArr.splice(i, 1);
							break;
						}
					}
				}
			});
		}
	};
	swan.ajax(options);
	
});
