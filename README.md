# Duriana-Cloudbox-Android
Android Client for Cloudbox Server https://github.com/duriana/Duriana-Cloudbox

How to use it:
Initialize static Cloudbox singleton instance in Apllication Class
```java
final static CloudBox cloudBox = CloudBox.getInstance("Domain");
```
To get File from server and store it on the device
```java
 cloudBox.getFileFromServer(getApplicationContext(), "File Name", "extension", new OnSyncFinish() {
            @Override public void finish(boolean status) {
                if (status)
                    System.out.println("success");
                else
                    System.out.println("fail");
            }
        });
```
To get file stored on the device
```java
cloudBox.readFile(getApplicationContext(), "File Name")
```

Installation 
```java
 repositories {
        maven { url = 'https://jitpack.io' }
    }
 dependencies {
     compile 'com.github.duriana:Cloudbox-Android:1.5.3'
    }
```
