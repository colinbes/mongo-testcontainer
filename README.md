# Example of using dimafeng TestContainer for using docker compose
 ref url: https://github.com/testcontainers/testcontainers-scala
 
# Comments
A lot of time was wasted getting to this work with me not realizng I was referring to older documentation.
The Api for setting up containers changed in new version.

Another issue I had was waiting for docker-compose to fully start up mongo before connecting mongodb client. This was resolved using the Wait.flor... method when setting up ContainerDef

# Sample tests

Tests using Scala test for inserting and querying database. This project is not intended to describe best practice in database operation.
Tests include using json4s for marshalling and unmarshalling of case class to mongo document as well as using Mongo's fromProviders macro to create Codecs to automatically handle reading/writing of case class to database

## Running tests

IntelliJ run/debug configuration files are provided for running `DeviceCodecTest` and `DeviceJsonTest` - make sure that VM env is set to include `application-test.conf` file.

For example `-Dconfig.file=<path to file>/application-test.conf`