/**
 *  Laundry Monitor
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
    name: "Laundry Monitor",
    namespace: "claytonjn",
    author: "Clayton Nummer",
    description: "Sends a notification when the laundry is done.",
    category: "My Apps",
    iconUrl: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/personal/icons/claytonjn.png",
    iconX2Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/personal/icons/claytonjn@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/personal/icons/claytonjn@3x.png")


preferences {
	page(name: "page", install: true, uninstall: true) {
        section("Title") {
        	paragraph "Sends a notification when the laundry is done."
            input "washers", "capability.powerMeter", title: "Washing Machine(s)", multiple: true, required: false
            input "dryers", "capability.powerMeter", title: "Dryer(s)", multiple: true, required: false
            input "speakers", "capability.musicPlayer", title: "Speaker(s)", multiple: true, required: false
            input "modes", "mode", title: "Mode(s) for speaker ", multiple: true, required: true
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
    subscribe(settings.washers, "power", washerHandler)
    subscribe(settings.dryers, "power", dryerHandler)
}

void washerHandler(evt) { laundryHandler("Washing", evt) }
void dryerHandler(evt) { laundryHandler("Drying", evt) }

void laundryHandler(verb, evt) {
    if (Double.parseDouble(evt.value) == 0 && evt.isStateChange() == true) {

        // check that contact book is enabled and recipients selected
        if (location.contactBookEnabled && recipients) {
            sendNotificationToContacts("Done ${verb} clothes", recipients)
        } else {
            sendPush("Done ${verb} clothes")
        }

        if (location.mode in settings.modes) {
            for (speaker in speakers) {
                //Check if speaker is playing
                def isOn = speaker.currentValue("switch")
                //If it is, check what it's playing
                if (isOn == "on") { def isPlaying = speaker.currentValue("trackData") }
                //Speak
                speaker.playText("Done ${verb} clothes")
                //Resume playing if it was before
                if (isOn == "on") { speaker.restoreTrack(isPlaying) }
            }
        }
    }
}
