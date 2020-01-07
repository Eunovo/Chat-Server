#!/bin/bash

echo Building App using Maven
./mvnw package
echo Build Completed

echo Building Docker Image
tag="chat-app/user-service"
docker build -t $tag . --rm
echo Docker Image built with tag $tag

deployment="user-service-deployment"
deployment_file="user-service-deployment.yaml"
namespace="chat-app"
kubectl delete deployment $deployment -n $namespace
kubectl apply -f $deployment_file