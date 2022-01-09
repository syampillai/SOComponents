. ~/.nvm/nvm.sh
nvm use v16.13.1
cd
rm jdk
ln -s jdkServer jdk
cd ~/IdeaProjects/SOComponents
cd src/main
rm -fr resources
mkdir -p resources/META-INF/resources/frontend
cd resources/META-INF/resources/frontend
ln -s ../../../../../../frontend/so .
cd ~/IdeaProjects/SOComponents
mvn clean install -Pdirectory
mkdir -p zipTarget
rm -f zipTarget/*.zip
cp target/*.zip zipTarget
cd
rm jdk
ln -s jdkLatest jdk
