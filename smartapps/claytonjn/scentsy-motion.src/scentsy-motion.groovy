/**
 *  Scentsy Motion
 *
 *  Copyright 2016 Clayton Nummer
 *
 */
definition(
    name: "Scentsy Motion",
    namespace: "claytonjn",
    author: "Clayton Nummer",
    description: "Turn on Scentsy(s) when there's motion.",
    category: "My Apps",
    iconUrl: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn.png",
    iconX2Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@3x.png")


preferences {
	page(name: "page", install: true, uninstall: true) {
        section("Preferences") {
            paragraph "Turn on Scentsy(s) when there's motion."
            input "scentsys", "capability.switch", title: "Scentsy(s)", multiple: true
            input "motions", "capability.motionSensor", title: "Motion Sensor(s)", multiple: true
            input "minutes", "number", title: "Minutes without motion before turning off Scentsy(s)"
            mode(title: "Set for specific mode(s)")
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
	subscribe(settings.motions, "motion", motionHandler)
}

void motionHandler(evt) {
	if (location.mode in ["Home"]){
        if (evt.value == "active") {
            unschedule(scentsysOff)
            settings.scentsys?.on()
        } else if (evt.value == "inactive") {
            runIn(60*settings.minutes, scentsysOff)
        }
    }
}

void scentsysOff() {
	settings.scentsys?.off()
}