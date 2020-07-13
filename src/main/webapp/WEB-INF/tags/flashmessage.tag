<%@ tag language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Show flash messages --%>
<c:if test="${flash.exists}">
	<p class="alert alert-${flash.color} mt-3">${flash.message}</p>
</c:if>
