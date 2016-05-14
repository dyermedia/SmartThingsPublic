/**
 *  Sensor Tamper
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
    name: "Sensor Tamper",
    namespace: "claytonjn",
    author: "Clayton Nummer",
    description: "Sends a notification when a sensor is tampered with.",
    category: "My Apps",
    iconUrl: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn.png",
    iconX2Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@3x.png")


preferences {
	page(name: "page", install: true, uninstall: true) {
        section("Title") {
        	paragraph "Sends a notification when a sensor is tampered with."
            input "tampers", "capability.tamperAlert", title: "Sensor(s)", multiple: true, required: false
            input("recipients", "contact", title: "Send notifications to", multiple: true, required: false) {
                input "sendPush", "bool", title: "Send push notifications?", required: false
            }
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
    subscribe(settings.tampers, "tamper.detected", tamperHandler)
}

void tamperHandler(evt) {
    log.debug "Notification: ${evt.displayName} has been tampered with!"

    // check that contact book is enabled and recipients selected
    if (location.contactBookEnabled && recipients) {
        sendNotificationToContacts("${evt.displayName} has been tampered with!", recipients)
    } else {
        if (sendPush) { sendPush("${evt.displayName} has been tampered with!") }
    }
}