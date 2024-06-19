export JAVA_HOME="/usr/lib64/java/jdk-17.0.1"

echo "Building jar..."

mvn clean package

echo "Starting docker..."

systemctl start docker

echo "Docker started."

echo "Build pictures-api docker image..."

docker build . -t ickee953/pictures-api:1.0.0

echo "Done."

echo "Creating Kubernetes cluster..."

kind create cluster --config kind-config.yaml

echo "Done."

echo "Loading pictures-api docker image on cluster..."

kind load docker-image ickee953/pictures-api:1.0.0

echo "Done."

echo "Apply k8s configs..."

kubectl apply -f k8s/deployment.yaml

echo "Deployments done."

kubectl apply -f k8s/service.yaml

echo "Service done."

kubectl apply -f k8s/ingress.yaml

echo "Ingress plugin config done."

echo "Kubernetes pods:"

kubectl get pods

echo "Kubernetes services:"

kubectl get services

echo "Done."
