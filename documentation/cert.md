left off: try running everything locally again, make sure password is good for self-signed cert.


# Working locally (localhost)

## Generating your own self-signed certificate (keystore)

keytool -genkey \
    -alias tomcat \
    -storetype PKCS12 \
    -keyalg RSA \
    -keysize 2048 \
    -keystore keystore.p12 \
    -dname "CN=Dan Barrese, OU=Unknown, O=Active Bean Coders, L=Denver, ST=CO, C=US" \
    -keypass mypassword \
    -storepass mypassword \
    -ext SAN=DNS:localhost,IP:127.0.0.1 \
    -validity 3650

## Import keystore into the JRE and generate truststore

keytool \
    -v \
    -importkeystore \
    -srckeystore keystore.p12 \
    -srcstoretype PKCS12 \
    -destkeystore truststore.jks \
    -deststoretype JKS

# Production (activebeancoders.com)

`2016.02.04 21:00:14`
```bash
$ ssh activebeancoders.com
$ git clone https://github.com/letsencrypt/letsencrypt
$ cd letsencrypt
$ ./letsencrypt-auto --help
Creating virtual environment...
Updating letsencrypt and virtual environment dependencies...../home/dan/.local/share/letsencrypt/local/lib/python2.7/site-packages/pip/_vendor/requests/packages/urllib3/util/ssl_.py:315: SNIMissingWarning: An HTTPS request has been made, but the SNI (Subject Name Indication) extension to TLS is not available on this platform. This may cause the server to present an incorrect TLS certificate, which can cause validation failures. For more information, see https://urllib3.readthedocs.org/en/latest/security.html#snimissingwarning.
  SNIMissingWarning
/home/dan/.local/share/letsencrypt/local/lib/python2.7/site-packages/pip/_vendor/requests/packages/urllib3/util/ssl_.py:120: InsecurePlatformWarning: A true SSLContext object is not available. This prevents urllib3 from configuring SSL appropriately and may cause certain SSL connections to fail. For more information, see https://urllib3.readthedocs.org/en/latest/security.html#insecureplatformwarning.
  InsecurePlatformWarning
Command "/home/dan/.local/share/letsencrypt/bin/python2.7 -u -c "import setuptools, tokenize;__file__='/tmp/pip-build-K7Vrzd/cryptography/setup.py';exec(compile(getattr(tokenize, 'open', open)(__file__).read().replace('\r\n', '\n'), __file__, 'exec'))" install --record /tmp/pip-meesAE-record/install-record.txt --single-version-externally-managed --compile --install-headers /home/dan/.local/share/letsencrypt/include/site/python2.7/cryptography" failed with error code 1 in /tmp/pip-build-K7Vrzd/cryptography
/home/dan/.local/share/letsencrypt/local/lib/python2.7/site-packages/pip/_vendor/requests/packages/urllib3/util/ssl_.py:120: InsecurePlatformWarning: A true SSLContext object is not available. This prevents urllib3 from configuring SSL appropriately and may cause certain SSL connections to fail. For more information, see https://urllib3.readthedocs.org/en/latest/security.html#insecureplatformwarning.
  InsecurePlatformWarning
```

```bash
$ python --version
Python 2.7.6

$ sudo apt-get update

$ sudo apt-get upgrade
...lots of output here, and then...
Errors were encountered while processing:
 mysql-server-5.6
E: Sub-process /usr/bin/dpkg returned an error code (1)

$ sudo apt-get install python-pip

$ sudo pip install --upgrade ndg-httpsclient
...lots of output here, and then...
Successfully installed ndg-httpsclient PyOpenSSL six cryptography idna pyasn1 setuptools enum34 ipaddress cffi pycparser
Cleaning up...

$ sudo pip install pyopenssl ndg-httpsclient pyasn1
Requirement already satisfied (use --upgrade to upgrade): pyopenssl in /usr/local/lib/python2.7/dist-packages
Requirement already satisfied (use --upgrade to upgrade): ndg-httpsclient in /usr/local/lib/python2.7/dist-packages
Requirement already satisfied (use --upgrade to upgrade): pyasn1 in /usr/local/lib/python2.7/dist-packages
Cleaning up...

$ alias python=python3

$ ./letsencrypt-auto --help
638 23:15:15 dan@activebeancoders ~/letsencrypt $ ./letsencrypt-auto --help
Updating letsencrypt and virtual environment dependencies......
Requesting root privileges to run with virtualenv: sudo /home/dan/.local/share/letsencrypt/bin/letsencrypt --help

  letsencrypt-auto [SUBCOMMAND] [options] [-d domain] [-d domain] ...

The Let's Encrypt agent can obtain and install HTTPS/TLS/SSL certificates.  By
default, it will attempt to use a webserver both for obtaining and installing
the cert. Major SUBCOMMANDS are:

  (default) run        Obtain & install a cert in your current webserver
  certonly             Obtain cert, but do not install it (aka "auth")
  install              Install a previously obtained cert in a server
  revoke               Revoke a previously obtained certificate
  rollback             Rollback server configuration changes made during install
  config_changes       Show changes made to server config during installation
  plugins              Display information about installed plugins

Choice of server plugins for obtaining and installing cert:

  --apache          Use the Apache plugin for authentication & installation
  --standalone      Run a standalone webserver for authentication
  (nginx support is experimental, buggy, and not installed by default)
  --webroot         Place files in a server's webroot folder for authentication

OR use different plugins to obtain (authenticate) the cert and then install it:

  --authenticator standalone --installer apache

More detailed help:

  -h, --help [topic]    print this message, or detailed help on a topic;
                        the available topics are:

   all, automation, paths, security, testing, or any of the subcommands or
   plugins (certonly, install, nginx, apache, standalone, webroot, etc)

$ ./letsencrypt-auto --apache
urls: www.activebeancoders.com, activebeancoders.com
email: danielbarrese@gmail.com
security level: "Secure: Make all requests redirect to secure HTTPS access"
to test:
https://www.ssllabs.com/ssltest/analyze.html?d=www.activebeancoders.com
https://www.ssllabs.com/ssltest/analyze.html?d=activebeancoders.com

IMPORTANT NOTES:
 - If you lose your account credentials, you can recover through
   e-mails sent to danielbarrese@gmail.com.
 - Congratulations! Your certificate and chain have been saved at
   /etc/letsencrypt/live/www.activebeancoders.com/fullchain.pem. Your
   cert will expire on 2016-05-05. To obtain a new version of the
   certificate in the future, simply run Let's Encrypt again.
 - Your account credentials have been saved in your Let's Encrypt
   configuration directory at /etc/letsencrypt. You should make a
   secure backup of this folder now. This configuration directory will
   also contain certificates and private keys obtained by Let's
   Encrypt so making regular backups of this folder is ideal.
 - If you like Let's Encrypt, please consider supporting our work by:

   Donating to ISRG / Let's Encrypt:   https://letsencrypt.org/donate
   Donating to EFF:                    https://eff.org/donate-le
```


left off...
so apache on activebeancoders.com is using https
how do we use this with spring boot tomcats?  set up ajp proxy through apache?
is the cert stuff I set up obsolete due to the cert installed in apache??
do I need to use BOTH the cert from letsencrypt and also the internal cert for service-service communication?

# Using Let's Encrypt cert in Tomcat

As root...
```bash
mkdir /activebeancoders

chmod 700 /activebeancoders

cd /activebeancoders

cp /etc/letsencrypt/live/www.activebeancoders.com/* .

openssl \
    pkcs12 \
    -export \
    -in cert.pem \
    -inkey privkey.pem \
    -out keystore.p12 \
    -name tomcat \
    -CAfile fullchain.pem
supplied password: CHANGEIT

blaaah___keytool \
    -importkeystore \
    -deststorepass changeit \
    -destkeypass changeit \
    -destkeystore tomcat.keystore \
    -srckeystore keystore.p12 \
    -srcstoretype PKCS12 \
    -srcstorepass CHANGEIT \
    -alias tomcat

keytool -import -alias root -keystore tomcat.keystore -trustcacerts -file fullchain.pem
    #Enter keystore password:
    #Certificate already exists in keystore under alias <tomcat>
    #Do you still want to add it? [no]:
    #Certificate was not added to keystore

keytool \
    -v \
    -importkeystore \
    -srckeystore keystore.p12 \
    -srcstoretype PKCS12 \
    -destkeystore truststore.jks \
    -deststoretype JKS

```


# References

From: http://stackoverflow.com/questions/9478168/tsl-ssl-configuration-of-spring-http-invoker
    Problem already solved! I kind of mixed up keystore with truststore. So if
    you found this question and look for an answer, take a look at: Trust Store
    vs Key Store - creating with keytool. In short: you have to create a
    certificate which you put in a keystore for the server and in a truststore
    for the client. Well, essentially there is no real different between
    keystore and truststore, the only difference is that you use different
    System-properties to define them in your program. (e.g.
    javax.net.ssl.trustStore for the truststore). Thats where i messed up!

    I am still interested in reasons on whether or not to use Spring HTTP
    Invoker though.

From: http://stackoverflow.com/questions/6340918/trust-store-vs-key-store-creating-with-keytool
    The terminology is a bit confusing indeed, but both javax.net.ssl.keyStore
    and javax.net.ssl.trustStore are used to specify which keystores to use,
    for two different purposes. Keystores come in various formats and are not
    even necessarily files (see this question), and keytool is just a tool to
    perform various operations on them (import/export/list/...).

    The javax.net.ssl.keyStore and javax.net.ssl.trustStore parameters are the
    default parameters used to build KeyManagers and TrustManagers
    (respectively), then used to build an SSLContext which essentially contains
    the SSL/TLS settings to use when making an SSL/TLS connection via an
    SSLSocketFactory or an SSLEngine. These system properties are just where
    the default values come from, which is then used by
    SSLContext.getDefault(), itself used by SSLSocketFactory.getDefault() for
    example. (All of this can be customized via the API in a number of places,
    if you don't want to use the default values and that specific SSLContexts
    for a given purpose.)

    The difference between the KeyManager and TrustManager (and thus between
    javax.net.ssl.keyStore and javax.net.ssl.trustStore) is as follows (quoted
    from the JSSE ref guide):

    TrustManager: Determines whether the remote authentication credentials (and
    thus the connection) should be trusted.

    KeyManager: Determines which authentication credentials to send to the
    remote host.  (Other parameters are available and their default values are
    described in the JSSE ref guide. Note that while there is a default value
    for the trust store, there isn't one for the key store.)

    Essentially, the keystore in javax.net.ssl.keyStore is meant to contain
    your private keys and certificates, whereas the javax.net.ssl.trustStore is
    meant to contain the CA certificates you're willing to trust when a remote
    party presents its certificate. In some cases, they can be one and the same
    store, although it's often better practice to use distinct stores
    (especially when they're file-based).

From: http://www.webfarmr.eu/2010/04/import-pkcs12-private-keys-into-jks-keystores-using-java-keytool/
    Import PKCS12 private keys into JKS keystores using Java Keytool

* http://robblake.net/post/18945733710/using-a-pem-private-key-and-ssl-certificate-with
* http://www.robinhowlett.com/blog/2016/01/05/everything-you-ever-wanted-to-know-about-ssl-but-were-afraid-to-ask/#two-way-spring-boot
* http://letsencrypt.readthedocs.org/en/latest/using.html#where-are-my-certificates

# KeyStores and TrustStores

https://docs.oracle.com/cd/E19509-01/820-3503/ggffo/index.html

The JSSE makes use of files called KeyStores and TrustStores. The KeyStore is
used by the adapter for client authentication, while the TrustStore is used to
authenticate a server in SSL authentication.

* A KeyStore consists of a database containing a private key and an associated
certificate, or an associated certificate chain. The certificate chain consists
of the client certificate and one or more certification authority (CA)
certificates.

* A TrustStore contains only the certificates trusted by the client (a “trust”
store). These certificates are CA root certificates, that is, self-signed
certificates.


