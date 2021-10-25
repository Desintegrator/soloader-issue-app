import React, { useEffect, useState } from 'react';
import { View, Text, NativeModules, DeviceEventEmitter, NativeEventEmitter } from 'react-native'

const WebRTC = require('react-native-webrtc')
const {
    RTCPeerConnection,
    RTCIceCandidate,
    RTCSessionDescription,
    RTCView,
    MediaStream,
    MediaStreamTrack,
    mediaDevices,
} = WebRTC

function App() {
    const [stream, setStream] = useState('')

    useEffect(() => {
        const configuration = {"iceServers": [{"url": "stun:stun.l.google.com:19302"}]};
        const pc = new RTCPeerConnection(configuration);

        let isFront = true;
        mediaDevices.enumerateDevices().then(sourceInfos => {
        let videoSourceId;
        for (let i = 0; i < sourceInfos.length; i++) {
            const sourceInfo = sourceInfos[i];
            if(sourceInfo.kind == "videoinput" && sourceInfo.facing == (isFront ? "front" : "environment")) {
            videoSourceId = sourceInfo.deviceId;
            }
        }
        mediaDevices.getUserMedia({
            audio: true,
            video: {
            width: 640,
            height: 480,
            frameRate: 30,
            facingMode: (isFront ? "user" : "environment"),
            deviceId: videoSourceId
            }
        })
        .then(stream => {
            setStream(stream)
        })
        .catch(error => {});
        });
    }, [])

    return (
        <View
            style={{
                flex: 1, justifyContent: 'center', alignItems: 'center',
                backgroundColor: 'teal', padding: 20,
            }}
        >
            <View
                style={{
                    flex: 1, justifyContent: 'center', alignItems: 'center'
                }}
            >
                <RTCView
                    name="local"
                    objectFit={'cover'} zOrder={1} mirror={true}
                    streamURL={stream && stream.toURL() || ''}
                    style={{
                        backgroundColor: 'b;ue',
                        width: 250, height: 250,
                        overflow: 'hidden',
                    }}
                />
            </View>
            <View
                style={{
                    flexDirection: 'row', justifyContent: 'space-between',
                }}
            >
                <View
                    style={{
                        flex: 1,
                        height: 100, marginTop: 25,
                        backgroundColor: 'orange'
                    }}
                ></View>
                <View
                    style={{
                        flex: 1,
                        height: 100, marginTop: 25,
                        backgroundColor: 'blue'
                    }}
                ></View>
                <View
                    style={{
                        flex: 1,
                        height: 100, marginTop: 25,
                        backgroundColor: 'orange'
                    }}
                ></View>
            </View>
            <Text
                style={{
                    fontSize: 20, color: 'white', margin: 20,
                }}
            >
                app loaded
            </Text>
            <View
                style={{
                    flexDirection: 'row', justifyContent: 'space-between',
                }}
            >
                <View
                    style={{
                        flex: 1,
                        height: 100, marginTop: 25,
                        backgroundColor: 'green'
                    }}
                ></View>
                <View
                    style={{
                        flex: 1,
                        height: 100, marginTop: 25,
                        backgroundColor: 'red'
                    }}
                ></View>
                <View
                    style={{
                        flex: 1,
                        height: 100, marginTop: 25,
                        backgroundColor: 'green'
                    }}
                ></View>
            </View>
        </View>
    )
}

export default App
