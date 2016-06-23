/**
 *  Snail Mail
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
    name: "Snail Mail",
    namespace: "claytonjn",
    author: "claytonjn",
    description: "Sends alerts based on Mailbox door sensor.",
    category: "My Apps",
    iconUrl: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn.png",
    iconX2Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@3x.png")


preferences {
	page(name: "page", install: true, uninstall: true) {
        section("Preferences") {
            paragraph "Sends alerts based on Mailbox door sensor."
            input "mailboxes", "capability.contactSensor", title: "Mailbox(s)", multiple: true
            input "mail", "capability.switch", title: "Mail Switch", multiple: false
            input "presences", "capability.presenceSensor", title: "Presence(s)", multiple: true
            input("recipients", "contact", title: "Send notifications to", multiple: true)
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
	subscribe(settings.mailboxes, "contact.open", mailboxHandler)
    subscribe(settings.mail, "switch", mailHandler)
}

void mailboxHandler(evt) {
	if (settings.mail.currentValue("switch") == "off") {
    	settings.mail.on()
    } else if (settings.mail.currentValue("switch") == "on") {
    	if (location.mode in ["Home"]) {
            settings.mail.off()
        } else if (location.mode in ["Away"] && "present" in settings.presences?.currentValue("presence")) {
        	settings.mail.off()
        } else {
        	def message = "Possible Unauthorized Mailbox Access!"
            // check that contact book is enabled and recipients selected
            if (location.contactBookEnabled && recipients) {
                sendNotificationToContacts(message, recipients)
            } else {
                sendPush(message)
            }
        }
    }
}

void mailHandler(evt) {
	if (evt.value == "on") {
    	def message = "You've Got Mail!"
        // check that contact book is enabled and recipients selected
        if (location.contactBookEnabled && recipients) {
            sendNotificationToContacts(message, recipients)
        } else {
            sendPush(message)
        }
    } else {
    	def message = "Mail Retrieved"
        // check that contact book is enabled and recipients selected
        if (location.contactBookEnabled && recipients) {
            sendNotificationToContacts(message, recipients)
        } else {
            sendPush(message)
        }
    }
}