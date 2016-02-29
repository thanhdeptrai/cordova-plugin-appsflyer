if(!window.CustomEvent) {
	window.CustomEvent =  function(type, config) {
		console.log('CustomEvent', type, config);
	}
	window.CustomEvent2 =  function(type, config) {
		console.log('CustomEvent2', type, config);
	}
}
(function (global) {
	var AppsFlyer;
	AppsFlyer = function () {
	};

	AppsFlyer.prototype.initSdk = function (args) {
    	cordova.exec(null, null, "AppsFlyerPlugin", "initSdk", args);
	};
	
	AppsFlyer.prototype.setCurrencyCode = function (currencyId) {
    	cordova.exec(null, null, "AppsFlyerPlugin", "setCurrencyCode", [currencyId]);
	};
	
	AppsFlyer.prototype.setAppUserId = function (customerUserId) {
    	cordova.exec(null, null, "AppsFlyerPlugin", "setAppUserId", [customerUserId]);
	};

	AppsFlyer.prototype.getAppsFlyerUID = function (callbackFn) {
        cordova.exec(function(result){
            callbackFn(result);
        }, null,
           "AppsFlyerPlugin",
           "getAppsFlyerUID",
        []);
	};
	
	AppsFlyer.prototype.sendTrackingWithEvent = function(eventName, eventValue) {
    	cordova.exec(null, null, "AppsFlyerPlugin", "sendTrackingWithEvent", [eventName,eventValue]);
	};

	AppsFlyer.prototype.trackEvent = function(eventName, eventValue) {
    	cordova.exec(null, null, "AppsFlyerPlugin", "trackEvent", [eventName,eventValue]);
	};

	AppsFlyer.prototype.onInstallConversionDataLoaded = function(conversionData) {
        var data = conversionData,
            event;
        if (typeof data === "string") {
            data = JSON.parse(conversionData);
        }
        if(window.CustomEvent2){
        	event = new CustomEvent2('onInstallConversionDataLoaded', {'detail': data});
        }
        else{
			event = new CustomEvent('onInstallConversionDataLoaded', {'detail': data});
        }
		global.document.dispatchEvent(event);
	};

	global.cordova.addConstructor(function() {
		if (!global.Cordova) {
			global.Cordova = global.cordova;
		};

		if (!global.plugins) {
			global.plugins = {};
		}

		global.plugins.appsFlyer = new AppsFlyer();
	});
}(window));

//document.addEventListener("deviceready", function(){
//    var args = [];
//    var devKey = "xxXXXXXxXxXXXXxXXxxxx8";  // your AppsFlyer devKey
//    args.push(devKey);
//    var userAgent = window.navigator.userAgent.toLowerCase();
//                          
//    if (/iphone|ipad|ipod/.test( userAgent )) {
//        var appId = "123456789";            // your ios app id in app store
//        args.push(appId);
//    }
//	window.plugins.appsFlyer.initSdk(args);
//}, false);

