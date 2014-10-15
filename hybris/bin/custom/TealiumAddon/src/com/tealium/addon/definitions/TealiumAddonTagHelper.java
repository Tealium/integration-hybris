/**
 * @author danielWilliamShields
 */
package com.tealium.addon.definitions;

import com.tealium.addon.exception.TealiumAddonException;
import com.tealium.*;


/**
 * @author danielWilliamShields
 */

@SuppressWarnings("unused")
public class TealiumAddonTagHelper
{

	//public variable assignments and declarations
	public static UDO homeUdo;
	public static UDO searchUdo;
	public static UDO categoryUdo;
	public static UDO productUdo;
	public static UDO cartUdo;
	public static UDO confirmationUdo;
	public static UDO customerUdo;

	public static TealiumTagHelper tealiumTagHelper;

	public static void init(){

		//notes here for holding
		tealiumTagHelper = new tealiumTagHelper("")
	}

	/*
	 *
	 */
	public array getParameters()
	{
		//constructs input parameters array
		return null;
	}

	public String buildRequest()
	{
		//builds url request to dynamic endpoint based on input parameters
		return null;
	}

	public Object updateUDO()
	{
		//manages data into and out of object
		return null;
	}


	public String getPageType()
	{
		//provides conventional data about the view state to the internal application layers
		return null;
	}


	public void getClientScript()
	{
		//renders JS code to the application views
		return;
	}

}
