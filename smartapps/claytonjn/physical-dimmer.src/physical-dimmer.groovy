/**
 *  Physical Dimmer
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
    name: "Physical Dimmer",
    namespace: "claytonjn",
    author: "claytonjn",
    description: "Sync physical dimmer switch and bulbs",
    category: "My Apps",
    iconUrl: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn.png",
    iconX2Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@3x.png")


preferences {
    page(name: "page", install: true, uninstall: true) {
        section("Preferences") {
            paragraph "Sync physical dimmer switch and bulbs"
            input "physical", "capability.switch", title: "Physical Switch", multiple: false, required: true
            input "lights", "capability.switch", title: "Light(s)", multiple: true, required: true
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
    subscribe(settings.physical, "switch.on", physicalOnHandler)
    subscribe(settings.physical, "switch.off", physicalOffHandler)
    subscribe(settings.physical, "level", physicalLevelHandler)
    //subscribe(settings.lights, "switch.on", lightOnHandler)
    //subscribe(settings.lights, "switch.off", lightOffHandler)
    //subscribe(settings.lights, "level", lightLevelHandler)
}

void physicalOnHandler(evt) {
    if (!["Alert"].contains(location.mode)) {
        for (light in settings.lights) {
            light.setLevel(settings.physical.currentValue("level"))
            light.on()
        }
    }
}

void physicalOffHandler(evt) {
    if (!["Alert"].contains(location.mode)) {
        for (light in settings.lights) {
            light.off()
        }
    }
}

void physicalLevelHandler(evt) {
    if (!["Alert"].contains(location.mode)) {
        for (light in settings.lights) {
            light.setLevel(evt.numberValue)
        }
    }
}

void lightOnHandler(evt) {
    physical.on()
}

void lightOffHandler(evt) {
    if (! "on" in settings.lights.currentValue("switch")) {
        physical.off()
    }
}

void lightLevelHandler(evt) {
    def levels = 0
    for (light in settings.lights) {
        levels += light.currentValue("level")
    }
    def avgLevel = levels / settings.lights.size()
    physical.setLevel(avgLevel)
}