FROM openjdk:8-jre-alpine

WORKDIR /

COPY jardueraFinalak/eventManager/eventManager.jar /eventManager.jar

COPY .kube/config ~/.kube/config

EXPOSE 8080

CMD ["java", "-jar", "/eventManager.jar"]