/**
 *  Pee Light
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
    name: "Pee Light",
    namespace: "claytonjn",
    author: "claytonjn",
    description: "Turns on light(s) when someone uses the bathroom in the middle of the night.",
    category: "My Apps",
    iconUrl: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn.png",
    iconX2Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@3x.png")


preferences {
	page(name: "page", install: true, uninstall: true) {
        section("Preferences") {
            paragraph "Turns on light(s) when someone uses the bathroom in the middle of the night."
            input "lights", "capability.switch", title: "Light(s)", multiple: true
            input "motions", "capability.motionSensor", title: "Motion(s)", multiple: true
            mode(title: "Set for specific mode(s)")
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
    if (evt.value == "active") {
        unschedule(lightsOff)
        settings.lights?.setLevel(1)
        settings.lights?.on()
    } else if (evt.value == "inactive") {
        runIn(60*2, lightsOff)
    }
}

void lightsOff() {
	settings.lights?.off()
}