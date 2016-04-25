/**
 *  Psylocke Refresh
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
    name: "Device Refresh",
    namespace: "claytonjn",
    author: "claytonjn",
    description: "Trigger a refresh on a device when various things happen",
    category: "My Apps",
    iconUrl: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn.png",
    iconX2Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@3x.png")


preferences {
	page(name: "page", install: true, uninstall: false) {
        section("Title") {
            paragraph "Trigger a refresh on a device when various things happen"
            input "lights", "capability.switch", title: "Light Trigger(s)", multiple: true, required: false
            input "motions", "capability.motionSensor", title: "Motion Trigger(s)", multiple: true, required: false
            input "refreshs", "capability.refresh", title: "Device(s) to refresh", multiple: true, required: true
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
    subscribe(lights, "switch", evtHandler)
    subscribe(motions, "motion", evtHandler)
    subscribe(app, appHandler)
}

void evtHandler(evt) {
	settings.refreshs?.refresh()
}

void appHandler(evt) {
    evtHandler(evt)
}
}
