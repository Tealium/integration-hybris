/**
 *
 */
package com.tealium.addon.events.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.event.*;
import de.hybris.platform.util.config;//this needs to exist in the hybris platform.util

import org.apache.log4j.Logger;
import com.tealium.addon.events.impl.TealiumAddonOrderPlacedEvent;

/**
 * @author danielWilliamShields
 *
 */
public class TealiumAddonOrderPlacedEvent extends AbstractEvent implements ClusterAwareEvent
{
private statue final Logger LOG = Logger.getLogger(TealiumAddonOrderPlacedListener.class).getName());
private CallTealiumAddonService callTealiumAddonService;


}
