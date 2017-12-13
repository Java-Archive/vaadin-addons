# vaadin-addons
A bunch of Vaadin Add Ons..


TODO´s

* ServletContainerExtension : generic ServletContainer Start/Stop



## Selenoid in Docker


## optionals 
docker pull selenoid/phantomjs:2.1.1

docker pull aerokube/cm:latest

[--browsers firefox,opera] [--last-versions 2] [--tmpfs 128]

docker run --rm \
-v /var/run/docker.sock:/var/run/docker.sock \
aerokube/cm:latest \
selenoid \
update \
--tmpfs 128 \  
--browsers chrome,firefox,opera 
--last-versions 4 > _data/selenoid/config/browsers.json

docker pull selenoid/hub
docker pull selenoid/video-recorder


docker run -d --name selenoid \
-p 4444:4444                                    \
-v /var/run/docker.sock:/var/run/docker.sock    \
-v `pwd`/config/:/etc/selenoid/:ro              \
-v `pwd`/video/:/opt/selenoid/video/            \
-e OVERRIDE_VIDEO_OUTPUT_DIR=`pwd`/video/       \
aerokube/selenoid:latest-release


docker run -d --name selenoid-ui --link selenoid \
-p 8080:8080 aerokube/selenoid-ui \
--selenoid-uri=http://selenoid:4444

