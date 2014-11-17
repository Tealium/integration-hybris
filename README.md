HybrisIntegration
=================

Hybris Integration for TealiumIQ

This is a simple setup guide to integrate teh tealiumIQ addon for Hybris. To setup tealiumIQ to work with your vendors after successful install, please contact your account manager.

1. Prerequisites
 - A Tealium IQ account.
 - hybris v5.2+ extracted and built, including the Commerce Accelerator.
2. Place the "tealiumIQ" directory into ${HYBRIS_BIN}/custom. This directory is in the /hybris/bin/custom/ folder.
3. Add <extension dir="${HYBRIS_BIN}/custom/tealiumIQ"/> to your config/localextensions.xml.
4. Add tealiumIQ to yacceleratorstorefront by using: 
 - ant addoninstall -Daddonnames="tealiumIQ" -DaddonStorefront.yacceleratorstorefront="yacceleratorstorefront"
5. Update: 
 - ${HYBRIS_BIN}/ext-template/yacceleratorstorefront/web/webroot/WEB-INF/tags/desktop/template/master.tag by adding:
   1. \<%@ taglib prefix="tealiumIQ" tagdir="/WEB-INF/tags/addons/tealiumIQ/shared/analytics" %\> at the top of the file
    2. \<tealiumIQ:tealium/\> after the \<body\> tag
 - ${HYBRIS_BIN}/ext-template/yacceleratorstorefront/web/webroot/WEB-INF/tags/addons/tealiumIQ/shared/analytics/data.tag by changing:
   1. accountString, profileString, and targetString to your tealiumIQ specific information.
6. Recompile and restart hybris.
