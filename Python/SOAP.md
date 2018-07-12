SUDS CLIENT
imagine that we have a SOAP API address was http://8.8.8.8/api/sdk/WWWSDKWebService.asmx?WSDL
Contains a service function "Login",which need 2 params,First one is his own type 'Credentials',another is int number.After you call it it will Returns a type contains SessionID params.
```
from suds.client import Client
url = 'http://8.8.8.8/api/sdk/WWWSDKWebService.asmx?WSDL'
client = Client(url)
# print client 
#here you can use print client to check all methods and types in this SOAP service.

cr=client.factory.create('Credentials')
# you can use 'print cr' to check the define of Credentials here
cr.User="admin"
cr.Pass="admin"


# print cr
loginc=client.service.Login(cr,1028)
session=loginc.SessionId

print session


```
