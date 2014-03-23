function goPhotoAdd() {

	var data = {};
	data.id = $("#menulist").val();
	data.largepic = $("#detailphotoValue").val();
	data.smallpic = $("#generalphotoValue").val();
	
	var options = {};
	options.url = server + "/menuitem/updatefoodphoto";
	options.data = data;
	options.successCallback = function(data) {
		if (data.status == 0) {
			alert(os_lang.submit_success);
		} else {
			alert(os_lang.submit_fail);
		}
	};
	options.errorCallback = function() {
		alert(os_lang.submit_fail);
	};
	swan.ajax(options);
};

var server = "";

function generalphotosChange() {
	$.ajaxFileUpload({
		timeout: 1000,
		url: server + "/photo/put",
		secureuri:false,
		fileUploadId: "generalphotos",
		dataType: 'json',
		success: function(data, status) {
			if (data.status == 0) {
				var src = server + "/photo/get?path=" + data.GeneralPhotos;
				$("#generalphotoValue").val(data.GeneralPhotos);
				$("#generalphoto").attr("src", src);
				$("#uploadDivgp").hide();
			} else {
				alert(os_lang.picture_upload_fail);		
			}
		},
		error: function(data, status, e) {
			console.log(status);
			console.log(e);
			alert(os_lang.picture_upload_fail);		
		}
	});
};

function detailphotosChange() {
	$.ajaxFileUpload({
		timeout: 1000,
		url: server + "/photo/put",
		secureuri:false,
		fileUploadId: "detailphotos",
		dataType: 'json',
		success: function(data, status) {
			if (data.status == 0) {
				var src = server + "/photo/get?path=" + data.DetailPhotos;
				$("#detailphotoValue").val(data.DetailPhotos);
				$("#detailphoto").attr("src", src);
				$("#uploadDivdp").hide();
			} else {
				alert(os_lang.picture_upload_fail);		
			}
		},
		error: function(data, status, e) {
			console.log(status);
			console.log(e);
			alert(os_lang.picture_upload_fail);		
		}
	});

};

function showDiagramOperationDp() {
	if ($("#detailphotoValue").val() != "") {
		$("#deleteDivdp").show();
	} else {
		$("#uploadDivdp").show();
	}
};

function hideDiagramOperationDp() {
	$("#uploadDivdp").hide();
	$("#deleteDivdp").hide();
}

function showDiagramOperationGp() {
	if ($("#generalphotoValue").val() != "") {
		$("#deleteDivgp").show();
	} else {
		$("#uploadDivgp").show();
	}
};

function hideDiagramOperationGp() {
	$("#uploadDivgp").hide();
	$("#deleteDivgp").hide();
}

function getPhotoByID() {

	var data = {};
	data.id = $("#menulist").val();
	
	var options = {};
	options.url = server + "/food/queryfoodbyid";
	options.data = data;
	options.successCallback = function(data) {
		if (data.status == 0) {
			var pathdetail = data.data.largepic;
			var pathgeneral = data.data.smallpic;
			
			if (pathdetail != null && pathdetail != "") {
				$("#detailphoto").attr("src", server + "/photo/get?path=" + pathdetail);
			} else {
				$("#detailphoto").attr("src", "");
			}
			
			if (pathgeneral != null && pathgeneral != "") {
				$("#generalphoto").attr("src", server + "/photo/get?path=" + pathgeneral);
			} else {
				$("#generalphoto").attr("src", "");
			}
			
			$("#detailphotoValue").val(pathdetail);
			$("#generalphotoValue").val(pathgeneral);
			
		} else {
			alert(os_lang.picture_download_fail);
		}
	};
	options.errorCallback = function() {
		alert(os_lang.picture_download_fail);
	};
	swan.ajax(options);

}

swan.onReady(function() {
	$(function() {
		$("#header").load("/OrderingSystem/page/layout/header.html",loadMenu);
		$("#footer").load("/OrderingSystem/page/layout/footer.html",resizeWindow);
		$(window).resize(resizeWindow);
	});
	
	hideDiagramOperationDp();
	hideDiagramOperationGp();
	
	// Load ajaxfileupload.js by swan.
    swan.fileUploadAuto.onReady(function(){});
    
	//language
	$("#menuIdLabel").html(os_lang.food_name);
	$("#submitBtn").html(os_lang.submit);
	
	$("#submitBtn").click(function() {
		goPhotoAdd();
	});
	
	$("#deleteDivdp").click(function() {
		$("#detailphotoValue").val("");
		$("#detailphoto").attr("src", "");
    });
	
	$("#deleteDivgp").click(function() {
		$("#generalphotoValue").val("");
		$("#generalphoto").attr("src", "");
    });
	
	$("#menulist").change(function() {
		getPhotoByID();
	});
	
	server = swan.getServerUrl("baseUrl");

	var options = {};
	options.url = server + "/food/querysimplefoodlist";
	options.successCallback = function(data) {
		if (data.status == 0) {
			var simplefoodlist = data.data;
			var food = "";
			for (var i in simplefoodlist) {
				food += "<option value=\""+simplefoodlist[i].id+"\">"+simplefoodlist[i].name+"</option>";
			}
			$("#menulist").html(food);
			
			getPhotoByID();
		}
	};
	swan.ajax(options);
	
});