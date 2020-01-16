#!/bin/bash

service_file="redis-service.yaml"
kubectl apply -f $service_file

kubectl apply -k ./config