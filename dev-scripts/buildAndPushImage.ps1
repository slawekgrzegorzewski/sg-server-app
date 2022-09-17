./gradlew clean build

$env:TAG="slawekgrzegorzewski/backend:$args"
$env:TAG_LATEST="slawekgrzegorzewski/backend:latest"

docker buildx build -f docker/production/Dockerfile --platform linux/amd64 -t $env:TAG -t $env:TAG_LATEST .
docker push $env:TAG
docker push $env:TAG_LATEST