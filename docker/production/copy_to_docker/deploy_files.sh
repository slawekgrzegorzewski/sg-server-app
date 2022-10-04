chmod 400  ~/.pgpass

dos2unix *

mkdir deploy
mkdir management

cp application.yml.tmpl deploy
chmod 644 deploy/application.yml.tmpl
cp docker-compose* deploy
chmod 644 deploy/docker-compose*
cp start*.sh deploy
cp stop.sh deploy

chmod 755 deploy/*.sh

cp backup_data.sh management/
cp restore_data.sh management/
cp setup_machine.sh management/
chmod 755 management/*.sh

cd ../
rm -rf tmp