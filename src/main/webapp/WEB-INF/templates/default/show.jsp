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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page session="false"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/maxdocs.tld" prefix="max"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<fmt:setBundle var="template" basename="template.default" /> 
<fmt:setBundle var="internal" basename="org.maxdocs.maxdocs" /> 
<title>
<fmt:message key="show.title" bundle="${template}">
      <fmt:param><max:pageName plain="true" /></fmt:param>
      <fmt:param>MaxDocs</fmt:param>
    </fmt:message>
</title>
<!-- (en) Add your meta data here -->
<!-- (de) Fuegen Sie hier ihre Meta-Daten ein -->
<link href="<%=request.getContextPath()%>/internal/css/layout_1-3-2.css"
	rel="stylesheet" type="text/css" />
<!--[if lte IE 7]>
<link href="<%=request.getContextPath()%>/internal/css/patches/patch_1-3-2.css" rel="stylesheet" type="text/css" />
<![endif]-->
</head>
<body>
	<!-- skip link navigation -->
	<ul id="skiplinks">
		<li><a class="skip" href="#nav">Skip to navigation (Press
				Enter).</a></li>
		<li><a class="skip" href="#col3">Skip to main content (Press
				Enter).</a></li>
	</ul>

	<div class="page_margins">
		<div class="page">
			<div id="header" role="banner">
				<div id="topnav" role="contentinfo">
					<span>
						<a href="#">Login</a> |
						<a href="#">Contact</a> | 
						<a href="#">Imprint</a>
					</span>
				</div>
				<h1>MaxDocs - The Multi Document Wiki Blog Engine</h1>
			</div>
			<!-- begin: main navigation #nav -->
<!-- 			<div id="nav" role="navigation">
      <div class="hlist">
        <ul>
          <li><a href="../index.html">Table Of Contents</a></li>
          <li><a href="3col_2-1-3.html">Next Example</a></li>
          <li><a href="3col_1-2-3.html">Previous Example</a></li>
        </ul>
      </div>
    </div> -->
    
    		<div id="breadcrumbs">
    			<max:breadcrumbs />
			</div>
    		<div id="action">
				<span>
					<a href="?action=edit">Edit</a> | 
					<a href="?action=source">Source</a> | 
					<a href="?action=info">Info</a>
				</span>
			</div>
			<!-- end: main navigation -->

			<!-- begin: main content area #main -->
			<div id="main">
				<!-- begin: #col1 - first float column -->
				<div id="col1" role="complementary">
					<div id="col1_content" class="clearfix">
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
				<div id="col2" role="complementary">
					<div id="col2_content" class="clearfix">
						<h2>TagCloud</h2>
						<max:tagCloud />
					</div>
				</div>
				<!-- end: #col2 -->

				<!-- begin: #col3 static column -->
				<div id="col3" role="main">
					<div id="col3_content" class="clearfix">
						<h2><max:pageName plain="true" /></h2>
						<max:pageContent />
					</div>
					<!-- IE Column Clearing -->
					<div id="ie_clearing">&nbsp;</div>
				</div>
				<!-- end: #col3 -->
			</div>
			<!-- end: #main -->

			<!-- begin: #footer -->
			<div id="footer" role="contentinfo">This page (version <max:pageVersion />) was last changed on 
				<max:date type="lastChange" /> by <max:author type="editor" />
				<br />Layout based on <a href="http://www.yaml.de/">YAML</a>
				<br />Version <fmt:message key="maxdocs.version" bundle="${internal}"/> vom <fmt:message key="maxdocs.buildtime" bundle="${internal}"/>
				<br />&copy; <fmt:message key="maxdocs.inceptionYear" bundle="${internal}"/>
				 - <fmt:message key="maxdocs.currentYear" bundle="${internal}"/>
				 <a href="<fmt:message key="maxdocs.organization.url" bundle="${internal}"/>"><fmt:message key="maxdocs.organization.name" bundle="${internal}"/></a>
			</div>
			<!-- end: #footer -->
		</div>
	</div>
	<!-- full skiplink functionality in webkit browsers -->
	<script
		src="<%=request.getContextPath()%>/internal/yaml/core/js/yaml-focusfix.js"
		type="text/javascript"></script>
</body>
</html>
