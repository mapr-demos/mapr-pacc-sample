FROM maprdocker.lab/pacc:5.2.0_2.0_centos6

# Create a directory for your MapR Application and copy the Application
RUN mkdir -p /usr/share/mapr-apps/
COPY ./target/sensor-service-1.0-SNAPSHOT.jar /usr/share/mapr-apps/sensor-service.jar
COPY run.sh /usr/share/mapr-apps/run.sh
RUN chmod +x /usr/share/mapr-apps/run.sh

CMD ["start", "/usr/share/mapr-apps/run.sh", "/apps/sensors:computer"]
