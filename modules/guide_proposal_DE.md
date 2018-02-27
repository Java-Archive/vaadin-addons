# Grundidee

Es werden in der Konfig `Grids` definiert. Ein `Grid` beschreibt eine technische Verbindung zu einem Webdriver und gibt an, welche Browser in welchen Version auf welchen Betriebssystem dieses `Grid` unterstützt.

Alle `@VaadinCompatTests` laufen grundsätzlich auf allen Browser/Version/OS Kombination der aktiven Grids. Grids die ein `Tag` besitzen werden nur von Tests genutzt die auch dieses `Tag` besitzen.

Grundsätzlich sind alle Grids aktiv, lediglich wenn explizit eine Auswahl von aktiven Grids getroffen wird, ist dies nicht der Fall. 

# Getting started

  1. Testbench key installieren
  1. TextbenchExtensions als Abhängikeit hinzufügen
  1. PageObject erstellen
  1. Test mit @VaadinUnitTest schreiben
  1. mvn clean install
  
## was passiert
  
  * Browser geht auf
  * Maus bewegt sich
  * Test wird grün
  
  
Wichtig: ich habe **keine** Konfiguration angelegt.

# Using CI
  
  * Installiere Selenoid/Selenium-Grid oder was ähnliches, oder binde es als Service in die CI-Konfig ein
  * Erstelle im Projekt die Datei `.testbenchextensions-ci/config.properties` mit dem Inhalt: 
  
```
unittesting.browser=firefox
unittesting.target=localhost/selenoid
```
  
  * Setze die System Property `rapidpm.configlocation=/path_to_git_checkout/.testbenchextensions-ci` in der CI-Konfig
  * push
      
## was hats gebracht

   * Auf dem CI-Server wird mit einer stabilen Konfiguration getestet. Lokal hat sich nichts verändert.
   
# Mich nervt das Browserfenster
  * Installiere Selenoid/Selenium-Grid oder was ähnliches
  * Erstelle im Projekt die Datei `~/.testbenchextensions/config.properties` mit dem Inhalt: 
  
```
unittesting.browser=firefox
unittesting.target=localhost
```
  * mvn clean install
  
## was hat's gebraucht?
  
  * die lokale Konfiguration wird genutzt und ich habe keine Browserfenster mehr da.
  
# Das Projekt wächst

  * Es gibt mehr PageObjects
  * Wir brauchen umfangreichere Tests auf verschiedenen Browsern
  
  * Schreibe tests mit @VaadinCompTest
  * `.testbenchextensions-ci/config.properties`:
  
```
compattesting.grid.selenoid.target=localhost
compattesting.grid.selenoid.os=linux
compattesting.grid.selenoid.browser=chrome,firefox
compattesting.grid.selenoid.browser.firefox.version=52,57
compattesting.grid.selenoid.browser.chrome.version=62,63
```

  * lokal laufen nur die Unit-Tests, deshalb `~/.testbenchextensions/config.properties`:
  
```
compattesting.grid.local.target=local # oder localhost, ip
compattesting.grid.local.browser=firefox
```
  
# Vererben von Configs

## Der neue Mitarbeiter beschwert sich, das er ja eine lokale Konfiguration braucht, das sei ja nicht mehr zeitgemäß.

  * `src/test/resources/.testbenchextensions/config.properties`:
  
```
compattesting.grid.selenoid.target=localhost
compattesting.grid.selenoid.os=linux
compattesting.grid.selenoid.browser=chrome,firefox
compattesting.grid.selenoid.browser.firefox.version=52,57
compattesting.grid.selenoid.browser.chrome.version=62,63

compattesting.grid.local.target=local
compattesting.grid.local.browser=firefox

compattesting.grid=local
```


  * `.testbenchextensions-ci/config.properties`:
  
```
compattesting.grid=selenoid  # überschreibt compattesting.grid=local
unittesting.browser=firefox
unittesting.target=localhost
```

## Ein anderer Mitarbeiter hat lokal Selenoid installiert und möchte das Browserfesnter nicht sehen


  * `~/.testbenchextensions-ci/config.properties`:
  
```
compattesting.grid.local.target=localhost # Überschreibt das target local - der rest bleibt da
unittesting.target=localhost
```

## Der QS Mitarbeiter möchte alle Tests vom lokalen Rechner aus starten und sich danach die Videos anschauen


  * `~/.testbenchextensions-ci/config.properties`:
  
```
compattesting.grid=selenoid
compattesting.grid.selenoid.target=192.168.123.4
compattesting.grid.selenoid.enableVideo=true
```

  * Danach liegen auf dem Selenoid server Videos für alle Browser/Versions Kombinationen
  

## Der QS Mitarbeiter möchte alle Tests vom lokalen Rechner aus starten, aber zusätzlich bei Browserstack


  * `~/.testbenchextensions-ci/config.properties`:
  
```
compattesting.grid=browserstack, selenoid

compattesting.grid.selenoid.target=192.168.123.4

compattesting.grid.browserstack.type=browserstack
compattesting.grid.browserstack.username=qs@vaadin.com
compattesting.grid.browserstack.key=ABCA3535 # oder als java propertie setzen
compattesting.grid.browserstack.devices=dev1,dev2

compattesting.grid.browserstack.device.dev1.browserName=android
compattesting.grid.browserstack.device.dev1.device=Samsung Galaxy Tab 4
compattesting.grid.browserstack.device.dev1.realMobile=true
compattesting.grid.browserstack.device.dev1.os_version=4.4

compattesting.grid.browserstack.device.dev2.browserName=...
```

# Tagging

QS hat durchgesetzt, daß auch die CI-Tests bei browserstack laufen sollen, das Mangement sagt aber, nicht alle ist zu teuer.

  * Die Tests die bei browserstack laufen sollen werden mit @VaadinCompatTest("browserstack") annotiert
  
 
```
compattesting.grid.browserstack.tag=browserstack
```
  