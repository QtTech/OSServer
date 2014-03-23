/*
 *
 */
var server;

var dataArray = new Array();

function showLoading()
{
    document.getElementById("over").style.display = "block";
    document.getElementById("layout").style.display = "block";
}

function showHide()
{
    document.getElementById("over").style.display = "none";
    document.getElementById("layout").style.display = "none";
}

function moveLeftOrRight(fromObj,toObj) 
{
    var fromObjOptions=fromObj.options;
    for(var i=0;i<fromObjOptions.length;i++){
        if(fromObjOptions[i].selected){
            toObj.appendChild(fromObjOptions[i]);
            i--;
        }
    }
    resetAutoWidth(fromObj);
    resetAutoWidth(toObj);
} 

function moveLeftOrRightAll(fromObj,toObj) 
{
    var fromObjOptions=fromObj.options;
    if(fromObjOptions.length>1000) {
        //if(!confirm("Are you sure to move options?")) return false;
    }
    for(var i=0;i<fromObjOptions.length;i++){
        fromObjOptions[0].selected=true;
        toObj.appendChild(fromObjOptions[i]);
        i--;
    }
    resetAutoWidth(fromObj);
    resetAutoWidth(toObj);
} 

function moveUp(selectObj) 
{ 
    var theObjOptions=selectObj.options;
    
    for(var i=1;i<theObjOptions.length;i++) {
        if( theObjOptions[i].selected && !theObjOptions[i-1].selected ) {
            swapOptionProperties(theObjOptions[i],theObjOptions[i-1]);
        }
    }
} 

function moveDown(selectObj) 
{ 
    var theObjOptions=selectObj.options;
    
    for(var i=theObjOptions.length-2;i>-1;i--) {
        if( theObjOptions[i].selected && !theObjOptions[i+1].selected ) {
        	console.log(theObjOptions[i].index);
            swapOptionProperties(theObjOptions[i],theObjOptions[i+1]);
        }
    }
} 

function moveToTop(selectObj){
    var theObjOptions=selectObj.options;
    
    var oOption=null;
    for(var i=0;i<theObjOptions.length;i++) {
        if( theObjOptions[i].selected && oOption) {
            selectObj.insertBefore(theObjOptions[i],oOption);
        }
        else if(!oOption && !theObjOptions[i].selected) {
            oOption=theObjOptions[i];
        }
    }
    
    for (var i in theObjOptions) {
    	dataArray[i] = theObjOptions[i].value;
    }
}

function moveToBottom(selectObj){
    var theObjOptions=selectObj.options;
    
    var oOption=null;
    for(var i=theObjOptions.length-1;i>-1;i--) {
        if( theObjOptions[i].selected ) {
            if(oOption) {
                oOption=selectObj.insertBefore(theObjOptions[i],oOption);
            }
            else {
            	oOption=selectObj.appendChild(theObjOptions[i]);
            }
            
        }
    }

    for (var i in theObjOptions) {
    	dataArray[i] = theObjOptions[i].value;
    }
}

function selectAllOption(selectObj){
    var theObjOptions=selectObj.options;
    
    for(var i=0;i<theObjOptions.length;i++){
        theObjOptions[0].selected=true;
    }
}

/* private function */
function swapOptionProperties(option1,option2){
    //option1.swapNode(option2);
	
    var tempStr=option1.value;
    option1.value=option2.value;
    option2.value=tempStr;
    tempStr=option1.text;
    option1.text=option2.text;
    option2.text=tempStr;
    tempStr=option1.selected;
    option1.selected=option2.selected;
    option2.selected=tempStr;
    tempStr=option1.getAttribute("index");
    option1.setAttribute("index", option2.getAttribute("index"));
    option2.setAttribute("index", tempStr);
    
	dataArray[option1.index] = option1.value;
	dataArray[option2.index] = option2.value;
}

function resetAutoWidth(obj){
    var tempWidth=obj.style.getExpression("width");
    if(tempWidth!=null) {
        obj.style.width="auto";
        obj.style.setExpression("width",tempWidth);
        obj.style.width=null;
    }
}

swan.onReady(function() {
	$(function() {
		$(window).resize(resizeWindow);
	});
	
	server = swan.getServerUrl("baseUrl");

	$("#btnMenuConfirm").html(os_lang.confirm);
	$("#btnMenuCancel").html(os_lang.cancel);
	$("#move_top").val(os_lang.move_top);
	$("#move_up").val(os_lang.move_up);
	$("#move_down").val(os_lang.move_down);
	$("#move_bottom").val(os_lang.move_bottom);

	$("#btnMenuCancel").click(function(){
		sessionStorage.removeItem("categoryid");
		window.close();
	});
	
	$("#btnMenuConfirm").click(function(){
		var data = {};
		data.categoryid = sessionStorage.categoryid;
		data.dishes = dataArray;
		var options = {};
		options.url = server + "/menuitem/updatemenuorders";
		options.data = data;
		options.successCallback = function(data) {
			showHide();
			if (data.status == 0) {
				sessionStorage.removeItem("categoryid");
				window.returnValue=true;
				window.close();
			}
		};
		options.errorCallback = function(data) {
			showHide();
		};
		swan.ajax(options);
		showLoading();
	});
	
	var data = {};
	if (!sessionStorage.categoryid) { 
		sessionStorage.categoryid = window.dialogArguments;
	}
	data.id = sessionStorage.categoryid;
	
	var options = {};
	options.url = server + "/food/querymenulistbycid";
	options.data = data;
	options.successCallback = function(data) {
		if (data.status == 0) {
			var simplefoodlist = data.data;
			var food = "";
			for (var i in simplefoodlist) {
				food += "<option value=\"" + simplefoodlist[i].id + "\" index=\"" + i + "\" name=\""+ simplefoodlist[i].name + "\">"+simplefoodlist[i].name+"</option>";
				dataArray[i] = simplefoodlist[i].id;
			}
			$("#ObjSelect").html(food);
		}
	};
	swan.ajax(options);
});
