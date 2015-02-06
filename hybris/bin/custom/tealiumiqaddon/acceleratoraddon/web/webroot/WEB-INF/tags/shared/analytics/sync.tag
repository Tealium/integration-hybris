<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ tag import="com.tealium.dataconnector.hybris.HybrisDataConverter" %>

<%
  String syncTagString = HybrisDataConverter.getSyncTag();
  String output = "";
  if (syncTagString != null){
    output = syncTagString;
  }
%>
  
<%=output%>