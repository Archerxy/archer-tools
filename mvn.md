# mvn install to local repository
mvn install:install-file -DgroupId="com.archer" -DartifactId="archer-tools" -Dversion="${version}" -Dpackaging=jar -Dfile=${path:}/archer-tools-1.1.5.jar


# pom 
<dependency>
    <groupId>com.archer</groupId>
    <artifactId>archer-tools</artifactId>
    <version>${version}</version>
</dependency>
