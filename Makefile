# Build parameters
OUT?=./target
DOCKER_TMP?=$(OUT)/docker_temp/
DOCKER_SERVER_TAG?=aeraki/thrift-sample-server:latest
DOCKER_CLIENT_TAG?=aeraki/thrift-sample-client:latest

Abuild:
	mvn install
docker-build: build
	rm -rf $(DOCKER_TMP)
	mkdir $(DOCKER_TMP)
	cp target/*.jar $(DOCKER_TMP)
	cp ./docker/Dockerfile.server $(DOCKER_TMP)Dockerfile
	docker build -t $(DOCKER_SERVER_TAG) $(DOCKER_TMP)
	cp ./docker/Dockerfile.client $(DOCKER_TMP)Dockerfile
	docker build -t $(DOCKER_CLIENT_TAG) $(DOCKER_TMP)
	rm -rf $(DOCKER_TMP)
docker-push: docker-build
	docker push $(DOCKER_SERVER_TAG)
	docker push $(DOCKER_CLIENT_TAG)
clean:
	mvn clean
	rm -rf $(OUT)

.PHONY: build docker-build docker-push clean
