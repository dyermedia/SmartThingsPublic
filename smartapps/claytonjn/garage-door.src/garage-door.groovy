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
    name: "Garage Door",
    namespace: "claytonjn",
    author: "claytonjn",
    description: "Ensures the Garage Door is closed when we leave, and sends a notification when the garage door is opened and we aren't home",
    category: "My Apps",
    iconUrl: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn.png",
    iconX2Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@2x.png")


preferences {
    page(name: "page", install: true, uninstall: true) {
        section("Preferences") {
            paragraph "Ensures the Garage Door is closed when we leave, and sends a notification when the garage door is opened and we aren't home"
            input "garageDoors", "device.myqGarageDoorOpener", title: "Garage Door(s)", multiple: true, required: true
            input "presence", "capability.presenceSensor", title: "Presence(s)", multiple: true, required: false
            input("recipients", "contact", title: "Send notifications to", multiple: true, required: true)
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
    subscribe(settings.presence, "presence", awayHandler)
    subscribe(settings.garageDoors, "door.open", notificationHandler)
}

def awayHandler(evt) {
    if (!settings.presence?.currentValue("presence").contains("present")) {
        settings.garageDoors?.close()
    }
}

def notificationHandler(evt) {
	if (!settings.presence?.currentValue("presence").contains("present")) {
        // check that contact book is enabled and recipients selected
        if (location.contactBookEnabled && settings.recipients) {
            sendNotificationToContacts("${evt.displayName} opened!", settings.recipients)
        } else {
            sendPush("${evt.displayName} opened!")
        }
    }
}