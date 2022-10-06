1. Set up Circle CI 
   1. Environment variable
      1. AWS_ACCESS_KEY_ID - from 1Password
      4. AWS_ECR_REGISTRY_ID - from AWS
      5. AWS_SECRET_ACCESS_KEY - from 1Password
      6. SSH_HOST
      7. SSH_USER
   2. Additional SSH keys
      1. private key for SSH to a server
2. Manual set up of server
   1. change `root` password
   2. create new `USERNAME` user with `PASSWORD`
3. Automatic set up
   1. copy `docker - ovh.zip` file from `Docker - secrets` 1Password entry to `<<dir1>>`
   2. copy db backup to be restored to `<<dir2>>` 
   3. run `dev-scripts/setup/setup-machine.sh grzegorzewski.org root slawek setup <<dir1>> AWS_ACCESS_ID AWS_SECRET_ACCESS_KEY AWS_ACCESS_ID_LOGS AWS_SECRET_ACCESS_KEY_LOGS POSTGRESS_PASSWORD`
1. Login to a server
   1. set up crontab
      1. `0 * * * * /home/slawek/management/backup_data.sh`
4. run `dev-scripts/setup/setup-machine.sh grzegorzewski.org root slawek restore_db <<dir2>>` 