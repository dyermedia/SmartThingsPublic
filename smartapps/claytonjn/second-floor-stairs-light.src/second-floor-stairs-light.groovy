/**
 *  Second Floor Stairs Light
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
    name: "Second Floor Stairs Light",
    namespace: "claytonjn",
    author: "claytonjn",
    description: "Turns on the Second Floor Stairs light when there's motion betwen sunset and sunrise.",
    category: "My Apps",
    iconUrl: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn.png",
    iconX2Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@3x.png")


preferences {
	page(name: "page", install: true, uninstall: true) {
        section("Title") {
            paragraph "Turns on the Second Floor Stairs light when there's motion betwen sunset and sunrise."
            input "lights", "capability.switch", title: "Light(s)", multiple: true, required: true
            input "motions", "capability.motionSensor", title: "Motion Sensor(s)", multiple: true, required: true
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
	def todayDate = new Date()
    def sunriseSunset = getSunriseAndSunset()
    if (todayDate > sunriseSunset.sunset || todayDate < sunriseSunset.sunrise) {
        if (location.mode in ["Home", "Night"]) {
            if (evt.value == "active") {
                unschedule(lightsOff)
                settings.lights?.on()
            } else if (evt.value == "inactive") {
                runIn(60*1, lightsOff)
            }
        }
    }
}

void lightsOff() {
	settings.lights?.off()
}
