# Deployment
1. mkdir `~/Application` on a machine
2. Copy `secrets.zip` and `circle_ci_id_rsa.pub` files to `~/Application` dir
3. Run:
   ```bash
   mkdir $HOME/.ssh
   cat ~/Application/circle_ci_id_rsa.pub | tee -a $HOME/.ssh/authorized_keys
   sudo service sshd restart
   ```
4. run infrastructure pipeline in circleci