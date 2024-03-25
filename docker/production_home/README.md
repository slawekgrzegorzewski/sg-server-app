# Deployment
1. `mkdir ~/Application` on a machine
2. Copy `secrets.zip` and `circle_ci_id_rsa.pub` files to `~/Application` dir
3. Run:
   ```bash
   mkdir $HOME/.ssh
   cat ~/Application/circle_ci_id_rsa.pub | tee -a $HOME/.ssh/authorized_keys
   sudo service sshd restart
   ```
4. `gradle infrastructureRpi4`, copy `build/distributions/infrastructureRpi4` to `~/Application`  
5. Make sure there is only one network interface set up (if you are connected trought ethernet and wifi docker swarm init will fail)
6. ```bash
   cd /home/slawek/Application 
   unzip infrastructureRpi4.zip -d tmp 
   cd tmp 
   dos2unix *.sh 
   chmod +x *.sh 
   ./setup.sh slawek $AWS_ACCESS_KEY_ID $AWS_SECRET_ACCESS_KEY $AWS_CW_ACCESS_KEY_ID $AWS_CW_SECRET_ACCESS_KEY $SG_DB_PASSWORD
   ```
7. `gradle dockerPackageRpi4`, copy `build/distributions/dockerPackageRpi4` to `~/Application`
8. copy db backup to `management/database-backups` directory 
8. ```bash
   cd ~/Application
   unzip dockerRpi4.zip -d dockerRpi4
   cd dockerRpi4/setup/
   dos2unix *
   chmod +x *.sh
   ./deploy_files.sh
   cd management
   ./start-db-only.sh
   ./restore_data.sh <<db_backup_file>>
   ./stop.sh
   ./start.sh
   ```