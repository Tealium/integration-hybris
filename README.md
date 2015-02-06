HybrisIntegration
=================

Hybris Integration for TealiumIQ

This is a simple setup guide to integrate the tealiumiq addon for Hybris. To setup tealiumiq to work with your vendors after successful install, please contact your account manager.

1. Prerequisites
 - A Tealium IQ account.
 - hybris v5.2+ extracted and built, including the Commerce Accelerator.
2. Place the "tealiumiq" directory into ${HYBRIS_BIN}/custom. This directory is in the /hybris/bin/custom/ folder in this repo.
3. Add \<extension dir="${HYBRIS_BIN}/custom/tealiumiq"/\> to your config/localextensions.xml.
4. Add tealiumiq to yacceleratorstorefront by using: 
 - ant addoninstall -Daddonnames="tealiumiq" -DaddonStorefront.yacceleratorstorefront="yacceleratorstorefront"
5. Update: 
 - ${HYBRIS_BIN}/ext-template/yacceleratorstorefront/web/webroot/WEB-INF/tags/desktop/template/master.tag by adding:
   1. \<%@ taglib prefix="tealiumiq" tagdir="/WEB-INF/tags/addons/tealiumiq/shared/analytics" %\> at the top of the file
    2. \<tealiumiq:sync/\> after the \<head\> tag
    3. \<tealiumiq:tealium/\> after the \<body\> tag
 - ${HYBRIS_BIN}/custom/tealiumiq/project.properties.template by changing:
   1. tealiumiq.account, tealiumiq.profile, and tealiumiq.target to your tealiumiq specific information.
    2. modify tealiumiq.utagSyncEnabled to =1 if you want to enable utag.sync.js injection into the \<head\> 
6. Rebuild and restart hybris.
