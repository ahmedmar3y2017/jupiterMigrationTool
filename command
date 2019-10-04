swagger ui for service
http://localhost:8085/v2/api-docs
http://localhost:8085/swagger-ui.html


install oracle 6 maven dependency
mvn install:install-file -Dfile=D:/asset/Tool/jupiterMigrationTool/src/main/resources/ojdbc6.jar -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0 -Dpackaging=jar


run this command to get jar without Test
mvn -Dmaven.test.skip=true -DskipTests=true clean install


run war
java -Dfile.encoding=UTF8 -jar warName.war