/**
 *  Outdoor Lighting
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
    name: "Outdoor Lighting",
    namespace: "claytonjn",
    author: "claytonjn",
    description: "Turns on the outside lights at sunset, turns them off at sunrise.",
    category: "My Apps",
    iconUrl: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn.png",
    iconX2Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@3x.png")


preferences {
	page(name: "page", install:true, uninstall: true) {
        section("Preferences") {
        	paragraph "Turns on the outside lights at sunset, turns them off at sunrise."
            input "lights", "capability.switch", title: "Light(s)", multiple: true
            input("recipients", "contact", title: "Send notifications to", multiple: true)
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
    subscribe(location, "sunset", sunsetHandler)
	subscribe(location, "sunrise", sunriseHandler)
    subscribe(app, appHandler)
    subscribe(location, "mode", appHandler)
    schedule("0 0 0 * * ?", appHandler)
}

void sunsetHandler(evt) {
    def todayDate = new Date()
    def todayTimeZone = TimeZone.getTimeZone('US/Eastern')
    def minusThreeW = todayDate - 21
    def minusFourW = todayDate - 28
    def plusOneW = todayDate + 7

    def messages = []

    if (todayDate.format("MM-dd",todayTimeZone) >= "12-31"
    	&& todayDate.format("MM-dd",todayTimeZone) <= "01-01") {					      //New Year's Eve / New Year's Day
        for (light in settings.lights) {
            light.on()
            if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                light.setColor([hue: (51/360)*100, saturation: 100, switch: "on"])
            } else if (light.displayName ==~ /^.*\bPorch\b.*\bStep\b.*$/) {
                light.setColor([hue: (51/360)*100, saturation: 100, switch: "on"])
            } else if ("Color Control" in light.capabilities?.name) {
                light.setColor([hue: (51/360)*100, saturation: 100, switch: "on"])
            }
        }
        messages << "Happy New Year!"
    } else if (todayDate.format("MM-dd",todayTimeZone) == "02-14") {				        //Valentine's Day
        for (light in settings.lights) {
            light.on()
            if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                light.setColor([hue: (350/360)*100, saturation: 24.7, switch: "on"])
            } else if (light.displayName ==~ /^.*\bPorch\b.*\bStep\b.*$/) {
                light.setColor([hue: (350/360)*100, saturation: 24.7, switch: "on"])
            } else if ("Color Control" in light.capabilities?.name) {
                light.setColor([hue: (350/360)*100, saturation: 24.7, switch: "on"])
            }
        }
        messages << "Happy Valentine's Day!"
    } else if (todayDate.format("MM-dd",todayTimeZone) == "03-17") {				        //St. Patrick's Day
        for (light in settings.lights) {
            light.on()
            if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                light.setColor([hue: (120/360)*100, saturation: 100, switch: "on"])
            } else if (light.displayName ==~ /^.*\bPorch\b.*\bStep\b.*$/) {
                light.setColor([hue: (120/360)*100, saturation: 100, switch: "on"])
            } else if ("Color Control" in light.capabilities?.name) {
                light.setColor([hue: (120/360)*100, saturation: 100, switch: "on"])
            }
        }
        messages << "Happy St. Patrick's Day!"
    } else if (todayDate.format("yyyy-MM-dd",todayTimeZone) == "2017-04-16") {		        //EASTER / Cory's Birthday
        for (light in settings.lights) {
            light.on()
            if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                light.setColor([hue: (350/360)*100, saturation: 24.7, switch: "on"])
            } else if (light.displayName ==~ /^.*\bPorch\b.*\bStep\b.*$/) {
                light.setColor([hue: (120/360)*100, saturation: 39.5, switch: "on"])
            } else if ("Color Control" in light.capabilities?.name) {
                light.setColor([hue: (300/360)*100, saturation: 100, switch: "on"])
            }
        }
        messages << "Happy Easter!"
        messages << "Happy Birthday, Cory!"
    } else if (todayDate.format("MM-dd",todayTimeZone) == "04-16") {				        //Cory's Birthday
        for (light in settings.lights) {
            light.on()
            if (light.displayName ==~ /^.*\bGardenspots\b.*$/ || light.displayName ==~ /^.*\bPorch\b.*\bStep\b.*$/) {
                light.setColor([hue: 8, saturation: 82, switch: "on"])
            } else if ("Color Temperature" in light.capabilities?.name) {
                light.setColorTemperature(2700)
            }
        }
        messages << "Happy Birthday, Cory!"
    } else if (todayDate.format("yyyy-MM-dd H:m",todayTimeZone) >= "2017-04-10 12:00"
    		&& todayDate.format("yyyy-MM-dd H:m",todayTimeZone) <= "2017-04-18 12:00") {	//PASSOVER
        for (light in settings.lights) {
            light.on()
            if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                light.setColor([hue: (222/360)*100, saturation: 100, switch: "on"])
            } else if (light.displayName ==~ /^.*\bPorch\b.*\bStep\b.*$/) {
                light.setColor([hue: (222/360)*100, saturation: 100, switch: "on"])
            } else if ("Color Control" in light.capabilities?.name) {
                light.setColor([hue: (0/360)*100, saturation: 0, switch: "on"])
            }
        }
        messages << "Happy Passover!"
    } else if (todayDate.format("yyyy-MM-dd",todayTimeZone) == "2017-05-14") {		        //MOTHER'S DAY
        for (light in settings.lights) {
            light.on()
            if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                light.setColor([hue: (350/360)*100, saturation: 24.7, switch: "on"])
            } else if (light.displayName ==~ /^.*\bPorch\b.*\bStep\b.*$/) {
                light.setColor([hue: (350/360)*100, saturation: 24.7, switch: "on"])
            } else if ("Color Control" in light.capabilities?.name) {
                light.setColor([hue: (350/360)*100, saturation: 24.7, switch: "on"])
            }
        }
        messages << "Happy Mother's Day!"
    } else if (todayDate.format("MM-u",todayTimeZone) == "05-1"
    		&& plusOneW.format("MM",todayTimeZone) != "05") {					            //Memorial Day
        for (light in settings.lights) {
            light.on()
            if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                light.setColor([hue: (0/360)*100, saturation: 100, switch: "on"])
            } else if (light.displayName ==~ /^.*\bPorch\b.*\bStep\b.*$/) {
                light.setColor([hue: (0/360)*100, saturation: 0, switch: "on"])
            } else if ("Color Control" in light.capabilities?.name) {
                light.setColor([hue: (240/360)*100, saturation: 100, switch: "on"])
            }
        }
        messages << "Happy Memorial Day!"
    } else if (todayDate.format("MM-dd",todayTimeZone) == "06-16") {				        //Clayton's Birthday
        for (light in settings.lights) {
            light.on()
            if (light.displayName ==~ /^.*\bGardenspots\b.*$/ || light.displayName ==~ /^.*\bPorch\b.*\bStep\b.*$/) {
                light.setColor([hue: 8, saturation: 82, switch: "on"])
            } else if ("Color Temperature" in light.capabilities?.name) {
                light.setColorTemperature(2700)
            }
        }
        messages << "Happy Birthday, Clayton!"
    } else if (todayDate.format("yyyy-MM-dd",todayTimeZone) == "2017-06-18") {		        //FATHER'S DAY
        for (light in settings.lights) {
            light.on()
            if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                light.setColor([hue: (240/360)*100, saturation: 100, switch: "on"])
            } else if (light.displayName ==~ /^.*\bPorch\b.*\bStep\b.*$/) {
                light.setColor([hue: (240/360)*100, saturation: 100, switch: "on"])
            } else if ("Color Control" in light.capabilities?.name) {
                light.setColor([hue: (240/360)*100, saturation: 100, switch: "on"])
            }
        }
        messages << "Happy Father's Day!"
    } else if (todayDate.format("MM-dd",todayTimeZone) == "07-04") {				        //July 4
        for (light in settings.lights) {
            light.on()
            if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                light.setColor([hue: (0/360)*100, saturation: 100, switch: "on"])
            } else if (light.displayName ==~ /^.*\bPorch\b.*\bStep\b.*$/) {
                light.setColor([hue: (0/360)*100, saturation: 0, switch: "on"])
            } else if ("Color Control" in light.capabilities?.name) {
                light.setColor([hue: (240/360)*100, saturation: 100, switch: "on"])
            }
        }
        messages << "Happy 4th of July!"
    } else if (todayDate.format("yyyy-MM-dd H:m",todayTimeZone) >= "2017-07-07 12:00"
            && todayDate.format("yyyy-MM-dd H:m",todayTimeZone) <= "2017-07-08 12:00") {		//ELIZABETH LAKE FIREWORKS
        for (light in settings.lights) {
            light.on()
            if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                light.setColor([hue: (0/360)*100, saturation: 100, switch: "on"])
            } else if (light.displayName ==~ /^.*\bPorch\b.*\bStep\b.*$/) {
                light.setColor([hue: (0/360)*100, saturation: 0, switch: "on"])
            } else if ("Color Control" in light.capabilities?.name) {
                light.setColor([hue: (240/360)*100, saturation: 100, switch: "on"])
            }
        }
        messages << "Happy Fireworks!"
    } else if (todayDate.format("yyyy-MM-dd H:m",todayTimeZone) >= "2017-09-20 12:00"
    		&& todayDate.format("yyyy-MM-dd H:m",todayTimeZone) <= "2017-09-22 12:00") {	     //ROSH HASHANAH
        for (light in settings.lights) {
            light.on()
            if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                light.setColor([hue: (222/360)*100, saturation: 100, switch: "on"])
            } else if (light.displayName ==~ /^.*\bPorch\b.*\bStep\b.*$/) {
                light.setColor([hue: (222/360)*100, saturation: 100, switch: "on"])
            } else if ("Color Control" in light.capabilities?.name) {
                light.setColor([hue: (0/360)*100, saturation: 0, switch: "on"])
            }
        }
        messages << "Happy Rosh Hashanah!"
    } else if (todayDate.format("yyyy-MM-dd H:m",todayTimeZone) >= "2017-09-29 12:00"
            && todayDate.format("yyyy-MM-dd H:m",todayTimeZone) <= "2017-09-30 12:00") {		//YOM KIPPUR
        for (light in settings.lights) {
            light.on()
            if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                light.setColor([hue: (222/360)*100, saturation: 100, switch: "on"])
            } else if (light.displayName ==~ /^.*\bPorch\b.*\bStep\b.*$/) {
                light.setColor([hue: (222/360)*100, saturation: 100, switch: "on"])
            } else if ("Color Control" in light.capabilities?.name) {
                light.setColor([hue: (0/360)*100, saturation: 0, switch: "on"])
            }
        }
        messages << "Happy Yom Kippur!"
    } else if (todayDate.format("MM-dd",todayTimeZone) == "10-31") {				            //Halloween
        for (light in settings.lights) {
            light.on()
            if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                light.setColor([hue: (30/360)*100, saturation: 100, switch: "on"])
            } else if (light.displayName ==~ /^.*\bPorch\b.*\bStep\b.*$/) {
                light.setColor([hue: (30/360)*100, saturation: 100, switch: "on"])
            } else if ("Color Control" in light.capabilities?.name) {
                light.setColor([hue: (300/360)*100, saturation: 100, switch: "on"])
            }
        }
        messages << "Happy Halloween!"
    } else if (todayDate.format("MM-dd",todayTimeZone) == "11-11") {				            //Veterans Day
        for (light in settings.lights) {
            light.on()
            if (light.displayName ==~ /^.*\bEast Gardenspots\b.*$/) {
                light.setColor([hue: (0/360)*100, saturation: 100, switch: "on"])
            } else if (light.displayName ==~ /^.*\bWest Gardenspots\b.*$/) {
                light.setColor([hue: (240/360)*100, saturation: 100, switch: "on"])
            } else if (light.displayName ==~ /^.*\bPorch\b.*\bStep\b.*$/) {
                light.setColor([hue: (0/360)*100, saturation: 0, switch: "on"])
            } else if ("Color Control" in light.capabilities?.name) {
                light.setColor([hue: (120/360)*100, saturation: 100, switch: "on"])
            }
        }
        messages << "Happy Veterans Day!"
    } else if (todayDate.format("MM-u",todayTimeZone) == "11-4"
    			&& minusThreeW.format("MM",todayTimeZone) == "11"
                && minusFourW.format("MM",todayTimeZone) != "11") {				             //Thanksgiving
        for (light in settings.lights) {
            light.on()
            if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                light.setColor([hue: (0/360)*100, saturation: 75, switch: "on"])
            } else if (light.displayName ==~ /^.*\bPorch\b.*\bStep\b.*$/) {
                light.setColor([hue: (0/360)*100, saturation: 100, switch: "on"])
            } else if ("Color Control" in light.capabilities?.name) {
                light.setColor([hue: (30/360)*100, saturation: 100, switch: "on"])
            }
        }
        messages << "Happy Thanksgiving!"
    } else if (todayDate.format("MM-dd",todayTimeZone) >= "12-24"
    		&& todayDate.format("MM-dd",todayTimeZone) <= "12-25") {			           //Christmas Eve / Christmas Day
        for (light in settings.lights) {
            light.on()
            if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                light.setColor([hue: (0/360)*100, saturation: 100, switch: "on"])
            } else if (light.displayName ==~ /^.*\bPorch\b.*\bStep\b.*$/) {
                light.setColor([hue: (0/360)*100, saturation: 100, switch: "on"])
            } else if ("Color Control" in light.capabilities?.name) {
                light.setColor([hue: (120/360)*100, saturation: 100, switch: "on"])
            }
        }
        messages << "Merry Christmas!"
    } else if (todayDate.format("yyyy-MM-dd H:m",todayTimeZone) >= "2017-12-12 12:00"
            && todayDate.format("yyyy-MM-dd H:m",todayTimeZone) == "2017-12-20 12:00") {	//HANUKKAH
        for (light in settings.lights) {
            light.on()
            if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                light.setColor([hue: (222/360)*100, saturation: 100, switch: "on"])
            } else if (light.displayName ==~ /^.*\bPorch\b.*\bStep\b.*$/) {
                light.setColor([hue: (222/360)*100, saturation: 100, switch: "on"])
            } else if ("Color Control" in light.capabilities?.name) {
                light.setColor([hue: (0/360)*100, saturation: 0, switch: "on"])
            }
        }
        messages << "Happy Hanukkah!"
    } else {
        for (light in settings.lights) {
            light.on()
            if (light.displayName ==~ /^.*\bGardenspots\b.*$/ || light.displayName ==~ /^.*\bPorch\b.*\bStep\b.*$/) {
                light.setColor([hue: 8, saturation: 82, switch: "on"])
            } else if ("Color Temperature" in light.capabilities?.name) {
                light.setColorTemperature(2700)
            }
        }
    }

    for (message in messages) {
        // check that contact book is enabled and recipients selected
        if (location.contactBookEnabled && recipients) {
            sendNotificationToContacts(message, recipients)
        } else {
            sendPush(message)
        }
    }
}

void sunriseHandler(evt) {
    settings.lights?.off()
}

void appHandler(evt) {
	def todayDate = new Date()
    def sunriseSunset = getSunriseAndSunset()
    if (todayDate > sunriseSunset.sunset || todayDate < sunriseSunset.sunrise) { sunsetHandler(evt) }
    else { sunriseHandler(evt) }
}