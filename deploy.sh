#!/bin/sh

echo "Starting docker..."

systemctl start docker

echo "Done."

echo "Creating Kubernetes cluster..."

if [ ! -f ../ServiceConfig/kind-config.yaml ]; then
    echo "Config file for kind not found! Get config repository..."
    git clone https://github.com/ickee953/ServiceConfig.git ../ServiceConfig
fi

kind create cluster --config ../ServiceConfig/kind-config.yaml

echo "Done."

echo "Loading pictures-api docker image on cluster..."

kind load docker-image ickee953/pictures-api:1.0.0

echo "Done."

echo "Apply ingress plugin..."

kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml

kubectl wait --namespace ingress-nginx --for=condition=ready pod --selector=app.kubernetes.io/component=controller --timeout=90s

echo "Apply k8s configs..."

kubectl apply -f k8s/deployment.yaml

echo "Deployments done."

kubectl apply -f k8s/service.yaml

echo "Service done."

if [ ! -f ../ServiceConfig/kind-config.yaml ]; then
    echo "Config file for ingress not found! Get config repository..."
    git clone https://github.com/ickee953/ServiceConfig.git ../ServiceConfig
fi

kubectl apply -f ../ServiceConfig/ingress.yaml

echo "Ingress plugin config done."

echo "Kubernetes pods:"

kubectl get pods

echo "Kubernetes services:"

kubectl get services

echo "Done."

echo "Run PostreSQL DB server..."

if [ ! -f ../ServiceConfig/kind-config.yaml ]; then
    echo "Config file for database docker image not found! Get config repository..."
    git clone https://github.com/ickee953/ServiceConfig.git ../ServiceConfig
fi

docker-compose -f ../ServiceConfig/db-compose-env.yaml up -d

echo "Done."

echo "Open browser at http://localhost:8089/pictures-api/pictures"
