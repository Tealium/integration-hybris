package com.tealium.dataconnector.hybris;
import java.util.HashMap;
import java.util.Map;

import com.tealium.dataconnector.hybris.HybrisDataController.HybrisCustomDataConverter;
import com.tealium.dataconnector.hybris.HybrisDataController.HybrisCustomPageTypeCustomData;
import com.tealium.util.udohelpers.UDO;
import com.tealium.util.udohelpers.exceptions.UDOUpdateException;

public class TealiumCustomDataController implements HybrisCustomDataConverter {
	private static Map<String, HybrisCustomPageTypeCustomData> customPagesMap;
	
	@Override
	public Map<String, HybrisCustomPageTypeCustomData> getHybrisCustomPageTypes() {
		
		return customPagesMap;
	}

	@Override
	public UDO homePage(UDO udo) {
		try {
			udo.setValue("page_name", "new home page name");
			udo.setValue("custom_key", "custom_value");
		} catch (UDOUpdateException e) {
			e.printStackTrace();
		}
		return udo;
	}

	@Override
	public UDO genericPage(UDO udo) {
		try {
			udo.setValue("page_name", "new generic page name");
			udo.setValue("custom_key", "custom_value");
		} catch (UDOUpdateException e) {
			e.printStackTrace();
		}
		return udo;
	}

	@Override
	public UDO searchPage(UDO udo) {
		try {
			udo.setValue("page_name", "new search page name");
			udo.setValue("custom_key", "custom_value");
		} catch (UDOUpdateException e) {
			e.printStackTrace();
		}
		return udo;
	}

	@Override
	public UDO categoryPage(UDO udo) {
		try {
			udo.setValue("page_name", "new category page name");
			udo.setValue("custom_key", "custom_value");
		} catch (UDOUpdateException e) {
			e.printStackTrace();
		}
		return udo;
	}

	@Override
	public UDO productPage(UDO udo) {
		try {
			udo.setValue("page_name", "new product page name");
			udo.setValue("custom_key", "custom_value");
		} catch (UDOUpdateException e) {
			e.printStackTrace();
		}
		return udo;
	}

	@Override
	public UDO cartPage(UDO udo) {
		try {
			udo.setValue("page_name", "new cart page name");
			udo.setValue("custom_key", "custom_value");
		} catch (UDOUpdateException e) {
			e.printStackTrace();
		}
		return udo;
	}

	@Override
	public UDO orderConfirmationPage(UDO udo) {
		try {
			udo.setValue("page_name", "new confirmation page name");
			udo.setValue("custom_key", "custom_value");
		} catch (UDOUpdateException e) {
			e.printStackTrace();
		}
		return udo;
	}

	@Override
	public UDO customerDetailPage(UDO udo) {
		try {
			udo.setValue("page_name", "new customer page name");
			udo.setValue("custom_key", "custom_value");
		} catch (UDOUpdateException e) {
			e.printStackTrace();
		}
		return udo;
	}

	@Override
	public void addCustomPages() {
		if (customPagesMap == null){
			customPagesMap = new HashMap<>();
		}
		customPagesMap.put("custom_one", new HybrisCustomPageTypeCustomData(){

			@Override
			public UDO getCustomDataUdo(UDO udo) {
				try {
					udo.setValue("page_name", "custom page 1");
					udo.setValue("custom_page1_key", "custom value");
				} catch (UDOUpdateException e) {
					e.printStackTrace();
				}
				return udo;
			}
			
		});	
		
		customPagesMap.put("custom_two", new HybrisCustomPageTypeCustomData(){

			@Override
			public UDO getCustomDataUdo(UDO udo) {
				try {
					udo.setValue("page_name", "custom page 2");
					udo.setValue("custom_page2_key", "custom value");
				} catch (UDOUpdateException e) {
					e.printStackTrace();
				}
				return udo;
			}
			
		});		
	}



}
