## soloader-issues ##

Sample app for soloader [app bundle issue](https://github.com/facebook/SoLoader/issues/76)

I don't know how to reproduce it. It happens within 2-3% of users. Seems like not much but has large impact for app rating at stores

This sample just load test screen with webrtc local video init (without transmitting it) to be sure app is started successfully

## this is ReactNative app ##

1. Install nodejs and npm (I have nodejs 14.5.1 and npm 6.14.9)

2. $ npm install -g react-native-cli

3. projectRoot$ npm install

4.1 projectRoot/android$ ./gradlew assembleRelease // make release apk

4.2 projectRoot/android$ ./gradlew bundleRelease // make release aab

4.3.1 projectRoot/android$ ./gradlew installDebug // make and install development build on device

4.3.2 projectRoot$ npm run start // run devserver

more info: https://reactnative.dev/docs/getting-started
