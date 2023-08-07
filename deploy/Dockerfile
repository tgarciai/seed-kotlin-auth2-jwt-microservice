FROM gradle:8.2.1-alpine AS build
ARG SENTRY_AUTH_TOKEN
ARG SENTRY_ORG
ARG APP_PORT
ENV SENTRY_AUTH_KEY=$SENTRY_AUTH_TOKEN
ENV SENTRY_ORG=$SENTRY_ORG
WORKDIR /app
COPY src .
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY start.sh .
RUN gradle clean build -x :bootRun -x test --continue
FROM amazoncorretto:17.0.8-alpine3.18
EXPOSE $APP_PORT
COPY --from=build /app/build/libs/app.jar app.jar
CMD bash start.sh
