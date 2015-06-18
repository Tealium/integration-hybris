package com.tealium.dataconnector.hybris;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.Date;

import com.tealium.util.udohelpers.TealiumHelper;
import com.tealium.util.udohelpers.TealiumHelper.UDOOptions;
import com.tealium.util.udohelpers.TealiumHelper.PrebuiltUDOPageTypes;
import com.tealium.util.udohelpers.UDO;
import com.tealium.util.udohelpers.exceptions.UDODefinitionException;
import com.tealium.util.udohelpers.exceptions.UDOUpdateException;
import com.tealium.addon.jalo.TealiumIQManager;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.storesession.data.CurrencyData;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.util.Config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.apache.log4j.Logger;

public class HybrisDataController {
	static Map<String, HybrisCustomDataConverter> customDataClassesMap;

	interface HybrisCustomPageTypeCustomData {
		UDO getCustomDataUdo(UDO udo);
	}

	interface HybrisCustomDataConverter {
		Map<String, HybrisCustomPageTypeCustomData> getHybrisCustomPageTypes();

		UDO homePage(UDO udo);

		UDO genericPage(UDO udo);

		UDO searchPage(UDO udo);

		UDO categoryPage(UDO udo);

		UDO productPage(UDO udo);

		UDO cartPage(UDO udo);

		UDO orderConfirmationPage(UDO udo);

		UDO customerDetailPage(UDO udo);

		void addCustomPages();
	}

	public static void registerCustomDataClass(String id,
			HybrisCustomDataConverter customDataClass) {
		if (customDataClassesMap == null) {
			customDataClassesMap = new HashMap<>();
		}
		customDataClassesMap.put(id, customDataClass);
	}

	public static void unregisterCustomDataClass(String id) {
		if (customDataClassesMap != null) {
			customDataClassesMap.remove(id);
		}
	}

	static UDO extendUdo(String pageType, UDO udo) {
		if (customDataClassesMap != null) {
			for (String customDataClassKey : customDataClassesMap.keySet()) {
				HybrisCustomDataConverter customDataClass = customDataClassesMap
						.get(customDataClassKey);
				customDataClass.addCustomPages();
				switch (pageType) {
				case "home":
					udo = customDataClass.homePage(udo);
					break;
				case "generic":
					udo = customDataClass.genericPage(udo);
					break;
				case "search":
					udo = customDataClass.searchPage(udo);
					break;
				case "category":
					udo = customDataClass.categoryPage(udo);
					break;
				case "product":
					udo = customDataClass.productPage(udo);
					break;
				case "cart":
					udo = customDataClass.cartPage(udo);
					break;
				case "confirmation":
					udo = customDataClass.orderConfirmationPage(udo);
					break;
				case "customer":
					udo = customDataClass.customerDetailPage(udo);
					break;
				default:
					Map<String, HybrisCustomPageTypeCustomData> customPageMap = customDataClass
							.getHybrisCustomPageTypes();
					if (customPageMap != null) {
						udo = getCustomPageUDO(pageType, customPageMap, udo);
					}
					break;
				}
			}
		}
		return udo;
	}

	static UDO extendUdo(PrebuiltUDOPageTypes pageType, UDO udo) {
		return extendUdo(pageType.name().toLowerCase(), udo);
	}

	static UDO getCustomPageUDO(String pageType,
			Map<String, HybrisCustomPageTypeCustomData> customData, UDO udo) {
		HybrisCustomPageTypeCustomData customDataRunnable = customData
				.get(pageType);
		if (customDataRunnable != null) {
			return customDataRunnable.getCustomDataUdo(udo);
		}
		return udo;
	}

	public HybrisDataController() {
	}

	static UDO setupUDO(TealiumHelper tealiumHelper,
			PrebuiltUDOPageTypes pageType) throws UDODefinitionException,
			UDOUpdateException {
		tealiumHelper.assumePageTypeUDO("global").mayHaveStringFields(
				EnumSet.of(UDOOptions.WRITE_IF_EMPTY_OR_NULL,
						UDOOptions.REQUIRED), "page_name", "site_currency",
				"site_region");

		HttpServletRequest request = getRequest();
		UDO udo = tealiumHelper.createDefaultUDO(pageType);
		udo.getPageType().includesFieldsFromPageType("global");

		CurrencyData currencyData = (CurrencyData) request
				.getAttribute("currentCurrency");
		LanguageData languageData = (LanguageData) request
				.getAttribute("currentLanguage");
		if (currencyData.getIsocode() != null) {
			String siteCurrency = currencyData.getIsocode();
			udo.setValue("site_currency", siteCurrency);
		}
		if (languageData.getIsocode() != null) {
			String siteLanguage = languageData.getIsocode();
			udo.setValue("site_region", siteLanguage);
		}

		String pageNameString;
		String pageTypeString = (String) request.getAttribute("pageType");
		if (pageTypeString != null) {
			pageNameString = (pageTypeString).toLowerCase();
		} else {
			pageNameString = (((ContentPageModel) request
					.getAttribute("cmsPage")).getLabel()).toLowerCase();
		}
		udo.setValue("page_name", pageNameString);

		return udo;
	}

	static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes()).getRequest();
	}

	static TealiumHelper setupTealiumHelper() throws UDODefinitionException,
			UDOUpdateException {
		String accountString = Config.getParameter("tealiumiqaddon.account");
		String profileString = Config.getParameter("tealiumiqaddon.profile");
		String targetString = Config.getParameter("tealiumiqaddon.target");
		;
		return new TealiumHelper(accountString, profileString, targetString);
	}

	public static String getSyncTag() {
		String scriptString = "";
		try {
			String utagSyncEnabledString = Config
					.getParameter("tealiumiqaddon.utagSyncEnabled");
			if (utagSyncEnabledString == "1") {
				TealiumHelper helper = setupTealiumHelper();
				scriptString = helper.outputUtagSyncJsTag();
			}
		} catch (Exception e) {
			scriptString = getExceptionString(e);
		}

		return scriptString;
	}

	public static String getHomeScript() throws UDODefinitionException,
			UDOUpdateException {
		String scriptString = "";
		try {
			TealiumHelper tealiumHelper = setupTealiumHelper();
			UDO udo = setupUDO(tealiumHelper, PrebuiltUDOPageTypes.HOME);

			udo.setValue(
					TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_TYPE,
					"home");
			udo = extendUdo(PrebuiltUDOPageTypes.HOME, udo);
			scriptString = tealiumHelper.outputFullHtml(udo);
		} catch (Exception e) {
			scriptString = getExceptionString(e);
		}

		return scriptString;
	}

	public static String getGenericPageScript() throws UDODefinitionException,
			UDOUpdateException {
		String scriptString = "";
		try {
			TealiumHelper tealiumHelper = setupTealiumHelper();
			UDO udo = setupUDO(tealiumHelper, PrebuiltUDOPageTypes.HOME);

			udo.setValue(
					TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_TYPE,
					"generic");
			udo = extendUdo("generic", udo);
			scriptString = tealiumHelper.outputFullHtml(udo);
		} catch (Exception e) {
			scriptString = getExceptionString(e);
		}

		return scriptString;
	}

	public static String getCustomPageScript(String pageType)
			throws UDODefinitionException, UDOUpdateException {
		String scriptString = "";
		try {
			TealiumHelper tealiumHelper = setupTealiumHelper();
			UDO udo = setupUDO(tealiumHelper, PrebuiltUDOPageTypes.HOME);

			udo.setValue(
					TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_TYPE,
					pageType);
			udo = extendUdo(pageType, udo);
			scriptString = tealiumHelper.outputFullHtml(udo);
		} catch (Exception e) {
			scriptString = getExceptionString(e);
		}

		return scriptString;
	}

	public static String getSearchPageScript() throws UDODefinitionException,
			UDOUpdateException {
		String scriptString = "";
		try {
			TealiumHelper tealiumHelper = setupTealiumHelper();
			UDO udo = setupUDO(tealiumHelper, PrebuiltUDOPageTypes.SEARCH);

			HttpServletRequest request = getRequest();
			ProductCategorySearchPageData searchData = (ProductCategorySearchPageData) request
					.getAttribute("searchPageData");

			udo.setValue(
					TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_TYPE,
					"search");
			if (searchData != null) {
				String searchKeyword = searchData.getFreeTextSearch();
				if (searchKeyword != null && searchKeyword.length() > 0) {
					udo.setValue(
							TealiumHelper.SearchPageUDO.PredefinedUDOFields.SEARCH_KEYWORD,
							searchKeyword);
				}
				String searchResults = ((PaginationData) searchData
						.getPagination()).getTotalNumberOfResults() + "";
				if (searchResults != null && searchResults.length() > 0) {
					udo.setValue(
							TealiumHelper.SearchPageUDO.PredefinedUDOFields.SEARCH_RESULTS,
							searchResults);
				}

			}
			udo = extendUdo(PrebuiltUDOPageTypes.SEARCH, udo);
			scriptString = tealiumHelper.outputFullHtml(udo);
		} catch (Exception e) {
			scriptString = getExceptionString(e);
		}

		return scriptString;
	}

	public static String getCategoryScript() throws UDODefinitionException,
			UDOUpdateException {
		String scriptString = "";
		try {
			TealiumHelper tealiumHelper = setupTealiumHelper();
			UDO udo = setupUDO(tealiumHelper, PrebuiltUDOPageTypes.CATEGORY);

			HttpServletRequest request = getRequest();
			String categoryNameString = (String) request
					.getAttribute("categoryName");

			udo.setValue(
					TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_TYPE,
					"product");
			if (categoryNameString != null && categoryNameString.length() > 0) {
				udo.setValue(
						TealiumHelper.CategoryPageUDO.PredefinedUDOFields.PAGE_CATEGORY_NAME,
						categoryNameString);
			}
			udo = extendUdo(PrebuiltUDOPageTypes.CATEGORY, udo);
			scriptString = tealiumHelper.outputFullHtml(udo);
		} catch (Exception e) {
			scriptString = getExceptionString(e);
		}

		return scriptString;
	}

	public static String getProductPageScript() throws UDODefinitionException,
			UDOUpdateException {
		String scriptString = "";
		try {
			TealiumHelper tealiumHelper = setupTealiumHelper();
			UDO udo = setupUDO(tealiumHelper, PrebuiltUDOPageTypes.PRODUCT);

			HttpServletRequest request = getRequest();
			ProductData productData = (ProductData) request
					.getAttribute("product");
			List<String> productCategoryList = new ArrayList<String>();
			String productCategory = "";
			String productBrand = "";
			String productSku = "";
			String productPrice = "";
			String productName = "";

			if (productData != null) {
				for (CategoryData category : productData.getCategories()) {
					productCategoryList.add(category.getName());
				}
			}
			Object[] productCategoryStrings = productCategoryList.toArray();
			if (productCategoryStrings.length > 0) {
				productCategory = (String) productCategoryStrings[0];
			}
			if (productCategoryStrings.length >= 2) {
				productBrand = (String) productCategoryStrings[1];
			}
			if (productData != null) {
				String productCodeString = productData.getCode();
				if (productCodeString != null) {
					productSku = productCodeString;
				}
				PriceData priceData = (PriceData) productData.getPrice();
				if (priceData != null) {
					productPrice = priceData.getValue().toPlainString();
				}
				String productNameData = productData.getName();
				if (productNameData != null) {
					productName = productNameData;
				}
			}

			udo.setValue(
					TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_TYPE,
					"product")
					.addArrayValues(
							TealiumHelper.ProductPageUDO.PredefinedUDOFields.PRODUCT_BRAND,
							Arrays.asList(productBrand))
					.addArrayValues(
							TealiumHelper.ProductPageUDO.PredefinedUDOFields.PRODUCT_CATEGORY,
							Arrays.asList(productCategory))
					.addArrayValues(
							TealiumHelper.ProductPageUDO.PredefinedUDOFields.PRODUCT_ID,
							Arrays.asList(""))
					.addArrayValues(
							TealiumHelper.ProductPageUDO.PredefinedUDOFields.PRODUCT_LIST_PRICE,
							Arrays.asList(productPrice))
					.addArrayValues(
							TealiumHelper.ProductPageUDO.PredefinedUDOFields.PRODUCT_NAME,
							Arrays.asList(productName))
					.addArrayValues(
							TealiumHelper.ProductPageUDO.PredefinedUDOFields.PRODUCT_SKU,
							Arrays.asList(productSku))
					.addArrayValues(
							TealiumHelper.ProductPageUDO.PredefinedUDOFields.PRODUCT_UNIT_PRICE,
							Arrays.asList(""));

			udo = extendUdo(PrebuiltUDOPageTypes.PRODUCT, udo);
			scriptString = tealiumHelper.outputFullHtml(udo);
		} catch (Exception e) {
			scriptString = getExceptionString(e);
		}

		return scriptString;
	}

	public static String getCartScript() throws UDODefinitionException,
			UDOUpdateException {
		String scriptString = "";
		try {
			TealiumHelper tealiumHelper = setupTealiumHelper();
			UDO udo = setupUDO(tealiumHelper, PrebuiltUDOPageTypes.CART);

			HttpServletRequest request = getRequest();
			CartData cartData = (CartData) request.getAttribute("cartData");

			List<String> productBrandList = new ArrayList<String>();
			List<String> productCategoryList = new ArrayList<String>();
			List<String> productIdList = new ArrayList<String>();
			List<String> productListPriceList = new ArrayList<String>();
			List<String> productNameList = new ArrayList<String>();
			List<String> productQuantityList = new ArrayList<String>();
			List<String> productSkuList = new ArrayList<String>();
			List<String> productUnitPriceList = new ArrayList<String>();
			if (cartData != null) {
				if (cartData.getTotalPrice() != null) {
					String cartTotal = (String) ((PriceData) cartData
							.getTotalPrice()).getValue().toPlainString();
					udo.setValue("cart_total", cartTotal);
				}
				for (OrderEntryData entry : cartData.getEntries()) {
					String sku = ((ProductData) entry.getProduct()).getCode();
					String name = ((ProductData) entry.getProduct()).getName();
					String quantity = entry.getQuantity() + "";
					String basePrice = ((PriceData) entry.getBasePrice())
							.getValue().toPlainString();

					List<String> categoryList = new ArrayList<String>();
					for (CategoryData thisCategory : ((ProductData) entry
							.getProduct()).getCategories()) {
						categoryList.add(thisCategory.getName());
					}
					Object[] categoryStrings = categoryList.toArray();
					String category = "";
					if (categoryStrings.length > 0) {
						category = (String) categoryStrings[0];
					}
					String brand = "";
					if (categoryStrings.length >= 2) {
						brand = (String) categoryStrings[1];
					}

					productBrandList.add(brand);
					productCategoryList.add(category);
					productIdList.add("");
					productListPriceList.add(basePrice);
					productNameList.add(name);
					productQuantityList.add(quantity);
					productSkuList.add(sku);
					productUnitPriceList.add("");
				}
			}

			udo.setValue(
					TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_TYPE,
					"checkout");
			if (productBrandList.size() > 0) {
				udo.addArrayValues(
						TealiumHelper.CartPageUDO.PredefinedUDOFields.PRODUCT_BRAND,
						productBrandList);
			}
			if (productCategoryList.size() > 0) {
				udo.addArrayValues(
						TealiumHelper.CartPageUDO.PredefinedUDOFields.PRODUCT_CATEGORY,
						productCategoryList);
			}
			if (productIdList.size() > 0) {
				udo.addArrayValues(
						TealiumHelper.CartPageUDO.PredefinedUDOFields.PRODUCT_ID,
						productIdList);
			}
			if (productListPriceList.size() > 0) {
				udo.addArrayValues(
						TealiumHelper.CartPageUDO.PredefinedUDOFields.PRODUCT_LIST_PRICE,
						productListPriceList);
			}
			if (productNameList.size() > 0) {
				udo.addArrayValues(
						TealiumHelper.CartPageUDO.PredefinedUDOFields.PRODUCT_NAME,
						productNameList);
			}
			if (productQuantityList.size() > 0) {
				udo.addArrayValues(
						TealiumHelper.CartPageUDO.PredefinedUDOFields.PRODUCT_QUANTITY,
						productQuantityList);
			}
			if (productSkuList.size() > 0) {
				udo.addArrayValues(
						TealiumHelper.CartPageUDO.PredefinedUDOFields.PRODUCT_SKU,
						productSkuList);
			}
			if (productUnitPriceList.size() > 0) {
				udo.addArrayValues(
						TealiumHelper.CartPageUDO.PredefinedUDOFields.PRODUCT_UNIT_PRICE,
						productUnitPriceList);
			}

			udo = extendUdo(PrebuiltUDOPageTypes.CART, udo);
			scriptString = tealiumHelper.outputFullHtml(udo);
		} catch (Exception e) {
			scriptString = getExceptionString(e);
		}

		return scriptString;
	}

	public static String getOrderConfirmationScript()
			throws UDODefinitionException, UDOUpdateException {
		String scriptString = "";
		try {
			TealiumHelper tealiumHelper = setupTealiumHelper();
			UDO udo = setupUDO(tealiumHelper, PrebuiltUDOPageTypes.CONFIRMATION);

			HttpServletRequest request = getRequest();
			CCPaymentInfoData ccInfo = (CCPaymentInfoData) request
					.getAttribute("paymentInfo");
			OrderData orderData = (OrderData) request.getAttribute("orderData");
			CurrencyData currencyData = (CurrencyData) request
					.getAttribute("currentCurrency");
			if (currencyData != null) {
				String siteCurrency = currencyData.getIsocode();
				if (siteCurrency != null) {
					udo.setValue(
							TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.ORDER_CURRENCY,
							siteCurrency);
				}
			}
			if (orderData != null) {
				String orderIDString = orderData.getCode();
				PriceData totalPriceData = (PriceData) orderData
						.getTotalPrice();
				PriceData deliveryCostData = (PriceData) orderData
						.getDeliveryCost();
				PriceData taxData = (PriceData) orderData.getTotalPrice();
				PriceData discountsData = (PriceData) orderData
						.getTotalDiscounts();
				PriceData subTotalData = (PriceData) orderData.getSubTotal();
				if (orderIDString != null) {
					udo.setValue(
							TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.ORDER_ID,
							orderIDString);
				}
				if (totalPriceData != null) {
					String orderTotal = (String) totalPriceData.getValue()
							.toPlainString();
					udo.setValue(
							TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.ORDER_TOTAL,
							orderTotal);
				}
				if (deliveryCostData != null) {
					String deliveryCost = (String) deliveryCostData.getValue()
							.toPlainString();
					udo.setValue(
							TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.ORDER_SHIPPING,
							deliveryCost);
				}
				if (taxData != null) {
					String totalTax = (String) taxData.getValue()
							.toPlainString();
					udo.setValue(
							TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.ORDER_TAX,
							totalTax);
				}
				if (discountsData != null) {
					String totalDiscounts = (String) discountsData.getValue()
							.toPlainString();
					udo.setValue(
							TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.ORDER_DISCOUNT,
							totalDiscounts);
				}
				if (subTotalData != null) {
					String subTotal = (String) subTotalData.getValue()
							.toPlainString();
					udo.setValue(
							TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.ORDER_SUBTOTAL,
							subTotal);
				}
			}
			if (ccInfo != null) {
				String paymentType = ccInfo.getCardType();
				if (paymentType != null) {
					udo.setValue(
							TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.ORDER_PAYMENT_TYPE,
							paymentType);
				}
			}
			String email = (String) request.getAttribute("email");
			if (email != null) {
				udo.setValue(
						TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.CUSTOMER_EMAIL,
						email);
			}

			List<String> productBrandList = new ArrayList<String>();
			List<String> productCategoryList = new ArrayList<String>();
			List<String> productIdList = new ArrayList<String>();
			List<String> productListPriceList = new ArrayList<String>();
			List<String> productNameList = new ArrayList<String>();
			List<String> productQuantityList = new ArrayList<String>();
			List<String> productSkuList = new ArrayList<String>();
			List<String> productUnitPriceList = new ArrayList<String>();
			List<String> productDiscountList = new ArrayList<String>();
			if (orderData != null) {
				for (OrderEntryData entry : orderData.getEntries()) {
					String sku = ((ProductData) entry.getProduct()).getCode();
					String name = ((ProductData) entry.getProduct()).getName();
					String quantity = entry.getQuantity() + "";
					String basePrice = ((PriceData) entry.getBasePrice())
							.getValue().toPlainString();

					List<String> categoryList = new ArrayList<String>();
					for (CategoryData thisCategory : ((ProductData) entry
							.getProduct()).getCategories()) {
						categoryList.add(thisCategory.getName());
					}
					Object[] categoryStrings = categoryList.toArray();
					String category = "";
					String brand = "";
					if (categoryStrings.length > 0) {
						category = (String) categoryStrings[0];
					}
					if (categoryStrings.length >= 2) {
						brand = (String) categoryStrings[1];
					}

					productBrandList.add(brand);
					productCategoryList.add(category);
					productIdList.add("");
					productListPriceList.add(basePrice);
					productNameList.add(name);
					productQuantityList.add(quantity);
					productSkuList.add(sku);
					productUnitPriceList.add("");
					productDiscountList.add("");
				}
			} else {
				productBrandList.add("");
				productCategoryList.add("");
				productIdList.add("");
				productListPriceList.add("");
				productNameList.add("");
				productQuantityList.add("");
				productSkuList.add("");
				productUnitPriceList.add("");
				productDiscountList.add("");
			}
			udo.setValue(
					TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_TYPE,
					"checkout")
					.addArrayValues(
							TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.PRODUCT_BRAND,
							productBrandList)
					.addArrayValues(
							TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.PRODUCT_ID,
							productIdList)
					.addArrayValues(
							TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.PRODUCT_LIST_PRICE,
							productListPriceList)
					.addArrayValues(
							TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.PRODUCT_NAME,
							productNameList)
					.addArrayValues(
							TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.PRODUCT_QUANTITY,
							productQuantityList)
					.addArrayValues(
							TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.PRODUCT_SKU,
							productSkuList)
					.addArrayValues(
							TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.PRODUCT_UNIT_PRICE,
							productUnitPriceList)
					.addArrayValues(
							TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.PRODUCT_DISCOUNT,
							productDiscountList)
					.addArrayValues(
							TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.PRODUCT_CATEGORY,
							productCategoryList);

			udo = extendUdo(PrebuiltUDOPageTypes.CONFIRMATION, udo);
			scriptString = tealiumHelper.outputFullHtml(udo);
		} catch (Exception e) {
			scriptString = getExceptionString(e);
		}

		return scriptString;
	}

	public static String getCustomerDetailScript()
			throws UDODefinitionException, UDOUpdateException {
		String scriptString = "";
		try {
			TealiumHelper tealiumHelper = setupTealiumHelper();
			UDO udo = setupUDO(tealiumHelper, PrebuiltUDOPageTypes.CUSTOMER);

			HttpServletRequest request = getRequest();
			CustomerData customerData = (CustomerData) request
					.getAttribute("customerData");
			if (customerData != null) {
				String customerEmailString = customerData.getDisplayUid();
				if (customerEmailString != null) {
					udo.setValue(
							TealiumHelper.CustomerPageUDO.PredefinedUDOFields.CUSTOMER_EMAIL,
							customerEmailString);
				}
				StringBuilder nameString = new StringBuilder("");

				if (customerData.getFirstName() != null) {
					nameString.append(customerData.getFirstName());
				}
				if (customerData.getLastName() != null) {
					if (nameString.toString().length() > 0) {
						nameString.append(" ");
					}
					nameString.append(customerData.getLastName());
				}
				if (nameString.toString() != "") {
					udo.setValue("customer_name", nameString.toString());
				}
				if (customerData.getTitleCode() != null) {
					String genderString = "unknown";
					if (Pattern.matches("^mr$", customerData.getTitleCode())) {
						genderString = "male";
					} else if (Pattern.matches("s$",
							customerData.getTitleCode())) {
						genderString = "female";
					}
					udo.setValue("gender", genderString);
				}
			}
			udo.setValue(
					TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_TYPE,
					"customer");

			udo = extendUdo(PrebuiltUDOPageTypes.CUSTOMER, udo);
			scriptString = tealiumHelper.outputFullHtml(udo);
		} catch (Exception e) {
			scriptString = getExceptionString(e);
		}

		return scriptString;
	}

	static String getExceptionString(Exception e) {
		Date date = new Date();
		String referenceIDString = date.hashCode() + "";
		Logger tealiumLog = Logger.getLogger(TealiumIQManager.class.getName());
		tealiumLog.error((new StringBuilder()).append("Tealium error ID: ")
				.append(referenceIDString), e);

		StringBuilder scriptBuilder = new StringBuilder();
		scriptBuilder.append("<!--  Tealium ERROR \n");
		scriptBuilder
				.append("There may be an error in your installation, please check you logging.");
		scriptBuilder.append("\n");
		scriptBuilder.append("Log refernce ID: ");
		scriptBuilder.append(referenceIDString);
		scriptBuilder.append("\n\t\t  END Tealium ERROR -->");
		return scriptBuilder.toString();
	}
}
