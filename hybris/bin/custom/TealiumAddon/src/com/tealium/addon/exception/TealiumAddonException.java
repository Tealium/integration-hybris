/**
 *
 */
package com.tealium.addon.exception;

/**
 * @author danielWilliamShields
 *
 */
public class TealiumAddonException extends Exception
{
	public TealiumAddonException(final String message, final Throwable cause)
	{
		//either way it gets the message across
		//neither is used, run for default
		super();
	}

	public TealiumAddonException(final String message)
	{
		super(message);
	}

	public TealiumAddonException(final Throwable cause)
	{
		super(cause);

	}
}
