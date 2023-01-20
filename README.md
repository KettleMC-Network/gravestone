# Gravestone Mod (1.7.10 Backport)
Places a gravestone with your inventory items inside when you die.

This fork backports features from newer versions to 1.7.10. It is currently used on the [KettleMC.net](https://KettleMC.net) server.


## Changes
- Fix serverside crash when no grave location was found
- ~~Fix 1.7.10 building~~ ([merged into upstream by now](https://github.com/henkelmax/gravestone/pull/125))
- Fix Grave creation upon death above build limit
- Backport GraveUtils from the latest version
- Fix code formatting and refactored old code
- Backported GraveUtils (internal)
- Backport sneaking feature
