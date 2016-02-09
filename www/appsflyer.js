if(!window.CustomEvent) {
	window.CustomEvent = function(type, config) {
		var e = document.createEvent("CustomEvent");
		e.initCustomEvent(type, true, true, config.detail);
		return e;
	}
}
(function (global, platform) {
	var AppsFlyer = function () {
	};

	AppsFlyer.prototype.initSdk = function (args) {
    	platform.exec(null, null, "AppsFlyerPlugin", "initSdk", args);
	};
	
	AppsFlyer.prototype.setCurrencyCode = function (currencyId) {
    	platform.exec(null, null, "AppsFlyerPlugin", "setCurrencyCode", [currencyId]);
	};
	
	AppsFlyer.prototype.setAppUserId = function (customerUserId) {
    	platform.exec(null, null, "AppsFlyerPlugin", "setAppUserId", [customerUserId]);
	};

	AppsFlyer.prototype.getAppsFlyerUID = function (callbackFn) {
        platform.exec(function(result){
            callbackFn(result);
        }, null,
           "AppsFlyerPlugin",
           "getAppsFlyerUID",
        []);
	};
	
	AppsFlyer.prototype.sendTrackingWithEvent = function(eventName, eventValue) {
    	platform.exec(null, null, "AppsFlyerPlugin", "sendTrackingWithEvent", [eventName,eventValue]);
	};

	AppsFlyer.prototype.trackEvent = function(eventName, eventValue) {
    	platform.exec(null, null, "AppsFlyerPlugin", "trackEvent", [eventName,eventValue]);
	};

	AppsFlyer.prototype.onInstallConversionDataLoaded = function(conversionData) {
        var data = conversionData,
            event;
        if (typeof data === "string") {
            data = JSON.parse(conversionData);
        }
		event = new CustomEvent('onInstallConversionDataLoaded', {'detail': data});
		global.document.dispatchEvent(event);
	};

	platform.addConstructor(function() {
		if (!global.Cordova) {
			global.Cordova = platform;
		}

		if (!global.plugins) {
			global.plugins = {};
		}

		global.plugins.appsFlyer = new AppsFlyer();
	});
}(window, window.cordova));

