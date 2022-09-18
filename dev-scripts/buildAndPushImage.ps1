./gradlew clean build

$env:TAG="215372400964.dkr.ecr.eu-central-1.amazonaws.com/backend:$args"
$env:TAG_LATEST="215372400964.dkr.ecr.eu-central-1.amazonaws.com/backend:latest"
$env:TAG_DEBUG="215372400964.dkr.ecr.eu-central-1.amazonaws.com/backend:$args-debug"
$env:TAG_LATEST_DEBUG="215372400964.dkr.ecr.eu-central-1.amazonaws.com/backend:latest-debug"

docker buildx build -f docker/production/Dockerfile --platform linux/amd64 -t $env:TAG -t $env:TAG_LATEST .
docker buildx build -f docker/production/Dockerfile-debug --platform linux/amd64 -t $env:TAG_DEBUG -t $env:TAG_LATEST_DEBUG .

aws ecr get-login-password --region eu-central-1 | docker login --username AWS --password-stdin 215372400964.dkr.ecr.eu-central-1.amazonaws.com

docker push $env:TAG
docker push $env:TAG_LATEST
docker push $env:TAG_DEBUG
docker push $env:TAG_LATEST_DEBUG