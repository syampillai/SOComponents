cd ~/IdeaProjects/SOComponents
rm -fr node_modules
cd src/main
rm -fr resources
mkdir -p resources/META-INF/resources/frontend
cd resources/META-INF/resources/frontend
ln -s ../../../../../../frontend/so .
cd ~/IdeaProjects/SOComponents
mvn clean install -Pdirectory
mkdir -p zipTarget
rm zipTarget/*.zip
cp target/*.zip zipTarget
cd src/main
rm -fr resources
cd ~/IdeaProjects/SOComponents
mvn clean
rm -fr node_modules
