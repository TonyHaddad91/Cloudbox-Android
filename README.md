# Cloudbox-Android
Android Client for Cloudbox Server https://github.com/duriana/Cloudbox-API

How to use it:
Initialize static Cloudbox singleton instance in Apllication Class
```java
final static CloudBox cloudBox = CloudBox.getInstance("Domain");
```
Optionally you can change RESOURCES_META_PATH if you changed it in the server side
```java
final static CloudBox cloudBox = CloudBox.getInstance("Domain","resourse_file_meta");
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
Enable/Disable Logging
```java
cloudBox.setLogEnabled(true);
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
```
   Copyright 2016 Duriana

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
