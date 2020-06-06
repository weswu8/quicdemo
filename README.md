QUIC Demo
=====

Google cloud CDN support QUIC protocol , Here is the android app demo. 

## Important Notes:
* The goal of this app is not to compare the performance of quic and http2.

## Project Goals
* Demonstrates how to enable QUIC and Http2 on the Android platform.
## Screenshot
![image](https://github.com/weswu8/quicdemo/blob/master/docs/quicdemo.gif)

## How to use

### 1.change the url of images(optional)
	Open src⁩ ▸ ⁨main⁩ ▸ ⁨java⁩ ▸ ⁨com⁩ ▸ ⁨weswu⁩ ▸ ⁨cloudcdn ▸ ⁨Images.java
	change the value of imageUrls
### 2.compile the project


## Some Tips
### 1.Enable Quic Or Https Or Both
	  CronetEngine.Builder myBuilder = new CronetEngine.Builder(context);
      cronetEngine = myBuilder
      		//disable cache for test, normally you should not do this
            .enableHttpCache(CronetEngine.Builder.HTTP_CACHE_DISABLED, 100 * 1024)
            .addQuicHint("cdn.obwiz.com", 443, 443)
            .enableQuic(true)
            //.enableHttp2(true)
            .setUserAgent(from(context))
            .build();
            
### 2.Customize user Agent 
	 new CronetEngine.Builder(context).setUserAgent(from(context))

## Related Reference
* [Employing QUIC Protocol to Optimize Uber’s App Performance](https://eng.uber.com/employing-quic-protocol/)

## Dependency
* com.google.android.gms:play-services-cronet

## License
	Copyright (C) 2020 Wesley Wu jie1975.wu@gmail.com
	This code is licensed under The General Public License version 3
	
## FeedBack
	Your feedbacks are highly appreciated! :)
