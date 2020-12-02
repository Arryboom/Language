## [Create ED25519 certificates for TLS with OpenSSL](https://blog.pinterjann.is/ed25519-certificates.html "Permalink to Create ED25519 certificates for TLS with OpenSSL")

by [Jannis Pinter](https://blog.pinterjann.is/author/jannis-pinter.html)

Sun 31 March 2019

Algorithms designed by Daniel J. Bernstein et al. are currenlty quite popular and were implemented by many applications. X25519 is now the most widely used key exchange mechanism in TLS 1.3 and the curve has been adopted by software packages such as OpenSSH, Signal and [many more](https://ianix.com/pub/ed25519-deployment.html).

Although ECC is a currently a thing in X.509 / WebPKI, the list of available curves is mostly limited to NIST's P-256, P-384 and P-521 curves. This is because the CA/Browser Forum, an industry consortium of browser vendors and public trust centers, defines only those curves as permitted in their [Baseline Requirements](https://cabforum.org/baseline-requirements-documents/). The Baseline Requirements are a set of rules for public trust centers, it is important for the CAs to follow those rules closely, otherwhise they get kicked out of the major root programs and their certificates would no longer be trusted by major browsers. However, private CAs are not subject to those rules and are free to choose whichever curve they want for their certificates.

Since the Snowden revelations in 2013, we know that the NSA has actively manipulated cryptographic standards to incorporate [backdoors in crypto algorithms](https://en.wikipedia.org/wiki/Dual_EC_DRBG#Weakness:_a_potential_backdoor). Since then, cryptographers raised concerns against the NIST curves, mainly because some parameters were chosen without any explanation and due to the fact that the curves were designed by the NSA. Wouldn't it be nice to use ECC with safe curves that everyone trusts?

Fortunately, that is indeed possible. Besides the NIST curves, there are several named curves from different standardization bodies available to choose from. In case you are using OpenSSL, you can output a list of supported curves using the following command:

$ openssl ecparam \-list\_curves

You might notice that the command won't list any of Bernstein's curves, this is due to the fact that the implementation of ED25519 and ED448 in OpenSSL works slightly different than for other named curves. I won't go into the details here, but both ED25519 and ED448 are supported in OpenSSL 1.1.1 and later versions.

## Generate a ED25519 CSR

Alright, let's create a TLS certificate with one of Bernstein's safe curves. We can generate a X.509 certificate using ED25519 (or ED448) as our public-key algorithm by first computing the private key:

$ openssl genpkey \-algorithm ED25519 > example.com.key

Then we should create a configuration file for OpenSSL, where we can list all the SANs we want to include in the certificate as well as setting proper key usage bits:

$ cat openssl-25519.cnf
---------------------------------------------------------------
\[req\]
distinguished\_name = req\_distinguished\_name
req\_extensions = v3\_req
prompt = no
\[req\_distinguished\_name\]
C = DE
CN = www.example.com
\[v3\_req\]
keyUsage = keyEncipherment, dataEncipherment
extendedKeyUsage = serverAuth
subjectAltName = @alt\_names
\[alt\_names\]
DNS.1 = www.example.com
DNS.2 = example.com

Afterwards we can create a PKCS#10 (Certificate Signing Request) using the private key and the configuration file:

$ openssl req -new -out example.com.csr -key example.com.key -config openssl-25519.cnf

In my case, the CSR file looks like this:

\-----BEGIN CERTIFICATE REQUEST-----
MIIBAzCBtgIBADAnMQswCQYDVQQGEwJERTEYMBYGA1UEAwwPd3d3LmV4YW1wbGUu
Y29tMCowBQYDK2VwAyEAK87g0b8CC1eA5mvKXt9uezZwJYWEyg74Y0xTZEkqCcyg
XDBaBgkqhkiG9w0BCQ4xTTBLMAsGA1UdDwQEAwIEMDATBgNVHSUEDDAKBggrBgEF
BQcDATAnBgNVHREEIDAegg93d3cuZXhhbXBsZS5jb22CC2V4YW1wbGUuY29tMAUG
AytlcANBAHSBX9+RjKgO3MjD72nHdiqmPdotBqF2+0mMxQB2sB3Z9WOCF1M+UvFd
JyTsMetxAQZ2UEYMCqo84oG2CWn6gAY=
-----END CERTIFICATE REQUEST-----

Fortunately, OpenSSL can decode the base64 encoded ASN.1 data structure and output it in a human readable form with the following command:

$ openssl req -in example.com.csr -text -noout
---------------------------------------------------------------
Certificate Request:
  Data:
      Version: 1 (0x0)
      Subject: C = DE, CN = www.example.com
      Subject Public Key Info:
          Public Key Algorithm: ED25519
              ED25519 Public-Key:
              pub:
                  2b:ce:e0:d1:bf:02:0b:57:80:e6:6b:ca:5e:df:6e:
                  7b:36:70:25:85:84:ca:0e:f8:63:4c:53:64:49:2a:
                  09:cc
      Attributes:
      Requested Extensions:
          X509v3 Key Usage:
              Key Encipherment, Data Encipherment
          X509v3 Extended Key Usage:
              TLS Web Server Authentication
          X509v3 Subject Alternative Name:
              DNS:www.example.com, DNS:example.com
  Signature Algorithm: ED25519
       74:81:5f:df:91:8c:a8:0e:dc:c8:c3:ef:69:c7:76:2a:a6:3d:
       da:2d:06:a1:76:fb:49:8c:c5:00:76:b0:1d:d9:f5:63:82:17:
       53:3e:52:f1:5d:27:24:ec:31:eb:71:01:06:76:50:46:0c:0a:
       aa:3c:e2:81:b6:09:69:fa:80:06

Looks good, we successfully created a PKCS#10 CSR with OpenSSL! Now let's get it signed by our CA so we can use it with TLS.

**Hint:** This obviously won't work with publicly trusted CAs such as Digicert, Sectigo or Let's Encrypt as they have to closely follow the rules defined by the CA/Browser Forum Baseline Requirements which currently only permit P-256, P-384 and P-521. However, when you run your own private CA for home or office use, this should work just fine.

## Signing the CSR

For this blog post, we will just issue a self signed certifcate. You can do this with OpenSSL like this:

$ openssl x509 -req -days 700 -in example.com.csr -signkey example.com.key -out example.com.crt

The command will issue a self signed certificate which is valid for 700 days. In my case, the issued certificate looks like this:

\-----BEGIN CERTIFICATE-----
MIIBCDCBuwIUGW78zw0OL0GptJi++a91dBa7DsQwBQYDK2VwMCcxCzAJBgNVBAYT
AkRFMRgwFgYDVQQDDA93d3cuZXhhbXBsZS5jb20wHhcNMTkwMzMxMTc1MTIyWhcN
MjEwMjI4MTc1MTIyWjAnMQswCQYDVQQGEwJERTEYMBYGA1UEAwwPd3d3LmV4YW1w
bGUuY29tMCowBQYDK2VwAyEAK87g0b8CC1eA5mvKXt9uezZwJYWEyg74Y0xTZEkq
CcwwBQYDK2VwA0EAIIu/aa3Qtr3IE5to/nvWVY9y3ciwG5DnA70X3ALUhFs+U5aL
tfY8sNT1Ng72ht+UBwByuze20UsL9qMsmknQCA==
-----END CERTIFICATE-----

As with the CSR previously, we can use OpenSSL to pretty-print the information from the certificate:

$ openssl x509 -in example.com.crt -text -noout
---------------------------------------------------------------
Certificate:
  Data:
      Version: 1 (0x0)
      Serial Number:
          19:6e:fc:cf:0d:0e:2f:41:a9:b4:98:be:f9:af:75:74:16:bb:0e:c4
      Signature Algorithm: ED25519
      Issuer: C = DE, CN = www.example.com
      Validity
          Not Before: Mar 31 17:51:22 2019 GMT
          Not After : Feb 28 17:51:22 2021 GMT
      Subject: C = DE, CN = www.example.com
      Subject Public Key Info:
          Public Key Algorithm: ED25519
              ED25519 Public-Key:
              pub:
                  2b:ce:e0:d1:bf:02:0b:57:80:e6:6b:ca:5e:df:6e:
                  7b:36:70:25:85:84:ca:0e:f8:63:4c:53:64:49:2a:
                  09:cc
  Signature Algorithm: ED25519
       20:8b:bf:69:ad:d0:b6:bd:c8:13:9b:68:fe:7b:d6:55:8f:72:
       dd:c8:b0:1b:90:e7:03:bd:17:dc:02:d4:84:5b:3e:53:96:8b:
       b5:f6:3c:b0:d4:f5:36:0e:f6:86:df:94:07:00:72:bb:37:b6:
       d1:4b:0b:f6:a3:2c:9a:49:d0:08

We can now use the certificate with our favorite webserver. If it was built against a recent OpenSSL release, it should be able to work with the certificate just fine.

Unfortunately, none of the major browsers seem to support ED25519 based certificates for TLS as of now. We can however use OpenSSL itself to test the connection and verify that it actually works.

$ openssl s\_client -connect example.com:443










---



#JAVA openssl

最近项目里面用到了SSL**双向认证**和传输加密的技术，研究了一下，想把相关的指令和代码分享出来，以期后来者能够少踩坑，顺利解决问题。我们的项目服务器是C，客户端为[Java](http://lib.csdn.net/base/javase "Java SE知识库")，CS[架构](http://lib.csdn.net/base/architecture "大型网站架构知识库")，中间通过Socket通讯。

OpenSSL和[Java](http://lib.csdn.net/base/java "Java 知识库") KeyStore本质上没有关系，只是客户端用到Java，Java里面SSL认证加密的密码和证书需要存储到KeyStore这个容器里面，所以OpenSSL产生的相关资料需要导入keyStore容器。当然也可以反过来，用Java的KeyTool产生资料，再导出密码、证书，给服务器端C使用。不过OpenSSL比Java的keytool强大很多，故选择前一种方式。OpenSSL产生资料，存入KeyStore。本文只介绍用到的一系列指令。

这里涉及到三份证书。根证书(root.crt)，自签名的，用于给其它证书授权。服务器证书(server.crt)。以及客户端证书(client.crt)，一般系统不需要，我们的系统是双向认证，客户端也要证书。

## [](http://blog.csdn.net/kimylrong/article/details/43525333#%E6%8C%87%E4%BB%A4%E5%88%97%E8%A1%A8)指令列表

1. **openssl genrsa -out rootkey.pem 2048**  
    生成根证书的密匙。
2. **openssl req -x509 -new -key rootkey.pem -out root.crt**  
    生成根证书。注意-x509，与步骤4和7不同。需要输入机构相关信息。
3. **openssl genrsa -out clientkey.pem 2048**  
    生成客户端的密匙。
4. **openssl req -new -key clientkey.pem -out client.csr** 生成客户端证书的请求文件。请求根证书来签发。
5. **openssl x509 -req -in client.csr -CA root.crt -CAkey rootkey.pem -CAcreateserial -days 3650-out client.crt**  
    用根证书来签发客户端请求文件，生成客户端证书client.crt。
6. **openssl genrsa -out serverkey.pem 2048**  
    生成服务器端的密匙。
7. **openssl req -new -key serverkey.pem -out server.csr**  
    生成服务器端证书的请求文件。请求根证书来签发。
8. **openssl x509 -req -in server.csr -CA root.crt -CAkey rootkey.pem -CAcreateserial -days 3650-out server.crt**  
    用根证书来签发服务器端请求文件，生成服务器端证书server.crt。
9. **openssl pkcs12 -export -in client.crt -inkey clientkey.pem -out client.pkcs12**  
    打包客户端资料为pkcs12格式(client.pkcs12)。需要输入密码，请记住。
10. **openssl pkcs12 -export -in server.crt -inkey serverkey.pem -out server.pkcs12**  
    打包服务器端资料为pkcs12格式(server.pkcs12 )。需要输入密码，请记住。
11. **keytool -importkeystore -srckeystore client.pkcs12 -destkeystore client.jks -srcstoretype pkcs12**  
    生成客户端keystore(client.jks)。使用keytool的importkeystore指令。pkcs12转jks。需要pkcs12密码和jks密码。
12. **keytool -importkeystore -srckeystore server.pkcs12 -destkeystore server.jks -srcstoretype pkcs12**  
    生成服务器端keystore(server.jks)。使用keytool的importkeystore指令。pkcs12转jks。需要pkcs12密码和jks密码。
13. **keytool -importcert -keystore server.jks -file root.crt**  
    这一步不一定需要的。我发现服务器使用JDK6和JDK7的时候，必须要把根证书加到服务器证书里面，否则交谈失败。nul certification。Google里面很多结果，但是都不灵光。

## [](http://blog.csdn.net/kimylrong/article/details/43525333#%E5%90%8E%E8%AE%B0)后记

理解概念是最重要的，路有很多条。以上指令还缺少几条，用来生成服务器端的trustkeystore，客户端的trustkeystore。**trustkeystore里面不包含私匙**。

## [](http://blog.csdn.net/kimylrong/article/details/43525333#%E5%8F%82%E8%80%83%E9%93%BE%E6%8E%A5)参考链接

- [OpenSSL官方文档](https://www.openssl.org/docs/apps/openssl.html)
- [keytool说明书](http://docs.oracle.com/javase/8/docs/technotes/tools/unix/keytool.html)

## [](http://blog.csdn.net/kimylrong/article/details/43525333#%E8%A1%A5%E4%B8%8Atrustkeystore%E6%8C%87%E4%BB%A4)补上TrustKeyStore指令

- **keytool -importcert -alias ca -file root.crt -keystore clienttrust.jks**  
    生成Client端的对外KeyStore。先把根证书放到里面。
- **keytool -importcert -alias clientcert -file client.crt -keystore clienttrust.jks**  
    把Client证书加到对外KeyStore里面。
- **keytool -importcert -alias ca -file root.crt -keystore servertrust.jks**  
    生成Server端的对外KeyStore。先把根证书放到里面。
- **keytool -importcert -alias servercert -file server.crt -keystore servertrust.jks**  
    把Server证书加到对外KeyStore里面。