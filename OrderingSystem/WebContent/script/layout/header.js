/**
 * 
 */

function onLogout() {
	sessionStorage.clear();
	location.href="/OrderingSystem";
}

function initUserInfo() {
	$("#user").html(sessionStorage.username);
	$("#welcome").html(os_lang.welcome);
	$("#logout").html(os_lang.logout);
}

function loadMenu(){
	var menuHTML = wrapMenuList(createMenu(menulist));
	var $menu=$("#menu");
	
	$menu.empty();
	$menu.html(menuHTML);
	
	var rootMenu = $("#menu").children("ul:eq(0)");
	rootMenu.addClass("nav");
	rootMenu.children("li").children("a").each(function(){
		if(!$(this).attr("url")){
			$(this).append("<b></b>");
		}
	});
	var headers = rootMenu.children("li");
	headers.each(function(){
		$(this).find("li").find("i").last().remove();
	});
	headers.hover(function() {
		$(this).addClass('h');
	}, function() {
		$(this).removeClass('h');
	});

	initUserInfo();
}

var clickMenu = function(component){
	var url = component.attributes["url"].value;
	window.location.href = url;
};

var wrapWithSpan = function(itemName){
	return "<i>/</i><span>"+itemName+"</span>";
};

var wrapWithLink = function(menuName,url){
	return '<a href="javascript:void(0)" url='+url+' onclick="clickMenu(this);">'+menuName+'</a>';
};

function sortByOrderNum(a, b) {
	a = parseInt(a.orderNum);
	b = parseInt(b.orderNum);
    return a >= b ? 1 : -1;
}

function createMenu(menu) {
	var menuList = [];
	var subList = [];
	
	for (i in menu) {
		if (menu[i].parentId == "#") {
			menuList.push(menu[i]);
		}
		else {
			subList.push(menu[i]);
		}
	}
	
	menuList.sort(sortByOrderNum);
	subList.sort(sortByOrderNum);
	
	for (i in menuList) {
		var parentId = menuList[i].menuId;
		var subMenu = [];
		for (j in subList) {
			if (subList[j].parentId == parentId) {
				subMenu.push(subList[j]);
			}
		}
		menuList[i].subMenu = subMenu;
	}
	
	return menuList;
}

var wrapMenuItem = function(item) {
	var hasSubMenu = item.subMenu&&item.subMenu.length>0;
	var needUrl = (!hasSubMenu && item.menuUrl);
	return "<li><a href='javascript:void(0)'" + (needUrl?" url=\""+item.menuUrl+"\" onclick=\"clickMenu(this)\">":">")
			+ item.menuName + "<i></i></a>"
			+ (hasSubMenu?wrapMenuList(item.subMenu):"")
			+ "</li>";
};

var wrapMenuList = function(list){
	var tmp = "<ul>";
	for(i in list){
		tmp += wrapMenuItem(list[i]);
	}
	tmp += "</ul>";
	return tmp;
};
