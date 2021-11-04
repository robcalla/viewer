<%@taglib uri="http://eng.it/ricerca/pa/taglib/i18n" prefix="i18n"%>

<!-- JSTL -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!-- OBJECT FROM BACKEND -->
<c:set var="enabler" value="${sessionScope.enabler}"/>
<c:set var="isMultiEnablerOn" value="${sessionScope.isMultiEnablerOn}"/>
<c:set var = "isAdmin" value = "${sessionScope.userIsAdmin}"/> 
<c:set var = "categoryPerms" value = "${requestScope.categoryPerms}"/> 
<c:set var = "userId" value = "${sessionScope.userInfo.getId()}"/>
<c:set var = "isTenantEnabled" value = "${sessionScope.isTenantEnabled}"/>

<html class="iotmanager">
<head>
	<title><i18n:message value="apptitle"/></title>
	<link rel="shortcut icon" href="favicon.ico?" type="image/x-icon" />
	
	
	<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
	<!-- Compiled and minified CSS -->
	<link rel="stylesheet" href="css/materialize.css">
	
	<link rel="stylesheet" href="css/main.css"/>
	<link rel="stylesheet" href="css/urbanservices.css"/>
	
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
   
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
	<!-- Compiled and minified JavaScript -->
	<script src="js/materialize.min.js"></script>
   
    <script type="text/javascript" src="js/knowageSDK/sbisdk-all-production.js"></script>
	<script type="text/javascript" src="js/conf/init_conf.js"></script>
</head>


<body class="materialize">
	<%@include file="../frames/loader.jspf"%>

	<div id="containerLayout">
		
		<div id="headerLayout">	
			<header>
				<%@include file="../frames/navbar.jspf"%>
			</header>
			
			<input type="hidden" id="enabler" name="enabler" value="${enabler}"> 
			<div class="section">
				<div class="h-40">
					<div class="row  valign-wrapper">
						<div class="col valign">
							<a href="index" class="btn-floating waves-effect waves-light left" id="contex_breadcrumb">
								<i class="material-icons ">arrow_back</i>
							</a>
						</div>
						<div class="col breadcrumb_bar nav-wrapper valign">
					       <a href="index" id="scope_breadcrumb" class="breadcrumb"><i18n:message value="scope"/></a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div id="bodyLayout">
		
			<div class="section container">
				<div class="row hide" id="errorMessage"></div>
				
				<div id="urbanserviceslist">
					<%@include file="../frames/urbanserviceitem.jspf" %>
					<div class="row">
						<div class="col s12 m4 l3 urbanserviceitem" id="newurbanservicetrigger">					
							<c:if test = "${(isAdmin eq true || categoryPerms.getCanCreate() eq true) && isTenantEnabled eq false}">
								<div class="hoverable z-depth-1 block nopadding" >
									<a href="#newsubcatmodal" class="serviceanchor center-align white-text urbanservicename">
										<i class="material-icons">add</i>
										<br/>
										<span><i18n:message value="new_urbanservice"/></span>
									</a>
								</div>
							</c:if>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div id="footerLayout">
			<%@include file="../frames/footer.jspf"%>
		</div>
		
		<%@include file="../frames/urbanservice_newmodal.jspf"%>
		<script>
			var isAdmin = "<c:out value='${isAdmin}'/>";
			var userId_fromSession = "<c:out value='${userId}'/>";
		</script>
	
		<script type="text/javascript" src="js/main.js"></script> 
		<script type="text/javascript" src="js/util.js"></script>
		<script type="text/javascript" src="js/urbanservices.js"></script>
	
		<%@include file="../frames/messages.jspf"%>
	</div>
</body>

</html>
