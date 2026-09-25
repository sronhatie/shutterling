welp just looking at how my sony a6300 works with wireshark and its fun.  
and i found out all the api thingies it has. (well almost all) `getVersions` `getMethodTypes` `getApplicationInfo` `getAvailableApiList` `getEvent` `startRecMode` `stopRecMode`  
OH SHOOT WHEN I SEND A REQUEST FOR STARTRECMODE IT ACTIVATED MY CAMERA. So cool. like wowe. scared me.  
welp the `startRecMode` once active it GIVES ME A LOT MORE STUFF AAAAAA cool so cool.
```text
actTakePicture
startLiveview
stopLiveview
actZoom
setSelfTimer
getSelfTimer
getAvailableSelfTimer
getSupportedSelfTimer
setExposureCompensation
getExposureCompensation
getAvailableExposureCompensation
getSupportedExposureCompensation
setShootMode
getShootMode
getAvailableShootMode
getSupportedShootMode
getSupportedFlashMode
```
what ill need most is prolly `startRecMode` `startLiveview` `actTakePicture` `stopLiveview` OH AND SELF TIMERS OFC. like yes. u need a self timer U KNOW  
OH NO ONE HUGE PROBLEM. the file format it shoots at is 24m FINE not RAW :(((  
oh nevermind i just had to change it to raw&jpeg inside the camera control settings. for some reason its diff than my regular camera settings.  
