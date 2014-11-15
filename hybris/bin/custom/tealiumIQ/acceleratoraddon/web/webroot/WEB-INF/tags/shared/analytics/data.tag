<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ tag import="com.tealium.udo.definitions.*" %>
<%@ tag import="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ tag import="de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData" %>
<%@ tag import="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ tag import="de.hybris.platform.commercefacades.storesession.data.CurrencyData" %>
<%@ tag import="de.hybris.platform.commercefacades.storesession.data.LanguageData" %>


<%
// Edit these to your TealiumIQ account settings
String accountString = "ACCOUNT";
String profileString = "PROFILE";
String targetString = "TARGET";

// Do not edit below this line **********************************************************************
ProductData productData = (ProductData) request.getAttribute("product");
CurrencyData currencyData = (CurrencyData) request.getAttribute("currentCurrency");
LanguageData languageData = (LanguageData) request.getAttribute("currentLanguage");
ProductCategorySearchPageData searchData = (ProductCategorySearchPageData) request.getAttribute("searchPageData");
CartData cartData = (CartData) request.getAttribute("cartData");

UDOBuilder.init(accountString, profileString, targetString);
UDOBuilder.setLanguageData(languageData);
UDOBuilder.setCurrencyData(currencyData);
%>

<%--                     Get Page Type                                    --%>

<c:set var="testPageType" value="${ fn:toLowerCase(pageType) }" />
<c:choose>
	<c:when test="${ not empty testPageType }">
		<c:set var="currentPageType" value="${testPageType}" />
	</c:when>
	<c:otherwise>
		<c:if test="${not empty cmsPage.label}">
			<c:set var="currentPageType" value="${fn:toLowerCase(cmsPage.label)}" />
		</c:if>
	</c:otherwise>
</c:choose>

<%--            *************************************                     --%>

<c:choose>
	<c:when test="${currentPageType == 'homepage'}">
		<%//=tealiumHelper.outputFullHtml(homeUdo)%>
		<%=UDOBuilder.getTealiumHelper().outputFullHtml(UDOBuilder.getHomeUdo())%>
	</c:when>
	
	<c:when test="${currentPageType == 'productsearch'}">
		<%UDOBuilder.setSearchData(searchData);%>
		<%=UDOBuilder.getTealiumHelper().outputFullHtml(UDOBuilder.getSearchUdo())%>
	</c:when>
	
	<c:when test="${currentPageType == 'category'}">
		<%UDOBuilder.setCategoryName((String) request.getAttribute("categoryName"));%>
		<%=UDOBuilder.getTealiumHelper().outputFullHtml(UDOBuilder.getCategoryUdo())%>
	</c:when>
	
	<c:when test="${currentPageType == 'product'}">
		<%UDOBuilder.setProductData(productData);%>
		<%=UDOBuilder.getTealiumHelper().outputFullHtml(UDOBuilder.getProductUdo())%>
	</c:when>
	
	<c:when test="${currentPageType == 'cart'}">
		<%UDOBuilder.setCartData(cartData);%>
		<%=UDOBuilder.getTealiumHelper().outputFullHtml(UDOBuilder.getCartUdo())%>
	</c:when>
</c:choose>
