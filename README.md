QUIC Demo
=====

Google cloud CDN support QUIC protocol , Here is the android app demo. 

## Important Notes:
* The goal of this app is not to compare the performance of quic and http2.

## Project Goals
* Demonstrates how to enable QUIC and Http2 on the Android platform.
## Screenshot
![image](https://github.com/weswu8/quicdemo/docs/quicdemo.gif)

## How to use

### 1.change the url of images(optional)
	Open src⁩ ▸ ⁨main⁩ ▸ ⁨java⁩ ▸ ⁨com⁩ ▸ ⁨weswu⁩ ▸ ⁨cloudcdn ▸ ⁨Images.java
	change the value of imageUrls
### 2.compile the project


## Some Tips
### 1.Enable Quic
	
### 2.Install blobfs-win
#### 2.1 Download the blobfs-win released version.
#### 2.2 Edit configuration file: 
	Open conf/config.xml
	change the setting of :
		<ProjectId>gcp_project_id</ProjectId>
		<!--  Gcp service account json file --> 
		<ServiceAccountFile>gcp_service_account.json</ServiceAccountFile>
		<!--  the prefix of target buckets or objects, use / will mount all buckets --> 
		<RootPrefix>/bucket_name/</RootPrefix>
		<!--  the dirve letter of local host --> 
		<MountDrive>F:</MountDrive>
		<!--  cached object TTL in seconds --> 
		<CacheTTL>180</CacheTTL>

### final.Start the blobfs service
    lanuch gcsfuse-win.exe
	
It is highly recommended that you should config it as a windows services.

## Related Reference
* [Employing QUIC Protocol to Optimize Uber’s App Performance](https://eng.uber.com/employing-quic-protocol/)

## Dependency
* com.google.android.gms:play-services-cronet


## License
	Copyright (C) 2020 Wesley Wu jie1975.wu@gmail.com
	This code is licensed under The General Public License version 3
	
## FeedBack
	Your feedbacks are highly appreciated! :)
