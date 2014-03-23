(function(w){
    //使IE的setTimeout方法支持第3个参数
    if(navigator.userAgent.indexOf("MSIE")>0){
		(function(overrideFn){
			w.setTimeout = overrideFn(w.setTimeout);
			w.setInterval = overrideFn(w.setInterval);
		})(function(originalFn){
			return function(code,delay){
				var args = Array.prototype.slice.call(arguments,2);
				return originalFn(function(){
					if(typeof code == 'string'){
						eval(code);
					}else{
						code.apply(this,args);
					}
				},delay);
			}
		})
	}
})(window);

//The Swan framework
var swan = {};

//The Swan framework for global variables
swan.scriptFile = "smartos.js";
swan.scriptSrc = "";
swan.rootSrc = "";
swan.loadCallBack = [];
if (navigator.language) {
	var language = navigator.language;
} else {
	var language = navigator.browserLanguage;
}
if (language == "zh-CN") {
	swan.lang = "zh_CN";
} else {
	swan.lang = "en_US";
}
swan.debug = false;
swan.serverUrl = "";
swan.getServerUrl = function(key){
	return swan.serverUrl.find("[key=" + key + "]").text();
};

swan.jqueryUI = {
	lock:false,
	load:false,
	queue:[],
	onReady:function(){
		var s = swan;
		if(this.lock || this.load){
			return;
		}
		s.jqueryUI.lock = true;
		var cssPath = s.scriptSrc + "/ext/jquery/jquery-ui-1.10.3.custom.css";
		s.loadStyle(cssPath, function(){}, "jqueryUI");
		s.loadScript(s.scriptSrc + "/ext/jquery/jquery-ui-1.10.3.custom.min.js", function(){
			s.jqueryUI.load = true;
			var q = swan.jqueryUI.queue;
			var len = q.length;
			
			for(var i = 0;i<len;i++){
				var qu = q[i];
				if(typeof qu.getReady == "function"){
					qu.getReady(qu.callBack, qu.options);
				}
			}
			
			q=[];
			s.jqueryUI.lock = false;
		}, "jqueryUI");
	}
};

//The dynamic loading JS
swan.loadScript = function(url, callback) {
	var script = document.createElement("script");
	script.type = "text/javascript";

	if (script.readyState) {
		script.onreadystatechange = function () {
			if (script.readyState == "loaded" || script.readyState == "complete") {
				script.onreadystatechange = null;
				callback();
			}
		};
	}
	else {
		script.onload = function () {
			callback();
		};
	}
	
	script.src = url;
	document.getElementsByTagName("head")[0].appendChild(script);
};
//The dynamic loading CSS
swan.loadStyle = function(url, callback, controlType){
	var link = document.createElement("link");
	link.type = "text/css";
	link.rel = "stylesheet";
	link.setAttribute("controltype",controlType);

	if (link.readyState) {
		link.onreadystatechange = function () {
			if (link.readyState == "loaded" || link.readyState == "complete") {
				link.onreadystatechange = null;
				callback();
			}
		};
	}
	else {
		link.onload = function () {
			callback();
		};
	}
	
	link.href = url;
	document.getElementsByTagName("head")[0].appendChild(link);
};
//Based on the dynamic loading JS file Swan framework
swan.getReady = function(){
	var scriptFile = "/" + swan.scriptFile;
	var scripts = document.getElementsByTagName("script"); 
	for(var i =0; i< scripts.length;i++){ 
		var src = scripts[i].src;
		if(src.indexOf(scriptFile) != -1){
			var scriptSrc =  src.substring(0, src.lastIndexOf(scriptFile));
			swan.scriptSrc = scriptSrc;
			swan.rootSrc = scriptSrc.substring(0, scriptSrc.lastIndexOf("/swan"));
			
			swan.loadScript(swan.scriptSrc + "/ext/jquery/jquery-1.10.2.min.js", function () {
				swan.loadScript(swan.scriptSrc + "/lang/"+ swan.lang +"/lang.js", function() {
					swan.loadScript(swan.scriptSrc + "/ext/jquery/jquery.multi-select.js", function() {
						swan.loadScript(swan.scriptSrc + "/ext/jsmart/smart-2.9.min.js", function () {
							var s = swan;
							//Read swanConfig.xml
							s.ajax.get(s.rootSrc + "/webConfig.xml", function(data){
								var data = $(data).find("swanConfig");
								var debug = data.find("debug").text();
								var serverUrl = data.find("serverUrl");
								swan.debug = debug=="true"?true:false;
								swan.serverUrl = serverUrl;
								
								$(function(){
									var callback =  swan.loadCallBack;
									var len = callback.length;
									for(var i = 0;i<len;i++){
										if(typeof callback[i] == "function"){
											callback[i]();
										}
									}	
									callback = null;
								});
							}, "xml");
						});
					});
				});
			});
		}
	}
};

//The Swan framework entrance
swan.onReady = function(callback){
	if(typeof callback == "function"){
		swan.loadCallBack.push(callback);
	}
};

//Swan frame encapsulation Ajax request
swan.ajax = function(options) {
	if(!options.sync){
		options.sync = false;
	}
	
	var type = options.type != null?options.type:"post";
	var data;
	if(type == "get"){
		data = options.data?options.data:null;
	}
	else{
		data = options.data?JSON.stringify(options.data):null;
	}
	
	var url = options.url;
	if(url.indexOf('?')>=0){
		url += "&timer=" + (new Date()).valueOf();
	}
	else{
		url += "?timer=" + (new Date()).valueOf();
	}

	var afterSend = (typeof swan.ajax.afterSend == "function"?swan.ajax.afterSend:options.afterSend);
	
    $.ajax({
        url: url,
        data: data,
        type: type,
        async: !options.sync,
        contentType: options.contentType?options.contentType:"application/json;charset=utf-8",
        dataType: options.dataType?options.dataType:"json",
        beforeSend: typeof swan.ajax.beforeSend == "function"?swan.ajax.beforeSend:options.beforeSend,
        success: function (data) {
        	if (afterSend && typeof afterSend == "function") {
        		if (!afterSend(url, data, "success"))
        			return;
        	}
            if (options.successCallback && typeof (options.successCallback) == "function") {
            	if(options.currentTarget){
            		data.watchCurrentTarget = options.currentTarget;
            	}
            	options.successCallback(data);
            }
        },
        error: function (data) {
        	if (afterSend && typeof afterSend == "function") {
        		if (!afterSend(url, data, "error"))
        			return;
        	}
            if (options.errorCallback && typeof (options.errorCallback) == "function") {
            	options.errorCallback(data);
            }
        }
    });
};

swan.ajax.get = function(url, successCallback, dateType) {
	/*if(typeof successCallback == "function"){
		$.get(url + "?" + (new Date()).valueOf(), successCallback);
	}
	else{
		$.get(url + "?" + (new Date()).valueOf());
	}*/
	swan.ajax({
		type:"get",
		url:url,
		dataType:dateType?dateType:"html",
		contentType:"text/html;charset=utf-8",
		successCallback:successCallback
	});
};


//Display jsmart template of Swan framework
swan.setJsmartTemplate = function(templateId, containerId, data, component){
	var template = new jSmart($("#"+ templateId).html());
	if(component){
		data.swan = {fmt:function(key){return swan.fmt(key, component);}};
	}
	var res = template.fetch(data);
	$("#" + containerId).html(res);
};
swan.setSimpleJsmartTemplate=function(html, containerId, data, component){
	var template = new jSmart(html);
	if(component){
		data.swan = {fmt:function(key){return swan.fmt(key, component);}};
	}
	var res = template.fetch(data);
	$(containerId).append(res);
};
swan.getJsmartFetch = function(html, data){
	var template = new jSmart(html);
	return template.fetch(data);
};

//The framework of Swan paging controls
swan.pager = function(){
	this.id = null;
	this.index = 0;
	this.pageData = [];
	this.totalData = [];
	this.currentPage = 0;
	this.totalPage = 0;
	this.pageSize = 10;
	this.showJsmartCallBack = null;
	this.pageFront = false;
	this.setTotalPage = function(){
		if(this.totalData && this.totalData.length>0){
			var totalPage = parseInt(this.totalData.length/this.pageSize);
			this.totalPage = this.totalData.length%this.pageSize==0?totalPage:totalPage+1;
		}
		else{
			this.currentPage = 0;
			this.totalPage = 0;
		}
	};
	this.setCurrentPageData = function(currentPage){
		this.pageData = [];
		if(this.totalData && this.totalData.length>0){
			this.currentPage = parseInt(currentPage);
			this.currentPage = this.currentPage <1?1:this.currentPage;
			this.currentPage = this.currentPage > this.totalPage?this.totalPage:this.currentPage;
			
			var startIndex = this.currentPage*this.pageSize-this.pageSize;
			var len = this.totalData.length;
			var data;
			for(startIndex; startIndex<len; startIndex++){
				data = this.totalData[startIndex];
				data.index = startIndex;
				this.pageData.push(data);
				if(this.pageData.length == this.pageSize){
					return;
				}
			}
		}
		else{
			this.currentPage = 0;
			this.totalPage = 0;
			this.totalData = [];
		}
	};
};

//The Swan framework paging list control
swan.pagination = {
		skin:null,
		cssPath:null,
		langPath:null,
		paginations:[],
		//Switching skin
		changeSkin:function(skin){
			$("link[controltype=pagination]").remove();
			
			var s = swan;
			if(skin){
				s.pagination.skin = skin;
			}
			else{
				s.pagination.skin = "default";
			}
			
			s.pagination.cssPath = s.scriptSrc + "/component/pagination/skin/" + s.pagination.skin + "/css/default.css";

			s.loadStyle(s.pagination.cssPath, function(){}, "pagination");
		},
		//The paging list object
		pagination:function(options){
			var s = swan;
			this.id = options.id;
			this.index = s.pagination.paginations.length;
			this.containerId = options.containerId ? options.containerId:"";
			this.dateType = options.dateType? options.dateType : "json";
			var tp = options.templatePath ? s.rootSrc + options.templatePath: s.scriptSrc + "/component/pagination/template/";
			var template = options.template ? options.template:"pagination_Default_Template";
			if(template.lastIndexOf('/') > -1){
				this.template = template.substring(template.lastIndexOf('/')+1, template.length);
			}
			else{
				this.template = template;
			}
			this.templatePath = tp + template + ".html";
			this.onShow = options.onShow;

			s.pagination.bindPageEvent({containerId:this.containerId, id:this.id});
			
			this.pager = new swan.pager();
			if(options.pager){
				this.pager.id = this.id;
				this.pager.index = this.index;
				this.pager.isPager = true;
				this.pager.pageSize = options.pageSize?options.pageSize:10;
				this.pager.showJsmartCallBack = function(){
					var p = s.pagination.paginations[this.index];
					s.setJsmartTemplate(p.template, p.containerId, this, "pagination");
					s.pagination.onDataLoadEnd();//invoke hook function
				};
				
				if(options.localSource && (!options.remoteSource || s.debug)){
					this.localSource = options.localSource;
					this.remoteSource = null;
					this.pager.pageFront = true;
					this.pager.totalData = this.localSource;
					this.pager.setTotalPage();
					this.pager.setCurrentPageData(this.pager.currentPage);
				}
				else if(options.remoteSource){
					this.localSource = null;
					this.remoteSource = options.remoteSource;;
					this.pager.pageFront = options.pageFront?true:false;
					this.remoteSource.pageSize = this.pager.pageSize;
					this.remoteSource.type = options.remoteSource.type !=null?options.remoteSource.type:"post";
					this.pager.successCallback = this.remoteSource.successCallback;
					this.remoteSource.successCallback  = function(data){
						var s = swan;
						var pager = s.pagination.getPagination(options.id).pager;
						if(pager.pageFront){
							pager.totalData = data.data;
							pager.setTotalPage();
							pager.setCurrentPageData(pager.currentPage);
						}
						else{
							pager.currentPage = data.currentPage;
							pager.totalPage = data.totalPage;
							pager.pageData = data.data;
						}
						s.pagination.showPaginations(pager.index);
						
						if(typeof pager.successCallback == "function"){
							pager.successCallback(data);
						}
					};
					this.remoteSource.index = this.index;
				}
			}
			else{
				this.pager.id = this.id;
				this.pager.index = this.index;
				this.pager.isPager = false;

				if(options.localSource && (!options.remoteSource || s.debug)){
					this.localSource = options.localSource;
					this.remoteSource = null;
					this.pager.pageFront = true;
					this.pager.pageData = this.localSource;
				}
				else if(options.remoteSource){
					this.localSource = null;
					this.remoteSource = options.remoteSource;
					this.remoteSource.type = options.remoteSource.type !=null?options.remoteSource.type:"post";
					this.pager.pageFront = options.pageFront?true:false;
					this.pager.successCallback = this.remoteSource.successCallback;
					this.remoteSource.successCallback  = function(data){
						var s = swan;
						var pager = s.pagination.getPagination(options.id).pager;
						pager.currentPage = data.currentPage;
						pager.totalPage = data.totalPage;
						pager.pageData = data.data;
						
						s.pagination.showPaginations(pager.index);
						
						if(typeof pager.successCallback == "function"){
							pager.successCallback(data);
						}
					};
					
					this.remoteSource.index = this.index;
				}
			}
		},
		//Display the page data
		showPaginations:function(index){
			var s = swan;
			var p = s.pagination.paginations[index];
			if(p && p.pager && p.pager.pageData && p.pager.pageData.length>=0){
				s.setJsmartTemplate(p.template, p.containerId, p.pager, "pagination");
				s.pagination.onDataLoadEnd();//invoke hook function
			}
			
			if(typeof p.onShow == "function"){
				p.onShow();
			}
		},
		//on data load end, the window will resize,but resize() function of JQuery will not be invoked
		onDataLoadEnd:function(){
			//this is a hook, default do nothing
		},
		//The pagination control entrance
		onReady:function(callBack, options){
			$("script[controltype=pagination]").remove();
			$("link[controltype=pagination]").remove();
			
			var s = swan;
			if(options && options.skin){
				s.pagination.skin = options.skin;
			}
			else{
				s.pagination.skin = "default";
			}
			if(options && options.onDataLoadEnd && (typeof options.onDataLoadEnd == "function")){
				s.pagination.onDataLoadEnd = options.onDataLoadEnd;
			}

			s.pagination.cssPath = s.scriptSrc + "/component/pagination/skin/" + s.pagination.skin + "/css/default.css";
			s.pagination.langPath = s.scriptSrc + "/component/pagination/lang/" + s.lang + "/lang.js";

			s.loadScript(s.pagination.langPath, function(){
				if(typeof callBack == "function"){
					callBack();
				}
			}, "pagination");

			//s.loadStyle(s.pagination.cssPath, function(){}, "pagination");
		},
		//Create a pagination control
		init:function(options){
			var p = swan.pagination;
			options = new p.pagination(options);
			p.paginations.push(options);
			
			swan.ajax.get(options.templatePath, function(data){
				if($("#" + options.template).length == 0){
					$("body").append(data);
				}
				
				var s = swan;
				if(options.localSource){
					s.pagination.showPaginations(options.index);
				}
				else if(options.remoteSource){
					var currentPage = options.remoteSource.currentPage;
					options.remoteSource.currentPage = currentPage!=null?currentPage:1;
					s.pagination.ajax(options.remoteSource, options.pager.pageFront);
				}
			});
		},
		//According to the ID to obtain a pagination object
		getPagination:function(id){
			var p = swan.pagination;
			var len = p.paginations.length;
			
			for(var i = 0;i<len;i++){
				if(p.paginations[i].id == id){
					return p.paginations[i];
				}
			}
			
			return null;
		},
		//Sets a pagination object data source
		setSource:function(options){
			var s = swan;
			var p = s.pagination.getPagination(options.id);
			
			if(p){
				if(p.pager.isPager){
					p.pager.currentPage = 0;
					if(options.localSource && (!options.remoteSource || s.debug)){
						p.localSource = options.localSource;
						p.remoteSource = null;
						p.pager.pageFront = true;
						p.pager.totalData = p.localSource;
						p.pager.setTotalPage();
						p.pager.setCurrentPageData(p.pager.currentPage);
						swan.pagination.showPaginations(p.index);
					}
					else if(options.remoteSource){
						p.localSource = null;
						p.remoteSource = options.remoteSource;
						p.remoteSource.type = options.remoteSource.type !=null?options.remoteSource.type:"post";
						
						p.remoteSource.successCallback = function(data){
							var s = swan;
							var pager = s.pagination.getPagination(options.id).pager;
							pager.currentPage = data.currentPage;
							pager.totalPage = data.totalPage;
							pager.pageData = data.data;
							
							s.pagination.showPaginations(pager.index);
							
							if(typeof pager.successCallback == "function"){
								pager.successCallback(data);
							}
						};
						
						p.remoteSource.index = p.index;
						p.remoteSource.currentPage = options.currentPage!=null?options.currentPage:1;
						p.remoteSource.pageSize = options.pageSize !=null?options.pageSize:10;
						swan.pagination.ajax(p.remoteSource, p.pager.pageFront);
					}
				}
				else{
					if(options.localSource && (!options.remoteSource || s.debug)){
						p.localSource = options.localSource;
						p.remoteSource = null;
						p.pager.pageFront = true;
						p.pager.pageData = p.localSource;
						swan.pagination.showPaginations(p.index);
					}
					else if(options.remoteSource){
						p.localSource = null;
						p.remoteSource = options.remoteSource;
						p.pager.pageFront = options.pageFront?true:false;
						p.remoteSource.pageSize = options.pageSize !=null?options.pageSize:10;
						
						p.remoteSource.successCallback  = function(data){
							var s = swan;
							var pager = s.pagination.getPagination(options.id).pager;
							pager.currentPage = data.currentPage;
							pager.totalPage = data.totalPage;
							pager.pageData = data.data;
							
							s.pagination.showPaginations(pager.index);
							
							if(typeof pager.successCallback == "function"){
								pager.successCallback(data);
							}
						};
						
						p.remoteSource.index = p.index;
						swan.pagination.ajax(p.remoteSource, p.pager.pageFront);
					}
				}
			}
		},
		//Binding paging events
		bindPageEvent:function(options){
			$("#" + options.containerId).on("click", "[forPagination=" + options.id + "]", function(e){
				var s = swan;
				var event = s.event;
				var target = event.getEventTarget(e);
				var operate = target.getAttribute("operate");
				if(!operate || target.nodeName == "SELECT"){
					return;
				}
				
				var currentTarget = event.getCurrentTarget(e);
				var id = currentTarget.getAttribute("forPagination");
				if(!id){
					return;
				}

				var pagination =  s.pagination.getPagination(id);
				var pager = pagination.pager;
				var currentPage = parseInt(pager.currentPage);
				switch(operate){
					case "prevPage":
						currentPage = pager.currentPage - 1;
						currentPage = currentPage<1?1:currentPage;
						break;
					case "nextPage":
						currentPage = pager.currentPage + 1;
						currentPage = currentPage>pager.totalPage?pager.totalPage:currentPage;
						break;
					case "gotoPage":
						currentPage = $("[forPagination="+pagination.id + "] .goTo").val();
						currentPage = parseInt(currentPage);
						if(isNaN(currentPage)){
							return;
						}
						currentPage = currentPage<1?1:currentPage;
						currentPage = currentPage>pager.totalPage?pager.totalPage:currentPage;
						break;
					case "firstPage":
						currentPage = 1;
						break;
					case "lastPage":
						currentPage = pager.totalPage;
						break;
					default:
						return;
				}

				if(pager.pageFront){
					pager.setCurrentPageData(currentPage);
					if(pager.showJsmartCallBack && typeof(pager.showJsmartCallBack) == "function"){
						pager.showJsmartCallBack();
					}
				}
				else{
					pagination.remoteSource.currentPage = currentPage;
					s.pagination.ajax(pagination.remoteSource, pager.pageFront);
				}

				event.stopEventBubble(e);
			}).on("change", "[forPagination=" + options.id + "]", function(e){
				var s = swan;
				var event = s.event;
				var target = event.getEventTarget(e);
				var operate = target.getAttribute("operate");
				if(!operate){
					return;
				}
				
				var currentTarget = event.getCurrentTarget(e);
				var id = currentTarget.getAttribute("forPagination");
				if(!id){
					return;
				}

				var pagination =  s.pagination.getPagination(id);
				var pager = pagination.pager;
				var currentPage = parseInt(pager.currentPage);
				switch(operate){
					case "gotoPage":
						currentPage = $("[forPagination="+pagination.id + "] .goTo").val();
						currentPage = parseInt(currentPage);
						if(isNaN(currentPage)){
							return;
						}
						currentPage = currentPage<1?1:currentPage;
						currentPage = currentPage>pager.totalPage?pager.totalPage:currentPage;
						break;
					default:
						return;
				}

				if(pager.pageFront){
					pager.setCurrentPageData(currentPage);
					if(pager.showJsmartCallBack && typeof(pager.showJsmartCallBack) == "function"){
						pager.showJsmartCallBack();
					}
				}
				else{
					pagination.remoteSource.currentPage = currentPage;
					s.pagination.ajax(pagination.remoteSource, pager.pageFront);
				}

				event.stopEventBubble(e);
			});
		},
		//Refresh the current page
		freshCurrentPage:function(id){
			var p = swan.pagination.getPagination(id);
			if(p.localSource){
				p.pager.showJsmartCallBack();
			}
			else if(p.remoteSource){
				this.ajax(p.remoteSource, p.pager.pageFront);
			}
		},
		//Request pages
		ajax:function(options, pageFront){
			if(!pageFront){
				if(!options.data){
					options.data = {};
				}
			
				options.data.currentPage = options.currentPage;
				options.data.pageSize = options.pageSize;
				options.data.order = options.order;
				options.data.orderField = options.orderField;
			
				swan.ajax(options);
			}
			else{
				swan.ajax(options);
			}
		}
};

//The Swan framework validation controls
swan.validation = {
    ready: false,
    load: false,
    queue: [],
    langPath: null,
    cssPath: null,
    skin: null,
    validations: [],
    setDefault: false,
    //The validation control object
    validation: function (options) {
        this.formId = options.formId;
        this.index = swan.validation.validations.length;
        this.validate = options.validate;
    },
    //Validation controls entrance
    onReady: function (callBack, options) {
        var s = swan;
        var v = s.validation;

        if (!v.ready) {
            v.ready = true;
            if (options && options.skin) {
                v.skin = options.skin;
            }
            else {
                v.skin = "default";
            }

            v.cssPath = s.scriptSrc + "/component/validation/skin/" + v.skin + "/css/default.css";
            v.langPath = s.scriptSrc + "/component/validation/lang/" + s.lang + "/lang.js";

            s.loadScript(swan.scriptSrc + "/ext/jquery/jquery.validate.min.js", function () {
                s.loadScript(v.langPath, function () {
                    var v = swan.validation;
                    $.extend($.validator.messages, v.lang);
                    v.extendRegex();

                    swan.validation.load = true;
                    var q = swan.validation.queue;
                    var len = q.length;

                    for (var i = 0; i < len; i++) {
                        var qu = q[i];
                        if (typeof qu.fun == "function") {
                            qu.fun();
                        }
                    }

                    q = [];

                    /*if (typeof callBack == "function") {
                        callBack();
                    }*/
                }, "validation");
            });

            s.loadStyle(s.validation.cssPath, function () { }, "validation");
        }

        if (v.load) {
            if (typeof callBack == "function") {
                callBack();
            }
        }
        else {
            if (typeof callBack == "function") {
                v.queue.push({ fun: callBack });
            }
        }
    },
    //Create a form validation
    init: function (options) {
        var config = {};
        config.onsubmit = false;
        config.onclick = false;
        if (options.onkeyup == null || options.onkeyup) {
            config.onkeyup = function (element, event) {
                var v = swan.validation;
                var formId = element.form.id;
                var validation = v.getValidationByformId(formId);
                if (validation) {
                    validation.validate.element("#" + element.id);
                }
            };
        }
        else {
            config.onkeyup = false;
        }
        if (options.onfocusout == null || options.onfocusout) {
            config.onfocusout = function (element, event) {
                var v = swan.validation;
                var formId = element.form.id;
                var validation = v.getValidationByformId(formId);
                if (validation) {
                    validation.validate.element("#" + element.id);
                }
            };
        }
        else {
            config.onfocusout = false;
        }
        config.success = (options.success == null ? "valid" : options.success);
        config.errorClass = (options.errorClass == null ? "error" : options.errorClass);
        config.errorElement = "div";
        if (options.errorContainerId) {
            config.errorLabelContainer = "#" + options.errorContainerId;
            config.wrapper = "li";
        }
        if (options.messages) {
            config.messages = options.messages;
        }

        var formId = options.formId;
        if (formId) {
            options.validate = $("#" + formId).validate(config);
            this.validations.push(new this.validation(options));

            for (var rule in options.rules) {
                this.addRule(rule, options.rules[rule]);
            }

            var inputs = $("#" + formId + " [rules]");
            var len = inputs.length;
            for (var i = 0; i < len; i++) {
                var input = inputs[i];
                var str = input.getAttribute("rules");
                var rules = eval("({" + str + "})");
                this.addRule(input.id, rules);
            }
        }
    },
    //Switching skin
    changeSkin: function (skin) {
        $("link[controltype=validation]").remove();

        var s = swan;
        if (skin) {
            s.validation.skin = skin;
        }
        else {
            s.validation.skin = "default";
        }
        s.validation.cssPath = s.scriptSrc + "/component/validation/skin/" + s.validation.skin + "/" + s.lang + "/css/default.css";

        s.loadStyle(s.validation.cssPath, function () { }, "validation");
    },
    //To add validation rules to specify the dynamic control
    addRule: function (id, rule) {
        var element = $("#" + id);
        if (element && element.length > 0) {
            element.rules("add", rule);
        }
    },
    //Validation controls to the specified ID
    validateById: function (id) {
        var element = $("[id=" + id + "]");
        if (element && element.length > 0) {
            this.validator.element(element);
            var error = this.validator.errors().filter(function () {
                return $(this).attr('for') == element.name;
            });
            if (error.length && error.css("display") != "none") {
                return false;
            }
            return true;
        }
        return false;
    },
    //To remove the specified control dynamic validation rules
    removRule: function (id) {
        var element = $("#" + id);
        if (element && element.length > 0) {
            element.rules("remove");
        }
    },
    //Verify that the specified form
    form: function (formId) {
        var validation = this.getValidationByformId(formId);
        if (validation) {
            return validation.validate.form();
        }

        return null;
    },
    element: function (formId, elementId) {
        var validation = this.getValidationByformId(formId);
        if (validation) {
            return validation.validate.element($("#" + elementId));
        }

        return null;
    },
    //Reset the specified form validation
    resetForm: function (formId) {
        var validation = this.getValidationByformId(formId);
        if (validation) {
            validation.validate.resetForm();
        }
    },
    //Gets a validation object
    getValidationByformId: function (formId) {
        var validations = swan.validation.validations;
        var len = validations.length;
        for (var i = 0; i < len; i++) {
            if (validations[i].formId == formId) {
                return validations[i];
            }
        }

        return null;
    },
    //Add a custom authentication method extension
    addRuleMethod: function (options) {
        if (typeof options.fun == "function") {
            $.validator.addMethod(options.name, options.fun, options.message);
        }
        else {
            $.validator.addMethod(options.name, function (value, element) {
                if ($.trim(value).length) {
                    var regex = options.regex;
                    value = value.replace(/(^\s*)/g, "").replace(/(\s*$)/g, "");  
                    return regex.test(value);
                }
                return true;
            }, options.message);
        }
    },
    //Within the Swan framework validation controls extension
    extendRegex: function () {
        var regex = /((\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$)/;
        var v = swan.validation;
        v.addRuleMethod({ name: "telephone", regex: regex, message: v.lang.telephone });
    }
};

//The Swan framework effect controls
swan.effect = {
		toolTipDelay:[],
		cssPath:null,
		skin:null,
		//Effect controls entrance
		onReady:function(callBack, options){
			$("script[controltype=effect]").remove();
			$("link[controltype=effect]").remove();
			
			var s = swan;
			if(options && options.skin){
				s.effect.skin = options.skin;
			}
			else{
				s.effect.skin = "default";
			}
			s.effect.cssPath = s.scriptSrc + "/effect/skin/" + s.effect.skin + "/css/default.css";
			s.loadStyle(s.effect.cssPath, function(){}, "effect");
			
			if(s.jqueryUI.load){
				this.getReady(callBack, options);
			}
			else{
				s.jqueryUI.queue.push({getReady:this.getReady, callBack:callBack, options:options});
				s.jqueryUI.onReady();
			}
		},
		getReady:function(callBack, options){
			var effect = swan.effect;
			var len = effect.toolTipDelay.length;
			for(var i = 0;i<len;i++){
				effect.toolTip(effect.toolTipDelay[i]);
			}
			effect.toolTipDelay = [];
			
			if(typeof callBack == "function"){
				callBack();
			}
		},
		//HightLight
		highLight:function(options){
			var defaultOptions={target:null,option:null,duration:500,className:null,callBack:null};
			if(typeof options.target =="string"){
				options.target = $("#" +options.target);
			}else{
				options.target = $(options.target);
			}
			options=$.extend(this.defaultOptions,options);
			if(options.className == null){
				options.target.effect("highlight",options.option,options.duration,options.callBack);
			}else{
				options.target.toggleClass(options.className,options.duration,options.callBack);
			}
		},
		//Fade
		fade:function(options){
			var defaultOptions={target:null,duration:500,callBack:null};
			if(typeof options.target =="string"){
				options.target = $("#" +options.target);
			}else{
				options.target = $(options.target);
			}
			
			options=$.extend(this.defaultOptions,options);
			if(options.target.css("display")!="none"){
				options.target.fadeOut(options.duration,options.callBack);
			}else{
				options.target.fadeIn(options.duration,options.callBack);
			}
		},
		//ToolTip
		toolTip:function(options){
			if(typeof $(document).tooltip == "function"){
				if(options && options.target){
					if(typeof options.target =="string"){
						options.target = $("#" +options.target);
					}else{
						options.target = $(options.target);
					}
			
					if(options.target.attr("title") == undefined){
						options.target.attr("title","");
					}
					options.target.tooltip(options.option);
				}
				else{
					$( document ).tooltip();
				}
			}
			else{
				this.toolTipDelay.push(options);
			}
		}
};

swan.message = {
    ready: false,
    load: false,
    queue: [],
    langPath: null,
    cssPath: null,
    skin: null,
    templatePath: null,
    defaultOption: { width: 300, height: 150, title: "" },
    setDefault: function (options) {
        var d = swan.message.defaultOption;
        swan.message.defaultOption = $.extend({}, d, options);
    },
    createMessage: function (options) {
        $("." + options.id).dialog({
            title: "",
            width: options.width ? options.width : this.defaultOption.width,
            height: options.height ? options.height : this.defaultOption.height,
            modal: options.model != null ? options.model : true,
            autoOpen: false,
            draggable: false,
            resizable: false,
            closeOnEscape: false,
            show: 300,
            hide: 300,
            fixed: false
        });
    },
    onReady: function (callBack, options) {
        var s = swan;
        var m = s.message;

        if (!m.ready) {
            m.ready = true;
            if (options && options.skin) {
                m.skin = options.skin;
            }
            else {
                m.skin = "default";
            }

            m.cssPath = s.scriptSrc + "/component/message/skin/" + m.skin + "/css/default.css";
            m.templatePath = s.scriptSrc + "/component/message/template";

            if (s.jqueryUI.load) {
                m.getReady(callBack, options);
            }
            else {
                s.jqueryUI.queue.push({ getReady: m.getReady, callBack: callBack, options: options });
                s.jqueryUI.onReady();
            }

            s.loadStyle(m.cssPath, function () { }, "message");
        }

        if (m.load) {
            if (typeof callBack == "function") {
                callBack();
            }
        }
        else {
            if (typeof callBack == "function") {
                m.queue.push({ fun: callBack });
            }
        }
    },
    getReady: function (callBack, options) {
        var s = swan;
        var templatePath = s.message.templatePath + "/message_Default_Template.html";
        s.message.langPath = s.scriptSrc + "/component/message/lang/" + s.lang + "/lang.js";
        s.loadScript(swan.message.langPath, function () {
            s.ajax.get(templatePath, function (data) {
                var div = document.createElement("div");
                var html = $(div).append(data).find("script").html();
                div = null;
                swan.setSimpleJsmartTemplate(html, "body", {}, "message");

                swan.message.load = true;
                var q = swan.message.queue;
                var len = q.length;

                for (var i = 0; i < len; i++) {
                    var qu = q[i];
                    if (typeof qu.fun == "function") {
                        if (qu.options) {
                            qu.fun(qu.options);
                        }
                        else {
                            qu.fun();
                        }
                    }
                }

                q = [];

                /*if (typeof callBack == "function") {
                    callBack();
                }*/
            });
        });
    },
    //title, message, width, height, type, callBack
    alert: function (options) {
        var m = swan.message;
        //已经加载message所需资源
        if (m.load) {
            options.id = "swan_alert_messageTemplate";
            m.createMessage(options);

            if (options.title) {
                $("." + options.id + " .swan_message_Title span").html(options.title);
            }
            else {
                $("." + options.id + " .swan_message_Title span").html(m.defaultOption.title);
            }
            if (options.message) {
                $("." + options.id + " .swan_message_Content .swan_message").html(options.message);
            }
            if (options.type) {
                $("." + options.id + " .swan_message_image").removeClass()
			.addClass("swan_message_image")
			.addClass("swan_message_" + options.type);
            }
            else {
                $("." + options.id + " .swan_message_image").removeClass().addClass("swan_message_image");
            }

            $("." + options.id + " [operate=okDialog]").unbind().bind("click", function () {
                if (typeof options.callBack == "function") {
                    if (options.data) {
                        options.callBack(options.data);
                    }
                    else {
                        options.callBack();
                    }
                }

                $(".swan_alert_messageTemplate").dialog("close");
            });

            $("." + options.id + " [operate=closeDialog]").unbind().bind("click", function () {
                $(".swan_alert_messageTemplate").dialog("close");
            });

            $(".swan_alert_messageTemplate").dialog("open");
        }
        //未加载message所需资源
        else {
            m.queue.push({ fun: m.alert, options: options });
        }
    },
    confirm: function (options) {
        var m = swan.message;
        //已经加载message所需资源
        if (m.load) {
            options.id = "swan_confirm_messageTemplate";
            m.createMessage(options);

            if (options.title) {
                $("." + options.id + " .swan_message_Title span").html(options.title);
            }
            else {
                $("." + options.id + " .swan_message_Title span").html(m.defaultOption.title);
            }
            if (options.message) {
                $("." + options.id + " .swan_message_Content .swan_message").html(options.message);
            }

            $("." + options.id + " [operate=confirmDialog]").unbind().bind("click", function () {
                if (typeof options.callBack == "function") {
                    if (options.data) {
                        options.callBack(options.data);
                    }
                    else {
                        options.callBack();
                    }
                }

                $(".swan_confirm_messageTemplate").dialog("close");
            });

            $("." + options.id + " [operate=closeDialog]").unbind().bind("click", function () {
                $(".swan_confirm_messageTemplate").dialog("close");
            });

            $(".swan_confirm_messageTemplate").dialog("open");
        }
        //未加载message所需资源
        else {
            m.queue.push({ fun: m.confirm, options: options });
        }
    },
    process: {
        timer: null,
        remoteSource: null,
        getUploadProcess: function (pb) {
            var s = swan;
            s.ajax(pb);
            s.message.process.timer = setTimeout(function (pb) { swan.message.process.getUploadProcess(pb); }, 500, pb);
        },
        open: function (options) {
            var m = swan.message;
            //已经加载message所需资源
            if (m.load) {
                options.id = "swan_process_messageTemplate";
                options.width = options.width ? options.width : 100;
                options.height = options.width ? options.width : 50;
                this.remoteSource = options.remoteSource;
                m.createMessage(options);

                //标题
                if (options.title) {
                    $("." + options.id + " .swan_message_Title span").html(options.title);
                }
                else {
                    $("." + options.id + " .swan_message_Title span").html(m.defaultOption.title);
                }

                this.remoteSource.successCallback = function (data) {
                    var p = swan.message.process;
                    $(".swan_process_messageTemplate .swan_message_upload_percent").html(data.percent + "%");
                    //清除定时器
                    if (data.percent == "100") {
                        clearTimeout(p.timer);
                    }
                };
                this.remoteSource.errorCallback = function () {
                };

                //开始定时执行进度条
                $(".swan_process_messageTemplate .swan_message_upload_percent").html("0%");
                var p = swan.message.process;
                p.timer = setTimeout(function (pb) { swan.message.process.getUploadProcess(pb); }, 500, p.remoteSource);

                $(".swan_process_messageTemplate").dialog("open");
            }
            //未加载message所需资源
            else {
                m.queue.push({ fun: m.process.open, options: options });
            }
        },
        close: function (success) {
            //清除定时器
            clearTimeout(swan.message.process.timer);
            if (success) {
                $(".swan_process_messageTemplate .swan_message_upload_percent").html("100%");
            }

            $(".swan_process_messageTemplate").dialog("close");
        }
    }
};

swan.dialog = {
    ready: false,
    load: false,
    queue: [],
    langPath: null,
    cssPath: null,
    skin: null,
    templatePath: null,
    defaultOption: { width: 300, height: 150, title: "" },
    setDefault: function (options) {
        var d = swan.message.defaultOption;
        swan.message.defaultOption = $.extend({}, d, options);
    },
    createDialog: function (options) {
        $("#" + options.id).dialog({
            title: "",
            width: options.width ? options.width : this.defaultOption.width,
            height: options.height ? options.height : this.defaultOption.height,
            modal: options.model != null ? options.model : true,
            autoOpen: false,
            draggable: false,
            resizable: false,
            closeOnEscape: false,
            show: 300,
            hide: 300,
            fixed: false
        });
    },
    onReady: function (callBack, options) {
        var s = swan;
        var d = s.dialog;

        if (!d.ready) {
            d.ready = true;
            if (options && options.skin) {
                d.skin = options.skin;
            }
            else {
                d.skin = "default";
            }

            d.cssPath = s.scriptSrc + "/component/dialog/skin/" + d.skin + "/css/default.css";
            d.langPath = s.scriptSrc + "/component/dialog/lang/" + s.lang + "/lang.js";
            s.loadStyle(d.cssPath, function () { }, "dialog");

            if (s.jqueryUI.load) {
                d.getReady(callBack, options);
            }
            else {
                s.jqueryUI.queue.push({ getReady: d.getReady, callBack: callBack });
                s.jqueryUI.onReady();
            }
        }

        if (d.load) {
            if (typeof callBack == "function") {
                callBack();
            }
        }
        else {
            if (typeof callBack == "function") {
                d.queue.push({ fun: callBack });
            }
        }
    },
    getReady: function (callBack) {
        var s = swan;
        s.loadScript(s.dialog.langPath, function () {
            swan.dialog.load = true;
            var q = swan.dialog.queue;
            var len = q.length;

            for (var i = 0; i < len; i++) {
                var qu = q[i];
                if (typeof qu.fun == "function") {
                    if (qu.options) {
                        qu.fun(qu.options);
                    }
                    else {
                        qu.fun();
                    }
                }
            }

            q = [];

            /*if (typeof callBack == "function") {
            callBack();
            }*/
        });
    },
    init: function (options) {
        var s = swan;
        var d = s.dialog;

        //已经加载message所需资源
        if (d.load) {
            var tp = options.templatePath ? s.rootSrc + options.templatePath : s.scriptSrc + "/component/dialog/template/";
            var template = options.template ? options.template : "dialog_Default_Template";
            if (template.lastIndexOf('/') > -1) {
                this.template = template.substring(template.lastIndexOf('/') + 1, template.length);
            }
            else {
                this.template = template;
            }
            this.templatePath = tp + template + ".html";

            swan.ajax.get(d.templatePath, function (data) {
                var div = document.createElement("div");
                var html = $(div).append(data).find("script").html();
                div = null;
                swan.setSimpleJsmartTemplate(html, "body", options, "dialog");
                swan.dialog.createDialog(options);

                if (options.title) {
                    $("#" + options.id + " .swan_dialog_Title span").html(options.title);
                }

                $("#" + options.id + " [operate=okDialog]").unbind().bind("click", function () {
                    if (typeof options.callBack == "function") {
                        if (options.data) {
                            options.callBack(options.data);
                        }
                        else {
                            options.callBack();
                        }
                    }

                    $("#" + options.id).dialog("close");
                });

                $("#" + options.id + " [operate=closeDialog]").unbind().bind("click", function () {
                    $("#" + options.id).dialog("close");
                });

                var btns = options.buttons;
                if (btns) {
                    for (var btn in btns) {
                        $("#" + btn).unbind().bind("click", btns[btn]);
                    }
                }

                if (typeof options.onInit == "function") {
                    options.onInit();
                }
            });
        }
        //未加载message所需资源
        else {
            d.queue.push({ fun: d.init, options: options });
        }
    },
    open: function (id) {
        $("#" + id).dialog("open");
    },
    close: function (id) {
        $("#" + id).dialog("close");
    }
};

//The Swan framework navigation control
swan.navigation = {
		skin:null,
		//cssPath:null,
		cssPath:null,
		navigations:[],
		changeSkin:function(skin){
			$("link[controltype=navigation]").remove();
			
			var s = swan;
			if(skin){
				s.navigation.skin = skin;
			}
			else{
				s.navigation.skin = "default";
			}
			
			s.navigation.cssPath = s.scriptSrc + "/component/navigation/skin/" + s.navigation.skin + "/css/default.css";	
			s.navigation(s.navigation.cssPath, function(){}, "navigation");
		},
		//The navigation object
		navigation:function(options){
			var s = swan;
			this.id = options.id;
			this.containerId = options.containerId ? options.containerId:"";
			this.dateType = options.dateType? options.dateType : "json";
			var tp = s.scriptSrc;
			var tp = tp.substring(0, tp.lastIndexOf("/swan"));
			this.tp = options.templatePath ? tp + options.templatePath: s.scriptSrc + "/component/navigation/template/";
			var template = options.template ? options.template:"navigation_Default_Template";
			if(template.lastIndexOf('/') > -1){
				this.template = template.substring(template.lastIndexOf('/')+1, template.length);
			}
			else{
				this.template = template;
			}
			this.templatePath = this.tp + template + ".html";
			
			//options.localSource && (!options.remoteSource || s.debug)
			if(options.localSource && (!options.remoteSource || s.debug)){
				this.localSource = options.localSource;
				this.remoteSource = null;
			}
			else if(options.remoteSource){
				this.localSource = null;
				this.remoteSource = options.remoteSource;
				this.remoteSource.type = options.remoteSource.type !=null?options.remoteSource.type:"post";

				
				this.remoteSource.successCallback  = function(data){
					var s = swan;
					s.navigation.showNavigation(data);
				};
			}
		},
		//Display the navigation data
		showNavigation:function(data){
			var s = swan;
			var n = s.navigation.navigations;
			s.setJsmartTemplate(n.template, n.containerId, data);
			$( ".menu" ).menu({ position: { at: "left bottom" } });
		},
		//The navigation control entrance
		onReady:function(callBack, options){
			$("script[controltype=navigation]").remove();
			$("link[controltype=navigation]").remove();
			
			var s = swan;
			if(options && options.skin){
				s.navigation.skin = options.skin;
			}
			else{
				s.navigation.skin = "default";
			}

			s.navigation.cssPath = s.scriptSrc + "/component/navigation/skin/" + s.navigation.skin + "/css/default.css";
			s.navigation.uiPath = s.scriptSrc + "/ext/jquery/jquery-ui-1.10.3.custom.min.js";
			s.loadStyle(s.navigation.cssPath, function(){}, "navigation");
			
			if(s.jqueryUI.load){
				this.getReady(callBack, options);
			}
			else{
				s.jqueryUI.queue.push({getReady:this.getReady, callBack:callBack});
				s.jqueryUI.onReady();
			}
		},
		getReady:function(callBack){
			if(typeof callBack == "function"){
				callBack();
			}
		},
		//Create a navigation control
		init:function(options){
			var n = swan.navigation;
			options = new n.navigation(options);
			n.navigations=options;

			swan.ajax.get(options.templatePath, function(data){
				if($("#" + options.template).length == 0){
					$("body").append(data);
				}
				
				var s = swan;
				if(options.localSource){
					s.navigation.showNavigation(options.localSource);
				}
				else if(options.remoteSource){
					if(typeof options.successCallback != "function"){
						options.successCallback = function(data){
							swan.navigation.showNavigation(data.data);
						};
					}
					
					s.ajax(options.remoteSource);
				}
			});
		}
};

//The Swan framework datepicker control
swan.datePicker = {
    ready: false,
    load: false,
    queue: [],
    skin: null,
    cssPath: null,
    //Display the datepicker data
    showDatePicker: function (options) {
        switch (options.mode) {
            case "basic":
                options.option = options.option;
                break;
            case "simple":
                options.option = $.extend(options.option, { changeYear: true, changeMonth: true, showButtonPanel: true, yearRange: "c-1000:c+1000" });
                break;
            default:
                break;
        }
        options.option.onSelect = function (dateText, inst) {
            $("#" + inst.id).blur().change();
        }
        if (options.containerId) {
        	var d = $("#" + options.containerId);
        	var dv = d.val();
            d.datepicker(options.option);
            if(dv && dv.length > 0){
            	d.val(dv);
            }
        } else if (options.containerBeginId && options.containerEndId) {
        	var b = $("#" + options.containerBeginId);
        	var bv = b.val();
            b.datepicker($.extend(options.option, { onClose: function (selectedDate) {
            	if(selectedDate && selectedDate.length>0){
            		$("#" + options.containerEndId).datepicker("option", "minDate", selectedDate);
            	}
            }
            }));
            if(bv && bv.length > 0){
            	b.val(bv);
            }
            
            var e = $("#" + options.containerEndId);
            var ev = e.val();
            e.datepicker($.extend(options.option, { onClose: function (selectedDate) {
            	if(selectedDate && selectedDate.length>0){
            		$("#" + options.containerBeginId).datepicker("option", "maxDate", selectedDate);
            	}
            }
            }));
            if(ev && ev.length > 0){
            	e.val(ev);
            }
        }
    },
    //The datepicker control entrance
    onReady: function (callBack, options) {
        var s = swan;
        var d = s.datePicker;

        if (!d.ready) {
            d.ready = true;
            if (options && options.skin) {
                d.skin = options.skin;
            }
            else {
                d.skin = "default";
            }

            d.cssPath = s.scriptSrc + "/component/datePicker/skin/" + d.skin + "/css/default.css";
            s.loadStyle(d.cssPath, function () { }, "datePicker");

            if (s.jqueryUI.load) {
                this.getReady(callBack);
            }
            else {
                s.jqueryUI.queue.push({ getReady: d.getReady, callBack: callBack });
                s.jqueryUI.onReady();
            }
        }

        if (d.load) {
            if (typeof callBack == "function") {
                callBack();
            }
        }
        else {
            if (typeof callBack == "function") {
                d.queue.push({ fun: callBack });
            }
        }
    },
    getReady: function (callBack) {
        /*if (typeof callBack == "function") {
        callBack();
        }*/
        swan.datePicker.load = true;
        var q = swan.datePicker.queue;
        var len = q.length;

        for (var i = 0; i < len; i++) {
            var qu = q[i];
            if (typeof qu.fun == "function") {
                if (qu.options) {
                    qu.fun(qu.options);
                }
                else {
                    qu.fun();
                }
            }
        }

        q = [];
    },
    //Create a datepicker control
    init: function (options) {
        var d = swan.datePicker;
        //已经加载message所需资源
        if (d.load) {
            d.showDatePicker(options);
        }
        else {
            d.queue.push({ fun: d.init, options: options });
        }
    },
    setOptions: function (options) { 
    	/*if(){
    		
    	}*/
    }
};

//The Swan framework fileUpload control
swan.fileUpload = {
    cssPath: null,
    skin: null,
    files: [],
    beginUpload: false,
    timer: null,
    template: "fileUpload_Default_Template",
    fileUploadAutoQueue: [],
    rules:[],
    currentUploadFile: null,
    onReady: function (callBack, options) {
        $("script[controltype=fileUpload]").remove();
        $("link[controltype=fileUpload]").remove();

        var s = swan;
        if (options && options.skin) {
            s.fileUpload.skin = options.skin;
        }
        else {
            s.fileUpload.skin = "default";
        }

        s.fileUpload.cssPath = s.scriptSrc + "/component/fileUpload/skin/" + s.fileUpload.skin + "/css/default.css";

        if (typeof callBack == "function") {
            callBack();
        }

        s.loadStyle(s.fileUpload.cssPath, function () { }, "fileUpload");
    },
    init: function (options) {
        var file = new swan.fileUpload.file(options);
        this.files.push(file);
        //创建一个文件上传
        this.createFile(file);
    },
    //上传文件实例对象
    file: function (options) {
        var f = swan.fileUpload;
        this.index = f.files.length;
        this.id = options.id;
        this.fileId = "swan_file_" + this.id;
        this.filter = options.filter;
        this.onChange = options.onChange;
        this.onClean = options.onClean;
        this.checkSize = true;
        this.message = options.message?options.message:{};
    },
    createFile: function (file) {
        var f = swan.fileUpload;
        var id = "swan_file_" + file.id;
        var txtId = "swan_file_txt_" + file.id;
        //上传控件html
        var fileHtml = '<div style="float:left;" class="swanFileUploadTxtDiv"><input class="swanFileUploadTxt" id="' + txtId + '" name="' + txtId + '" readonly="readonly" type="text"/>' +
        '<input type="button" class="swanFileUploadCleanBtn" forFile="' + file.id + '"/></div>' +
        '<div style="float:left;margin-left:5px;"><div class="swanFileUploadBtn">' +
        '<input class="swanOldFileUpload" type="file" id="' + id + '" name="' + id + '" status="0" forFile="' + file.id + '" index="' + file.index + '"/>' +
        '<input class="swanNewFileUpload" type="file" id="' + id + '_temp" status="0" forFile="' + file.id + '" index="' + file.index + '"/>' +
        '</div></div><br style="height:0;width:0;clear:both;"/>';
        //上传控件错误提示
        $("#" + file.id).after('<div errorForFile="' + file.id + '" class="swanFileUploadError"></div>');

        //生成上传控件
        var fileObj = $("#" + file.id).html(fileHtml);
        fileObj.on("click", ".swanFileUploadCleanBtn", function(){
        	var id = this.getAttribute("forFile");
            swan.fileUpload.refresh(id);
            
            id = "swan_file_" + id;
            var fileUpload = $("#" + id);
            var index = fileUpload.attr("index");
            var file = swan.fileUpload.files[index];
            
            if(typeof file.onClean == "function"){
            	file.onClean(fileUpload[0]);
            }
        }).on("change", "#" + id + "_temp", function(){
        	var f = swan.fileUpload;
            var id = this.getAttribute("forFile");
            var index = this.getAttribute("index");
            var status = this.getAttribute("status");
            var file = f.files[index];
            
            var fileUpload = $("#swan_file_" + id + "_temp");
            var value = fileUpload[0].value;
            
            //上传控件有值
            if(value && value.length>0){
            	//用当前控件覆盖真正要提交的表单
            	$("#swan_file_" + id).remove();
            	fileUpload.attr("class", "swanOldFileUpload").attr("id", "swan_file_" + id).attr("name", "swan_file_" + id);
            	var temp = '<input class="swanNewFileUpload" type="file" id="swan_file_' + id + '_temp" status="0" forFile="' + file.id + '" index="' + file.index + '"/>';
            	
            	//创建新的temp控件
            	$("#" + id + " .swanFileUploadBtn").append(temp);
            }
            else{
            	return;
            }
            
            value = value.substring(value.lastIndexOf("\\") + 1, value.length);
            $("#swan_file_txt_" + id).val(value);

            file.checkSize = true;
            var result = f.validate(id);
            if (result) {
                var fileDiv = $("#" + id);
                var maxSize = fileDiv.attr("maxSize");
                if (maxSize && value && value.length > 1) {
                    swan.previewImg.checkImageSize(fileUpload[0], maxSize);
                }
            }
            
            //触发用户自定义的onChange事件
            if (typeof file.onChange == "function") {
                file.onChange(fileUpload[0], result);
            }
        });
    },
    refresh: function (fileUploadId) {
        var id = "swan_file_" + fileUploadId;
        var txtId = "swan_file_txt_" + fileUploadId;
        //清空上传文本框
        $("#" + fileUploadId + " #" + txtId).val("");
        //清空上传文本框验证错误
        $("[errorForFile=" + fileUploadId + "]").html("");

        //重新生成上传控件
        var index = $("#" + id).attr("index");
        var html = '<input class="swanOldFileUpload" type="file" id="' + id + '" name="' + id + '" status="0" forFile="' + fileUploadId + '" index="' + index + '"/>' +
        '<input class="swanNewFileUpload" type="file" id="' + id + '_temp" status="0" forFile="' + fileUploadId + '" index="' + index + '"/>'
        $("#" + fileUploadId + " .swanFileUploadBtn").html("").html(html);
    },
    addRuleMethod:function(options){
    	options.fun = function(value, regex){
    		if ($.trim(value).length) {
    			return regex.test(value);
    		}
    	
    		return true;
    	}
    	
    	this.rules.push(options);
    },
    validate: function (fileUploadId) {
        var fileDiv = $("#" + fileUploadId);
        var value = $("#swan_file_txt_" + fileUploadId).val();
        var index = $("#swan_file_" + fileUploadId).attr("index");
        var f = swan.fileUpload.files[index];
        var message;

        //非空验证
        var required = fileDiv.attr("required");
        if (required) {
            if (!value || value.length < 1) {
            	f.message.required = f.message.required?f.message.required:"必填";
                $("[errorforfile=" + fileUploadId + "]").html(f.message.required);
                return false;
            }
        }

        //文件类型验证
        var filter = fileDiv.attr("filter");
        var t = filter;
        if (filter && value && value.length > 1) {
            //检查上传文件名
            var type = value.substr(value.lastIndexOf('.')).toLowerCase();
            type = (type == ".jpeg"?".jpg":type);
            filter = filter.split(",");
            var len = filter.length;
            var match = false;
            for (var i = 0; i < len; i++) {
                if (("." + filter[i].toLowerCase()) == type) {
                    match = true;
                    break;
                }
            }

            if (!match) {
            	f.message.filter = f.message.filter?f.message.filter:"仅支持" + t.toLowerCase() + "格式的文件";
                $("[errorforfile=" + fileUploadId + "]").html(f.message.filter);
                return false;
            }
        }
        
        var len = this.rules.length;
        for(var i = 0; i< len; i++){
        	var rule = this.rules[i];
        	var r = fileDiv.attr(rule.name);
        	if(r){
        		if(rule.fun(value, rule.regex)){
        			continue;
        		}
        		else{
        			$("[errorforfile=" + fileUploadId + "]").html(rule.message);
                    return false;
        		}
        	}
        }

        if (f.checkSize) {
            $("[errorforfile=" + fileUploadId + "]").html("");

            return true;
        }
        else {
            return false;
        }
    },
    getTotalUploadProccess: function () {
        var s = swan;
        s.ajax(s.fileUploadAuto.currentUploadFile.proccessBar);

        s.fileUploadAuto.timer = setTimeout(swan.fileUploadAuto.getTotalUploadProccess, 1000);
    },
    //开始上传
    upload: function () {
        //全部上传完毕
        if (this.fileUploadAutoQueue.length <= 0) {
            this.fileUploadAutoQueue = [];
            this.beginUpload = false;
            return;
        }
        else {
            this.beginUpload = true;
        }

        //异步上传一个文件
        this.ajaxFileUpload(this.fileUploadAutoQueue[0]);
    }
};

//ajax提交表单
swan.ajaxFormSubmit = {
    submit: function (options) {
        var formId = options.formId;
        var frameId = "swan_ajaxFrame_" + (new Date()).valueOf();

        //获取待提交表单
        var form = $("#" + formId)[0];
        //存在用于异步提交的iframe，清除iframe
        if (form.target) {
        	$("#" + form.target).remove();
        }
        
        var frame = document.createElement("iframe");
        frame.id = frameId;
        frame.name = frameId;
        frame.style.display = "none";
        document.body.insertBefore(frame, document.body.childNodes[0]);
        //设置表单提交信息
        form.target = frameId;
        form.method = options.method ? options.method : "post";
        form.encoding = options.encoding ? options.encoding : "application/x-www-form-urlencoded";
        form.action = options.action;

        options.dataType = options.dataType ? options.dataType : "json";
        var s = $.extend({}, $.ajaxSettings, options);
        if (s.global && !$.active++) {
            $.event.trigger("ajaxStart");
        }
        var requestDone = false;
        var xml = {};
        if (s.global) {
            $.event.trigger("ajaxSend", [xml, s]);
        }

        if (s.timeout > 0) {
            setTimeout(function () {
                if (!requestDone) uploadCallback("timeout");
            }, s.timeout);
        }

        try {
        	if (typeof options.onSubimt == "function") {
            	options.onSubimt();
            }
        	
        	//提交表单
            $("#" + formId).submit();
        } catch (e) {
            if (typeof s.errorCallBack == "function") {
                s.errorCallBack();
            }
            //$.handleError(s, xml, null, e);
        }

        //请求完毕回调
        var uploadCallback = function (isTimeout) {
            var afs = swan.ajaxFormSubmit;
            var io = document.getElementById(frameId);
            try {
                if (io.contentWindow) {
                    xml.responseText = io.contentWindow.document.body ? io.contentWindow.document.body.innerHTML : null;
                    xml.responseXML = io.contentWindow.document.XMLDocument ? io.contentWindow.document.XMLDocument : io.contentWindow.document;

                } else if (io.contentDocument) {
                    xml.responseText = io.contentDocument.document.body ? io.contentDocument.document.body.innerHTML : null;
                    xml.responseXML = io.contentDocument.document.XMLDocument ? io.contentDocument.document.XMLDocument : io.contentDocument.document;
                }
            } catch (e) {
                if (typeof s.errorCallBack == "function") {
                    s.errorCallBack();
                }
                //$.handleError(s, xml, null, e);
            }

            if (xml || isTimeout == "timeout") {
                requestDone = true;
                var status;
                try {
                    status = isTimeout != "timeout" ? "success" : "error";
                    //表单提交成功
                    if (status != "error") {
                        var data = swan.ajaxFormSubmit.uploadHttpData(xml, s.dataType);
                        if (typeof s.successCallBack == "function") {
                            s.successCallBack(data, status);
                        }

                        if (s.global) {
                            $.event.trigger("ajaxSuccess", [xml, s]);
                        }
                    } else {
                        if (typeof s.errorCallBack == "function") {
                            s.errorCallBack();
                        }
                        //$.handleError(s, xml, status);
                    }
                } catch (e) {
                    status = "error";
                    if (typeof s.errorCallBack == "function") {
                        s.errorCallBack();
                    }
                    //$.handleError(s, xml, status, e);
                }

                if (s.global) {
                    jQuery.event.trigger("ajaxComplete", [xml, s]);
                }

                if (s.global && ! --jQuery.active) {
                    jQuery.event.trigger("ajaxStop");
                }

                if (s.complete) {
                    s.complete(xml, status);
                }

                xml = null;
            }
        };

        $('#' + frameId).load(uploadCallback);
        return { abort: function () { } };
    },
    uploadHttpData: function (r, type) {
        var data = !type;
        data = type == "xml" || data ? r.responseXML : r.responseText;
        if (type == "script"){
            $.globalEval(data);
        }
        else if (type == "json"){
        	data = data.substring(data.indexOf("{"), data.lastIndexOf("}")+1);
            eval("data = " + data);
        }
        else if (type == "html"){
            $("<div>").html(data).evalScripts();
        }

        return data;
    }
};

//The Swan framework fileUploadAuto control
swan.fileUploadAuto = {
    cssPath: null,
    skin: null,
    files: [],
    beginUpload: false,
    timer: null,
    template: "fileUploadAuto_Default_Template",
    fileUploadAutoQueue: [],
    currentUploadFile: null,
    onReady: function (callBack, options) {
        $("script[controltype=fileUploadAuto]").remove();
        $("link[controltype=fileUploadAuto]").remove();

        var s = swan;
        if (options && options.skin) {
            s.fileUploadAuto.skin = options.skin;
        }
        else {
            s.fileUploadAuto.skin = "default";
        }

        s.fileUploadAuto.cssPath = s.scriptSrc + "/component/fileUploadAuto/skin/" + s.fileUploadAuto.skin + "/css/default.css";
        s.loadScript(s.scriptSrc + "/ext/jquery/ajaxfileupload.js", function () {
            swan.ajax.get(swan.scriptSrc + "/component/fileUploadAuto/template/fileUploadAuto_Default_Template.html", function (data) {
                if ($("#" + swan.fileUploadAuto.template).length == 0) {
                    $("body").append(data);
                }

                if (typeof callBack == "function") {
                    callBack();
                }
            });
        });

        s.loadStyle(s.fileUploadAuto.cssPath, function () { }, "fileUploadAuto");
    },
    init: function (options) {
        var file = new swan.fileUploadAuto.file(options);
        this.files.push(file);

        //创建一个文件上传
        this.createFile(file, true);
    },
    //上传文件实例对象
    file: function (options) {
        var f = swan.fileUploadAuto;
        this.index = f.files.length;
        this.id = options.id;
        this.action = options.action;
        this.filter = options.filter;
        this.proccessBar = options.proccessBar;
        if (this.proccessBar) {
            this.proccessBar.successCallback = function (data) {
                var f = swan.fileUploadAuto;
                $("[forFile=" + f.currentUploadFile.id + "][class=swan_upload_percent]").html(data.percent + "%");
                //清除定时器
                if (data.percent == "100") {
                    clearTimeout(f.timer);
                }
            };
            this.proccessBar.errorCallback = function () {
            };
        };
        this.onChange = options.onChange;
        this.onSuccess = options.onSuccess;
        this.onError = options.onError;
    },
    createFile: function (file, init) {
        var f = swan.fileUploadAuto;
        var id = "swan_file_" + file.id;
        if (init) {
            var html = $("#" + f.template).html();
            html = swan.getJsmartFetch(html, { id: file.id });
            $("#" + file.id).after(html);
        }

        var fileHtml = '<input class="swanNewFileUpload" type="file" id="'
			+ id + '" name="'
			+ id + '" status="0" forFile="' + file.id + '" index="' + file.index + '"/>';
        $("#" + file.id).html(fileHtml).find("[id=" + id + "]")
        .unbind().bind("change", function () {
            var f = swan.fileUploadAuto;
            var id = this.getAttribute("forFile");
            var index = this.getAttribute("index");
            var status = this.getAttribute("status");
            var file = f.files[index];
            //触发用户自定义的onChange事件
            if (typeof file.onChange == "function") {
                file.onChange($("#swan_file_" + id));
            }

            //状态是还未上传
            if (status == "0") {
                //未开始上传,启动上传机制
                if (!f.beginUpload) {
                    f.fileUploadAutoQueue = [];
                    //加入队列
                    f.fileUploadAutoQueue.push(file);
                    f.upload();
                }
                else {
                    //加入队列
                    f.fileUploadAutoQueue.push(file);
                }

                //隐藏当前上传文件控件,状态更新为准备中(1)
                $("[type=file][forFile=" + id + "]").removeClass().addClass("fileUploading").attr("status", "1");
                //显示loading图标
                $("[forFile=" + id + "][class$=swan_upload_result]").css("display", "none");
                $("[forFile=" + id + "][class=swan_upload_loading]").css("display", "block");
                $("[forFile=" + id + "][class=swan_upload_percent]").html("0%").css("display", "block");
            }

            return false;
        });
    },
    getTotalUploadProccess: function () {
        var s = swan;
        s.ajax(s.fileUploadAuto.currentUploadFile.proccessBar);

        s.fileUploadAuto.timer = setTimeout(swan.fileUploadAuto.getTotalUploadProccess, 1000);
    },
    //开始上传
    upload: function () {
        //全部上传完毕
        if (this.fileUploadAutoQueue.length <= 0) {
            this.fileUploadAutoQueue = [];
            this.beginUpload = false;
            return;
        }
        else {
            this.beginUpload = true;
        }

        //异步上传一个文件
        this.ajaxFileUpload(this.fileUploadAutoQueue[0]);
    },
    //异步上传一个文件
    ajaxFileUpload: function (file) {
        //当前上传文件控件状态更新为上传中(2)
        $("[type=file][forFile=" + file.id + "]").attr("status", "2");

        $.ajaxFileUpload({
            url: file.action, //用于文件上传的服务器端请求地址
            secureuri: false, //一般设置为false
            fileUploadId: "swan_file_" + file.id, //文件上传空间的id属性  <input type="file" id="file" name="file" />
            file: file, //文件上传空间的id属性  <input type="file" id="file" name="file" />
            dataType: 'json', //返回值类型 一般设置为json
            //上传成功
            success: function (data, status) {
                //清除定时器
                var f = swan.fileUploadAuto;
                clearTimeout(f.timer);

                $("[forFile=" + this.file.id + "][class=swan_upload_loading]").css("display", "none");
                $("[forFile=" + this.file.id + "][class=swan_upload_percent]").css("display", "none");
                //显示上传结果
                if (data.success) {
                    $("[forFile=" + this.file.id + "][class$=swan_upload_result]").attr("class", "swan_upload_result_success swan_upload_result").css("display", "block");
                }
                else {
                    $("[forFile=" + this.file.id + "][class$=swan_upload_result]").attr("class", "swan_upload_result_error swan_upload_result").css("display", "block");
                }

                //当前上传结束，创建新上传控件
                f.createFile(this.file);
                f.fileUploadAutoQueue.splice(0, 1);
                //上传完毕
                if (f.fileUploadAutoQueue.length <= 0) {
                    f.beginUpload = false;
                    //触发用户自定义的onChange事件
                    if (typeof this.file.onSuccess == "function") {
                        this.file.onSuccess(data);
                    }
                }
                else {
                    //触发用户自定义的onChange事件
                    if (typeof this.file.onSuccess == "function") {
                        this.file.onSuccess(data);
                    }

                    //继续下一个文件上传
                    var file = f.fileUploadAutoQueue[0];
                    f.ajaxFileUpload(file);
                }
            },
            //上传失败
            error: function (data, status, e) {
                //清除定时器
                var f = swan.fileUploadAuto;
                clearTimeout(f.timer);

                $("[forFile=" + this.file.id + "][class=swan_upload_loading]").css("display", "none");
                $("[forFile=" + this.file.id + "][class=swan_upload_percent]").css("display", "none");
                $("[forFile=" + this.file.id + "][class$=swan_upload_result]").attr("class", "swan_upload_result_error swan_upload_result").css("display", "block");

                //当前上传结束，创建新上传控件
                f.createFile(this.file);
                f.fileUploadAutoQueue.splice(0, 1);
                //上传完毕
                if (f.fileUploadAutoQueue.length <= 0) {
                    f.beginUpload = false;
                    //触发用户自定义的onChange事件
                    if (typeof this.file.onError == "function") {
                        this.file.onError(data);
                    }
                }
                else {
                    //触发用户自定义的onChange事件
                    if (typeof this.file.onError == "function") {
                        this.file.onError(data);
                    }

                    //继续下一个文件上传
                    var file = f.fileUploadAutoQueue[0];
                    f.ajaxFileUpload(file);
                }
            }
        });

        //定时执行进度条
        var f = swan.fileUploadAuto;
        f.currentUploadFile = file;
        f.timer = setTimeout(f.getTotalUploadProccess, 1000);
    }
};

swan.previewImg = {
    maxWidth: 200,
    maxHeight: 130,
    timerCheck:null,
    timerPreview:null,
    checkImageSize: function (file, maxSize) {
        var previewId = "checkImageSize";

        var value = file.value;
        var type = value.substr(value.lastIndexOf('.')).toLowerCase();

        if (file.files && file.files[0]) {
            var preview = $("#swanCheckSizeImg");
            if (preview.length < 1) {
                $("body").append('<img id="swanCheckSizeImg" width="-1" height="-1" style="position:absolute;top:-100000000px;left:-100000000px;" onload="swan.previewImg.checkSize(this,\'' + file.id + '\',\'' + maxSize + '\')"/>');
                preview = $("#swanCheckSizeImg");
            }
            else {
                preview.attr("width", -1).attr("height", -1).attr("style", "position:absolute;top:-100000000px;left:-100000000px;").attr("onload", "swan.previewImg.checkSize(this,'" + file.id + "','" + maxSize + "')");
            }

            if (value) {
                if (!(type == ".jpg" || type == ".gif" || type == ".jpeg" || type == ".png")) {
                    preview.attr("width", -1).attr("height", -1).attr("style", "position:absolute;top:-100000000px;left:-100000000px;");

                    return;
                }
            }
            else {
                return;
            }

            preview = preview[0];
            preview.style.width = "auto";
            preview.style.height = "auto";
            preview.src = window.URL.createObjectURL(file.files[0]);
        } else {
            var imgDivSize = $("#swanCheckSizeImg");
            if (imgDivSize.length < 1) {
                $("body").append('<img width="-1" height="-1" id="swanCheckSizeImg" style="visibility:hidden;position:absolute;top:-100000000px;left:-100000000px;" />');
                imgDivSize = $("#swanCheckSizeImg");
            }
            else {
                imgDivSize.attr("width", -1).attr("height", -1).attr("style", "visibility:hidden;position:absolute;top:-100000000px;left:-100000000px;");
            }

            imgDivSize = imgDivSize[0];
            file.select();
            file.blur();
            var imgSrc = document.selection.createRange().text;
            imgDivSize.width = "1";
            imgDivSize.height = "1";
            try {
                imgDivSize.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=image)";
                imgDivSize.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = imgSrc + "?" + (new Date()).valueOf();
            } catch (e) {
                return;
            }

            document.selection.empty();
            clearTimeout(swan.previewImg.timerCheck);
            swan.previewImg.timerCheck = setTimeout(function (options) { swan.previewImg.checkSizeForIE(options.fileId, options.maxSize) }, 20, { fileId: file.id, maxSize: maxSize });
        }

        return;
    },
    checkSize: function (preview, fileId, maxSize) {
        var file = $("#" + fileId);
        var fileUploadId = file.attr("forfile");
        var index = file.attr("index");
        var f = swan.fileUpload.files[index];

        maxSize = maxSize.split(',');
        if (preview.offsetWidth == maxSize[0] && preview.offsetHeight == maxSize[1]) {
            $("[errorforfile=" + fileUploadId + "]").html("");
            f.checkSize = true;
        }
        else {
        	f.message.maxSize = f.message.maxSize?f.message.maxSize:"仅支持图片尺寸"+ maxSize[0] + "*" + maxSize[1];
            $("[errorforfile=" + fileUploadId + "]").html(f.message.maxSize);
            f.checkSize = false;
        }
    },
    checkSizeForIE: function (fileId, maxS) {
        maxSize = maxS.split(',');
        var imgDivSize = $("#swanCheckSizeImg")[0];
        if (imgDivSize.offsetWidth > 1 && imgDivSize.offsetHeight > 1) {
        	clearTimeout(swan.previewImg.timerCheck);
        	
            var file = $("#" + fileId);
            var fileUploadId = file.attr("forfile");
            var index = file.attr("index");
            var f = swan.fileUpload.files[index];

            if (imgDivSize.offsetWidth == maxSize[0] && imgDivSize.offsetHeight == maxSize[1]) {
                $("[errorforfile=" + fileUploadId + "]").html("");
                f.checkSize = true;
            }
            else {
            	f.message.maxSize = f.message.maxSize?f.message.maxSize:"仅支持图片尺寸"+ maxSize[0] + "*" + maxSize[1];
                $("[errorforfile=" + fileUploadId + "]").html(f.message.maxSize);
                f.checkSize = false;
            }
        }
        else {
            //setTimeout(function (preview) { swan.previewImg.checkSizeForIE(fileId) }, 20, { fileId: file.id, maxSize: maxSize });
        	swan.previewImg.timerCheck = setTimeout(function (options) { swan.previewImg.checkSizeForIE(options.fileId, options.maxSize) }, 20, { fileId: fileId, maxSize: maxS });
        }
    },
    cleanImagePreview: function (previewId) {
        var preview = $("#" + previewId + " .previewImag");
        //IE
        if(navigator.userAgent.indexOf("MSIE")>0) {
        	preview = $("#" + previewId + " .previewDivForIE");
            preview.removeAttr("style").css("display","none");;
        }
        else {
        	preview.attr("width", -1);
            preview.attr("height", -1);
            preview.attr("style", "diplay:none;visibility:hidden;");
        }
    },
    setImage:function(previewId, url){
    	var preview = $("#" + previewId + " .previewImag");
        if (preview.length < 1) {
            $("#" + previewId).append('<img class="previewImag" width="-1" height="-1" src="' + url + '" style="diplay:none;visibility:hidden;" onload="swan.previewImg.previewLoad(this)"/>');
            preview = $("#" + previewId + " .previewImag");
        }
    },
    setImagePreview: function (file, previewId) {
        var value = file.value;
        var type = value.substr(value.lastIndexOf('.')).toLowerCase();
 
        if (file.files && file.files[0]) {
            var preview = $("#" + previewId + " .previewImag");
            if (preview.length < 1) {
                $("#" + previewId).append('<img class="previewImag" width="-1" height="-1" style="diplay:none;visibility:hidden;" onload="swan.previewImg.previewLoad(this)"/>');
                preview = $("#" + previewId + " .previewImag");
            }
            else{
            	preview.attr("width", -1).attr("height", -1).attr("style", "diplay:none;visibility:hidden;")
            }

            if (value) {
                if (!(type == ".jpg" || type == ".gif" || type == ".jpeg" || type == ".png")) {
                    preview.attr("width", -1);
                    preview.attr("height", -1);
                    preview.attr("style", "diplay:none;visibility:hidden;");

                    return;
                }
            }
            else {
                return;
            }

            preview = preview[0];
            preview.style.display = "none";
            preview.style.visibility = "hidden";
            preview.style.width = "auto";
            preview.style.height = "auto";
            preview.src = window.URL.createObjectURL(file.files[0]);
        } else {
        	$("#" + previewId + " .previewImag").attr("style", "display:none;visibility:hidden;width:auto;height:auto;")
        	
            var preview = $("#" + previewId + " .previewDivForIE");
            if (preview.length < 1) {
                $("#" + previewId).append('<div class="previewDivForIE"/>');
                preview = $("#" + previewId + " .previewDivForIE");
            }
            else{
            	preview.removeAttr("style");
            }

            if (value) {
                if (!(type == ".jpg" || type == ".gif" || type == ".jpeg" || type == ".png")) {
                    preview.removeAttr("style");

                    return;
                }
            }
            else {
                return;
            }

            preview = preview[0];
            var imgDivSize = $("#preview_size_fake");
            if (imgDivSize.length < 1) {
                $("body").append('<img width="-1" height="-1" id="preview_size_fake" style="position:absolute;top:-100000000px;left:-100000000px;" />');
                imgDivSize = $("#preview_size_fake");
            }
            else{
            	imgDivSize.attr("width", -1).attr("height", -1).attr("style", "position:absolute;top:-100000000px;left:-100000000px;");
            }
            
            imgDivSize = imgDivSize[0];
            file.select();
            file.blur();
            var imgSrc = document.selection.createRange().text;
            imgDivSize.width = "1";
            imgDivSize.height = "1";
            try {
                preview.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
                preview.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = imgSrc + "?" + (new Date()).valueOf();
                imgDivSize.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=image)";
                imgDivSize.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = imgSrc+"?" + (new Date()).valueOf();
            } catch (e) {
                //alert("您上传的图片格式不正确，请重新选择!");
                preview.removeAttribute("style");
                return;
            }

            clearTimeout(swan.previewImg.timerPreview);
            swan.previewImg.timerPreview = setTimeout(function (preview) { swan.previewImg.checkPreviewLoadForIE(preview) }, 20, preview);
            document.selection.empty();
        }

        return;
    },
    checkPreviewLoadForIE: function (preview) {
        var p = swan.previewImg;
        var imgDivSize = $("#preview_size_fake")[0];
        if (imgDivSize.offsetWidth > 1 && imgDivSize.offsetHeight > 1) {
        	clearTimeout(swan.previewImg.timerPreview);
            p.autoSizePreview(preview, imgDivSize.offsetWidth, imgDivSize.offsetHeight);
        }
        else {
            setTimeout(function (preview) { swan.previewImg.checkPreviewLoadForIE(preview) }, 20, preview);
        }
    },
    previewLoad: function (preview) {
        preview.style.width = "auto";
        preview.style.height = "auto";
        preview.style.display = "";
        preview.style.visibility = "visible";
        this.autoSizePreview(preview, preview.offsetWidth, preview.offsetHeight);
    },
    autoSizePreview: function (preview, originalWidth, originalHeight) {
        var zoomParam = this.clacImgZoomParam(this.maxWidth, this.maxHeight, originalWidth, originalHeight);
        preview.style.width = zoomParam.width + 'px';
        preview.style.height = zoomParam.height + 'px';
        preview.style.marginTop = zoomParam.top + 'px';
        preview.style.marginLeft = zoomParam.left + 'px';
    },
    clacImgZoomParam: function (maxWidth, maxHeight, width, height) {
        var param = { width: width, height: height, top: 0, left: 0 };
        if (width > maxWidth || height > maxHeight) {
            rateWidth = width / maxWidth;
            rateHeight = height / maxHeight;

            if (rateWidth > rateHeight) {
                param.width = maxWidth;
                param.height = height / rateWidth;
            } else {
                param.width = width / rateHeight;
                param.height = maxHeight;
            }
        }
        param.left = (maxWidth - param.width) / 2;
        param.top = (maxHeight - param.height) / 2;

        return param;
    }
};

//The Swan framework tree control
swan.tree = {
		langPath:null,
		cssPath:null,
		skin:null,
		ztreejqPath:null,
		ztreeCorePath:null,
		ztreeCheckPath:null,
		//Switching skin
		changeSkin:function(skin){
			$("link[controltype=tree]").remove();
			var s = swan;
			if(skin){
				s.tree.skin = skin;
			}
			else{
				s.tree.skin = "default";
			}
			s.tree.cssPath = s.scriptSrc + "/component/tree/skin/" + s.tree.skin + "/css/default.css";
			s.loadStyle(s.tree.cssPath, function(){}, "tree");
		},
		//The tree control entrance
		onReady:function(callBack,options){
			$("script[controltype=tree]").remove();
			$("link[controltype=tree]").remove();
			var s = swan;
			if(options && options.skin){
				s.tree.skin=options.skin;
			}
			else{
				s.tree.skin="default";
			}
			s.tree.cssPath = s.scriptSrc + "/component/tree/skin/" + s.tree.skin + "/css/zTreeStyle.css";
			s.loadStyle(s.tree.cssPath, function(){}, "tree");
			if(s.jqueryUI.load){
				this.getReady(callBack,options);
			}
			else{
				s.jqueryUI.queue.push({getReady:this.getReady,callBack:callBack,options:options});
				s.jqueryUI.onReady();
			}
		},
		//load js files
		getReady:function(callBack,options){
			var s = swan;
			s.tree.langPath = s.scriptSrc + "/component/tree/lang/" + s.lang + "/lang.js";	
			s.tree.ztreeCorePath = s.scriptSrc + "/ext/jquery/jquery.ztree.core-3.5.js";
			s.tree.ztreeCheckPath = s.scriptSrc + "/ext/jquery/jquery.ztree.excheck-3.5.js";

			s.loadScript(s.tree.langPath, function(){		
				s.loadScript(s.tree.ztreeCorePath, function(){
					s.loadScript(s.tree.ztreeCheckPath, function(){
						if(typeof callBack == "function"){
							callBack();
						}
					});
				});
			});			
		},
		
		//Create a tree
		init:function(options){
			$.fn.zTree.init($("#"+options.id), options.setting, options.data);
		}
};

//The Swan framework comboBox control
swan.comboBox = {
	langPath:null,
	cssPath:null,
	skin:null,
	onReady:function(callBack, options){
        $("link[controltype=comboBox]").remove();

        var s = swan;
        if (options && options.skin) {
        	s.comboBox.skin = options.skin;
        }
        else {
            s.comboBox.skin = "default";
        }
            
        s.comboBox.cssPath = s.scriptSrc + "/component/comboBox/skin/" + s.comboBox.skin + "/css/default.css";
        s.loadStyle(s.comboBox.cssPath, function () {}, "comboBox");
    				
        if (typeof callBack == "function") {
    			callBack();
        }
	},
	init:function(options){
		this.createComboBox(options);
	},
	createComboBox:function(options){
		var s=$.extend({
			width:'auto',
			height:80,
			editable:false,
			sly:false,
			hover:false,
			onSelect:function(){},
			onOpen:function(){}
		},options);
		
		$("#"+s.comboBoxId).each(function(){
			var obj=$(this);
			var str='';
			obj.find('option').each(function(){
				var s_obj=$(this);
				str+='<li val="'+s_obj.val()+'">'+s_obj.html()+'</li>';
			});
			obj.hide().after('<div class="comboBox"><div class="value"><input id="comboBox_' + this.id + '" name="comboBox_' + this.id + '" type="text"/></div><div class="options"><ul>'+str+'</ul></div></div>');
			var s_obj=obj.next('.comboBox');
			var tObj=s_obj.find('input');
			var o_obj=s_obj.find('.options');
			var u_obj=o_obj.find('ul');
			var l_obj=u_obj.find('li');

			if (s.width=='auto'){
				s_obj.width(obj.width()+20);
				t_width=obj.width()-15;
				o_obj.width(obj.width()+18);
			}else if (s.width>0){
				s_obj.width(s.width);
				t_width=s.width-35;
				o_obj.width(s.width-2);
			}
			tObj.width(t_width);
			s_obj.find('input').val(obj.find('option:selected').html());
			if (s.sly&&typeof $.fn.sly=='function'&&s.height!='auto'){
				o_obj.css({'overflow':'hidden','height':s.height+'px'}).append('<div class="scrollbar"><div class="handle"></div></div>');
				s_obj.find('.scrollbar').height(s.height-5);
				s_obj.find('.handle').height(s.height*30/100);
			}else if (!s.sly&&s.height!='auto'){
				o_obj.css({'overflow-y':'auto','height':s.height+'px'});
			}
			s.onOpen();
			var showOpt=function(){
				s_obj.css('z-index','101').find('.options').fadeIn(50,function(){$(document).bind('click',clickClose);});
				tObj.focus();
				if (s.sly){
					s_obj.find('.scrollbar').show();
					o_obj.sly({
						scrollBar: s_obj.find('.scrollbar'),
						dragHandle: 1,
						dynamicHandle: 1,
						dragContent: 1,
						speed: 50,
						startAt: 0,
						scrollBy: 20,
						elasticBounds: 1
					},true);
				}
			};
			//use up or down to generate a event to select a option
			var keyScroll=function(e){
				var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
				if ((keyCode==40||keyCode==38)&&!o_obj.is(':visible')){
					showOpt();
					return false;
				}
				var t;
				if (keyCode==40){
					t='next';
				}else if (keyCode==38){
					t='prev';
				}else if (keyCode==13){
					t='ok';
				}else{
					return;
				}
				selectOpt(t);
			};
			//automatic select a option
			var selectOpt=function(t){
				if (typeof t=='undefined')t='next';
				var cur=l_obj.index(u_obj.find('li.sel'));
				var tar,len=l_obj.length;
				if (t=='next'){
					tar=cur+1;
					tar=tar>=len?0:tar;
				}else if (t=='prev'){
					tar=cur-1;
					if (cur<=0)
						tar=len-1;
				}else if (t=='ok'){
					if (o_obj.is(':visible')&&cur!=-1){
						l_obj.eq(cur).click();
					}else{
						tObj.blur();
						hideOpt();
					}
					return false;
				}
				l_obj.removeClass('sel').eq(tar).addClass('sel');
				setInSight(tar);
			};
			//set the option in sight
			var setInSight=function(n){
				var lst=l_obj.eq(0);
				var oH=o_obj.height();
				var lH=lst.height()+parseInt(lst.css('padding-top').replace(/px/,''))+parseInt(lst.css('padding-bottom').replace(/px/,''))+1;
				o_obj.scrollTop(n*lH-oH+lH);
			};
			var hideOpt=function(){
				o_obj.hide();
				$(document).unbind('click',clickClose);
				if (s.sly){
					s_obj.find('.scrollbar').hide();
				}
			};
			var clickClose=function(){hideOpt();$(document).unbind('click',clickClose);};
			if (s.hover){
				s_obj.find('.value').hover(function(event){
					$('.comboBox').css('z-index','100').find('.options').hide();
					showOpt();
					$(document).bind('click',clickClose);
				},function(){});
			}
			var checkVal=function(){
				var same=false;
				obj.find('option').each(function(){
					if ($(this).val()==tObj.val()){
						same=true;
						obj.find('option').removeAttr('selected');
						$(this).attr('selected','selected');
						return false;
					}
				});
				if (same===false){
					obj.find('option').removeAttr('selected');
					obj.append('<option selected="selected" auto="true" value="'+tObj.val()+'">'+tObj.val()+'</option>');
				}else{
					
				}
				obj.change();
			};
			if (s.editable)
				tObj.css('cursor','text').blur(function(){
					checkVal();
				}).click(function(){
					if (typeof tObj.attr('sel')!='undefined'&&tObj.attr('sel')=='true')
						tObj.removeAttr('sel');
					else
						tObj.select().attr('sel','true');
				});
			else{
				tObj.attr('readonly','readonly');
				s_obj.find('.value').css('cursor','pointer');
			}
			s_obj.find('.value').click(function(){
				if (o_obj.is(":hidden")){
					$('.comboBox').css('z-index','100').find('.options').hide();
					showOpt();
				}
				else{
					hideOpt();
				}
			}).keydown(keyScroll);
			s_obj.find('ul li').click(function(){
				hideOpt();
				tObj.val($(this).html());
				tObj.blur();
				if (typeof $(this).attr('val')!='undefined')
					obj.val($(this).attr('val'));
				else
					obj.val($(this).html());
				obj.change();
				$(document).unbind('click',clickClose);
				s.onSelect();
			}).hover(function(){$(this).addClass('sel');},function(){$(this).removeClass('sel');});
		});
	}
};

//The Swan framework timePicker control
swan.timePicker = {
		skin:null,
		cssPath:null,
		//Switching skin
		changeSkin:function(skin){
			$("link[controltype=timePicker]").remove();
			
			var s = swan;
			if(skin){
				s.timePicker.skin = skin;
			}
			else{
				s.timePicker.skin = "default";
			}
			
			s.timePicker.cssPath = s.scriptSrc + "/component/timePicker/skin/" + s.timePicker.skin + "/css/default.css";
					
			s.timePicker(s.timePicker.cssPath, function(){}, "timePicker");
		},
		//The timepicker object
		timePicker:function(options){
			var s = swan;
			this.id = options.id;
			this.containerId = options.containerId ? options.containerId:"";
			this.startContainerId = options.startContainerId ? options.startContainerId:"";
			this.endContainerId = options.endContainerId ? options.endContainerId:"";
			//this.dateType = options.dateType? options.dateType : "json";

		},
		//Display the datepicker data
		showTimePicker:function(options){
			if(options.containerId){
				$("#"+options.containerId).timePicker(options.option);
			}
			if(options.startContainerId && options.endContainerId){
				$("#"+options.startContainerId).timePicker(options.option);
				$("#"+options.endContainerId).timePicker(options.option);
			}
			//Validate
			$("#"+options.endContainerId).change(function() {
				if($.timePicker("#"+options.startContainerId).getTime() > $.timePicker(this).getTime()) {
					$(this).addClass("error");
				}
				else {
					$(this).removeClass("error");
				}
			});
			// Store time used by duration.
			var oldTime = $.timePicker("#"+options.startContainerId).getTime();
			// Keep the duration between the two inputs.
			$("#"+options.startContainerId).change(function() {
			  if ($("#"+options.endContainerId).val()) { // Only update when second input has a value.
			    // Calculate duration.
			    var duration = ($.timePicker("#"+options.endContainerId).getTime() - oldTime);
			    if(isNaN(duration)){
			    	duration = 0;
			    }
			    var time = $.timePicker("#"+options.startContainerId).getTime();
			    // Calculate and update the time in the second input.
			    $.timePicker("#"+options.endContainerId).setTime(new Date(new Date(time.getTime() + duration)));
			    oldTime = time;
			  }
			});
		},
		//The datepicker control entrance
		onReady:function(callBack, options){
			$("script[controltype=timePicker]").remove();
			$("link[controltype=timePicker]").remove();
			
			var s = swan;
			if(options && options.skin){
				s.timePicker.skin = options.skin;
			}
			else{
				s.timePicker.skin = "default";
			}

			s.timePicker.cssPath = s.scriptSrc + "/component/timePicker/skin/" + s.timePicker.skin + "/css/default.css";
			s.loadStyle(s.timePicker.cssPath, function(){}, "timePicker");
			
			if(s.jqueryUI.load){
				this.getReady(callBack, options);
			}
			else{
				s.jqueryUI.queue.push({getReady:this.getReady, callBack:callBack});
				s.jqueryUI.onReady();
			}
		},
		getReady:function(callBack, options){
			var s = swan;
			s.timePicker.timePickerPath = s.scriptSrc + "/ext/jquery/jquery.timePicker.js";
			s.loadScript(s.timePicker.timePickerPath, function(){		
				if(typeof callBack == "function"){
					callBack();
				}
			});
		},
		//Create a timePicker control
		init:function(options){
			var s = swan;
			s.timePicker.showTimePicker(options);
		}
};

//The Swan framework searchBox control
swan.searchBox = {
	cssPath:null,
	skin:null,
	flexboxPath:null,
	
	//Switching skin
	changeSkin:function(skin){
		$("link[controltype=searchBox]").remove();
		var s = swan;
		if(skin){
			s.searchBox.skin = skin;
		} 
		else{
			s.searchBox.skin = "default";
		}		
		s.searchBox.cssPath = s.scriptSrc + "/component/searchBox/skin/" + s.searchBox.skin + "/css/default.css";
		s.loadStyle(s.searchBox.cssPath, function(){}, "searchBox");
	},
	
	//The searchBox control entrance
	onReady:function(callBack,options){
		$("script[controltype=searchBox]").remove();
		$("link[controltype=searchBox]").remove();
		var s = swan;
		if(options && options.skin){
			s.searchBox.skin = options.skin;
		}
		else{
			s.searchBox.skin = "default";
		}
		s.searchBox.cssPath = s.scriptSrc + "/component/searchBox/skin/" + s.searchBox.skin + "/css/default.css";
		s.loadStyle(s.searchBox.cssPath, function(){}, "searchBox");
		this.getReady(callBack, options);
	},
	
	//load js files
	getReady:function(callBack,options){
		var s = swan;
		s.searchBox.flexboxPath = s.scriptSrc + "/ext/jquery/jquery.flexbox.js";
		s.loadScript(s.searchBox.flexboxPath, function(){
			if(typeof callBack == "function"){
				callBack();
			}
		});
	},
	
	//Create a searchBox
	init:function(options){
		$("#"+options.id).flexbox(options.data,options.param);
	}
};

swan.event={
	getEventTarget:function(event){
		var ev = event || window.event;
		var target = ev.target || ev.srcElement;
		return target;
	},
	getCurrentTarget:function(event){
		var ev = event || window.event;
		var target = ev.currentTarget || ev.activeElement;
		return target;
	},
	stopEventBubble:function(event){
		if(typeof event.preventDefault === "function"){
			event.preventDefault();
			event.stopPropagation();
		}
		else{
			event.returnValue = false;
			event.cancelBubble = true;
		}
	}
};

swan.setLang = function(lang){
	swan.lang = lang;
};
swan.fmt = function(key, component){
	var lang = swan[component].lang;
	if(lang && lang[key]){
		var l = lang[key];
		return lang[key];
	}
	else{
		return "$" + key + "$";
	}
};

//Based on the dynamic loading JS file Swan framework
swan.getReady();