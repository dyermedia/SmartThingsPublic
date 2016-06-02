/**
 *  Bias Lighting
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
    name: "Bias Lighting",
    namespace: "claytonjn",
    author: "claytonjn",
    description: "Turn on/off bias lighting with Harmony activities",
    category: "My Apps",
    iconUrl: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn.png",
    iconX2Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@3x.png")


preferences {
	page(name: "page", install: true, uninstall: true) {
        section("Preferences") {
            paragraph "Turn on/off bias lighting with Harmony activities"
            input "activities", "capability.switch", title: "Harmony Activity(s)", multiple: true, required: true
            input "lights", "capability.switch", title: "Bias Lights(s)", multiple: true, required: true
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
    subscribe(settings.activities, "switch.on", onHandler)
    subscribe(settings.activities, "switch.off", offHandler)
    subscribe(app, appHandler)
}

void onHandler(evt) {
    for (light in settings.lights) {
        light.setLevel(5)
        light.setColorTemperature(6500)
    }
}

void offHandler(evt) {
    def todayDate = new Date()
    def sunriseSunset = getSunriseAndSunset()
    if (todayDate < sunriseSunset.sunset || todayDate > sunriseSunset.sunrise) {
        for (light in settings.lights) {
            light.off()
        }
    }
}

void appHandler(evt) {
    if ("on" in settings.activities?.currentValue("switch")) {
        onHandler(evt)
    } else {
        offHandler(evt)
    }
}