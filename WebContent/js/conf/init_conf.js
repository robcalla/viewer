var ConfResponse = {};
$.ajax({
	url: 'LoadConfigs',
    type: 'GET',
    async: false,
    success: function(result) {
    	if(result != null)
    		ConfResponse = result;
    }
});

var Knowage = {};
var Grafana = {};
var data4enablers = {};
var Superset = {};
var App = {};
try {

	Knowage.enabled = ConfResponse["frontend.Knowage.enabled"];
	Knowage.protocol = ConfResponse["frontend.Knowage.protocol"];
	Knowage.host = ConfResponse["frontend.Knowage.host"];
	Knowage.port = ConfResponse["frontend.Knowage.port"];
	Knowage.auth = ConfResponse["frontend.Knowage.auth"];
	Knowage.username = ConfResponse["frontend.Knowage.username"];
	Knowage.password = ConfResponse["frontend.Knowage.password"];
	Knowage.defaultFunctionality = ConfResponse["frontend.Knowage.defaultFunctionality"];
	Knowage.defaultExecutionRole = ConfResponse["frontend.Knowage.defaultExecutionRole"];
	Knowage.publicFunctionality = ConfResponse["frontend.Knowage.publicFunctionality"];
	
	Grafana.enabled = ConfResponse["frontend.Grafana.enabled"];
	Grafana.protocol = ConfResponse["frontend.Grafana.protocol"];
	Grafana.host = ConfResponse["frontend.Grafana.host"];
	Grafana.port = ConfResponse["frontend.Grafana.port"];
	
	Superset.enabled = ConfResponse["frontend.Superset.enabled"];
	Superset.protocol = ConfResponse["frontend.Superset.protocol"];
	Superset.host = ConfResponse["frontend.Superset.host"];
	Superset.port = ConfResponse["frontend.Superset.port"];

	data4enablers.city = {};
	data4enablers.city.cats = ConfResponse["frontend.data4enablers.city.cats"].split("|");
	data4enablers.facility = {};
	data4enablers.facility.cats = ConfResponse["frontend.data4enablers.facility.cats"].split("|");
	data4enablers.farm = {};
	data4enablers.farm.cats = ConfResponse["frontend.data4enablers.farm.cats"].split("|");

	App.context = {};
	App.context.images = {};
	App.context.images.path = ConfResponse["frontend.context.images.path"];
	
} catch(e) {
	console.error("Error while loading configurations");
}

//Keep the LowerCase !
var uslistcolor = {parking:"blue", waste:"blue-grey", mobility:"purple", illumination:"amber",
				   environment:"light-green", tourism:"orange", water:"cyan", kpi:"pink",
				   issues:"red", taxes:"brown", controlroom:"teal", manufacturing:"lime",
				   energy:"yellow", industry:"deep-purple", building:"grey",
				   animals:"deep-orange", harvest:"light-blue", finance:"indigo"
				   };

/* Help links*/
var helpLink = "https://github.com/telefonicaid/iotagent-node-lib/blob/2.7.0/README.md"

var emptyCockpitName = "__empty__";
