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
