<%@taglib uri="http://eng.it/ricerca/pa/taglib/i18n" prefix="i18n"%>

<!-- JSTL -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!-- OBJECT FROM BACKEND -->
<c:set var = "isAdmin" value = "${sessionScope.userIsAdmin}"/>
<c:set var = "userId" value = "${sessionScope.userInfo.getId()}"/>

<%
	boolean isAdmin = (Boolean) request.getSession().getAttribute("userIsAdmin");
	boolean isCityEnabled = (Boolean) request.getSession().getAttribute("cityEnabled");
	boolean isFacilityEnabled = (Boolean) request.getSession().getAttribute("facilityEnabled");
	boolean isFarmEnabled = (Boolean) request.getSession().getAttribute("farmEnabled");
%>

<html class="iotmanager">
	<head>
		<title><i18n:message value="apptitle"/></title>
		<link rel="shortcut icon" href="favicon.ico?" type="image/x-icon" />
		<!-- Compiled and minified CSS -->
		<link rel="stylesheet"
			href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" />
		<link rel="stylesheet" href="css/materialize.css">
		<link rel="stylesheet"
			href="https://fonts.googleapis.com/icon?family=Material+Icons">
		
		<script
			src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
		<!-- Compiled and minified JavaScript -->
		<script src="js/materialize.min.js"></script>
		<script type="text/javascript" src="js/conf/init_conf.js"></script>
		
		<link rel="stylesheet" href="css/main.css" />
		<link rel="stylesheet" href="css/home.css" />
		
		<script type="text/javascript" src="js/home.js"></script>
		
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		
		<link rel="stylesheet" href="https://unpkg.com/leaflet@1.3.4/dist/leaflet.css"
				    integrity="sha512-puBpdR0798OZvTTbP4A8Ix/l+A4dHDD0DGqYW6RQ+9jxkRFclaxxQb/SJAWZfWAkuyeQUytO7+7N4QKrDh+drA=="
				    crossorigin=""/>    

		<script src="https://unpkg.com/leaflet@1.3.4/dist/leaflet.js"
					integrity="sha512-nMMmRyTVoLYqjP9hrbed9S+FzjZHW5gY1TWCHA5ckwXZBadntCNs8kEqAWdrb9O7rxbCaA4lKTIWjDXZxflOcA=="
					crossorigin=""></script>
					
		<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.5.0/css/all.css" integrity="sha384-B4dIYHKNBt8Bc12p+WXckhzcICo0wtJAoU8YZTY5qE0Id1GSseTk6S+L3BlXeVIU" crossorigin="anonymous">
			
		<link rel="stylesheet" href="css/leaflet.extra-markers.min.css">
		<script src="js/leaflet.extra-markers.min.js"></script>
		<script type="text/javascript">
			console.log("CFE v" + '<%=request.getAttribute("version").toString()%>');
		</script>
		
	</head>
	
<body class="materialize">
	
	<div id="containerLayout">
		
		<div id="headerLayout">	
			<header>
				<%@include file="../frames/navbar.jspf"%>
			</header>
		</div>
		<div id="bodyLayout">
	
			<div class="container <c:if test="${isAdmin == false}">citizen-container</c:if>">
				<div class="row hide" id="errorMessage"></div>
				
				<div class="row">
	
					<div class="<c:if test="${isAdmin == true}">col s12 m6</c:if><c:if test="${isAdmin == false}">row</c:if>">
						<div id="catlist" class="row">
							<% if (isCityEnabled == true || isAdmin == true){%> 
							<div class="<c:if test="${isAdmin == true}">col s12 m9 push-m1 catitem enabler</c:if><c:if test="${isAdmin == false}">col s4 m4 catitem enabler</c:if>" id="city" >
								<div class="hoverable z-depth-1 block white nopadding <c:if test="${isAdmin == false}">badge-large</c:if>">
									<a href="index?enabler=city" class="catanchor secondary-text">
										<span class="labelholder"><i18n:message value="enabler" /></span>
										<span class="catname left"><i class="white-text fas fa-city fa-xs ">&nbsp;</i><i18n:message value="badge_city"/></span>
									</a>
								</div>
							</div>
							<% } %>
							<% if (isFacilityEnabled == true || isAdmin == true){%>
							<div class="<c:if test="${isAdmin == true}">col s12 m9 push-m1 catitem enabler</c:if><c:if test="${isAdmin == false}">col s4 m4 catitem enabler</c:if>" id="facility">
								<div class="hoverable z-depth-1 block white nopadding <c:if test="${isAdmin == false}">badge-large</c:if>">
									<a href="index?enabler=facility" class="catanchor secondary-text">
										<span class="labelholder"><i18n:message value="enabler" /></span>
										<span class="catname"><i class="white-text fas fa-industry fa-xs ">&nbsp;</i><i18n:message value="badge_industry"/></span>
									</a>
								</div>
							</div>
							<% } %>
							<% if (isFarmEnabled == true || isAdmin == true){%>
							<div class="<c:if test="${isAdmin == true}">col s12 m9 push-m1 catitem enabler</c:if><c:if test="${isAdmin == false}">col s4 m4 catitem enabler large</c:if>" id="farm">
								<div class="hoverable z-depth-1 block white nopadding <c:if test="${isAdmin == false}">badge-large</c:if>">
									<a href="index?enabler=farm" class="catanchor secondary-text">
										<span class="labelholder"><i18n:message value="enabler" /></span>
										<span class="catname"><i class="white-text fas fa-tractor fa-xs ">&nbsp;</i><i18n:message value="badge_agriculture"/></span>
									</a>
								</div>
							</div>
							<% } %>
						</div>
					</div>
					<div class="col s12 m6" style="display:${isAdmin ? 'block' : 'none'}">
						<div id="map-container">
							<h5 id="mapTitle"><i18n:message value="map-title" /></h5>
							<div id="context-map" ></div>
						</div>
					</div>
				</div>
				
			</div>
			<%@include file="../frames/messages.jspf"%>
		</div>
		<div id="footerLayout">
			<%@include file="../frames/footer.jspf"%>
		</div>
	</div>
	<script>
		var isAdmin = "<c:out value='${isAdmin}'/>";
		var userId = "<c:out value='${userId}'/>";
	</script>
	
</body>

</html>
