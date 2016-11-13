/**
 *  Night Humidity
 *
 *  Copyright 2016 Clayton Nummer
 *
 */
definition(
    name: "Night Humidity",
    namespace: "claytonjn",
    author: "Clayton Nummer",
    description: "Controls a Humidifier to maintain 50% RH at night.",
    category: "My Apps",
    iconUrl: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn.png",
    iconX2Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@3x.png")


preferences {
	page(name: "page", install: true, uninstall: true) {
        section("Preferences") {
        	paragraph "Controls a Humidifier to maintain 50% RH at night."
            input "humiditys", "capability.relativeHumidityMeasurement", title: "Humidy Sensor(s)", multiple: true
            input "humidifiers", "capability.switch", title: "Humidifier(s)", multiple: true
            label title: "Assign a name", required: false
        }
    }
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	subscribe(settings.humiditys, "humidity", evtHandler)
    subscribe(location, "mode", evtHandler)
}

void evtHandler(evt) {
    if (location.mode in ["Night"]) {
        def avgHumidity = 0
        for (humidity in settings.humiditys) {
            avgHumidity += humidity.currentValue("humidity")
        }
        avgHumidity = avgHumidity / settings.humiditys.size()
        if (avgHumidity < 50) {
            settings.humidifiers?.on()
        }
    }
    else {
    	settings.humidifiers?.off()
    }
}