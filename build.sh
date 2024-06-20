export JAVA_HOME="/usr/lib64/java/jdk-17.0.1"

echo "Building jar..."

mvn clean package

echo "Starting docker..."

systemctl start docker

echo "Docker started."

echo "Build pictures-api docker image..."

docker build . -t ickee953/pictures-api:1.0.0

echo "Done."

echo "To run PictureService type on terminal: 'docker run ickee953/pictures-api:1.0.0' and open browser at http://localhost:8080/swagger-ui.html"
