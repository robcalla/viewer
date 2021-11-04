var grafanaDashboards = [];
var grafanaUrl = "";
var supersetDashboards = [];
var supersetUrl = "";

$(document).ready(function(){
	
	if(Knowage.enabled === 'true') {
		loadKnowage();
	}	
	if(Grafana.enabled === 'true') {
		grafanaUrl = Grafana.protocol + "://"+ Grafana.host+ ":"+ Grafana.port
		loadGrafana();
	}
	if(Superset.enabled === 'true') {
		supersetUrl = Superset.protocol + "://"+ Superset.host+ ":"+ Superset.port
		loadSuperset();
	}
	
});

function loadKnowage(){
	
	var knowageUrl = Knowage.protocol + "://"+ Knowage.host+ ":"+ Knowage.port+ "/knowage";	
	$('#knowage-loader').attr("src",knowageUrl);
	
}

function loadGrafana(){
	
	$.ajax({
		url: "/grafana",
	    type: 'GET',
	    async: false,
	    success: function(result) {
	    	if(result != null) {
	    		grafanaDashboards = JSON.parse(result);
				grafanaDashboards.forEach(function(d){
					var dashboard = d.title;		
					 $('#grafananamefield').append($('<option>', {
					    value: dashboard,
					    text: dashboard
				    }));
					
				});
			}
	    }
	});
	
}

function loadSuperset(){
	
	$.ajax({
		url: "/superset",
	    type: 'GET',
	    async: false,
	    success: function(result) {
	    	if(result != null) {
				supersetDashboards = JSON.parse(result).result;
				supersetDashboards.forEach(function(d){
					var dashboard = d.dashboard_title;		
					 $('#supersetnamefield').append($('<option>', {
					    value: dashboard,
					    text: dashboard
				    }));
					
				});
			}
	    }
	});
	
}