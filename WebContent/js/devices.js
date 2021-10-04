var checkedall = false;
var deviceperpage = 6;
var currpage = 1;
var devicelist = new Object();
var mapcenter = {};
var scope = '';
var urbanservice = '';
var activeDashboard = {};
var dashboardListGlobal = [];
var replaceChars={"_":" " };
var role_suffix = '_user';

$(document).ready(function(){
	
	$('#chooseType').material_select();
	document.querySelectorAll('.select-wrapper').forEach(t => t.addEventListener('click', e=>e.stopPropagation()))
	
	onDocReady();
	
	// Get the refScope name
	var scopeId = get('scope');
	getServiceName(scopeId);
	var urbanserviceId = get('urbanservice');
	getServicePathName(scopeId, urbanserviceId);

});
	

function onDocReady(){
	mainOnDocReady();
	//invokeKnowage('empty_login');
	$('.modal').modal({dismissible:false});

	scope = get('scope');
	urbanservice = get('urbanservice');
	
	$('#scope_breadcrumb').text(scope).attr("href", 'index');
	$('#urbanservice_breadcrumb').text(urbanservice).attr("href", "urbanservices?scope="+scope);
	$('#dashboard_breadcrumb').text('').attr("href", "");
	$('#cockpitNameTitle').text('');
	$('#backlink').attr('href', 'urbanservices?scope='+scope);
	
	$('#scopeformval').val(scope);
	$('#urbanserviceformval').val(urbanservice);

	$('#pageloader').fadeOut();
	
	getCockpitName(get('urbanservice'));
	
};

function appendDashboard(data) {
	data.forEach(function(d){
		var dashboard = d.name;		
		 $('#cockpitnamefield').append($('<option>', {
		    value: dashboard,
		    text: dashboard
	    }));
		
	});
}

function getCockpitName(urbanService) {
	var input = new Object();
	input['action'] = 'getCockpitNames';
	input['category'] = urbanService; 
	input['context'] = scope;
	
	$.ajax({
		url: 'ajaxhandler',
	    type: 'GET',
	    dataType: 'text',
	    data: input,
		success: function(cockpitNames) {
			var cNames = JSON.parse(cockpitNames);
			dashboardListGlobal = cNames;
			
			var appendDashboard = function appendDashboard(data) {
				data.forEach(function(d){
					var dashboard = d.name;		
					 $('#documentnamefield').append($('<option>', {
					    value: dashboard,
					    text: dashboard
				    }));
					
				});
			};
			
			getDashboardList(Knowage.username,Knowage.password,appendDashboard,cNames);
			
			if(dashboardListGlobal.length == 0) {
				$(".deviceitem").addClass("hide"); 
				$("#remove-cockpit-link").parent().addClass("hide");
				$("#change-cockpit-link").parent().addClass("hide");
				$("#cockpit-link").css("bottom","30px");
			}
			else if(dashboardListGlobal.length == 1 && dashboardListGlobal[0].type == "knowage") {
				$("#change-cockpit-link").parent().addClass("hide");
				renderCockpit(dashboardListGlobal[0].id);
			}
			else if (dashboardListGlobal.length == 1 && dashboardListGlobal[0].type == "grafana") {
				$("#change-cockpit-link").parent().addClass("hide");
				renderGrafana(dashboardListGlobal[0]);
			}
			else {
				cNames.forEach(function(d){
					var dashboard = d;
					$('#cockpitnames').append($('<option>', {
					    value: dashboard.id,
					    text: dashboard.id
				    }));				 					
				});
				$('#cockpitselectmodal').modal('open'); 
			}
			
		},
	    error: function(xhr, status, error) {
	    	alert(error);
	    	console.error(error);
	    	return null;
	     }
	});
}

function renderCockpit(cockpitName) {
	if(cockpitName!=null && cockpitName.trim().length != 0) {
		activeDashboard = dashboardListGlobal.filter(dashboard => dashboard.id == cockpitName);
		//Hide No cockpit linked div
		$('#no-cockpit-linked').addClass("hide");
		//Pre fill cockpit name
		//$('#cockpitnamefield').val(cockpitName);
		//Remove hide class from iframe
		$('#iframedashboard').removeClass("hide");
		//Set iFrame Attribute 
		$('#dashboard_breadcrumb').text(activeDashboard[0].id).attr("href", "");
		//Set iFrame Title 
		$('#cockpitNameTitle').text(activeDashboard[0].id.replace(/_/g,function(match) {return replaceChars[match];}));
		invokeKnowage(cockpitName);		
	} else {
		$('.deviceitem').addClass("hide");
	}
}

function renderGrafana(dashboard) {
	if(dashboard.url!=null && dashboard.url.trim().length != 0) {
		activeDashboard = dashboardListGlobal.filter(dashboard => dashboard.id == dashboard.id);
		//Hide No cockpit linked div
		$('#no-cockpit-linked').addClass("hide");
		//Remove hide class from iframe
		$('#iframedashboard').removeClass("hide");
		//Set iFrame Attribute 
		document.getElementById('iframedashboard').src = dashboard.url;
		//$('#iframedashboard').attr("src", url.split('http')[1]);
		//Set iFrame Title 
		$('#cockpitNameTitle').text(dashboard.id);
		//invokeKnowage(cockpitName);		
	} else {
		$('.deviceitem').addClass("hide");
	}
}


$(document).on('submit','#cockpitnameform',function(){
	var cockpitName = $('#documentname').val();
	if(cockpitName.trim().length > 0 && (dashboardListGlobal.filter(d => d.id === cockpitName)).length <= 0)
		setCockpitName(cockpitName, success_callback);
	/*else {
		$('#cockpitnamefield').css("border-bottom-color", "red");
		$('#cockpitnamefield').css("box-shadow", "0 1px 0 0 red");
		return false;
	}
	*/
});

function addNewDashboard() {
	var type = $("select#chooseType option:checked").val();
	if(type == "knowage") {
		var cockpitName = $('#documentname').val();
		if(cockpitName.trim().length > 0 && (dashboardListGlobal.filter(d => d.id === cockpitName)).length <= 0)
			setCockpitName(cockpitName, "", type, function(data) {location.replace("/index");});
	} else if (type == "grafana") {
		var cockpitName = $('#dashboardname').val();
		var cockpitUrl = $('#dashboardurl').val();
		if(cockpitName.trim().length > 0 && (dashboardListGlobal.filter(d => d.id === cockpitName)).length <= 0)
			setCockpitName(cockpitName, cockpitUrl, type, function(data) {location.replace("/index");});
	}
}


$(document).on('submit','#cockpitselectnameform',function(){
	var cockpitName = $('#cockpitnames').val();
	activeDashboard = dashboardListGlobal.filter(dashboard => dashboard.id == cockpitName);
	if(activeDashboard[0].type == "knowage") {
		renderCockpit(cockpitName);
	} else if (activeDashboard[0].type == "grafana") {
		renderGrafana(activeDashboard[0]);
	}
	
});

$(document).on('click','.modal-close',function(){
	$('#cockpitnamefield').val(activeDashboard[0].id);
	$('#cockpitnamefield').css("border-bottom-color", "#9e9e9e");
	$('#cockpitnamefield').css("box-shadow", "none");
});

$("#chooseType").on('change', function() {
	if($("select#chooseType option:checked").val() == "grafana") {
		$("#knowage_choose").hide();
		$("#grafana_choose").show();
	} else if ($("select#chooseType option:checked").val() == "knowage") {
		$("#grafana_choose").hide();
		$("#knowage_choose").show();
	}
});

function removeDashboard(id, cockpitId, success_callback){
	
	var input = new Object();
		input['action'] = 'removeDashboard';
		input['entityId'] = id;
		input['cockpitName'] = cockpitId;
		
	var headersAll = new Object();
		

		
	$.ajax({
		url: 'ajaxhandler',
		type : 'GET',
		dataType : 'json',
		data : input,
		headers: headersAll,
		success : success_callback,
		error : function(xhr, status, error) {
			alert(error);
			console.error(error);
		}
	});
}

$('#remove-cockpit-link').click(function(){
	if(confirm($('#msg_confirm').text())){
		var id = get('urbanservice');
		var dashboardId = activeDashboard[0].id;
		
		removeDashboard(id,dashboardId, function(data) {
			location.reload();
		})
	}
});



//Add new cockpit
function setCockpitName(cockpitName, cockpitUrl, type, success_callback) {
	var input = new Object();
	input['action'] = 'appendDashboard';
	input['cockpitName'] = cockpitName; 
	input['cockpitUrl'] = cockpitUrl; 
	input['entityId'] = get('urbanservice');
	input['type'] = type;
	
	$.ajax({
		url: 'ajaxhandler',
	    type: 'POST',
	    dataType: 'json',
	    data: input,
		success: success_callback,
	    error: function(xhr, status, error) {
	    	alert(error);
	    	console.error(error);
	    	return null;
	     }
	});
}


function invokeKnowage(cockpitName) {
    Sbi.sdk.services.setBaseUrl({
        protocol: Knowage.protocol
        , host: Knowage.host
        , port: Knowage.port
        , contextPath: 'knowage'
        , controllerPath: 'servlet/AdapterHTTP'
    });    
    
    this.renderCockpitInFrame = function() {
    	var executionRole = scope + role_suffix;
    	console.log('Execution Role : ' + executionRole);
	    var url = Sbi.sdk.api.getDocumentUrl({
			documentLabel: cockpitName
			, executionRole: executionRole
			, displayToolbar: false
			, displaySliders: false
			, height: '1200px'
			, width: '1200px'
			, iframe: {
				style: 'border: 0px;'
			}
		});
	    
	    
	    document.getElementById('iframedashboard').src = url;
	};
	
    //Callback definition for basic auth
    var callback = function(result, args, success) {
      if(success) {
    	  alert(success);
        this.renderCockpitInFrame();
      } else {
        alert("Connection problem!");
      }
    };
    
	switch (Knowage.auth) {
		case "idm":
			renderCockpitInFrame();
		break;
		
		case "basic":
			
		    //Knowage Authentication
				//Already done on getDashboardList
			/*
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
		  	*/
			this.renderCockpitInFrame();
		break;
	}





}


