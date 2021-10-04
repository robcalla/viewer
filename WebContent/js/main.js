/**
 * Get all the parameters from the url
 *
 * @return [string] The vars
 */
function get(name) {
	 if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
	      return decodeURIComponent(name[1]);
}

/**
 * Logout Go to the IDM login page
 */
/*
function logout() {
	delete code;
	window.location.replace(cityenablerUrl + auth_logout);
}
*/
// Function not used
//function getServiceList() {
//
//	var input = new Object();
//	input['action'] = 'getContextsList';
//
//	$.ajax({
//		url : 'ajaxhandler',
//		type : 'GET',
//		dataType : 'json',
//		data : input,
//		success : function(data) {
//			appendCategories(data);
//		},
//		error : function(xhr, status, error) {
//			console.error(error);
//		}
//	});
//}

var currentLanguage;
var userId;
function mainOnDocReady(){

	if(typeof userId_fromSession !== "undefined") userId = userId_fromSession;

	$(".activeLang").text(currentLanguage);

	$('.languagemenu').each(function(){
		var self = $(this);
		self.find('a').each(function(){
			$(this).attr('href', $(this).attr('href')+"&scope="+get('scope')+"&urbanservice="+get('urbanservice'));
		});
	});

	$("#"+currentLanguage).closest('a').attr('href', '#!');
	$("#"+currentLanguage).removeClass('hide');

	var devreg_success = get("success");
	if(devreg_success){
		setTimeout(function(){
			Materialize.toast(get("msg"), 4000, 'green');
		}, 1000);

	}

	$(".button-collapse").sideNav();
	$(".dropdown-button").each(function() {
				var constrainWidth_p = true;
				constrainWidth_p = constrainWidth_p && $(this).attr('data-constrainwidth');
				$(this).dropdown({
					hover : false,
					constrainWidth : constrainWidth_p,
					alignment : 'right'
				});
			});
};

$(document).on('click', '#mobilesearch_trigger', function() {
	var searchbar = $('#mobilesearch');
	searchbar.is(":visible") ? searchbar.slideUp() : searchbar.slideDown();
	return false;
});

//SSH
var session_token = "token";
var session_refreshToken = "refresh_token";
function SetLocalStorage (evt) {
      localStorage.removeItem(session_token);
      localStorage.removeItem(session_refreshToken);

}

if (window.addEventListener) {
// For standards-compliant web browsers
  window.addEventListener("message", SetLocalStorage, false);
}
else {
  window.attachEvent("onmessage", SetLocalStorage);
}

$(document).ready(function() {
    $('select').material_select();
});

$(document).on('input change', '#search', function(){
	var input = $(this).val().trim();
	var close_icon = $(this).closest('form').find('.closeicon');

	if(input.length>0){ close_icon.removeClass('hide'); }
	else{ close_icon.addClass('hide'); }
});

$(document).on('change', '#search', function(){
	var trimmed = $(this).val().trim();
	$(this).val(trimmed);
});

$(document).on('click', '.closeicon', function(){
	$(this).closest('form').find('#search').val("").change();
});


function resizeCockpitPage(){
	console.log("Resize cockpit");
	  //$(window.top.document).find('body').height('0px');
	  var footer = $("footer").outerHeight(true);
	  var win = $(window).height()
	  var bread =  $('#breads').outerHeight(true);
	  var toolbar = $('#navb').outerHeight(true);
	  var menuCont = $('#menuContestuale').outerHeight(true);
	  var iframeHeight = win-toolbar-bread-menuCont-footer;

	  var iframe = $(window.top.document).find("#iframedashboard");

//	  console.log("outerHeight: "+iframe[0].contentWindow.parent.outerHeight)
//	  console.log("pageYOffset: "+iframe[0].contentWindow.parent.pageYOffset)
//	  console.log("scrollY: "+iframe[0].contentWindow.parent.scrollY)
//	  console.log("scrollHeight: "+iframe[0].scrollHeight)
//	  console.log("scrollTop: "+iframe[0].scrollTop)

	  $("#devicelist .deviceitem").height(iframe[0].contentWindow.parent.outerHeight+iframe[0].scrollTop+200+"px");

//	  $("#devicelist .deviceitem").height(iframe.height()+100+"px");

//	  iframe.attr("height", iframeHeight)

}

function getDashboardList(user,pass,appenderFn,elementToRemove = []) {

	Sbi.sdk.services.setBaseUrl({
        protocol: Knowage.protocol
        , host: Knowage.host
        , port: Knowage.port
        , contextPath: 'knowage'
        , controllerPath: 'servlet/AdapterHTTP'
    });

	var docCb = function(json, args, success) {
		if(success) {
			//When uncommented, CFE does not return any available dashboards even for admin
			json = filterResultsByFunctionality(json);
			json = removeElements(json,elementToRemove);
			appenderFn(json);
		}
	}

    this.getDocuments = function() {
	    var url = Sbi.sdk.api.getDocuments({
	    	callback: docCb
	    });
	};

	this.filterResultsByFunctionality = function(data) {
		var filteredResults = [];
		for(var i in data) {
			var document = data[i];
			var docFunctionalities = document.functionalities;
			for (var i = 0; i < docFunctionalities.length; i++) {
				if(docFunctionalities[i] == Knowage.defaultFunctionality) {
					filteredResults.push(document);
				} else
				if(docFunctionalities[i] == Knowage.publicFunctionality) {
					filteredResults.push(document);
				} else
				if(docFunctionalities.includes('/'+userId)) {
					filteredResults.push(document);
				}
			}
		}

		var uniqueNames = uniq = [...new Set(filteredResults)];
		return uniqueNames;
	};

	this.removeElements = function(json,elementToRemove) {
		var filteredResults = json.slice();
		if (elementToRemove.length == 0) return json;
		for (var i in elementToRemove) {
			e = elementToRemove[i];
			for(var j in filteredResults) {
				var document = filteredResults[j];
				var docName = document.name;
				if(docName == e) {
					filteredResults.splice(j,1);
					break;
				}
			}
		}

		return filteredResults;
	};

    //Callback definition for basic auth
    var callback = function(result, args, success) {
      if(success) {
        this.getDocuments();
      } else {
        alert("Connection problem!");
      }
    };

	switch (Knowage.auth) {
		case "idm":
			getDocuments();
		break;

		case "basic":
		    //Knowage Authentication
		    Sbi.sdk.api.authenticate({
		  		params: {
		  			user: Knowage.username,
		  			password: Knowage.password
		  		},
		  		callback: {
		  			fn: callback,
		  			scope: this
		  		}
		  	});
		break;
	}
}
