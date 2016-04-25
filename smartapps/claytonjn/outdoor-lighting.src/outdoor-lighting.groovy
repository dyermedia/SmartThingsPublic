/**
 *  Outdoor Lighting - 
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
    name: "Outdoor Lighting - ",
    namespace: "claytonjn",
    author: "claytonjn",
    description: "Turns on the outside lights at sunset, turns them off at sunrise.",
    category: "My Apps",
    iconUrl: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn.png",
    iconX2Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@2x.png",
    iconX3Url: "https://raw.githubusercontent.com/claytonjn/SmartThingsPublic/claytonjn-personal/icons/claytonjn@3x.png")


preferences {
	page(name: "page", install:true, uninstall: true) {
        section("Title") {
        	paragraph "Turns on the outside lights at sunset, turns them off at sunrise."
            input "lights", "capability.switch", title: "Light(s)", multiple: true
            input("recipients", "contact", title: "Send notifications to", multiple: true)
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
}

void sunsetHandler(evt) {
	if (location.mode in ["Away", "Home", "Night"]) {
        def todayDate = new Date()
        def minusThreeW = todayDate - 21
        def minusFourW = todayDate - 28
        def plusOneW = todayDate + 7
        if (todayDate.format("MM-dd",TimeZone.getTimeZone('US/Eastern')) == "12-31"
        	|| todayDate.format("MM-dd",TimeZone.getTimeZone('US/Eastern')) == "01-01") {					//New Year's Eve / New Year's Day
            for (light in settings.lights) {
                light.on()
                if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                    light.setColor([hue: (51/360)*100, saturation: 100, switch: "on"])
                }
                else if ("Color Control" in light.capabilities?.name) {
                    light.setColor([hue: (51/360)*100, saturation: 100, switch: "on"])
                }
            }
            def message = "Happy New Year!"
            // check that contact book is enabled and recipients selected
            if (location.contactBookEnabled && recipients) {
                sendNotificationToContacts(message, recipients)
            } else {
                sendPush(message)
            }
        } else if (todayDate.format("MM-dd",TimeZone.getTimeZone('US/Eastern')) == "02-14") {				//Valentine's Day
            for (light in settings.lights) {
                light.on()
                if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                    light.setColor([hue: (350/360)*100, saturation: 24.7, switch: "on"])
                }
                else if ("Color Control" in light.capabilities?.name) {
                    light.setColor([hue: (350/360)*100, saturation: 24.7, switch: "on"])
                }
            }
            def message = "Happy Valentine's Day!"
            // check that contact book is enabled and recipients selected
            if (location.contactBookEnabled && recipients) {
                sendNotificationToContacts(message, recipients)
            } else {
                sendPush(message)
            }
        } else if (todayDate.format("MM-dd",TimeZone.getTimeZone('US/Eastern')) == "03-17") {				//St. Patrick's Day
            for (light in settings.lights) {
                light.on()
                if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                    light.setColor([hue: (120/360)*100, saturation: 100, switch: "on"])
                }
                else if ("Color Control" in light.capabilities?.name) {
                    light.setColor([hue: (120/360)*100, saturation: 100, switch: "on"])
                }
            }
            def message = "Happy St. Patrick's Day!"
            // check that contact book is enabled and recipients selected
            if (location.contactBookEnabled && recipients) {
                sendNotificationToContacts(message, recipients)
            } else {
                sendPush(message)
            }
        } else if (todayDate.format("yyyy-MM-dd",TimeZone.getTimeZone('US/Eastern')) == "2016-03-27") {		//EASTER
            for (light in settings.lights) {
                light.on()
                if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                    light.setColor([hue: (350/360)*100, saturation: 24.7, switch: "on"])
                }
                else if ("Color Control" in light.capabilities?.name) {
                    light.setColor([hue: (300/360)*100, saturation: 100, switch: "on"])
                }
            }
            def message = "Happy Easter!"
            // check that contact book is enabled and recipients selected
            if (location.contactBookEnabled && recipients) {
                sendNotificationToContacts(message, recipients)
            } else {
                sendPush(message)
            }
        } else if (todayDate.format("MM-dd",TimeZone.getTimeZone('US/Eastern')) == "04-16") {				//Cory's Birthday
            for (light in settings.lights) {
                light.on()
                if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                    light.setColor([hue: 7, saturation: 90, switch: "on"])
                }
                else if ("Color Temperature" in light.capabilities?.name) {
                    light.setColorTemperature(2700)
                }
            }
            def message = "Happy Birthday, Cory!"
            // check that contact book is enabled and recipients selected
            if (location.contactBookEnabled && recipients) {
                sendNotificationToContacts(message, recipients)
            } else {
                sendPush(message)
            }
        } else if (todayDate.format("yyyy-MM-dd",TimeZone.getTimeZone('US/Eastern')) == "2016-04-22"
        			|| todayDate.format("yyyy-MM-dd",TimeZone.getTimeZone('US/Eastern')) == "2016-04-28") {	//PASSOVER
            for (light in settings.lights) {
                light.on()
                if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                    light.setColor([hue: (222/360)*100, saturation: 100, switch: "on"])
                }
                else if ("Color Control" in light.capabilities?.name) {
                    light.setColor([hue: (0/360)*100, saturation: 0, switch: "on"])
                }
            }
            def message = "Happy Passover!"
            // check that contact book is enabled and recipients selected
            if (location.contactBookEnabled && recipients) {
                sendNotificationToContacts(message, recipients)
            } else {
                sendPush(message)
            }
        } else if (todayDate.format("yyyy-MM-dd",TimeZone.getTimeZone('US/Eastern')) == "2016-05-08") {		//MOTHER'S DAY
            for (light in settings.lights) {
                light.on()
                if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                    light.setColor([hue: (350/360)*100, saturation: 24.7, switch: "on"])
                }
                else if ("Color Control" in light.capabilities?.name) {
                    light.setColor([hue: (350/360)*100, saturation: 24.7, switch: "on"])
                }
            }
            def message = "Happy Mother's Day!"
            // check that contact book is enabled and recipients selected
            if (location.contactBookEnabled && recipients) {
                sendNotificationToContacts(message, recipients)
            } else {
                sendPush(message)
            }
        } else if (todayDate.format("yyyy-MM-dd",TimeZone.getTimeZone('US/Eastern')) == "2016-05-08") {		//FATHER'S DAY
            for (light in settings.lights) {
                light.on()
                if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                    light.setColor([hue: (240/360)*100, saturation: 100, switch: "on"])
                }
                else if ("Color Control" in light.capabilities?.name) {
                    light.setColor([hue: (240/360)*100, saturation: 100, switch: "on"])
                }
            }
            def message = "Happy Father's Day!"
            // check that contact book is enabled and recipients selected
            if (location.contactBookEnabled && recipients) {
                sendNotificationToContacts(message, recipients)
            } else {
                sendPush(message)
            }
        } else if (todayDate.format("MM-u",TimeZone.getTimeZone('US/Eastern')) == "05-1"
        			&& plusOneW.format("MM",TimeZone.getTimeZone('US/Eastern')) != "05") {					//Memorial Day
            for (light in settings.lights) {
                light.on()
                if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                    light.setColor([hue: (0/360)*100, saturation: 100, switch: "on"])
                }
                else if ("Color Control" in light.capabilities?.name) {
                    light.setColor([hue: (240/360)*100, saturation: 100, switch: "on"])
                }
            }
            def message = "Happy Memorial Day!"
            // check that contact book is enabled and recipients selected
            if (location.contactBookEnabled && recipients) {
                sendNotificationToContacts(message, recipients)
            } else {
                sendPush(message)
            }
        } else if (todayDate.format("MM-dd",TimeZone.getTimeZone('US/Eastern')) == "06-16") {				//Clayton's Birthday
            for (light in settings.lights) {
                light.on()
                if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                    light.setColor([hue: 7, saturation: 90, switch: "on"])
                }
                else if ("Color Temperature" in light.capabilities?.name) {
                    light.setColorTemperature(2700)
                }
            }
            def message = "Happy Birthday, Clayton!"
            // check that contact book is enabled and recipients selected
            if (location.contactBookEnabled && recipients) {
                sendNotificationToContacts(message, recipients)
            } else {
                sendPush(message)
            }
        } else if (todayDate.format("MM-dd",TimeZone.getTimeZone('US/Eastern')) == "07-04") {				//July 4
            for (light in settings.lights) {
                light.on()
                if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                    light.setColor([hue: (0/360)*100, saturation: 100, switch: "on"])
                }
                else if ("Color Control" in light.capabilities?.name) {
                    light.setColor([hue: (240/360)*100, saturation: 100, switch: "on"])
                }
            }
            def message = "Happy 4th of July!"
            // check that contact book is enabled and recipients selected
            if (location.contactBookEnabled && recipients) {
                sendNotificationToContacts(message, recipients)
            } else {
                sendPush(message)
            }
        } else if (todayDate.format("yyyy-MM-dd",TimeZone.getTimeZone('US/Eastern')) == "2016-10-02"
        			|| todayDate.format("yyyy-MM-dd",TimeZone.getTimeZone('US/Eastern')) == "2016-10-03") {	//ROSH HASHANAH
            for (light in settings.lights) {
                light.on()
                if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                    light.setColor([hue: (222/360)*100, saturation: 100, switch: "on"])
                }
                else if ("Color Control" in light.capabilities?.name) {
                    light.setColor([hue: (0/360)*100, saturation: 0, switch: "on"])
                }
            }
            def message = "Happy Rosh Hashanah!"
            // check that contact book is enabled and recipients selected
            if (location.contactBookEnabled && recipients) {
                sendNotificationToContacts(message, recipients)
            } else {
                sendPush(message)
            }
        } else if (todayDate.format("yyyy-MM-dd",TimeZone.getTimeZone('US/Eastern')) == "2016-10-11") {		//YOM KIPPUR
            for (light in settings.lights) {
                light.on()
                if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                    light.setColor([hue: (222/360)*100, saturation: 100, switch: "on"])
                }
                else if ("Color Control" in light.capabilities?.name) {
                    light.setColor([hue: (0/360)*100, saturation: 0, switch: "on"])
                }
            }
            def message = "Happy Yom Kippur!"
            // check that contact book is enabled and recipients selected
            if (location.contactBookEnabled && recipients) {
                sendNotificationToContacts(message, recipients)
            } else {
                sendPush(message)
            }
        } else if (todayDate.format("MM-dd",TimeZone.getTimeZone('US/Eastern')) == "10-31") {				//Halloween
            for (light in settings.lights) {
                light.on()
                if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                    light.setColor([hue: (30/360)*100, saturation: 100, switch: "on"])
                }
                else if ("Color Control" in light.capabilities?.name) {
                    light.setColor([hue: (300/360)*100, saturation: 100, switch: "on"])
                }
            }
            def message = "Happy Halloween!"
            // check that contact book is enabled and recipients selected
            if (location.contactBookEnabled && recipients) {
                sendNotificationToContacts(message, recipients)
            } else {
                sendPush(message)
            }
        } else if (todayDate.format("MM-dd",TimeZone.getTimeZone('US/Eastern')) == "11-11") {				//Veterans Day
            for (light in settings.lights) {
                light.on()
                if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                    light.setColor([hue: (0/360)*100, saturation: 100, switch: "on"])
                }
                else if ("Color Control" in light.capabilities?.name) {
                    light.setColor([hue: (240/360)*100, saturation: 100, switch: "on"])
                }
            }
            def message = "Happy Veterans Day!"
            // check that contact book is enabled and recipients selected
            if (location.contactBookEnabled && recipients) {
                sendNotificationToContacts(message, recipients)
            } else {
                sendPush(message)
            }
        } else if (todayDate.format("MM-u",TimeZone.getTimeZone('US/Eastern')) == "11-4"
        			&& minusThreeW.format("MM",TimeZone.getTimeZone('US/Eastern')) == "11"
                    && minusFourW.format("MM",TimeZone.getTimeZone('US/Eastern')) != "11") {				//Thanksgiving
            for (light in settings.lights) {
                light.on()
                if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                    light.setColor([hue: (0/360)*100, saturation: 100, switch: "on"])
                }
                else if ("Color Control" in light.capabilities?.name) {
                    light.setColor([hue: (30/360)*100, saturation: 100, switch: "on"])
                }
            }
            def message = "Happy Thanksgiving!"
            // check that contact book is enabled and recipients selected
            if (location.contactBookEnabled && recipients) {
                sendNotificationToContacts(message, recipients)
            } else {
                sendPush(message)
            }
        } else if (todayDate.format("MM-dd",TimeZone.getTimeZone('US/Eastern')) == "12-24"
        			|| todayDate.format("MM-dd",TimeZone.getTimeZone('US/Eastern')) == "12-25") {			//Christmas Eve / Christmas Day
            for (light in settings.lights) {
                light.on()
                if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                    light.setColor([hue: (0/360)*100, saturation: 100, switch: "on"])
                }
                else if ("Color Control" in light.capabilities?.name) {
                    light.setColor([hue: (120/360)*100, saturation: 100, switch: "on"])
                }
            }
            def message = "Merry Christmas!"
            // check that contact book is enabled and recipients selected
            if (location.contactBookEnabled && recipients) {
                sendNotificationToContacts(message, recipients)
            } else {
                sendPush(message)
            }
        } else if (todayDate.format("yyyy-MM-dd",TimeZone.getTimeZone('US/Eastern')) == "2016-12-29") {		//HANUKKAH
            for (light in settings.lights) {
                light.on()
                if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                    light.setColor([hue: (222/360)*100, saturation: 100, switch: "on"])
                }
                else if ("Color Control" in light.capabilities?.name) {
                    light.setColor([hue: (0/360)*100, saturation: 0, switch: "on"])
                }
            }
            def message = "Happy Hanukkah!"
            // check that contact book is enabled and recipients selected
            if (location.contactBookEnabled && recipients) {
                sendNotificationToContacts(message, recipients)
            } else {
                sendPush(message)
            }
        } else {
            for (light in settings.lights) {
                light.on()
                if (light.displayName ==~ /^.*\bGardenspots\b.*$/) {
                    light.setColor([hue: 7, saturation: 90, switch: "on"])
                }
                else if ("Color Temperature" in light.capabilities?.name) {
                    light.setColorTemperature(2700)
                }
            }
        }
    }
}

void sunriseHandler(evt) {
	if (location.mode in ["Away", "Home", "Night"]) {
    	settings.lights?.off()
    }
}

void appHandler(evt) {
	def todayDate = new Date()
    def sunriseSunset = getSunriseAndSunset()
    if (todayDate > sunriseSunset.sunset || todayDate < sunriseSunset.sunrise) { sunsetHandler(evt) }
    else { sunriseHandler(evt) }
}
