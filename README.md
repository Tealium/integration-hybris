HybrisIntegration
=================

Hybris Integration for TealiumIQ

This is a simple setup guide to integrate the tealiumiqaddon addon for Hybris. To setup tealiumiqaddon to work with your vendors after successful install, please contact your account manager.

1. Prerequisites
 - A Tealium IQ account.
 - hybris v5.2+ extracted and built, including the Commerce Accelerator.
2. Place the "tealiumiqaddon" directory into ${HYBRIS_BIN}/custom. This directory is in the /hybris/bin/custom/ folder in this repo.
3. Add \<extension dir="${HYBRIS_BIN}/custom/tealiumiqaddon"/\> to your config/localextensions.xml.
4. Add tealiumiqaddon to yacceleratorstorefront by using: 
 - ant addoninstall -Daddonnames="tealiumiqaddon" -DaddonStorefront.yacceleratorstorefront="yacceleratorstorefront"
5. Update: 
 - ${HYBRIS_BIN}/ext-template/yacceleratorstorefront/web/webroot/WEB-INF/tags/desktop/template/master.tag by adding:
   1. \<%@ taglib prefix="tealiumiqaddon" tagdir="/WEB-INF/tags/addons/tealiumiqaddon/shared/analytics" %\> at the top of the file
    2. \<tealiumiqaddon:sync/\> after the \<head\> tag
    3. \<tealiumiqaddon:tealium/\> after the \<body\> tag
 - ${HYBRIS_BIN}/custom/tealiumiqaddon/project.properties.template by changing:
   1. tealiumiqaddon.account, tealiumiqaddon.profile, and tealiumiqaddon.target to your tealiumiqaddon specific information.
    2. modify tealiumiqaddon.utagSyncEnabled to =1 if you want to enable utag.sync.js injection into the \<head\> 
6. Rebuild and restart hybris.
