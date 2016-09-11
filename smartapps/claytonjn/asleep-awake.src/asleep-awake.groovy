/**
 *  Asleep/Awake
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
    name: "Asleep/Awake",
    namespace: "claytonjn",
    author: "claytonjn",
    description: "Turn on sleep when everyone goes to sleep, turn off Sleep switch when everyone wakes up.",
    category: "My Apps",
    iconUrl: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn.png",
    iconX2Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@3x.png")


preferences {
	page(name: "page", install: true, uninstall: true) {
        section("Preferences") {
        	paragraph "Turn on sleep when everyone goes to sleep, turn off Sleep switch when everyone wakes up."
            input "clayton", "capability.presenceSensor", title: "Clayton - Presence", multiple: false
            input "cory", "capability.presenceSensor", title: "Cory - Presence", multiple: false
            input "claytonSleep", "capability.switch", title: "Clayton - Sleep", multiple: false
            input "corySleep", "capability.switch", title: "Cory - Sleep", multiple: false
            input "sleep", "capability.switch", title: "Sleep Switch", multiple: false
            input "ceilingFans", "capability.switch", title: "Fans(s) to control", multiple: true, required: false
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
    subscribe(claytonSleep, "switch.off", wakeHandler)
    subscribe(corySleep, "switch.off", wakeHandler)
    subscribe(claytonSleep, "switch.on", sleepHandler)
    subscribe(corySleep, "switch.on", sleepHandler)
}

void wakeHandler(evt) {
    if (location.mode in ["Home", "Night"]) {
        location.helloHome?.execute("Home")
        if (clayton.currentValue("presence") == "present" && cory.currentValue("presence") == "present") {
            if (claytonSleep.currentValue("switch") == "off" && corySleep.currentValue("switch") == "off") {
                settings.sleep.off()
                settings.ceilingFans?.setSleepOff()
            }
        }
        else if (clayton.currentValue("presence") == "present" && cory.currentValue("presence") == "not present") {
            if (claytonSleep.currentValue("switch") == "off") {
                settings.sleep.off()
                settings.ceilingFans?.setSleepOff()
            }
        }
        else if (clayton.currentValue("presence") == "not present" && cory.currentValue("presence") == "present") {
            if (corySleep.currentValue("switch") == "off") {
                settings.sleep.off()
                settings.ceilingFans?.setSleepOff()
            }
        }
    }
}

void sleepHandler(evt) {
	if (location.mode in ["Home", "Night"]) {
        if (clayton.currentValue("presence") == "present" && cory.currentValue("presence") == "present") {
            if (claytonSleep.currentValue("switch") == "on" && corySleep.currentValue("switch") == "on") {
                location.helloHome?.execute("Night")
            }
        }
        else if (clayton.currentValue("presence") == "present" && cory.currentValue("presence") == "not present") {
            if (claytonSleep.currentValue("switch") == "on") {
                location.helloHome?.execute("Night")
            }
        }
        else if (clayton.currentValue("presence") == "not present" && cory.currentValue("presence") == "present") {
            if (corySleep.currentValue("switch") == "on") {
                location.helloHome?.execute("Night")
            }
        }
    }
}