# block2
您的第一个Java区块链
后台数据库采用mongodb
网路tomcat 8.5.54
pom.xml:



        <!-- https://mvnrepository.com/artifact/org.json/json -->

   <dependencies>
   
        <dependency>
            <groupId>org.json</groupId>
            <artifactId>json</artifactId>
            <version>20151123</version>
        </dependency>
     <dependency>
            <groupId>javax</groupId>
            <artifactId>javaee-api</artifactId>
            <version>7.0</version>
            <scope>provided</scope>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.10</version>
            <scope>provided</scope>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.mongodb/mongo-java-driver -->
        <dependency>
            <groupId>org.mongodb</groupId>
            <artifactId>mongo-java-driver</artifactId>
            <version>3.0.0</version>
        </dependency>


        <!-- https://mvnrepository.com/artifact/com.google.code.gson/gson -->
        <!--   <dependency>
               <groupId>com.google.code.gson</groupId>
               <artifactId>gson</artifactId>
               <version>2.8.5</version>
           </dependency>-->

        <!-- https://mvnrepository.com/artifact/com.alibaba/fastjson -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.67</version>
        </dependency>
    </dependencies>
