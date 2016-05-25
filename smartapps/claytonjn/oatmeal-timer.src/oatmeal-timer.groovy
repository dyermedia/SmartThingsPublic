/**
 *  Oatmeal Timer
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
    name: "Oatmeal Timer",
    namespace: "claytonjn",
    author: "claytonjn",
    description: "Turns on the slow cooker in the evening, and turns it off when unplugged.",
    category: "My Apps",
    iconUrl: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn.png",
    iconX2Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@3x.png")


preferences {
	page(name: "page", install: true, uninstall: true) {
        section("Preferences") {
        	paragraph "Turns on the slow cooker in the evening, and turns it off when unplugged."
            input "outlets", "capability.switch", title: "Outlet(s)", multiple: true
            input "onTime", "time", title: "When to turn on"
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
    schedule(settings.onTime, onHandler)
	subscribe(settings.outlets, "power", offHandler)
}

void onHandler(evt) {
    settings.outlets?.on()
    runIn(60, onNotificationHandler)
}

void onNotificationHandler() {
    def on = false
    for (outlet in settings.outlets) {
        if (outlet.currentValue("power") >= 1) {
            on = true
        } else {
            outlet.off()
        }
    }
    if (on == true) {
        // check that contact book is enabled and recipients selected
        if (location.contactBookEnabled && settings.recipients) {
            sendNotificationToContacts("Oatmeal turned on", settings.recipients)
        } else {
            if (sendPush) { sendPush("Oatmeal turned on") }
        }
    }
}

void offHandler(evt) {
    if(evt.device.currentValue("switch") == "on" && evt.numericValue <= 1) {
        settings.outlets?.off()

        // check that contact book is enabled and recipients selected
        if (location.contactBookEnabled && settings.recipients) {
            sendNotificationToContacts("Oatmeal turned off", settings.recipients)
        } else {
            if (sendPush) { sendPush("Oatmeal turned off") }
        }
    }
}