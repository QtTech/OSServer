function searchByDate() {
	var p = swan.pagination.getPagination('pagination1');
	var server = swan.getServerUrl("baseUrl");
	
	if (!$("#txtDurationBegin").val() && !$("#txtDurationEnd").val()) {
		p.remoteSource.url = server + "/statistics/dishstatistics";
		p.remoteSource.data.starttime = null;
		p.remoteSource.data.endtime = null;
	} else {
		p.remoteSource.url = server + "/statistics/dishstatistics";
		if ($("#txtDurationBegin").val()) {
			p.remoteSource.data.starttime = $("#txtDurationBegin").val();
		}
		
		if ($("#txtDurationEnd").val()) {
			p.remoteSource.data.endtime = $("#txtDurationEnd").val();
		}
	}
	
	swan.pagination.freshCurrentPage('pagination1');
};

swan.onReady(function() {
	$(function() {
		$("#header").load("/OrderingSystem/page/layout/header.html",loadMenu);
		$("#footer").load("/OrderingSystem/page/layout/footer.html",resizeWindow);
		$(window).resize(resizeWindow);
	});

	//language
	$("#fromlabel").html(os_lang.from_src);
	$("#tolabel").html(os_lang.to_dst);
	$("#searchBtn").html(os_lang.search);
	$("#exportBtn").html(os_lang.export_table);
	
	var server = swan.getServerUrl("baseUrl");

	$("#searchBtn").click(function() {
		searchByDate();
	});

	$("#exportBtn").click(function() {
		var postfix = "?lang=" + swan.lang;
		postfix += "&starttime="+$("#txtDurationBegin").val();
		postfix += "&endtime="+$("#txtDurationEnd").val();
		postfix += "&type=0";
		window.location.href = server + "/statistics/exportstatistics" + postfix;
	});
	
	//Create a pagination control
	swan.pagination.onReady(function() {
		//Remote Source
		var remoteData = {
			url : server + "/statistics/dishstatistics",
			order : "desc",
			orderField : "id",
			currentPage : 1,
			type : "post",
			data : {}
		};
		//Create a pagination
		swan.pagination.init({
			template : "dishstatistics_template",
			templatePath : "/style/statistics/",
			id : "pagination1",
			containerId : "dishStatisticsContent",
			pager : true,
			pageFront : true,
			remoteSource : remoteData,
			pageSize : 10
		});
	}, {
		skin : "default",
		onDataLoadEnd : resizeWindow
	});

	swan.datePicker.onReady(function() {
		swan.datePicker.init({
			id:"datePickerBegin",
			containerId:"txtDurationBegin",
			option:{changeMonth:true, dateFormat:"yy-mm-dd"}
		});
	}, {skin:"default"});

	swan.datePicker.onReady(function() {
		swan.datePicker.init({
			id:"datePickerEnd",
			containerId:"txtDurationEnd",
			option:{changeMonth:true, dateFormat:"yy-mm-dd"}
		});
	}, {skin:"default"});

});