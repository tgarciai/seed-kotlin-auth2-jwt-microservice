FROM gradle:8.2.1 AS build
ARG SENTRY_AUTH_TOKEN
ARG SENTRY_ORG
ARG APP_PORT
ENV SENTRY_AUTH_KEY=$SENTRY_AUTH_TOKEN
ENV SENTRY_ORG=$SENTRY_ORG
WORKDIR /app
COPY src/ src/
COPY build.gradle.kts .
COPY settings.gradle.kts .
RUN gradle clean build -x :bootRun -x test --continue
COPY start.sh .
FROM amazoncorretto:17.0.8
EXPOSE $APP_PORT
WORKDIR /app
COPY --from=build /app/build/libs/app.jar /app/app.jar
COPY --from=build /app/start.sh start.sh
CMD /bin/sh start.sh
