start_all_required_services:
	docker-compose up -d

stop_all_required_services:
	docker-compose down

build:
	docker build --build-arg SENTRY_AUTH_TOKEN=your_sentry_token --build-arg SENTRY_ORG=your_sentry_org --build-arg APP_PORT=your_app_port -f deploy/Dockerfile . -t auth-ms:latest
