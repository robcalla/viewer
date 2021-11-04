var contexts = [];
var filtered = [];
var elements = {};
$(document).ready(function(){
	
		contexts = [];
		var enabler = $('#enabler').val();
		var enablerText = capitalize(enabler) + " Enabler";
		$('#scope_breadcrumb').text(enablerText).attr("href", 'home');
		
		mainOnDocReady();	
		$('.modal').modal({dismissible: false});
		
		 //Populate the scopelist through REST API invocation
		try{ 
			var input = new Object();
			input['action'] = 'getContexts';
			input['enabler'] = enabler;
	
			$.ajax({
				url: 'ajaxhandler',
				type : 'GET',
				dataType : 'json',
				data : input,
				success : function(data) {
					$.each(data, function(i, e){					
						var _id = e.id.toLowerCase();
							var queryparams = new Object();
								queryparams['scope'] = _id;
								
							var querystring = $.param(queryparams);
						if(e.name.value){
							var context = {
								id:e.id,
								name:e.name.value,
								urbanServices:e.urbanServices.value,
								queryString:'urbanservices?'+querystring,
								permission:e.permission.value
							};
							contexts.push(context);
							appendItems(e.id,e.name.value,e.urbanServices.value, 'urbanservices?'+querystring, e.permission.value);
						} else {
							var context = {
								id:e.id,
								name:e.name,
								urbanServices:e.urbanServices,
								queryString:'urbanservices?'+querystring,
								permission:e.permission
							};
							contexts.push(context);
							appendItems(e.id,e.name,e.urbanServices, 'urbanservices?'+querystring, e.permission);
						}
					});
					
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
});

function setImage(urlToFile, gradient, item){
	var ext=[".jpg", ".jpeg", ".png"];
	// variable used to check if the for loop should stop once a correct extension is found
	var succ = false;
	
	for (var i = 0; i < ext.length; i++) {
		var file = urlToFile+ext[i];
		

	    $.ajax({
	        url: file,
	        type:'HEAD',
	        async: false,
	        crossDomain : true,
	        xhrFields: {
	            withCredentials: false
	        },
	        success: function () {
	        	item.find(".catanchor").css('background', 'url('+file+') no-repeat center').css('background-size','cover');
	        	succ = true;
	        }
	    })
	}
	
	if (succ) return;
}

function appendItems(id,name,urbanServices, url, permission){

	var template = $('#catlist').find('.catitem.template');
	var item = template.clone().attr("id", id);
	//capitalize string
	//value = value.charAt(0).toUpperCase() + value.slice(1);
	item.removeClass('template hide');
	
	var bkgColor = "";
	
	if($(".catanchor.primary").length){
		bkgColor = $(".catanchor.primary").css("background-color").replace(')',', 0.5)');
		item.find(".catanchor").removeClass('primary');
	}else{
		bkgColor = $(".catanchor.secondary").css("background-color").replace(')',', 0.5)');
		item.find(".catanchor").removeClass('secondary');
	}
	var urbanServicesList = item.find(".previewDev");
	item.find(".catname").text(name);
	
	for(var urbanService in urbanServices) {
		var dashboardCount = urbanServices[urbanService].length;
		urbanServicesList.append('<li class="countDev '+uslistcolor[urbanService.toLowerCase()]+' ">'+dashboardCount+'</li>');
	}
	
	item.find(".catanchor").css('background', "linear-gradient("+bkgColor+","+bkgColor+"), url('imgs/cat/defaultCity.svg')" +' no-repeat center').css('background-size','cover');
	setImage(App.context.images.path+id, bkgColor, item);
	
	item.find(".catanchor").attr("href", url);
	
	if(permission!== undefined) {
		if(permission.canDelete === true)
			item.find(".delcat").removeClass('hide');
		if(permission.canUpdate === true)
			item.find(".editcat").removeClass('hide');
	} 
	
	var appender = $('#catlist .row');
	
	appender.append(item);
	elements[id] = $('#' + id);
}

function registerScope(service, servicepath, formData, isUploadRequired, success_callback, error_callback) {
	
	var headersAll = new Object();
		headersAll['fiware-service'] = service;
		headersAll['fiware-servicepath'] = servicepath;
		
		
	var input = new Object();
		input["action"] = 'createContext';
		input["context"] = service;

	formData.append("action","createContext");
	
	var cb = function(data) {
		if(data == "OK") {
			$.ajax({
				url: 'ajaxhandler',
			    type: 'POST',
			    dataType: 'json',
			    data: input,
				headers: headersAll,
				success: success_callback,
			    error: error_callback
			});
		} else {
			alert("Error while uploading the image");
			$('.modal#newcatmodal').modal('close');
			form.find('#categorynamefield').val("");
			$("#local_input").val("");
			$(".file-path").val("");
			$("#remote_input").val("");
			form.find('.modal-footer button').prop('disabled', false);
		}		
	}
	if(isUploadRequired === true) {
		$.ajax({
			  url: 'uploadhandler',
			  type: 'POST',
			  headers: headersAll,
			  data: formData,
		      enctype: 'multipart/form-data',
			  processData: false,
			  contentType: false,
			  async: false,
			  success: cb,
			  error: error_callback
		});
	} else {
		cb("OK");
	}
	
}

function editImage(formData, success_callback, error_callback) {
	$.ajax({
		  url: 'uploadhandler',
		  type: 'POST',
		  data: formData,
	      enctype: 'multipart/form-data',
		  processData: false,
		  contentType: false,
		  async: false,
		  success: success_callback,
		  error: error_callback
	});
}


function deleteContext(serv, success_callback){

	var input = new Object();
		input['action'] = 'deleteContext';
		input['context'] = serv;
		
	var headersAll = new Object();
		headersAll['fiware-service'] = serv;

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

//checks to see if the image is of correct size
function checkImageSize() {
	var input = document.getElementById('local_input');
	if (input.files[0] && input.files[0].size > 7000000) {
		document.getElementById('upload_error').innerHTML = 'The image cant be larger them 7MB';
		document.getElementById('upload_error').style.color = '#F00';
		document.getElementById('local_input').value = '';
		document.getElementById('local_input_file_path').value = '';
		return false;
	} else {
		document.getElementById('upload_error').innerHTML = '';
		return true;
	}
}

//Submit on New city modal
$(document).on('submit', '#newcatform', function() {
	
	
	
	var form = $(this);
	var exists = false;
	var isUploadRequired = false;
	formData = new FormData();
	var newcatname = form.find('#categorynamefield').val();
	var cleanCatName = newcatname.replace(/\s\s+/g, ' ').trim();//.replace(new RegExp(" ", 'g'), "_");
	
	formData.append("scope",cleanCatName);
	//cleanCatName = String(cleanCatName).toLowerCase();
	
	//Check if the scope already exists
	$('.catitem').each(function(cat) {
		var escapedCatName = cleanCatName.toLowerCase();
		if($(this).find(".catname").text().toLowerCase() == escapedCatName) {
			exists = true;
			alert("The city already exists");
			return false;
		}		
	});	
	if(exists) return false;
	
	
	if($('#local_input').val() != '' || $('#remote_input').val() != '') {
		isUploadRequired = true;
		//Pre-process image upload
		var isRemoteUpload = form.find('#scopeimagesourcetypeswitch').prop("checked");
		
		formData.append("isRemoteUpload", isRemoteUpload);
		
		if(isRemoteUpload === true) {
			//Send an HEAD Request to the remote path to check if it contains a valid image
			var remoteUrl = form.find("#remote_input").val();
			var reqHEAD = '';
			$.ajax({
				url: remoteUrl,
				type : 'HEAD',
				async: false
			}).done(function(message,text,jqXHR) {
				reqHEAD = jqXHR.getResponseHeader('Content-Type');
			});
			if(!reqHEAD.match('^image')) {
				alert("The url must point to an image");
				return false;
			} else {
				var ext = reqHEAD.split('/');
				ext = ext[ext.length-1].toLowerCase();
				formData.append("remoteUrl",remoteUrl);
				formData.append("ext",ext);
			}
		} else {		
			var localPath = form.find("#local_input").val();
			
			if (!checkImageSize()) return false;
			
			var ext = localPath.split('.');
			ext = ext[ext.length-1].toLowerCase();
			if(!(ext === 'jpg' || ext === 'jpeg' || ext === 'png')) {
				document.getElementById('upload_error').innerHTML = 'The format of the image must be either .jpg, .jpeg, .png';
				document.getElementById('upload_error').style.color = '#F00';
				document.getElementById('local_input').value = '';
				document.getElementById('local_input_file_path').value = '';
				return false;
			} else {
				var file = $('#local_input').get(0).files[0];
				formData.append('ext',ext);
				formData.append('file', file);
			}
		}
	}

	
	form.find('.modal-footer input').prop('disabled', true);
	registerScope(cleanCatName, "", formData,isUploadRequired, function(data) {
			
			var queryparams = new Object();
			var catId = cleanCatName.replace(new RegExp(" ", 'g'), "_").toLowerCase();
			queryparams['enabler'] = $('#enabler').val();//get('enabler');
		    queryparams['scope'] = catId; 
		   
		    var querystring = $.param(queryparams);
			
		    var context = {
				id:catId,
				name:cleanCatName,
				urbanServices:[],
				queryString:'urbanservices?'+querystring,
			};
			contexts.push(context);
			
			var permission = {
				canCreate:true,
				canDelete:true,
				canRead:true,
				canUpdate:true,
				type:"CONTEXT"
			}
		    
			appendItems(catId,cleanCatName,[], 'urbanservices?'+querystring, permission);
			
			contexts.forEach(e => {
				$('#' + e.id).detach();
			});
			contexts.sort((a,b) => {
				if (a.id < b.id) {
			        return -1;
			    }
			    if (a.id > b.id) {
			        return 1;
			    }
			});
			contexts.forEach(e => {
				$('#catlist .row').append(elements[e.id]);
			});
			
			$('.modal#newcatmodal').modal('close');
			form.find('#categorynamefield').val("");
			form.find('.modal-footer button').prop('disabled', false);
		}, 
		function(xhr, status, error){
//			if(xhr.responseJSON.message){
//				error = xhr.responseJSON.message;
//			}
	    	console.error(error);
			form.find('.modal-footer button').prop('disabled', false);
			alert(error);
		});
	
	return false;
	
});

$(document).on('click', '.delcat', function(){
	if(confirm($('#msg_confirm_delete_context').text())){
		var self = $(this);
		var itemId = self.closest('.catitem').find('.catanchor').attr('href').split('scope=')[1].split('&')[0];
		var input = new Object();
		input['action'] = 'deleteCategoriesByContextName';
		input['context'] = itemId;
		
		for (var i = 0; i < contexts.length; i++) {
			var elem = contexts[i];
			if(elem.id == itemId.toLowerCase()) {
				contexts.splice(i,1);
				delete elements[elem.id];
			}
		};
		
		//Remove all the dashboard associated with the scope
		$.ajax({
			url: 'ajaxhandler',
			type : 'GET',
			dataType : 'json',
			data : input,
			success : function(data) {
				deleteContext(itemId,function(){
					self.closest('.catitem').remove();
				});
				alert($('#scope').text().trim() + " " + itemId.charAt(0).toUpperCase() + itemId.slice(1) + " " + $('#msg_success_delete').text());
				
			},
			error : function(xhr, status, error) {
				alert(error);
				console.log(error);
			}
		});
	}
});

$(document).on('click', '.editcat', function(){
	var self = $(this);
	var catname = self.closest('.catitem').find('.catanchor').attr('href').split('scope=')[1].split('&')[0];
	$('#editcategorynamefield').val(catname);
});

$(document).on('submit', '#editcatform', function(e){
	var isDefault = $('#editscopeimagesourcetypeswitch').is(":checked");
	var form = $(this);
	formData = new FormData();
	var newcatname = $('#editcategorynamefield').val();
	var cleanCatName = newcatname.replace(/\s\s+/g, ' ').trim();
	formData.append("scope",cleanCatName);
	formData.append("default", isDefault);
	
	if($('#edit_local_input').val() != '' || $('#edit_remote_input').val() != '') {
		//Pre-process image upload
		var isRemoteUpload = form.find('#scopeimagesourcetypeswitch').prop("checked");
		
		formData.append("isRemoteUpload", isRemoteUpload);
		
		if(isRemoteUpload === true) {
			//Send an HEAD Request to the remote path to check if it contains a valid image
			var remoteUrl = form.find("#remote_input").val();
			var reqHEAD = '';
			$.ajax({
				url: remoteUrl,
				type : 'HEAD',
				async: false
			}).done(function(message,text,jqXHR) {
				reqHEAD = jqXHR.getResponseHeader('Content-Type');
			});
			if(!reqHEAD.match('^image')) {
				alert("The url must point to an image");
				return false;
			} else {
				var ext = reqHEAD.split('/');
				ext = ext[ext.length-1].toLowerCase();
				formData.append("remoteUrl",remoteUrl);
				formData.append("ext",ext);
			}
		} else {		
			var localPath = form.find("#edit_local_input").val();
			
			if (!checkImageSize()) return false;
			
			var ext = localPath.split('.');
			ext = ext[ext.length-1].toLowerCase();
			if(!(ext === 'jpg' || ext === 'jpeg' || ext === 'png')) {
				document.getElementById('upload_error').innerHTML = 'The format of the image must be either .jpg, .jpeg, .png';
				document.getElementById('upload_error').style.color = '#F00';
				document.getElementById('local_input').value = '';
				document.getElementById('local_input_file_path').value = '';
				return false;
			} else {
				var file = $('#edit_local_input').get(0).files[0];
				formData.append('ext',ext);
				formData.append('file', file);
			}
		}
	}

	editImage(formData, function(data) {
		var queryparams = new Object();
		var catId = cleanCatName.replace(new RegExp(" ", 'g'), "_").toLowerCase();
		queryparams['enabler'] = $('#enabler').val();
	    queryparams['scope'] = catId; 
	   
	    var querystring = $.param(queryparams);
		
	    var permission = {
			canCreate:true,
			canDelete:true,
			canRead:true,
			canUpdate:true,
			type:"CONTEXT"
		}
	    
		appendItems(catId,cleanCatName,[], 'urbanservices?'+querystring, permission);
		
		$('.modal#editcatmodal').modal('close');
		form.find('.modal-footer button').prop('disabled', false);
	}, 
	function(xhr, status, error){
		alert("An error occurred while updating image");
    	console.error(error);
		form.find('.modal-footer button').prop('disabled', false);
	});	
});

$(document).on('click', '#scopeimagesourcetypeswitch',function() {
	var isRemoteUpload = $(this).prop('checked');
	if(isRemoteUpload === true) {
		$("#local_input").val("");
		$(".file-path").val("");
		$("#localimage").addClass("hide");
		$("#remoteimage").removeClass("hide");
	} else {
		$("#remote_input").val("");
		$("#remoteimage").addClass("hide");
		$("#localimage").removeClass("hide");
	}
});

document.getElementById('cancle_upload').addEventListener('click', () => {
	document.getElementById('upload_error').innerHTML = '';
});

$("#searchField").on("keyup", function() {
	var value = $(this).val().toLowerCase();
	contexts.forEach(e => {
		$('#' + e.id).detach();
	});
	filtered = contexts.filter(context => context.id.includes(value));
	filtered.forEach(e => {
		$('#catlist .row').append(elements[e.id]);
	});
});

function sortContexts() {
	if($('#sorter').text() == "keyboard_arrow_down") {
		if($("#searchField").val() != "" && $("#searchField").val() != undefined) {
			filtered.reverse();
		} else {
			contexts.reverse();
		}
		$('#sorter').text("keyboard_arrow_up");
	} else if ($('#sorter').text() == "keyboard_arrow_up") {
		if($("#searchField").val() != "" && $("#searchField").val() != undefined) {
			filtered.reverse();
		} else {
			contexts.reverse();
		}
		$('#sorter').text("keyboard_arrow_down");
	}
	contexts.forEach(e => {
		$('#' + e.id).detach();
	});
	if($("#searchField").val() != "" && $("#searchField").val() != undefined) {
		filtered.forEach(e => {
			$('#catlist .row').append(elements[e.id]);
		});
	} else {
		contexts.forEach(e => {
			$('#catlist .row').append(elements[e.id]);
		});
	}	
}




















