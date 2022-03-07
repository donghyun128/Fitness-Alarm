# 운동 알람 (V1.00)


## 어플리케이션 소개
설정 화면에서 운동의 종류와 횟수를 미리 설정해두면 정해진 시간에 알람이 울린다. 주어진 운동을 모두 수행하면 알람 음악과 진동이 꺼지게 된다.

APK 다운로드 링크: https://github.com/donghyun128/Fitness-Alarm/raw/master/app/release/FitnessAlarm.apk

## 사용 방법
<img src="https://github.com/donghyun128/Fitness-Alarm/blob/8516b9a35f43ea87f3a74ce85f970f30b25e44d4/screenshots/%EB%A9%94%EC%9D%B8%ED%99%94%EB%A9%B4.jpg" width="360" height="740">|<img src="https://github.com/donghyun128/Fitness-Alarm/blob/8516b9a35f43ea87f3a74ce85f970f30b25e44d4/screenshots/%EC%95%8C%EB%9E%8C%20%EC%84%A4%EC%A0%95%20%ED%99%94%EB%A9%B4.jpg" width="360" height="740">

메인 화면에서 정해진 알람 시간을 확인할 수 있고, 왼쪽 하단 버튼 (알람 끄기/켜기)를 통해 알람 ON/OFF 설정이 가능하다.
오른쪽 하단의 '+' 버튼을 눌러 알람 설정 화면에 들어갈 수 있다.

알람 설정 화면에서 알람 시간, 운동의 종류, 운동 횟수, 벨소리를 설정하고 알람 설정 버튼을 누르면 알람이 설정된다.

## 알람 동작 영상

## 개발 동기
유튜브에서 팔굽혀펴기(푸쉬업) 갯수를 늘리는 방법에 대한 영상을 본 적이 있다. 그 중 하나는 아침에 기상하자마자 최대 횟수로 진행하는 것이다.

실제로 해보니 그 날의 주간 컨디션이 굉장히 좋았다. 이에 착안하여 설정한 운동을 모두 마치면 꺼지는 알람이 있으면 좋겠다고 생각했다.
운동을 하면 꺼지는 어플리케이션은 기존에 존재했다. 그러나 실제로 자세를 인식하기 보다는 간접적인 방식으로 이루어져 있었다.

실제로 자세를 인식하는 방법을 조사하던 중, 실시간으로 17개의 신체 좌표 위치를 추론하는 딥러닝 모델 MoveNet을 발견하였다.
MoveNet의 기능을 활용하면 그 알람을 구현할 수 있을거라 생각하여 개발에 착수하게 되었다.

## 개발 환경
Android Studio Arctic Fox | 2020.3.1

## Application Version
minSdkVersion : 28

targetSdkVersion 30

## 사용한 기술  
TensorFlow Lite 2.5.0

## REFERENCE
https://github.com/tensorflow/examples/tree/master/lite/examples/pose_estimation/android (TensorFlow Lite MoveNet Android Example)

https://github.com/miguelrochefort/fitness-camera/tree/master/android/app (Counting 알고리즘 관련)
