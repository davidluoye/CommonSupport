## What is in CommomSupport
* many simple and convenient utils.
* Remote IPC bwtween two different android process.
* UI model interface.
* TODO others ......


### How to use IPC api

#### 1. TODO in server
  If app want to publish service as a remote server, it can declare a subclass of BaseRemoteService in AndroidManifest.xml,
  also we set properties as bollow as declare a provider. 
```xml
<provider
        android:name="sub class of RemoteService"
        android:authorities="xxxx"
        android:multiprocess="false"
        android:enabled="true"
        android:exported="true" 
/>
```

#### 2. TODO in client
  If we want to bind remote server, we can do create subclass of BaseRemoteManager with authority and name.

  * @param authority, should be provider authority in server.
  * @name identity of IBinder in server.



##关于作者

```javascript
  name: davidluoye
```
