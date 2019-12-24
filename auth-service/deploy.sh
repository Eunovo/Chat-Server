#!/bin/bash

echo Building App using Maven
./mvnw package
echo Build Completed

echo Building Docker Image
tag="chat-app/auth-service"
docker build -t $tag .
echo Docker Image built with tag $tag

deployment="auth-service-deployment"
deployment_file="auth-service-deployment.yaml"
namespace="chat-app"
kubectl delete deployment $deployment -n $namespace
kubectl apply -f $deployment_file