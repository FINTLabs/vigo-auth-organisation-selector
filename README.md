# Vigo Authentication Organisation Selector

## Setup certificate
1. Get the certificate from the server

`echo "" | openssl s_client -connect localhost:6360 -showcerts 2>/dev/null | openssl x509 > server.cer`

2. Add the server certificate to a keystore (jks)

`keytool -import -file server.cer -alias namadmcon01 -keystore cacerts.jks`

3. Create a secret in Kubernetes

`kubectl create secret generic <name of secret> --from-literal=truststore_password=changeit --from-file=cacerts.jks`