<!doctype html>
<%--

    Copyright (c) 2011-2013, Team maxdocs.org

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
<%@ page session="true"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib uri="/WEB-INF/maxdocs.tld" prefix="max"%>

<html lang="en">
<head>
<meta charset="utf-8" />
<fmt:setBundle var="template" basename="template.flexible" />
<fmt:setBundle var="internal" basename="org.maxdocs.maxdocs" />
<fmt:setBundle var="maxdocs" basename="maxdocs" />
<title><fmt:message key="show.title" bundle="${template}">
		<fmt:param>
			<max:pageName plain="true" />
		</fmt:param>
		<fmt:param>MaxDocs</fmt:param>
	</fmt:message></title>

<!-- Mobile viewport optimisation -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<c:set var="description">
	<fmt:message key="meta.description" bundle="${maxdocs}" />
</c:set>
<c:set var="author">
	<fmt:message key="meta.author" bundle="${maxdocs}" />
</c:set>
<meta name="description" content="${description}" />
<meta name="author" content="${author}" />
<link href="<%=request.getContextPath()%>/internal/templates/flexible/css/maxdocs.css" rel="stylesheet" type="text/css" />
<!--[if lte IE 7]>
<link rel="stylesheet" href="<%=request.getContextPath()%>/internal/templates/flexible/yaml/core/iehacks.min.css" type="text/css"/>
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

	<header>
		<div class="ym-wrapper">
			<div class="ym-wbox">
				<div id="topnav">
					<c:url var="login" value="">
						<c:param name="action" value="login" />
					</c:url>
					<c:url var="logout" value="">
						<c:param name="action" value="doLogout" />
					</c:url>
					<span> <shiro:notAuthenticated>
							<a href="${login}"><fmt:message key="link.login.title" bundle="${template}" /></a> |
					</shiro:notAuthenticated> <shiro:authenticated>
							<a href="${logout}"><fmt:message key="link.logout.title" bundle="${template}" /></a> |
					</shiro:authenticated> <a href="Contact"><fmt:message key="link.contact.title" bundle="${template}" /></a> | <a href="Imprint"><fmt:message
								key="link.imprint.title" bundle="${template}" /></a>
					</span>
				</div>
				<h1>
					<img src="<%=request.getContextPath()%>/internal/templates/flexible/images/maxdocs-klein.png"
						alt="MaxDocs" /><br />
					<fmt:message key="show.heading" bundle="${template}" />
				</h1>
			</div>
		</div>
	</header>

	<nav id="nav">
		<div class="ym-wrapper">
			<div class="ym-hlist">
				<c:url var="show" value="">
					<c:param name="action" value="show" />
				</c:url>
				<c:url var="edit" value="">
					<c:param name="action" value="edit" />
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
				<ul>
					<li class="active"><strong><fmt:message key="link.show.title" bundle="${template}" /></strong></li>
					<li><a href="${edit}"><fmt:message key="link.edit.title" bundle="${template}" /></a></li>
					<max:pageExists>
					<li><a href="${delete}"><fmt:message key="link.delete.title" bundle="${template}" /></a></li>
					<li><a href="${rename}"><fmt:message key="link.rename.title" bundle="${template}" /></a></li>
					<li><a href="${source}"><fmt:message key="link.source.title" bundle="${template}" /></a></li> 
					<li><a href="${info}"><fmt:message key="link.info.title" bundle="${template}" /></a></li>
					</max:pageExists>
					<max:noSuchPage>
					<li><strong><fmt:message key="link.delete.title" bundle="${template}" /></strong></li>
					<li><strong><fmt:message key="link.rename.title" bundle="${template}" /></strong></li>
					<li><strong><fmt:message key="link.source.title" bundle="${template}" /></strong></li>
					<li><strong><fmt:message key="link.info.title" bundle="${template}" /></strong></li>
					</max:noSuchPage>
				</ul>
			</div>
		</div>
	</nav>

	<div id="main">
		<div class="ym-wrapper">
			<div class="ym-wbox">

				<section class="ym-grid linearize-level-1">
					<article class="ym-g66 ym-gl content">
						<div class="ym-gbox-left ym-clearfix">
							<max:breadcrumbs />
							<c:if test="${! empty requestScope.MAXDOCS_MESSAGES}">
							<div id="errors"><max:messages /></div>
							</c:if>
							<h1>
								<max:pageName plain="true" />
							</h1>

						<max:pageExists><max:pageContent /></max:pageExists>
						<max:noSuchPage><div class="maxdocsContent"><p>Die Seite <strong><max:pageName plain="true" /></strong> existiert nicht.!<br/>
						<a href="${edit}">Erstelle</a> sie doch einfach.</p></div></max:noSuchPage>
						</div>
					</article>

					<aside class="ym-g33 ym-gr">
						<div class="ym-gbox-right ym-clearfix">
							<max:insertPage name="LeftMenu" styleClass="box info" />
							<div class="box info">
								<h3>TagCloud</h3>
								<max:tagCloud />
							</div>
						</div>
					</aside>
				</section>
			</div>
		</div>
	</div>




	<footer>
		<div class="ym-wrapper">
			<div class="ym-wbox">
				This page (version
				<max:pageVersion />
				) was last changed on
				<max:date type="lastchange" />
				by
				<max:author type="editor" />
				<br />Layout based on <a href="http://www.yaml.de/">YAML</a> <br />Version
				<fmt:message key="maxdocs.version" bundle="${internal}" />
				vom
				<fmt:message key="maxdocs.buildtime" bundle="${internal}" />
				<br />&copy;
				<fmt:message key="maxdocs.inceptionYear" bundle="${internal}" />
				-
				<fmt:message key="maxdocs.currentYear" bundle="${internal}" />
				<a href="<fmt:message key="maxdocs.organization.url" bundle="${internal}"/>"><fmt:message
						key="maxdocs.organization.name" bundle="${internal}" /></a>
			</div>
		</div>
	</footer>

	<!-- full skiplink functionality in webkit browsers -->
	<script src="<%=request.getContextPath()%>/internal/templates/flexible/yaml/core/js/yaml-focusfix.js"
		type="text/javascript"></script>
</body>
</html>
