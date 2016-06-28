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
	definition (name: "Haiku Home Fan", namespace: "claytonjn", author: "claytonjn") {
		capability "Switch"
		capability "Switch Level"

		command "lightOn"
		command "lightOff"
		command "lightSetLevel"
		command "setSmartModeOff"
		command "setSmartModeFollowTStat"
		command "setSmartModeCooling"
		command "setSmartModeHeating"
		command "setSmartTemp"
		command "setSmartMin"
		command "setSmartMax"
		command "setMotionFanOn"
		command "setMotionFanOff"
		command "setMotionLightOn"
		command "setMotionLightOff"
		command "setMotionTimeout"
		command "setSleepOn"
		command "setSleepOff"
		command "setSleepTemp"
		command "setSleepMin"
		command "setSleepMax"
		//command "setSleepWakeLightLevel"
		//command "setSleepWakeLightOff"
		command "setWhooshOn"
		command "setWhooshOff"

		attribute "lightLevel", "NUMBER"
		attribute "smartTemp", "NUMBER"
		attribute "smartMin", "NUMBER"
		attribute "smartMax", "NUMBER"
		attribute "sleepTemp", "NUMBER"
		attribute "sleepMin", "NUMBER"
		attribute "sleepMax", "NUMBER"
		//attribute "sleepWakeLight", "STRING"
	}


	simulator {
		// TODO: define status and reply messages here
	}

	tiles(scale: 2) {
        standardTile("fanOff", "device.switch", width: 1, height: 1, decoration: "flat") {
            state "default", label: 'Off', action: "switch.off", icon: "st.Lighting.light24", decoration: flat
        }
        controlTile("fanLevel", "device.level", "slider", width: 4, height: 1, decoration: "flat", range: "(1..7)") {
            state "level", action: "switch level.setLevel"
        }
        standardTile("fanOn", "device.switch", width: 1, height: 1, decoration: "flat") {
            state "default", label: 'On', action: "switch.on", icon: "st.Lighting.light24", decoration: flat
        }

		standardTile("lightOff", "device.lightSwitch", width: 1, height: 1, decoration: "flat") {
            state "default", label: 'Off', action: "lightOff", icon: "st.Lighting.light21", decoration: flat
        }
        controlTile("lightLevel", "device.lightLevel", "slider", width: 4, height: 1, decoration: "flat") {
            state "lightLevel", action: "lightSetLevel"
        }
        standardTile("lightOn", "device.lightSwitch", width: 1, height: 1, decoration: "flat") {
            state "default", label: 'On', action: "lightOn", icon: "st.Lighting.light21", decoration: flat
        }

		valueTile("modeOff", "device.mode", width: 2, height: 1, decoration: "flat") {
			state "default", label: "Mode Off", action: "setSmartModeOff"
		}
		valueTile("modeFollowTStat", "device.mode", width: 2, height: 1, decoration: "flat") {
			state "default", label: "Mode Thermostat", action: "setSmartModeFollowTStat"
		}
		valueTile("modeCooling", "device.mode", width: 2, height: 1, decoration: "flat") {
			state "default", label: "Mode Cooling", action: "setSmartModeCooling"
		}
		valueTile("modeHeating", "device.mode", width: 2, height: 1, decoration: "flat") {
			state "default", label: "Mode Heating", action: "setSmartModeHeating"
		}

		controlTile("smartTemp", "device.smartTemp", "slider", width: 4, height: 1, decoration: "flat", range: "(50..90)") {
            state "smartTemp", action: "setSmartTemp"
        }

		controlTile("smartMin", "device.smartMin", "slider", width: 4, height: 1, decoration: "flat", range: "(0..7)") {
            state "smartMin", action: "setSmartMin"
        }
		controlTile("smartMax", "device.smartMax", "slider", width: 4, height: 1, decoration: "flat", range: "(0..7)") {
            state "smartMax", action: "setSmartMax"
        }

		valueTile("motionFanOn", "device.motionFan", width: 2, height: 1, decoration: "flat") {
			state "default", label: "Motion Fan On", action: "setMotionFanOn"
		}
		valueTile("motionFanOff", "device.motionFan", width: 2, height: 1, decoration: "flat") {
			state "default", label: "Motion Fan Off", action: "setMotionFanOff"
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
		controlTile("sleepTemp", "device.sleepTemp", "slider", width: 4, height: 1, decoration: "flat", range: "(50..90)") {
            state "sleepTemp", action: "setSleepTemp"
        }
		controlTile("sleepMin", "device.sleepMin", "slider", width: 4, height: 1, decoration: "flat", range: "(0..7)") {
            state "sleepMin", action: "setSleepMin"
        }
		controlTile("sleepMax", "device.sleepMax", "slider", width: 4, height: 1, decoration: "flat", range: "(0..7)") {
            state "sleepMax", action: "setSleepMax"
        }
		/*
		controlTile("setSleepWakeLightLevel", "device.sleepWakeLight", "slider", width: 4, height: 1, decoration: "flat", range: "(1..16)") {
            state "sleepWakeLight", action: "setSleepWakeLightLevel"
        }
		valueTile("setSleepWakeLightOff", "device.sleepWakeLight", width: 2, height: 1, decoration: "flat") {
			state "default", label: "Wake Light Off", action: "setSleepWakeLightOff"
		}
		*/

		valueTile("whooshOn", "device.whoosh", width: 2, height: 1, decoration: "flat") {
			state "default", label: "Whoosh On", action: "setWhooshOn"
		}
		valueTile("whooshOff", "device.whoosh", width: 2, height: 1, decoration: "flat") {
			state "default", label: "Whoosh Off", action: "setWhooshOff"
		}
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
	log.debug "Executing 'on' for fan"
	sendEvent(name: "switch", value: "on", descriptionText: "Fan has been turned on", isStateChange: true)
	sendCommand("FAN;PWR;ON")
}

def off() {
	log.debug "Executing 'off' for fan"
	sendEvent(name: "switch", value: "off", descriptionText: "Fan has been turned off", isStateChange: true)
	sendCommand("FAN;PWR;OFF")
}

def setLevel(level) {
	log.debug "Executing 'setLevel' for fan"
	sendEvent(name: "level", value: level, descriptionText: "Fan speed has been set to level ${level}", isStateChange: true)
	sendCommand("FAN;SPD;SET;${level}")
}

def lightOn() {
	log.debug "Executing 'on' for light"
	sendEvent(name: "lightSwitch", value: "on", descriptionText: "Light has been turned on", isStateChange: true)
	sendCommand("LIGHT;PWR;ON")
}

def lightOff() {
	log.debug "Executing 'off' for light"
	sendEvent(name: "lightSwitch", value: "off", descriptionText: "Light has been turned off", isStateChange: true)
	sendCommand("LIGHT;PWR;OFF")
}

def lightSetLevel(percent) {
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

def setSmartMode(mode) {
	log.debug "Executing 'setSmartMode:${mode}'"
	sendEvent(name: "mode", value: mode, isStateChange: true)
	sendCommand("SMARTMODE;STATE;SET;${mode}")
}

def setSmartModeOff() {
	setSmartMode("OFF")
}

def setSmartModeFollowTStat() {
	setSmartMode("FOLLOWTSTAT")
}

def setSmartModeCooling() {
	setSmartMode("COOLING")
}

def setSmartModeHeating() {
	setSmartMode("HEATING")
}

def setSmartTemp(temp) {
	log.debug "Executing 'setSmartTemp:${temp}'"
	sendEvent(name: "smartTemp", value: temp, isStateChange: true)
	temp = (temp - 32) / 1.8 //Convert Fahrenheit to Celcius
	temp = Math.round(temp * 100) as Integer //Convert to int
	sendCommand("LEARN;ZEROTEMP;SET;${temp}")
}

def setSmartMin(level) {
	log.debug "Executing 'setSmartMin:${level}'"
	sendEvent(name: "smartMin", value: level, isStateChange: true)
	sendCommand("LEARN;MINSPEED;SET;${level}")
}

def setSmartMax(level) {
	log.debug "Executing 'setSmartMax:${level}'"
	sendEvent(name: "smartMax", value: level, isStateChange: true)
	sendCommand("LEARN;MAXSPEED;SET;${level}")
}

def setMotionFanOn() {
	log.debug "Executing 'setMotionFanOn'"
	sendEvent(name: "motionFan", value: "ON", isStateChange: true)
	sendCommand("FAN;AUTO;ON")
}

def setMotionFanOff() {
	log.debug "Executing 'setMotionFanOff'"
	sendEvent(name: "motionFan", value: "OFF", isStateChange: true)
	sendCommand("FAN;AUTO;OFF")
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

def setSleepTemp(temp) {
	log.debug "Executing 'setSleepTemp:${temp}'"
	sendEvent(name: "sleepTemp", value: temp, isStateChange: true)
	temp = (temp - 32) / 1.8 //Convert Fahrenheit to Celcius
	temp = Math.round(temp * 100) as Integer //Convert to int
	sendCommand("SMARTSLEEP;IDEALTEMP;SET;${temp}")
}

def setSleepMin(level) {
	log.debug "Executing 'setSleepMin:${level}'"
	sendEvent(name: "sleepMin", value: level, isStateChange: true)
	sendCommand("SMARTSLEEP;MINSPEED;SET;${level}")
}

def setSleepMax(level) {
	log.debug "Executing 'setSleepMax:${level}'"
	sendEvent(name: "sleepMax", value: level, isStateChange: true)
	sendCommand("SMARTSLEEP;MAXSPEED;SET;${level}")
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

def setWhooshOn() {
	log.debug "Executing 'setWhooshOn'"
	sendEvent(name: "whoosh", value: "ON", isStateChange: true)
	sendCommand("FAN;WHOOSH;ON")
}

def setWhooshOff() {
	log.debug "Executing 'setWhooshOff'"
	sendEvent(name: "whoosh", value: "OFF", isStateChange: true)
	sendCommand("FAN;WHOOSH;OFF")
}

private def sendCommand(command) {
    device.deviceNetworkId = ipToHex(settings.ip) + ":" + portToHex(31415)
    command = "<${settings.mac};${command}>"
    log.debug "Sending command ${command}"
    return new physicalgraph.device.HubAction(command,physicalgraph.device.Protocol.LAN)
}

private def ipToHex(ip) {
    return ip.tokenize('.').collect { String.format('%02X', it.toInteger()) }.join()
}

private def portToHex(port) {
	return port.toString().format('%04X', port.toInteger())
}