/**
 *  Detected Smoke/CO
 *
 *  Copyright 2016 Clayton Nummer
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Detected Smoke/CO",
    namespace: "claytonjn",
    author: "claytonjn",
    description: "Unlocks doors, turns off thermostat, and sends notifications when Smoke or CO is detected.",
    category: "My Apps",
    iconUrl: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn.png",
    iconX2Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@3x.png")

preferences {
	page(name: "page", install: true, uninstall: true) {
        section("Title") {
            paragraph "Unlocks doors, turns off thermostat, and sends notifications when CO is detected."
            input "smokeDetectors", "capability.smokeDetector", title: "Smoke Detector(s)", multiple: true, required: false
            input "coDetectors", "capability.carbonMonoxideDetector", title: "CO Detector(s)", multiple: true, required: false
            input "coAlert", "capability.switch", title: "CO Alert", multiple: false, required: false
            input "locks", "capability.lock", title: "Door Lock(s)", multiple: true, required: false
            input "thermostats", "capability.thermostat", title: "Thermostat(s)", multiple: true, required: false
            input "switches", "capability.switch", title: "Light(s)", multiple: true, required: false
            input("recipients", "contact", title: "Send notifications to", multiple: true, required: false)
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
	subscribe(settings.smokeDetectors, "smoke.detected", smokeHandler)
	subscribe(settings.coDetectors, "carbonMonoxide.detected", coHandler)
}

void smokeHandler(evt) {
	evtHandler("Smoke", evt)
}

void coHandler(evt) {
	settings.coAlert.on()
    evtHandler("Carbon Monoxide", evt)
}

void evtHandler(type, evt) {
	settings.thermostats?.off()
    
    // check that contact book is enabled and recipients selected
    if (location.contactBookEnabled && settings.recipients) {
        sendNotificationToContacts("${type} detected at ${evt.displayName}!", settings.recipients)
    } else {
        sendPush("${type} detected at ${evt.displayName}!")
        def phones = ["3134057084", "3135493545", "2484108350", "2482078762", "3134029668", "3134029660"]
        for (phone in phones) { sendSmsMessage(phone, "${type} detected at ${evt.displayName}!") }
    }
    
    settings.locks?.unlock()
    
    if (type == "Carbon Monoxide") {
    	for (light in settings.switches) { 
            light.on()
            if ("Switch Level" in light.capabilities?.name) { light.setLevel(100) }
            if ("Color Control" in light.capabilities?.name) { light.setColor([hex: "#FF0000", level: 100, switch: "on"]) }
        }
    }
}
