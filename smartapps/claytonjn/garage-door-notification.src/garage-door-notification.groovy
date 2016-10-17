/**
 *  Garage Door Notification
 *
 *  Copyright 2016 claytonjn
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
    name: "Garage Door Notification",
    namespace: "claytonjn",
    author: "claytonjn",
    description: "Send a notification when the garage door is opened and we aren't home",
    category: "My Apps",
    iconUrl: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn.png",
    iconX2Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@2x.png")


preferences {
    page(name: "page", install: true, uninstall: true) {
        section("Preferences") {
            paragraph "Send a notification when the garage door is opened and we aren't home"
            input "garageDoors", "device.myQGarageDoorOpener", title: "Garage Door(s)", multiple: true, required: true
            input "clayton", "capability.presenceSensor", title: "Clayton - Presence", multiple: false
            input "cory", "capability.presenceSensor", title: "Cory - Presence", multiple: false
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
	subscribe(settings.garageDoors, "door.open", notificationHandler)
}

def notificationHandler(evt) {
    if (settings.clayton.currentValue("presence") == "not present" && settings.cory.currentValue("presence") == "not present") {
        // check that contact book is enabled and recipients selected
        if (location.contactBookEnabled && settings.recipients) {
            sendNotificationToContacts("${evt.displayName} opened!", settings.recipients)
        } else {
            sendPush("${evt.displayName} opened!")
        }
    }
}