# vaadin-addons
A bunch of Vaadin Add Ons..

## Selenoid in Docker

## optionals 


## needed

This is needed to manage the driver images 

```
docker pull aerokube/cm:latest
docker pull selenoid/hub
docker pull selenoid/video-recorder
docker pull selenoid/phantomjs:2.1.1
```

This is needed to pull the docker images for the last 4 versions

```
docker run --rm \
-v /var/run/docker.sock:/var/run/docker.sock \
aerokube/cm:latest \
selenoid \
update \
--tmpfs 128 \
--browsers chrome,firefox,opera,phantomjs \
--last-versions 4 > _data/selenoid/config/browsers.json
```


start the selenoid node

```
docker run -d --name selenoid \
-p 4444:4444                                    \
-v /var/run/docker.sock:/var/run/docker.sock    \
-v `pwd`/_data/selenoid/config/:/etc/selenoid/:ro              \
-v `pwd`/_data/selenoid/video/:/opt/selenoid/video/            \
-e OVERRIDE_VIDEO_OUTPUT_DIR=`pwd`/_data/selenoid/video/       \
aerokube/selenoid:latest-release
```

start the ui for selenoid if you want, not needed

```
docker run -d --name selenoid-ui --link selenoid \
-p 8080:8080 aerokube/selenoid-ui \
--selenoid-uri=http://192.168.0.228:4444
```

