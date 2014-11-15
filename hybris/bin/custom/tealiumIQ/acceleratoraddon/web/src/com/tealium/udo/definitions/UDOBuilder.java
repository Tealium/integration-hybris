package com.tealium.udo.definitions;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import com.tealium.util.udohelpers.*;
import com.tealium.util.udohelpers.exceptions.UDODefinitionException;
import com.tealium.util.udohelpers.exceptions.UDOUpdateException;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.storesession.data.CurrencyData;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;


public class UDOBuilder {
	static UDO homeUdo;
	static UDO searchUdo;
	static UDO categoryUdo;
	static UDO productUdo;
	static UDO cartUdo;
	static UDO confirmationUdo;
	static UDO customerUdo;
	static TealiumHelper tealiumHelper;
	static ProductData productData;
	static CurrencyData currencyData;
	static LanguageData languageData;
	static ProductCategorySearchPageData searchData;
	static CartData cartData;
	static String categoryNameString;
	

	public static void init(String accountString, String profileString, String targetString) throws UDODefinitionException, UDOUpdateException {	
		// initialize the library with your tIQ account/profile/environment
		tealiumHelper = new TealiumHelper(accountString, profileString,targetString);	
	}
	
	public static UDO getHomeUdo() throws UDODefinitionException, UDOUpdateException {
		String siteCurrency = (currencyData != null)?currencyData.getIsocode():"null";
		String siteLanguage = (languageData != null)?languageData.getIsocode():"null";
		homeUdo = tealiumHelper.createDefaultUDO(TealiumHelper.PrebuiltUDOPageTypes.HOME)
				.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_NAME, "home page")
				.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_TYPE, "home")
				.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.SITE_CURRENCY, siteCurrency)
				.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.SITE_REGION, siteLanguage);
		return homeUdo;
	}

	public static UDO getSearchUdo() throws UDODefinitionException, UDOUpdateException {
		String siteCurrency = (currencyData != null)?currencyData.getIsocode():"null";
		String siteLanguage = (languageData != null)?languageData.getIsocode():"null";
		String searchKeyword = (searchData != null)?searchData.getFreeTextSearch():"null";
		String searchResults = (searchData != null)?((PaginationData)searchData.getPagination()).getTotalNumberOfResults()+"":"null";

		searchUdo = tealiumHelper.createDefaultUDO(TealiumHelper.PrebuiltUDOPageTypes.SEARCH)
			.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_NAME, "search page")
			.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_TYPE, "search")
			.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.SITE_CURRENCY, siteCurrency)
			.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.SITE_REGION, siteLanguage)
			.setValue(TealiumHelper.SearchPageUDO.PredefinedUDOFields.SEARCH_KEYWORD, searchKeyword)
			.setValue(TealiumHelper.SearchPageUDO.PredefinedUDOFields.SEARCH_RESULTS, searchResults);
		return searchUdo;
	}

	public static UDO getCategoryUdo() throws UDODefinitionException, UDOUpdateException {
		String siteCurrency = (currencyData != null)?currencyData.getIsocode():"null";
		String siteLanguage = (languageData != null)?languageData.getIsocode():"null";
		categoryUdo = tealiumHelper.createDefaultUDO(TealiumHelper.PrebuiltUDOPageTypes.CATEGORY)
				.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_NAME, "category page")
				.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_TYPE, "product")
				.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.SITE_CURRENCY, siteCurrency)
				.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.SITE_REGION, siteLanguage)
				.setValue(TealiumHelper.CategoryPageUDO.PredefinedUDOFields.PAGE_CATEGORY_NAME, categoryNameString)
				.setValue(TealiumHelper.CategoryPageUDO.PredefinedUDOFields.PAGE_SECTION_NAME, "null")
				.setValue(TealiumHelper.CategoryPageUDO.PredefinedUDOFields.PAGE_SUBCATEGORY_NAME, "null");
		return categoryUdo;
	}

	public static UDO getProductUdo() throws UDODefinitionException, UDOUpdateException {
		List<String> productCategoryList = new ArrayList<String>();
		if (productData != null){
			for (CategoryData category : productData.getCategories())
			{
				productCategoryList.add(category.getName());
			}
		}
		String siteCurrency = (currencyData != null)?currencyData.getIsocode():"null";
		String siteLanguage = (languageData != null)?languageData.getIsocode():"null";
		String productCategory = (productCategoryList.toArray().length > 0 && productCategoryList.toArray()[0] != null) ? (String) productCategoryList.toArray()[0] : "null";
		String productBrand = (productCategoryList.toArray().length >= 2 && productCategoryList.toArray()[1] != null) ? (String) productCategoryList.toArray()[1] : "null";
		String productSku = (productData != null)?productData.getCode():"null";
		String productPrice = (productData != null)?((PriceData)productData.getPrice()).getValue().toPlainString():"null";
		String productName = (productData != null)?productData.getName():"null";

		productUdo = tealiumHelper.createDefaultUDO(TealiumHelper.PrebuiltUDOPageTypes.PRODUCT)
				.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_NAME, "product detail page")
				.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_TYPE, "product")
				.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.SITE_CURRENCY, siteCurrency)
				.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.SITE_REGION, siteLanguage)
				.setValue(TealiumHelper.ProductPageUDO.PredefinedUDOFields.PAGE_CATEGORY_NAME, "null")
				.setValue(TealiumHelper.ProductPageUDO.PredefinedUDOFields.PAGE_SECTION_NAME, "null")
				.setValue(TealiumHelper.ProductPageUDO.PredefinedUDOFields.PAGE_SUBCATEGORY_NAME, "null")
				.addArrayValues(TealiumHelper.ProductPageUDO.PredefinedUDOFields.PRODUCT_BRAND, Arrays.asList(productBrand))
				.addArrayValues(TealiumHelper.ProductPageUDO.PredefinedUDOFields.PRODUCT_CATEGORY, Arrays.asList(productCategory))
				.addArrayValues(TealiumHelper.ProductPageUDO.PredefinedUDOFields.PRODUCT_ID, Arrays.asList("null"))
				.addArrayValues(TealiumHelper.ProductPageUDO.PredefinedUDOFields.PRODUCT_LIST_PRICE, Arrays.asList(productPrice))
				.addArrayValues(TealiumHelper.ProductPageUDO.PredefinedUDOFields.PRODUCT_NAME, Arrays.asList(productName))
				.addArrayValues(TealiumHelper.ProductPageUDO.PredefinedUDOFields.PRODUCT_SKU, Arrays.asList(productSku))
				.addArrayValues(TealiumHelper.ProductPageUDO.PredefinedUDOFields.PRODUCT_UNIT_PRICE, Arrays.asList("null"));
		return productUdo;
	}

	public static UDO getCartUdo() throws UDODefinitionException, UDOUpdateException {
		String siteCurrency = (currencyData != null)?currencyData.getIsocode():"null";
		String siteLanguage = (languageData != null)?languageData.getIsocode():"null";
		String cartTotal = (cartData != null)?(String)((PriceData) cartData.getTotalPrice()).getValue().toPlainString():"null";

		List<String> productBrandList = new ArrayList<String>();
		List<String> productCategoryList = new ArrayList<String>();
		List<String> productIdList = new ArrayList<String>();
		List<String> productListPriceList = new ArrayList<String>();
		List<String> productNameList = new ArrayList<String>();
		List<String> productQuantityList = new ArrayList<String>();
		List<String> productSkuList = new ArrayList<String>();
		List<String> productUnitPriceList = new ArrayList<String>();
		if (cartData != null){
			for (OrderEntryData entry : cartData.getEntries())
			{
				String sku = ((ProductData)entry.getProduct()).getCode();
				String name = ((ProductData)entry.getProduct()).getName();
				String quantity = entry.getQuantity()+"";
				String basePrice = ((PriceData)entry.getBasePrice()).getValue().toPlainString();

				List<String> categoryList = new ArrayList<String>();
				for (CategoryData thisCategory : ((ProductData)entry.getProduct()).getCategories()){
					categoryList.add(thisCategory.getName());
				}
				String category = (categoryList.toArray().length > 0 && categoryList.toArray()[0] != null) ? (String) categoryList.toArray()[0] : "null";
				String brand = (categoryList.toArray().length >= 2 && categoryList.toArray()[1] != null) ? (String) categoryList.toArray()[1] : "null";
				
				
				productBrandList.add(brand);
				productCategoryList.add(category);
				productIdList.add("null");
				productListPriceList.add(basePrice);
				productNameList.add(name);
				productQuantityList.add(quantity);
				productSkuList.add(sku);
				productUnitPriceList.add("null");
			}
		}

		cartUdo = tealiumHelper.createDefaultUDO(TealiumHelper.PrebuiltUDOPageTypes.CART)
				.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_NAME, "cart page")
				.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_TYPE, "checkout")
				.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.SITE_CURRENCY, siteCurrency)
				.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.SITE_REGION, siteLanguage)
				.setValue("cart_total", cartTotal)
				.addArrayValues(TealiumHelper.CartPageUDO.PredefinedUDOFields.PRODUCT_BRAND, productBrandList)
				.addArrayValues(TealiumHelper.CartPageUDO.PredefinedUDOFields.PRODUCT_CATEGORY, productCategoryList)
				.addArrayValues(TealiumHelper.CartPageUDO.PredefinedUDOFields.PRODUCT_ID, productIdList)
				.addArrayValues(TealiumHelper.CartPageUDO.PredefinedUDOFields.PRODUCT_LIST_PRICE, productListPriceList)
				.addArrayValues(TealiumHelper.CartPageUDO.PredefinedUDOFields.PRODUCT_NAME, productNameList)
				.addArrayValues(TealiumHelper.CartPageUDO.PredefinedUDOFields.PRODUCT_QUANTITY, productQuantityList)
				.addArrayValues(TealiumHelper.CartPageUDO.PredefinedUDOFields.PRODUCT_SKU, productSkuList)
				.addArrayValues(TealiumHelper.CartPageUDO.PredefinedUDOFields.PRODUCT_UNIT_PRICE, productUnitPriceList);
		return cartUdo;
	}

	public static UDO getConfirmationUdo() throws UDODefinitionException, UDOUpdateException {
		confirmationUdo = tealiumHelper.createDefaultUDO(TealiumHelper.PrebuiltUDOPageTypes.CONFIRMATION)
				.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_NAME, "confirmation page")
				.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_TYPE, "checkout")
				.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.SITE_CURRENCY, "EN")
				.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.SITE_REGION, "US")
				.setValue(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.CUSTOMER_EMAIL, "john.doe@gmail.com")
				.setValue(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.CUSTOMER_ID, "12346231ffb3")
				.setValue(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.ORDER_CURRENCY, "US")
				.setValue(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.ORDER_DISCOUNT, "2.00")
				.setValue(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.ORDER_ID, "123634784567")
				.setValue(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.ORDER_PAYMENT_TYPE, "CC")
				.setValue(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.ORDER_SHIPPING, "1.25")
				.setValue(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.ORDER_SUBTOTAL, "16.95")
				.setValue(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.ORDER_TAX, "2.35")
				.setValue(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.ORDER_TOTAL, "21.95")
				.addArrayValues(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.PRODUCT_BRAND, "brand1", "brand2", "brand3")
				.addArrayValues(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.PRODUCT_CATEGORY,"cat1", "cat2", "cat3")
				.addArrayValues(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.PRODUCT_ID, "1", "2", "3")
				.addArrayValues(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.PRODUCT_LIST_PRICE, "1.00", "2.00", "3.00")
				.addArrayValues(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.PRODUCT_NAME, "name1", "name2", "name3")
				.addArrayValues(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.PRODUCT_QUANTITY, "2", "3", "1")
				.addArrayValues(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.PRODUCT_SKU, "123", "456", "231")
				.addArrayValues(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.PRODUCT_UNIT_PRICE, "1.00", "2.00", "3.00")
				.addArrayValues(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.PRODUCT_DISCOUNT, "0.00", "2.00", "3.00");
		return confirmationUdo;
	}

	public static UDO getCustomerUdo() throws UDODefinitionException, UDOUpdateException {
		customerUdo = tealiumHelper.createDefaultUDO(TealiumHelper.PrebuiltUDOPageTypes.CUSTOMER)
				.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_NAME, "custoemr page")
				.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_TYPE, "customer")
				.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.SITE_CURRENCY, "EN")
				.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.SITE_REGION, "US")
				.setValue(TealiumHelper.CustomerPageUDO.PredefinedUDOFields.CUSTOMER_EMAIL, "john.doe@gmail.com")
				.setValue(TealiumHelper.CustomerPageUDO.PredefinedUDOFields.CUSTOMER_ID, "12346231ffb3")
				.setValue(TealiumHelper.CustomerPageUDO.PredefinedUDOFields.CUSTOMER_TYPE, "return");
		return customerUdo;
	}

	public static void setProductData(ProductData product){
		productData = product;
	}
	
	public static void setCurrencyData(CurrencyData currency){
		currencyData = currency;
	}
	
	public static void setLanguageData(LanguageData language){
		languageData = language;
	}

	public static void setSearchData(ProductCategorySearchPageData search){
		searchData = search;
	}
	
	public static void setCategoryName(String category){
		categoryNameString = category;
	}
	
	public static void setCartData(CartData cart){
		cartData = cart;
	}
	
	public static TealiumHelper getTealiumHelper() {
		return tealiumHelper;
	}
	
}
