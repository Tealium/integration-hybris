/**
 *package Tealium Addon - Aspect
 */
package com.tealium.addon.aop;

///todo: test this service after build and dependencies to
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

/*
 * Java stuff
 */
import java.util.*;
import javax.servlet.http.HttpServletRequest;

/*
 * apache stuff
 */
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;  //reference implementation and utilities.
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;



/*
 * spring framework dependencies
 */
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


//import from local plugin work
/*
 * Connect to Facade dependencies for Aspect
 */
import com.tealium.addon.facades.frontend.TealiumAddonDataFacade;
import com.tealium.addob.facades.



/**
 * @author danielWilliamShields
 */
public class TealiumAddonAspect
{

	private static final Logger LOGGER = Logger.getLogger(TealiumAddonAspect.class);




	  private TealiumAddonDataFacade tealiumAddonDataFacade;
	  private BaseStoreService baseStoreService;




	  /*
	   * Tealium Specific Variables and tests for this class
	   */
	  private static final String TEALIUM_UDO = tealiumudo;
	  private static final String TEALIUM_COMMERCE = tealiumcommerce;
	  private static final String TEALIUM_PAGETYPE = tealiumpagetype;
	  private static final String TEALIUM_ISENABLED = tealiumenabled;


	  /*
	   * Spring Framework 'Advice' for Aspect (AOP)
	   * see docs.spring.io/spring/framework/docs/current/spring-framework-reference if needed
	   * Aspect is meant to make the join modular for multiple classes
	   */
	  public Object advise(final ProceedingJoinPoint joinPoint) throws Throwable
	  {
		  //set up servlet leverages RequestContextHolder from Spring Framework API
		  final HttpServletRequest currentRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

		  //bring collected object data back and test values
		  final Object udObject = CollectionUtils.find(args, PredicateUtils.instanceofPredicate(BindingAwareModelMap.class));
		  if(udObject != null){
			              final BindingAwareModelMap model = (BindingAwareModelMap) udObject;
			              if(!model.containsAttribute(TEALIUM_UDO))//conditionally aware object for Tealium
			              {
			            	  final String servletPath
			              }
		  }
	  }

	  /*
	   * function isTealiumEnabled - checks to see if the assignment is made for this technology
	   */
	protected boolean isTealiumEnabled()
	{
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		return currentBaseStore != null && Boolean.TRUE.equals(currentBaseStore.getIfTealiumEnabled())
	}

	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	public TealiumAddonDataFacade getTealiumAddonDataFacade(){
		return tealiumAddonDataFacade;
	}
}
