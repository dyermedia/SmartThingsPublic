/**
 *  Routines
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
    name: "Routines",
    namespace: "claytonjn",
    author: "claytonjn",
    description: "Runs when a Routine is triggered.",
    category: "My Apps",
    iconUrl: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn.png",
    iconX2Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@3x.png")


preferences {
	page(name: "page", install: true, uninstall: true) {
        section("Preferences") {
            paragraph "Runs when a Routine is triggered."
            input "thermostats", "capability.thermostat", title: "Thermostat(s)", multiple: true, required: false
            input "locks", "capability.lock", title: "Door Lock(s)", multiple: true, required: false
            input "garageDoors", "capability.switch", title: "Garage Door(s)", multiple: true, required: false
            input "awaySwitches", "capability.switch", title: "Switch(s) to turn off when Away", multiple: true, required: false
            input "homeSwitchesOn", "capability.switch", title: "Switch(s) to turn on when Home", multiple: true, required: false
            input "homeSwitchesOff", "capability.switch", title: "Switch(s) to turn off when Home", multiple: true, required: false
            input "nightSwitchesOn", "capability.switch", title: "Switch(s) to turn on when Night", multiple: true, required: false
            input "nightSpeakers", "capability.musicPlayer", title: "Speaker(s) to play lullaby", multiple: true, required: false
            input "nightVolume", "number", title: "Volume to play lullaby", defaultValue: 10, required: false
            input "nightSwitchesOffPriority", "capability.switch", title: "Switch(s) to prioritize turning off when Night", multiple: true, required: false
            input "nightSwitchesOff", "capability.switch", title: "Switch(s) to turn off when Night", multiple: true, required: false
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
	subscribe(location, "routineExecuted", routineHandler)
    subscribe(location, "alarmSystemStatus", routineHandler)
}

void routineHandler(evt) {
	if (evt.displayName == "Home" || evt.value == "off") {
    	sendLocationEvent(name: "alarmSystemStatus", value: "off")
        settings.homeSwitchesOn?.on()
        settings.homeSwitchesOff?.off()
        setLocationMode("Home")
        settings.thermostats?.setThermostatProgram("Home", "indefinite")
    } else if (evt.displayName == "Away" || evt.value == "away") {
    	//sendLocationEvent(name: "alarmSystemStatus", value: "away")
        settings.garageDoors?.close()
        settings.locks?.lock()
        setLocationMode("Away")
        settings.thermostats?.setThermostatProgram("Away", "nextTransition")
        settings.awaySwitches?.off()
    } else if (evt.displayName == "Night") {
    	sendLocationEvent(name: "alarmSystemStatus", value: "stay")
        setLocationMode("Night")
        settings.nightSwitchesOn?.on()
        settings.nightSwitchesOffPriority?.off()
        settings.garageDoors?.close()
        settings.locks?.lock()
        settings.nightSpeakers?.playTrackAtVolume("https://f001.backblazeb2.com/file/SmartThings/GreenNoiseTenHours.mp3", settings.nightVolume)
        settings.thermostats?.setThermostatProgram("Sleep", "nextTransition")
        settings.ceilingFans?.setSleepOn()
        settings.nightSwitchesOff?.off()
    }
}