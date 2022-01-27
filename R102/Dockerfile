FROM openjdk:11

EXPOSE ${SPRINGBOOT_PORT}

ADD ./build/libs/*.jar appBatch.jar

ADD ./batchRun.sh batchRun.sh

