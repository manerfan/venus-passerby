# This Dockerfile users the java image
# VERSION 1.0.0
# Auther: manerfan
# Github: https://github.com/manerfan/venus-passerby

FROM java:8

MAINTAINER manerfan manerfan.china@gmail.com

COPY build/libs/ /var/lib/venus
COPY region.db /var/lib/venus

VOLUME ["/var/lib/venus"]

WORKDIR /var/lib/venus

ENTRYPOINT ["./venus-passerby.jar", "start"]

HEALTHCHECK --interval=30s --timeout=3s CMD curl -fs http://localhost:16531/actuator/info || exit 1

EXPOSE 16531