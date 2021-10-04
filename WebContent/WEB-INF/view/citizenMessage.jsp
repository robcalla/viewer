<%@taglib uri="http://eng.it/ricerca/pa/taglib/i18n" prefix="i18n"%>

<!-- JSTL -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!-- OBJECT FROM BACKEND -->
<c:forEach items="${requestScope.userPerms}" var="userPerms"> 
 	<c:if test = "${fn:containsIgnoreCase(userPerms.applicationRole, 'Admin')}">
 		<c:set var = "isAdmin" scope = "session" value = "true"/> 
 	 </c:if> 
 </c:forEach> 


		

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
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
	<!-- Compiled and minified JavaScript -->
	<script src="js/materialize.min.js"></script>
	<script type="text/javascript" src="js/conf/init_conf.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    
</head>

<body class="container materialize grey lighten-3">
	<%@include file="../frames/loader.jspf"%>
	
	<div class="materialize grey lighten-3">
      <h2 class="materialize grey lighten-3"><i18n:message value="msg_citizen_message"/></h2>
      <hr/>
     
      <table>
      <thead>
         <tr><th><i18n:message value="msg_citizen_warning"/></th></tr>
      </thead>
     
      </table>
	</div>
	
<!-- SCRIPTs -->		

	<script type="text/javascript" src="js/main.js"></script>
	<script type="text/javascript" src="js/util.js"></script>
	<script type="text/javascript" src="js/index.js"></script>
	
	<%@include file="../frames/messages.jspf"%>
	
</body>

</html>
