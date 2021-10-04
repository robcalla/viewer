//$(document).ready(function(){
//		
//	onDocReady();
//	
//});

window.onload = function(e){ 
	onDocReady();
};

var checkedall = false;
var deviceperpage = 6;
var currpage = 1;
var urbanserviceslist = new Object();
var usUsed = new Object();
var serviceUsed = new Object();
var uslist = [];
function onDocReady(){
	//$('#knowage-loader').on('load', function() {
		mainOnDocReady();
		
		$('.modal').modal({dismissible:false});
		var scope = get('scope');
		var enabler = $('#enabler').val();;
		uslist = data4enablers[enabler].cats.map(c => c.toLowerCase());
		// Get the refScope name
		getServiceName(scope);
		
		$('#contex_breadcrumb').attr("href", 'index');
		
		
		//Populate the scopelist through REST API invocation
		try{ 
			var input = new Object();
			input['action'] = 'getCategories';
			input['context'] = scope; 
	
			$.ajax({
				url: 'ajaxhandler',
				type : 'GET',
				dataType : 'json',
				data : input,
				success : function(data) {
					$.each(data, function(i, e){
						
						if(e.name.value) {
							if(uslist.indexOf(e.name.value.toLowerCase())>-1) {
								var queryparams = new Object();
									//queryparams['enabler'] = enabler;
									queryparams['scope'] = scope;
									queryparams['urbanservice'] = e.id;
									queryparams['dashboardid'] = e.dashboardid.value;
									
								var querystring = $.param(queryparams);
								
								appendItems(e, 'dashboard?'+querystring);
								usUsed[e] = true;
								serviceUsed[e.name.value] = true;
							}
						} else {
							if(uslist.indexOf(e.name.toLowerCase())>-1) {
								var queryparams = new Object();
									queryparams['scope'] = scope;
									queryparams['urbanservice'] = e.id;
									queryparams['dashboardid'] = e.dashboardid;
									
								var querystring = $.param(queryparams);
								
								appendItems(e, 'dashboard?'+querystring);
								usUsed[e] = true;
								serviceUsed[e.name] = true;
							}
						}		
						
					});
					
					$('.delsubcat').removeClass("hide");
					$.each(uslist, function(i, e){
						if(typeof usUsed[e] === 'undefined' || !usUsed[e]){
							$('#newurbanservicetrigger').removeClass('hide');
						}
					});
					
					//appendSelectOption(usUsed);
					appendSelectOption(serviceUsed);
					
					var appendDashboard = function appendDashboard(data) {
						$('#documentnamefield').append($('<option>', {
						    value: emptyCockpitName,
						    text: "(Empty)"
					    }));
						data.forEach(function(d){
							var dashboard = d.name;		
							 $('#documentnamefield').append($('<option>', {
							    value: dashboard,
							    text: dashboard
						    }));
						});
					};
					
					getDashboardList(Knowage.username,Knowage.password,appendDashboard,[]);
					
					$('#pageloader').fadeOut();
				},
				error : function(xhr, status, error) {
					console.log(error);
				}
			});
		}
		catch(error){
			console.log(error);
		}
	
//	});
};

function appendItems(value, url){
	
	var name = value.name.value ? value.name.value : value.name;

	if(typeof value.id === 'undefined' || value.id == null || value.id == "" || value.id == "/"){
		alert("ERROR VALUE: "+value);
	    return;
	}
		var template = $('#urbanserviceslist').find('.urbanserviceitem.template');
		var item = template.clone();
		item.removeClass('template hide');
		
		item.addClass(name);
		
		item.find('.block').css("background-image","url('imgs/icons/"+name.toLowerCase()+".svg')");
		item.find('.block').addClass(uslistcolor[name.toLowerCase()]);
			
		var replaceChars={"_":" " };
		var servicename = name;
		item.find(".urbanservicename").text(servicename.replace(/_/g,function(match) {return replaceChars[match];}));
		item.find(".serviceanchor").attr("href", url);
		item.append('<input type="hidden" name="us_id" value=' + value.id + ' />');
		
		var appender = $('#urbanserviceslist .row #newurbanservicetrigger');
		
		appender.before(item);

}

function appendNewItems(value, url){
	
	if(typeof value === 'undefined' || value == null || value == "" || value == "/"){
		alert("ERROR VALUE: "+value);
	    return;
	}
		var template = $('#urbanserviceslist').find('.urbanserviceitem.template');
		var item = template.clone();
		item.removeClass('template hide');
		
		var cleanpath = value.replace(new RegExp(" ", 'g'), "_")
		item.addClass(cleanpath);
		
		cleanpath=cleanpath.toLowerCase();
		var id = String(get('scope')) + "_" + String(cleanpath);
		
		
		item.find('.block').css("background-image","url('imgs/icons/"+String(cleanpath).toLowerCase()+".svg')");
		item.find('.block').addClass(uslistcolor[value.toLowerCase()]);
		
		var replaceChars={"_":" " };
		item.find(".urbanservicename").text(value.replace(/_/g,function(match) {return replaceChars[match];}));
		item.find(".serviceanchor").attr("href", url);
		item.append('<input type="hidden" name="us_id" value=' + id + ' />');
		
		var appender = $('#urbanserviceslist .row #newurbanservicetrigger');
		
		appender.before(item);

}

function appendSelectOption(usUsed){
	$('#subcategorynamefield').material_select('destroy');
	
	var replaceChars={"_":" " };
	
	uslist.forEach(function(e){
		e=e.replace(" ","_");
		//e=e.replace(/_/g,function(match) {return replaceChars[match];})
		var us = usUsed[e];
		if(typeof us === "undefined" || us == null || !us){
			 e=e.replace("_"," ");
			 $('#subcategorynamefield').append($('<option>', {
			    value: e,
			    text: e
			 }));
		}
	});
	
	$('#subcategorynamefield').material_select();
	
	$("#subcategorynamefield").val($("#subcategorynamefield option:first").val());
}

function registerUrbanService(scope, service,categoryName, cockpitName, success_callback) {
	var headersAll = new Object();
		headersAll['fiware-service'] = scope;
		headersAll['fiware-servicepath'] = categoryName;
					
	var input = new Object();
		input["action"] = 'createCategory';
		if(cockpitName.trim().length > 0) {
			input["dashboardName"] = cockpitName;
		}
		
	$.ajax({
		url: 'ajaxhandler',
	    type: 'POST',
	    dataType: 'json',
	    data: input,
		headers: headersAll,
		success: success_callback,
	    error: function(xhr, status, error) {
	    	alert(error);
	    	console.error(error);
	     }
	});
}

function deleteDashboard(serv, path, id, success_callback){
	
	var input = new Object();
		input['action'] = 'deleteCategory';
		input['context'] = serv;
		input['category'] = path.toLowerCase();
		
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

$(document).on('click', '#newurbanservicebutton', function(){
	var selectedOption = $('#subcategorynamefield').find('option:selected');
	$('#selectedurbanservice').val(selectedOption.val());
	selectedOption.remove();
	$("#subcategorynamefield").val($("#subcategorynamefield option:first").val());
});

$(document).on('submit', '#newsubcatform', function(){
	
	var path = $(this).find('#selectedurbanservice').val();
	$(this).find('#selectedurbanservice').val("");
	var categoryName = path.replace(/\s\s+/g, ' ').trim();
	var cleanpath = categoryName.toLowerCase().replace(new RegExp(" ", 'g'), "_");
	var cockpitName = $(this).find('#documentname').val();
	
	if(cockpitName!=null && cockpitName!==undefined && cockpitName!=emptyCockpitName)
		cockpitName=cockpitName.replace(/\s\s+/g, ' ').trim().replace(new RegExp(" ", 'g'), "_");
	else
		cockpitName="";
	
	
	var serv = get('scope');
	var queryparams = new Object();
		queryparams['scope'] = serv;
		queryparams['urbanservice'] = serv + "_"+ cleanpath;
		queryparams['dashboardid'] = cockpitName;
		
	var querystring = $.param(queryparams);
	
	registerUrbanService(serv, cleanpath,categoryName, cockpitName, function() {
		appendNewItems(categoryName, "dashboard?"+querystring);
		
		usUsed[cleanpath]  = true;
		var full = true;
		$.each(uslist, function(i, e){
			if(typeof usUsed[e] === 'undefined' || !usUsed[e]){
				full = false;
			}
		});
		if(full){
			$('#newurbanservicetrigger').addClass('hide');
		}
	});

	
	$('.modal#newsubcatmodal').modal('close');
	//$(this).find('#cockpitname').val("");
	return false;
});

$(document).on('click', '.delsubcat', function(){
	if(confirm($('#msg_confirm').text())){
		var self = $(this);
		var serv = get('scope'); 
		var path = self.closest('.urbanserviceitem ').find('.urbanservicename').text();
		
		var replaceChars={" ":"_" };
		path= path.replace(/ /g,function(match) {return replaceChars[match];});
		
		var id = $("input[name=us_id]").val();
		
		deleteDashboard(serv, path, id, function(data){	
			self.closest('.urbanserviceitem ').remove();
			self.closest('.catitem').remove();
			delete usUsed[path];
			$('#newurbanservicetrigger').removeClass('hide');
			alert($('#urbanservice').text().trim() + " " + id.split("_")[1].charAt(0).toUpperCase() + id.split("_")[1].slice(1) + " " + $('#msg_success_delete').text());
		});
		
		path = path.replace("_"," ");
		$('#subcategorynamefield').append($('<option>', {
		    value: path,
		    text: path
		 }));
			
	};
});


