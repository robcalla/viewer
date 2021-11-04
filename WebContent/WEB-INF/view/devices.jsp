<%@page import="it.eng.iot.configuration.Conf"%>
<%@taglib uri="http://eng.it/ricerca/pa/taglib/i18n" prefix="i18n"%>

<!-- JSTL -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!-- OBJECT FROM BACKEND -->
<c:set var="enabler" value="${sessionScope.enabler}"/>
<c:set var="isMultiEnablerOn" value="${sessionScope.isMultiEnablerOn}"/>
<c:set var = "isAdmin" value = "${sessionScope.userIsAdmin}"/>
<c:set var = "dashboardPerms" value = "${requestScope.dashboardPerms}"/>
<c:set var = "userId" value = "${sessionScope.userInfo.getId()}"/>


<html class="iotmanager">
<head>
	<title><i18n:message value="apptitle"/></title>
	<link rel="shortcut icon" href="favicon.ico?" type="image/x-icon" />

	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"/>
   	<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <!-- Compiled and minified CSS -->
	<link rel="stylesheet" href="css/materialize.css">


	<link rel="stylesheet" href="css/main.css"/>
	<link rel="stylesheet" href="css/devices.css"/>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

   	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
	<!-- Compiled and minified JavaScript -->
	<script type="text/javascript" src="js/materialize.min.js"></script>

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

			<div class="section" id="breads">
				<div class="h-40">
					<div class="row  valign-wrapper">
						<div class="col valign">
							<a href="urbanservices?scope=" id="backlink" class="btn-floating waves-effect waves-light left">
								<i class=" material-icons">arrow_back</i>
							</a>
						</div>
						<div class="col breadcrumb_bar nav-wrapper  valign-wrapper">
					       <a href="#!" id="scope_breadcrumb" class=" breadcrumb truncate"></a>
					       <a href="#!" id="urbanservice_breadcrumb" class=" breadcrumb truncate"></a>
					       <a href="#!" id="dashboard_breadcrumb" class="valign breadcrumb truncate"></a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div id="bodyLayout">

			<c:if test = "${isAdmin eq true || dashboardPerms.getCanCreate() eq true}">

				<!-- Dropdown Structure -->
				<ul id="menuCont" class="dropdown-content">
				  	<li>
				    	<a href="#cockpitlinkmodal" class="" id="cockpit-link"><i class="material-icons left">add</i><i18n:message value="add_dashboard"/></a>
					</li>
				    <li>
						<a href="#cockpitselectmodal" class="" id="change-cockpit-link"><i class="material-icons left">autorenew</i><i18n:message value="change_dashboard"/></a>
					</li>
				    <li class="divider"></li>
				    <li>
				    	<a href="#cockpitremove" class="" id="remove-cockpit-link"><i class="material-icons left">delete</i><i18n:message value="delete_dashboard"/></a>
					</li>
				</ul>
				<nav id="menuContestuale">
				  <div class="nav-wrapper">
				    <span id="cockpitNameTitle" class="brand-logo">Logo</span>
				    <ul class="right">
				      <!-- Dropdown Trigger -->
				      <li><a class="dropdown-button white-text" href="#!" data-activates='menuCont'><i class="material-icons">more_vert</i></a></li>
				    </ul>
				  </div>
				</nav>

			</c:if>
			<div class="section">
				<div class="row hide" id="errorMessage"></div>

				<div id="listvisualization">
					<div id="devicelist">

						<script type="text/javascript">
							currentLanguage = '<%=request.getSession().getAttribute("lang")%>';
						</script>

						<%@include file="../frames/index_deviceitem.jspf" %>

						<div class="row center-align" id="no-cockpit-linked">
							<h2><i18n:message value="no_cockpit_linked"/></h2>
						</div>

					</div>
				</div>

				<%@include file="../frames/messages.jspf"%>
				<%@include file="../frames/dashboard_newmodal.jspf"%>
				<%@include file="../frames/dashboard_select.jspf" %>
			</div>

		</div>
		<div id="footerLayout">
			<%@include file="../frames/footer.jspf"%>
		</div>
	</div>
	
	<iframe id="knowage-loader" class="hide" src=""></iframe>

	<script>
		var isAdmin = "<c:out value='${isAdmin}'/>";
		var userId_fromSession = "<c:out value='${userId}'/>";
	</script>
	<script type="text/javascript" src="js/main.js"></script>
	<script type="text/javascript" src="js/util.js"></script>
	<script type="text/javascript" src="js/devices.js"></script>
	<script type="text/javascript" src="js/managers.js"></script>


	<script type="text/javascript">

		 $('.dropdown-button').dropdown({
		      inDuration: 300,
		      outDuration: 225,
		      constrainWidth: false, // Does not change width of dropdown to that of the activator
		      hover: false, // Activate on hover
		      gutter: 0, // Spacing from edge
		      belowOrigin: false, // Displays dropdown below the button
		      alignment: 'right', // Displays dropdown with edge aligned to the left of button
		      stopPropagation: false // Stops event propagation
		    }
		  );

		 window.onload = function(e){
			resizeCockpitPage()
			window.onresize = function() {
				resizeCockpitPage()
			};
		 };
	</script>
</body>

</html>
