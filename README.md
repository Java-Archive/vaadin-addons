
<center>
<a href="https://vaadin.com">
 <img src="https://vaadin.com/images/hero-reindeer.svg" width="200" height="200" /></a>
</center>

# vaadin-addons

[![BrowserStack Status](https://www.browserstack.com/automate/badge.svg?badge_key=RitUcWEyOFVWZmFGY0tTQ0xyREhKM0M1WWtkeG1wdWFLc2xFWm4rVTBldz0tLXNFRWQzM1NrdG5LMHJWUENxeTdTK1E9PQ==--c72faa283696a22e518e89ca57e90e564b2ad348)](https://www.browserstack.com/automate/public-build/RitUcWEyOFVWZmFGY0tTQ0xyREhKM0M1WWtkeG1wdWFLc2xFWm4rVTBldz0tLXNFRWQzM1NrdG5LMHJWUENxeTdTK1E9PQ==--c72faa283696a22e518e89ca57e90e564b2ad348)





This ist the first version of my TestBench Add On. **TestBench** is 
from [https://vaadin.com/](https://vaadin.com/) and could 
be found here [https://vaadin.com/testbench](https://vaadin.com/testbench).

To use/try Vaadin TestBench you can get 
a License / Trail here : [https://vaadin.com/pro/licenses](https://vaadin.com/pro/licenses) 
and if you are using TestBench for an Open Source Project you can apply for
an Open Source License.


The basic documentation about TestBench for Vaadin 8 
is here [https://vaadin.com/docs/testbench/testbench-overview.html](https://vaadin.com/docs/testbench/testbench-overview.html)

## Target of this Add On
The target for this project is the 
optimized handling of the webdrivers and PageObject-Pattern
to write effective and compact tests
with jUnit5 , TestBench and different Selenium Implementations.

A few different ways of writing Junit Tests for Vaadin Apps
you can find here : [https://github.com/vaadin-developer/testbench-jumpstart](https://github.com/vaadin-developer/testbench-jumpstart)

As mentioned before, the documentation is not ready until now.
Have a look at the tests or better, ask me ;-)

email: [mailto:sven.ruppert@gmail.com](mailto:sven.ruppert@gmail.com)
Twitter: [https://twitter.com/SvenRuppert](https://twitter.com/SvenRuppert) 

## Configuration
The Add on is configured by a bunch of files. You have to place them in a folder called `.testbenchextension` is folder can be located at different places:
  1. The root of the classpath
  1. The current work directory
  1. The users home directory
  1. In a directory specified by the property `rapidm.configlocation`
  
Properties defined in the higher locations override properties the lower one.

Templates for the configuration are placed in the `.testbenchextentsions` directories in the modules.
  
## Selenoid in Docker
Selenoid is a nice alternative for Selenium-Hub written in GO.
Give it a try and check the githup repo here : [https://github.com/aerokube/selenoid](https://github.com/aerokube/selenoid)
One easy way of using it, will be based on Docker.

This is needed to manage the driver images 

```bash
docker pull aerokube/cm:latest
docker pull selenoid/hub
docker pull selenoid/video-recorder
docker pull selenoid/phantomjs:2.1.1
```

This is needed to pull the docker images for the last 4 versions of the declared browsers.

````bash
docker run --rm \
-v /var/run/docker.sock:/var/run/docker.sock \
-v `pwd`/selenoid/config:/root/.aerokube/selenoid/ \
aerokube/cm:latest \
selenoid configure \
--tmpfs 128 \
--browsers chrome,firefox,opera,phantomjs \
--last-versions 4
````

To reconfigure the browsers.json, delete the file in the folder
selenoid/browsers.json and container.

start the selenoid hub

```bash
docker run --rm -d --name selenoid \
-p 4444:4444                                    \
-v /var/run/docker.sock:/var/run/docker.sock    \
-v `pwd`/_data/selenoid/config/:/etc/selenoid/:ro              \
-v `pwd`/_data/selenoid/video/:/opt/selenoid/video/            \
-e OVERRIDE_VIDEO_OUTPUT_DIR=`pwd`/_data/selenoid/video/       \
aerokube/selenoid:latest-release
```

start the ui for selenoid if you want, not needed

```bash
docker run --rm -d --name selenoid-ui --link selenoid \
-p 8080:8080 aerokube/selenoid-ui \
--selenoid-uri=http://selenoid:4444
```

