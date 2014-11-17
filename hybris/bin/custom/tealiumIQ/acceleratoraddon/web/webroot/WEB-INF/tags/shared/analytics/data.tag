<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ tag import="com.tealium.udo.definitions.*" %>
<%@ tag import="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ tag import="de.hybris.platform.cms2.model.pages.ContentPageModel" %>
<%@ tag import="de.hybris.platform.commercefacades.user.data.CustomerData" %>
<%@ tag import="de.hybris.platform.commercefacades.storesession.data.CurrencyData" %>
<%@ tag import="de.hybris.platform.commercefacades.storesession.data.LanguageData" %>
<%@ tag import="de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData" %>
<%@ tag import="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ tag import="de.hybris.platform.commercefacades.order.data.OrderData" %>
<%@ tag import="de.hybris.platform.commercefacades.order.data.CCPaymentInfoData" %>


<%
<<<<<<< Updated upstream
// Edit these to your TealiumIQ account settings
String accountString = "ACCOUNT";
String profileString = "PROFILE";
String targetString = "TARGET";
=======
	// Edit these to your TealiumIQ account settings
	String accountString = "tealium";
	String profileString = "services.patrick";
	String targetString = "dev";
%>
>>>>>>> Stashed changes

<%-- // Do not edit below this line *********************************************** --%>
<%
CurrencyData currencyData = (CurrencyData) request.getAttribute("currentCurrency");
LanguageData languageData = (LanguageData) request.getAttribute("currentLanguage");
String pageName = "";
%>

<%--           ------            Get Page Type         ------           --%>

<c:set var="testPageType" value="${ fn:toLowerCase(pageType) }" />
<c:choose>
	<c:when test="${ not empty testPageType }">
		<c:set var="currentPageType" value="${testPageType}" />
		<%
			pageName = ((String) request.getAttribute("pageType")).toLowerCase();
		%>
	</c:when>
	<c:otherwise>
		<c:if test="${not empty cmsPage.label}">
			<c:set var="currentPageType" value="${fn:toLowerCase(cmsPage.label)}" />
		<%
			pageName = (((ContentPageModel) request.getAttribute("cmsPage")).getLabel()).toLowerCase();
		%>
		</c:if>
	</c:otherwise>
</c:choose>

<%--                    ---------------------                            --%>

<%
	UDOBuilder.setPageName(pageName);
	UDOBuilder.setLanguageData(languageData);
	UDOBuilder.setCurrencyData(currencyData);
	UDOBuilder.init(accountString, profileString, targetString);
%>

<!-- UDO for page type "${currentPageType}" -->
<c:choose>
	<c:when test="${currentPageType == 'homepage'}">
		<%=UDOBuilder.getTealiumHelper().outputFullHtml(UDOBuilder.getHomeUdo())%>
	</c:when>
	
	<c:when test="${currentPageType == 'productsearch'}">
		<%
			ProductCategorySearchPageData searchData = (ProductCategorySearchPageData) request.getAttribute("searchPageData");
			UDOBuilder.setSearchData(searchData);
		%>
		<%=UDOBuilder.getTealiumHelper().outputFullHtml(UDOBuilder.getSearchUdo())%>
	</c:when>
	
	<c:when test="${currentPageType == 'category'}">
		<%UDOBuilder.setCategoryName((String) request.getAttribute("categoryName"));%>
		<%=UDOBuilder.getTealiumHelper().outputFullHtml(UDOBuilder.getCategoryUdo())%>
	</c:when>
	
	<c:when test="${currentPageType == 'product'}">
		<%
			ProductData productData = (ProductData) request.getAttribute("product");
			UDOBuilder.setProductData(productData);
		%>
		<%=UDOBuilder.getTealiumHelper().outputFullHtml(UDOBuilder.getProductUdo())%>
	</c:when>
	
	<c:when test="${currentPageType == 'cart'}">
		<%
			CartData cartData = (CartData) request.getAttribute("cartData");
			UDOBuilder.setCartData(cartData);
		%>
		<%=UDOBuilder.getTealiumHelper().outputFullHtml(UDOBuilder.getCartUdo())%>
	</c:when>
<<<<<<< Updated upstream
</c:choose>
=======
	
	<c:when test="${currentPageType == 'profile'}">
		<%
			CustomerData customerData = (CustomerData) request.getAttribute("customerData");
			UDOBuilder.setCustomerData(customerData);
		%>
		<%=UDOBuilder.getTealiumHelper().outputFullHtml(UDOBuilder.getCustomerUdo())%>
	</c:when>
	
	<c:when test="${currentPageType == 'orderconfirmation'}">
		<%
			CCPaymentInfoData ccInfo = (CCPaymentInfoData) request.getAttribute("paymentInfo");
			OrderData orderData = (OrderData) request.getAttribute("orderData");
			UDOBuilder.setCcInfo(ccInfo);
			UDOBuilder.setOrderData(orderData);
			UDOBuilder.setEmailAddress((String)request.getAttribute("email"));
		%>
		
		<%=UDOBuilder.getTealiumHelper().outputFullHtml(UDOBuilder.getConfirmationUdo())%>
	</c:when>
	
	<c:otherwise>
		<%=UDOBuilder.getTealiumHelper().outputFullHtml(UDOBuilder.getGenericUdo())%>
	</c:otherwise>
</c:choose>
>>>>>>> Stashed changes
