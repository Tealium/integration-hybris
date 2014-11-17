HybrisIntegration
=================

Hybris Integration for TealiumIQ

This is a simple setup guide to integrate teh tealiumIQ addon for Hybris. To setup tealiumIQ to work with your vendors after successful install, please contact your account manager.
0. Prerequisites
a. A Tealium IQ account.
b. hybris v5.2+ extracted and built, including the Commerce Accelerator.
1. Place the "tealiumIQ" directory into ${HYBRIS_BIN}/custom. This directory is in the /hybris/bin/custom/ folder.
2. Add <extension dir="${HYBRIS_BIN}/custom/tealiumIQ"/> to your config/localextensions.xml.
3. Add tealiumIQ to yacceleratorstorefront by using: 
b. ant addoninstall -Daddonnames="tealiumIQ" -DaddonStorefront.yacceleratorstorefront="yacceleratorstorefront"
4. Update: 
a. ${HYBRIS_BIN}/ext-template/yacceleratorstorefront/web/webroot/WEB-INF/tags/desktop/template/master.tag by adding:
1. <%@ taglib prefix="tealiumIQ" tagdir="/WEB-INF/tags/addons/tealiumIQ/shared/analytics" %> at the top of the file
2. <tealiumIQ:tealium/> after the <body> tag
b. ${HYBRIS_BIN}/ext-template/yacceleratorstorefront/web/webroot/WEB-INF/tags/addons/tealiumIQ/shared/analytics/data.tag by changing:
1. accountString, profileString, and targetString to your tealiumIQ specific information.
5. Recompile and restart hybris.
