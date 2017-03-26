# Cloud Foundry C2C Networking for legacy Java apps

This sample shows how to use Container-to-Container Networking in **Non Spring Boot Java App**.

See also

* https://www.cloudfoundry.org/meet-new-container-networking-stack-cloud-foundry/
* http://docs.pivotal.io/pivotalcf/1-10/concepts/understand-cf-networking.html
* https://docs.cloudfoundry.org/devguide/deploy-apps/cf-networking.html

Note that Spring Boot + Spring Cloud is muuuuuch easier than this sample.

### Deploy Eureka Server

```
cd eureka-server
mvn clean package -DskipTests=true
cf push
```

### Deploy Backend

Set eureka-server's url to `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE` in `manifest.yml`

```
cd backend
mvn clean package -DskipTests=true
cf push
```

### Deploy Frontend

Set eureka-server's url to `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE` in `manifest.yml`

```
cd frontend
mvn clean package -DskipTests=true
cf push
```

### Enable Container to Container Networking

```
cf allow-access frontend backend --protocol tcp --port 8080
```

Access frontend app.

Then scale out backend

```
cf scale -i 3 backend
```