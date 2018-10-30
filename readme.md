# Scala + Play Framework bare-bones auth demo

This is a sample app for doing OpenID Connect (OIDC) flow without any libraries.

## To configure:

The app needs a few environment variables to run:

* **AUTH_DOMAIN**: The domain of the OIDC account
* **AUTH_CLIENTID**: The client ID of the app
* **AUTH_SECRET**: The secret key

## To run:

`sbt run 9000`

Then hit `http://localhost:9000` in the browser to perform authentication.
