<!doctype html>
<%--

    Copyright (c) 2011-2012, Team maxdocs.org

    All rights reserved.

    Redistribution and use in source and binary forms, with or without modification, are permitted provided
    that the following conditions are met:

    1. Redistributions of source code must retain the above copyright notice, this list of conditions and the
       following disclaimer.
    2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and
       the following disclaimer in the documentation and/or other materials provided with the distribution.
    3. The name of the author may not be used to endorse or promote products derived from this software
       without specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
    NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
    DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
    EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
    SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
    LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
    ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--%>
<%@ page session="false"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib uri="/WEB-INF/maxdocs.tld" prefix="max"%>

<html lang="en">
<head>
<meta charset="utf-8"/>
<fmt:setBundle var="template" basename="template.default" /> 
<fmt:setBundle var="internal" basename="org.maxdocs.maxdocs" /> 
<fmt:setBundle var="maxdocs" basename="maxdocs" /> 
<title>
<fmt:message key="edit.title" bundle="${template}">
      <fmt:param><max:pageName plain="true" /></fmt:param>
      <fmt:param>MaxDocs</fmt:param>
    </fmt:message>
</title>
<c:set var="description"><fmt:message key="description" bundle="${maxdocs}" /></c:set>
<c:set var="author"><fmt:message key="author" bundle="${maxdocs}" /></c:set>
<meta name="description" content="${description}"/>
<meta name="author" content="${author}"/>
<link href="<%=request.getContextPath()%>/internal/templates/default/css/maxdocs.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/internal/templates/default/css/smoothness/jquery-ui-1.9.1.custom.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/internal/templates/default/js/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/internal/templates/default/js/jquery-ui-1.9.1.custom.js"></script>
<script type="text/javascript">
var contextPath = "<%=request.getContextPath()%>";
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/internal/templates/default/js/maxdocs.js"></script>
<!--[if lte IE 7]>
<link rel="stylesheet" href="<%=request.getContextPath()%>/internal/templates/default/yaml/core/iehacks.min.css" type="text/css"/>
<![endif]-->
<!--[if lt IE 9]>
<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
</head>
<body>
	<!-- skip link navigation -->
	<ul class="ym-skiplinks">
		<li><a class="ym-skip" href="#nav">Skip to navigation (Press Enter)</a></li>
		<li><a class="ym-skip" href="#main">Skip to main content (Press Enter)</a></li>
	</ul>
	<div class="ym-wrapper">
		<div class="ym-wbox">
			<header>
				<div id="topnav">
					<c:url var="login" value="">
						<c:param name="action" value="login" />
					</c:url>
					<c:url var="logout" value="">
						<c:param name="action" value="doLogout" />
					</c:url>
					<span>
						<shiro:notAuthenticated>
							<a href="${login}"><fmt:message key="link.login.title" bundle="${template}" /></a> |
						</shiro:notAuthenticated>
						<shiro:authenticated>
							<a href="${logout}"><fmt:message key="link.logout.title" bundle="${template}" /></a> |
						</shiro:authenticated>
						<a href="Contact"><fmt:message key="link.contact.title" bundle="${template}" /></a> |
						<a href="Imprint"><fmt:message key="link.imprint.title" bundle="${template}" /></a>
					</span>
				</div>
				<h2><img src="<%=request.getContextPath()%>/internal/templates/default/images/maxdocs-klein.png" alt="MaxDocs" /><br/>
				<fmt:message key="edit.heading" bundle="${template}" /></h2>
			</header>
			<div id="breadcrumbs">
				<max:breadcrumbs />
			</div>
			<div id="action">
				<c:url var="show" value="">
					<c:param name="action" value="show" />
				</c:url>
				<c:url var="delete" value="">
					<c:param name="action" value="delete" />
				</c:url>
				<c:url var="rename" value="">
					<c:param name="action" value="rename" />
				</c:url>
				<c:url var="source" value="">
					<c:param name="action" value="source" />
				</c:url>
				<c:url var="info" value="">
					<c:param name="action" value="info" />
				</c:url>
				<span>
					<a href="${show}"><fmt:message key="link.show.title" bundle="${template}" /></a> | 
					<fmt:message key="link.edit.title" bundle="${template}" /> | 
					<a href="${delete}"><fmt:message key="link.delete.title" bundle="${template}" /></a> | 
					<a href="${rename}"><fmt:message key="link.rename.title" bundle="${template}" /></a> | 
					<a href="${source}"><fmt:message key="link.source.title" bundle="${template}" /></a> | 
					<a href="${info}"><fmt:message key="link.info.title" bundle="${template}" /></a>
				</span>
			</div>
			<div id="main">
				<!-- begin: #col1 - first float column -->
				<div class="ym-col1">
					<div class="ym-cbox">
						<h2>Navigation</h2>
						<max:pageExists page="LeftMenu">
						<max:insertPage name="LeftMenu" />
						</max:pageExists>
						<max:noSuchPage page="LeftMenu">
						<p>Menu nicht vorhanden</p>
						</max:noSuchPage>
					</div>
				</div>
				<!-- end: #col1 -->
		
				<!-- begin: #col2 second float column -->
				<div class="ym-col2">
					<div class="ym-cbox">
						<h2>TagCloud</h2>
						<max:tagCloud size="15" />
					</div>
				</div>
				<!-- end: #col2 -->
		
				<!-- begin: #col3 static column -->
				<div class="ym-col3">
					<div class="ym-cbox">
						<h2>Seite <max:pageName plain="true" />
						<c:set var="pageName"><max:pageName plain="true"/></c:set>
						<max:noSuchPage page="${pageName}">
							erstellen
						</max:noSuchPage>
						<max:pageExists page="${pageName}">
							bearbeiten
						</max:pageExists>
						</h2>
						<form method="post" action="?action=save" accept-charset="UTF-8">
							<p>Markup: <max:markupLanguage type="input" size="1" /></p>
							<textarea rows="15" cols="58" name="content"><max:pageSource /></textarea>
							<label for="tags">Tags:</label><input type="text" name="tags" id="tags" size="50" value="${MAXDOCS_MARKUP_PAGE.tagsAsString}" /><br/>
							<input type="hidden" name="action" value="save"/>
							<input type="hidden" name="version" value="${MAXDOCS_MARKUP_PAGE.version}" />
							<input type="hidden" name="editor" value=""/>
							<input type="button" name="preview" value="Vorschau" />&nbsp;<input
								type="submit" name="save" value="Speichern" />&nbsp;<a href="?action=show">Abbrechen</a>
						</form>
					</div>
				</div>
				<!-- end: #col3 -->
			</div>
	<!-- end: #main -->

	<!-- begin: #footer -->
			<footer>
				This page (version <max:pageVersion />) was last changed on 
				<max:date type="lastChange" /> by <max:author type="editor" />
				<br />Layout based on <a href="http://www.yaml.de/">YAML</a>
				<br />Version <fmt:message key="maxdocs.version" bundle="${internal}"/> vom <fmt:message key="maxdocs.buildtime" bundle="${internal}"/>
				<br />&copy; <fmt:message key="maxdocs.inceptionYear" bundle="${internal}"/>
				- <fmt:message key="maxdocs.currentYear" bundle="${internal}"/>
				<a href="<fmt:message key="maxdocs.organization.url" bundle="${internal}"/>"><fmt:message key="maxdocs.organization.name" bundle="${internal}"/></a>
			</footer>
		</div>
	</div>
		<!-- end: #footer -->
	<!-- full skiplink functionality in webkit browsers -->
	<script src="<%=request.getContextPath()%>/internal/templates/default/yaml/core/js/yaml-focusfix.js" type="text/javascript"></script>
</body>
</html>
