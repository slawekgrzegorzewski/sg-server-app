# Deployment
1. Build docker images for backend and frontend
   `dev-ops\buildAndPushImage.ps1 tag`
2. Copy content of `docker\production\copy_to_docker` directory to a remote directory
3. Copy zip content with secrets to the same remote directory 
4. run `start.sh`