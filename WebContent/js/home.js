$(document).ready(function(){	
	
	var mapTitleH = $('#mapTitle').outerHeight(true);
	var h = window.innerHeight;
	var mapOffset = $("#map-container").offset();
	var hColums = h - mapOffset.top - mapTitleH - $("footer").outerHeight(true);
	var isUserAdmin = isAdmin;
	
	$("#context-map").css("height", hColums-mapTitleH+"px");
//	$("#map-container").css("max-height", hColums-$("footer").outerHeight()+"px");
	$("#catlist").css("margin", "10px 0px 0px 0px");
	if(isUserAdmin != "false") {
		$(".catitem .block").css("max-height", (hColums/3)-10+"px");
	}
	
	var map = L.map('context-map').setView([51.505, -0.09], 1);
	L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
		zoomOut: 1,
	    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
	}).addTo(map);
	
	  var redMarker = L.ExtraMarkers.icon({
		    icon: 'fa-industry',
		    markerColor: 'red',
		    shape: 'square',
		    prefix: 'fas'
		  });
	  
	  var blueMarker = L.ExtraMarkers.icon({
		    icon: 'fa-city',
		    markerColor: 'blue',
		    shape: 'square',
		    prefix: 'fas'
		  });
	  
	  var greenMarker = L.ExtraMarkers.icon({
		    icon: 'fa-tractor',
		    markerColor: 'green',
		    shape: 'square',
		    prefix: 'fas'
		  });
	
	//Populate the map through REST API invocation
	var input = new Object();
	input['action'] = 'getContexts';
	$.ajax({
		url: 'ajaxhandler',
		type : 'GET',
		dataType : 'json',
		data : input,
		success : function(data) {
//			console.log(data);
			var markerArray = [];
			$.each(data, function(i, e){
				var lat,lng,context,enabler;
				try  {
					if(e.mapcenter){
						lat = e.mapcenter.value.lat
						lng = e.mapcenter.value.lng;
						enabler = e.refScope.value;
						context = e.name.value;
					} else {
						lat = e.latitude
						lng = e.longitude;
						enabler  = e.refEnabler;
						context  =  e.name;
					}
					if((lat != 0 && lat != undefined) && (lng != 0 && lat != undefined)) {
						var marker  = [lat,lng];
						markerArray.push(marker);
						switch(enabler) {
							case 'city':
								L.marker(marker,{icon: blueMarker}).bindPopup(context).addTo(map);
							break;
							
							case 'facility':
								L.marker(marker,{icon: redMarker}).bindPopup(context).addTo(map);
							break;
							
							case 'farm':
								L.marker(marker,{icon: greenMarker}).bindPopup(context).addTo(map);
							break;
							
							default:
								L.marker(marker).bindPopup(context).addTo(map);
							break;
						}
						
					}
				} catch(e){}

			});
			if(markerArray.length != 0) {
				map.fitBounds(markerArray);
			}
		},
		error : function(xhr, status, error) {
			console.log(error);
		}
	});
});

