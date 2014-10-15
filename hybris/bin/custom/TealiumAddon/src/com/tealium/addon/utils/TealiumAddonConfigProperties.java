/**
 *
 */
package com.tealium.addon.utils;

/**
 * @author danielWilliamShields
 *
 */
public interface TealiumAddonConfigProperties
{
	//construct settings references for user library constructs
	String TEALIUM_ACCOUNT_ID = "tealium.account.id";
	String TEALIUM_EVENTS_GROUPING_ENABLED = "";


	////a few other updated points of configuratin need to be mapped here.
	////dws - 10/14/2014


	/*
	 * Build Server to Server Communication Channel Configs
	 */
	String TEALIUM_TRANSPORT_SERVER_ENDPOINT = "tealium.transport.server.endpoint";
	String TEALIUM_TRANSPORT_DEFAULT_SERVER_URL = "";//user will have to enter a default for this todo: tealium.


}
