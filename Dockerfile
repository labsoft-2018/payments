FROM java:8
MAINTAINER labsoft-2018

ADD target/payments-0.0.1-SNAPSHOT-standalone.jar /srv/payments.jar

EXPOSE 8875

CMD ["java", "-jar", "/srv/payments.jar"]
