<html>
    <head>
        <title><g:layoutTitle default="Grails" /></title>
        <p:css name='bundled'/>
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
		<g:javascript library="jquery" plugin="jquery"/>
		<jqui:resources/>        
		<p:javascript src="jquery.plugins"/>
		<p:javascript src="app.all"/>
		<g:javascript>
			serverUrl = "${resource(dir:'/')}";
		</g:javascript>
        <g:layoutHead />
    </head>
    <body>
		<div id="header">
			 <div id='searchBoxDiv' style='display:none;'><input type='text' id='searchBox' size='15'/></div>
		     <div id="appname"><g:link url="[action:'index',controller:'home']"><p:image src='logo.png' alt='aajkaaj'/></g:link></div>
			 <div id="user"><g:message code="nimble.label.usergreeting" /> <n:principalName /> |
			 <g:link url="[action:'index',controller:'inbox']">Inbox</g:link> |
			 <g:if test="${isAdmin}"><a href='${resource(dir:'/')}administration/users/list'>User Admin</a> | </g:if>  
			 <g:link controller="auth" action="logout" class="logout"><g:message code="nimble.link.logout.basic" /></g:link></div>
			 <g:pageProperty name="page.headerContent"/>
		</div>
    	<div id="content">
        	<g:layoutBody />
        </div>
        <div id='footer'>
        	<div class='version'>version <g:meta name="app.version"/></div>
        </div>
    </body>
</html>
