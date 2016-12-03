/**
 *  Haiku Home Fan
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
metadata {
	definition (name: "Haiku Home Light", namespace: "claytonjn", author: "claytonjn") {
		capability "Switch"
		capability "Switch Level"

		command "setMotionLightOn"
		command "setMotionLightOff"
		command "setMotionTimeout"
		command "setSleepOn"
		command "setSleepOff"
		//command "setSleepWakeLightLevel"
		//command "setSleepWakeLightOff"

		//attribute "sleepWakeLight", "STRING"
	}


	simulator {
		// TODO: define status and reply messages here
	}

	tiles(scale: 2) {
		standardTile("lightOff", "device.switch", width: 1, height: 1, decoration: "flat") {
            state "default", label: 'Off', action: "off", icon: "st.Lighting.light21", decoration: flat
        }
        controlTile("lightLevel", "device.level", "slider", width: 4, height: 1, decoration: "flat") {
            state "lightLevel", action: "setLevel"
        }
        standardTile("lightOn", "device.switch", width: 1, height: 1, decoration: "flat") {
            state "default", label: 'On', action: "on", icon: "st.Lighting.light21", decoration: flat
        }

		valueTile("motionLightOn", "device.motionLight", width: 2, height: 1, decoration: "flat") {
			state "default", label: "Motion Light On", action: "setMotionLightOn"
		}
		valueTile("motionLightOff", "device.motionLight", width: 2, height: 1, decoration: "flat") {
			state "default", label: "Motion Light Off", action: "setMotionLightOff"
		}

		valueTile("sleepOn", "device.sleep", width: 2, height: 1, decoration: "flat") {
			state "default", label: "Sleep On", action: "setSleepOn"
		}
		valueTile("sleepOff", "device.sleep", width: 2, height: 1, decoration: "flat") {
			state "default", label: "Sleep Off", action: "setSleepOff"
		}
		/*
		controlTile("setSleepWakeLightLevel", "device.sleepWakeLight", "slider", width: 4, height: 1, decoration: "flat", range: "(1..16)") {
            state "sleepWakeLight", action: "setSleepWakeLightLevel"
        }
		valueTile("setSleepWakeLightOff", "device.sleepWakeLight", width: 2, height: 1, decoration: "flat") {
			state "default", label: "Wake Light Off", action: "setSleepWakeLightOff"
		}
		*/
	}
}

preferences {
    input "mac", "text", title: "MAC address", required: true
    input "ip", "text", title: "IP address", required: true
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
	// TODO: handle 'switch' attribute
	// TODO: handle 'level' attribute

}

// handle commands
def on() {
	log.debug "Executing 'on' for light"
	sendEvent(name: "lightSwitch", value: "on", descriptionText: "Light has been turned on", isStateChange: true)
	sendCommand("LIGHT;PWR;ON")
}

def off() {
	log.debug "Executing 'off' for light"
	sendEvent(name: "lightSwitch", value: "off", descriptionText: "Light has been turned off", isStateChange: true)
	sendCommand("LIGHT;PWR;OFF")
}

def setLevel(percent) {
	log.debug "Executing 'setLevel' for light"
	if (percent == 0) {
		lightOff()
	} else {
		def stRange = (100 - 1)
		def haikuRange = (16 - 1)
		def haikuValue = (((percent - 1) * haikuRange) / stRange) + 1
		haikuValue = Math.round(haikuValue) as Integer
		sendEvent(name: "lightLevel", value: percent, descriptionText: "Light level has changed to ${percent}%", isStateChange: true)
		sendCommand("LIGHT;LEVEL;SET;${haikuValue}")
	}
}

def setMotionLightOn() {
	log.debug "Executing 'setMotionLightOn'"
	sendEvent(name: "motionLight", value: "ON", isStateChange: true)
	sendCommand("LIGHT;AUTO;ON")
}

def setMotionLightOff() {
	log.debug "Executing 'setMotionLightOff'"
	sendEvent(name: "motionLight", value: "OFF", isStateChange: true)
	sendCommand("LIGHT;AUTO;OFF")
}

def setMotionTimeout(minutes) {
	log.debug "Executing 'setMotionTimeout:${minutes}'"
	sendEvent(name: "motionTimeout", value: minutes, isStateChange: true)
	minutes = minutes * 60000
	sendCommand("SNSROCC;TIMEOUT;SET;${minutes}")
}

def setSleepOn() {
	log.debug "Executing 'setSleepOn'"
	sendEvent(name: "sleep", value: "ON", isStateChange: true)
	sendCommand("SLEEP;STATE;ON")
}

def setSleepOff() {
	log.debug "Executing 'setSleepOff'"
	sendEvent(name: "sleep", value: "OFF", isStateChange: true)
	sendCommand("SLEEP;STATE;OFF")
}

/*
def setSleepWakeLightLevel(level) {
	log.debug "Executing 'setSleepWakeLightLevel:${level}'"
	sendEvent(name: "sleepWakeLight", value: level, isStateChange: true)
	sendCommand("SLEEP;EVENT;OFF;LIGHT;LEVEL;${level}")
}

def setSleepWakeLightOff() {
	log.debug "Executing 'setSleepWakeLightOff'"
	sendEvent(name: "sleepWakeLight", value: "OFF", isStateChange: true)
	sendCommand("SLEEP;EVENT;OFF;LIGHT;PWR;OFF")
}
*/

private def sendCommand(command) {
    device.deviceNetworkId = ipToHex(settings.ip) + ":" + portToHex(31415)
    command = "<${settings.mac};${command}>"
    log.debug "Sending command ${command}"
    return new physicalgraph.device.HubAction(command,physicalgraph.device.Protocol.LAN)
	device.deviceNetworkId = ipToHex(settings.ip) + ":" + portToHex(31415) + "-light"
}

private def ipToHex(ip) {
    return ip.tokenize('.').collect { String.format('%02X', it.toInteger()) }.join()
}

private def portToHex(port) {
	return port.toString().format('%04X', port.toInteger())
}