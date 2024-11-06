import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
  url: "http://localhost:8090",
  realm: "flashback",
  clientId: "flashback-client",
});

export default keycloak;
