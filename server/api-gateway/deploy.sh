#!/bin/bash

echo Building Docker Image
tag="chat-app/api-gateway"
docker build -t $tag . --rm
echo Docker Image built with tag $tag

deployment="api-gateway-deployment"
deployment_file="api-gateway-deployment.yaml"
namespace="chat-app"
kubectl delete deployment $deployment -n $namespace
kubectl apply -f $deployment_file