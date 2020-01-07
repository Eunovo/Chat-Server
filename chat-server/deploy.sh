#!/bin/bash

echo Building Docker Image
tag="chat-app/chat-server"
docker build -t $tag . --rm
echo Docker Image built with tag $tag

service_file="chat-server-service.yaml"
kubectl apply -f $service_file

deployment="chat-server-deployment"
deployment_file="chat-server-deployment.yaml"
namespace="chat-app"
kubectl delete deployment $deployment -n $namespace
kubectl apply -f $deployment_file