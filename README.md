# Exchange REST Service

This service is exposing a REST endpoint that retrieves the EURO conversion rate
for any other currency provided by European Central Bank for any of the past 90 days

Steps to run the service
1. Execute 'mvn clean package' from exchange-service directory
2. Execute 'java -jar target/exchange-service-0.0.1-SNAPSHOT.jar'

Note: By default the server will start on port 8080, but the port could be
changed by setting the desired port on the property server.port from
src/main/resources/application.properties

Sample GET - http://localhost:8080//rest/exchange/rate/USD?date=2017-03-28
