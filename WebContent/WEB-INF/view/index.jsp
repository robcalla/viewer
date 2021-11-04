<%@taglib uri="http://eng.it/ricerca/pa/taglib/i18n" prefix="i18n"%>

<!-- JSTL -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!-- OBJECT FROM BACKEND -->
<c:set var="enabler" value="${sessionScope.enabler}"/>
<c:set var="isMultiEnablerOn" value="${sessionScope.isMultiEnablerOn}"/>
<c:set var = "isAdmin" value = "${sessionScope.userIsAdmin}"/> 
<c:set var = "contextPerms" value = "${requestScope.contextPerms}"/> 
<c:set var = "userId" value = "${sessionScope.userInfo.getId()}"/>
<c:set var = "isTenantEnabled" value = "${sessionScope.isTenantEnabled}"/>
<%
	String home = (String) request.getSession().getAttribute("home");
%>
<html class="iotmanager">
<head>
	<title><i18n:message value="apptitle"/></title>
	<link rel="shortcut icon" href="favicon.ico?" type="image/x-icon" />
	
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"/>
	<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
		
	<!-- Compiled and minified CSS -->
 	<link rel="stylesheet" href="css/materialize.css">
	
	<link rel="stylesheet" href="css/main.css"/>
	<link rel="stylesheet" href="css/index.css"/>
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
	<!-- Compiled and minified JavaScript -->
 	<script src="js/materialize.min.js"></script>
	
	<script type="text/javascript" src="js/conf/init_conf.js"></script>
	
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    
</head>

<body class="materialize">
	<%@include file="../frames/loader.jspf"%>
	
	<div id="containerLayout">
		
		<div id="headerLayout">	
			<header>
				<%@include file="../frames/navbar.jspf"%>
			</header>
		</div>
		
		<div id="bodyLayout">
		
			<input type="hidden" id="enabler" name="enabler" value="${enabler}"> 
		
			<div class="section">
				<div class="h-40">
					<c:if test = "${isMultiEnablerOn eq true}" >
						<% if ("true".equalsIgnoreCase(home)) { %>
						<div class="row  valign-wrapper">						
							<div class="col valign">
								<a href="home" class="btn-floating waves-effect waves-light left">
									<i class="material-icons ">arrow_back</i>
								</a>
							</div>
							<div class="col breadcrumb_bar nav-wrapper valign">
						       <a href="home" id="scope_breadcrumb" class="breadcrumb"><i18n:message value="enabler"/></a>
							</div>
						</div>
						<% } else { %>
						<div class="row  valign-wrapper">
							<div class="col breadcrumb_bar nav-wrapper valign">
						       <span id="scope_breadcrumb" class="breadcrumb"><i18n:message value="enabler"/></span>
							</div>
						</div>
						<% } %>
					</c:if>
				</div>
			</div>
		
			<div class="row" style="margin-left:5%;">
				<div class="input-field col s6 m4 l6">
					<input type="text" id="searchField" placeholder="Find contexts"></input>
				</div>				
	            <div class="col s6 m3 l2" style="margin-top: 25px;">
	            	<a class="waves-effect waves-light btn blue-grey darken-4" onclick="sortContexts()"><i id="sorter" class="material-icons right">keyboard_arrow_down</i>Sort</a>            	
	            </div>
	            <c:if test = "${(isAdmin eq true || contextPerms.getCanCreate() eq true) && isTenantEnabled eq false}">
		            <div class="col s12 m5 l4">
		            	<a class="waves-effect waves-light btn" href="#newcatmodal" style="margin-top: 25px;">Create new Context</a>
		            </div>
	            </c:if>
	                        
			</div>		
		
			<div class="section container">
				<div class="row hide" id="errorMessage"></div>
		  
				<div id="catlist">
					<%@include file="../frames/categoryitem.jspf" %>
					<!-- isSeller and is integrated with urbo -->
					<div class="row">
						<div class="col s12 m6 l4 catitem hide" id="newcattrigger">
							
						</div>
					</div>
				</div>
			
<!-- 				<div id="push"> </div> -->
			</div>

		</div>
		<div id="footerLayout">	
			<%@include file="../frames/footer.jspf"%>
		</div>
				
		<!-- 	MODALI -->
		<%@include file="../frames/settings_newcatmodal.jspf"%>
		<%@include file="../frames/settings_editcatmodal.jspf"%>
	</div>
	<script>
		var isAdmin = "<c:out value='${isAdmin}'/>";
		var userId_fromSession = "<c:out value='${userId}'/>";
	</script>
	<script type="text/javascript" src="js/main.js"></script>
	<script type="text/javascript" src="js/util.js"></script>
	<script type="text/javascript" src="js/index.js"></script>
	<script type="text/javascript" src="js/model/category.js"></script>
	<%@include file="../frames/messages.jspf"%>
	
</body>

</html>
