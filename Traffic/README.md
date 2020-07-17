#@Author : [Mohammed Khalid Hamid]

*The project is in Kotlin
*Uses MVVM + Clean code Architecture
*Uses Co-routines for network calls
*Uses LiveData for UI update
* Built on Android Studio 4.2 Canary 2 build
* Android SDK 29
* Gradle 5.6.4
* Android Gradle Plugin 1.3.60-eap-25


## Task requirement

Traffic condition map

Requirements
•	Build an iOS/Android app to show all traffic camera on map as a pin
•	When the user taps on a pin, the app should show the latest photo capture by that traffic light.

Tips
•	The data at https://data.gov.sg/dataset/traffic-images should be used.


### Functionality

App shows LTA Camera view over Map as pins
On Tapping of pin will present user with current camera image

The LTA data is updated periodically every 1 minute

#### LTAFragment
This fragment displays the map over with pins

### Pre requisits
App uses stream api of Java 8 hence min supported version is 24
App uses Google Maps, so Huawie P40 and above device are not supported

#### Building
##[The project uses Gradle KTS script]
## The project is in Kotlin

## APK can be found in [release/apks]  
## Screenshots can be found in  [release/apks]  



### Testing
The project uses both instrumentation tests that run on the device
and local unit tests that run on your computer.
To run both of them and generate a coverage report, you can run:



#### Device Tests
##### UI Tests
The projects uses Espresso for UI testing. Since each fragment
is limited to a ViewModel, each test mocks related ViewModel to
run the tests.

##### Database Tests
The project creates an in memory database for each database test but still
runs them on the device.

#### Local Unit Tests
##### ViewModel Tests
Each ViewModel is tested using local unit tests with mock Repository
implementations.

##### Repository Tests
Each Repository is tested using local unit tests with mock web service and
mock database.
##### Webservice Tests
The project uses [MockWebServer][mockwebserver] and Fake implementations

## Jacoco
run "jacocoPRODDEBUGReport"


### Libraries
* [Android Support Library][support-lib]
* [Android Architecture Components][arch]
* [Android Data Binding][data-binding]
* [Dagger 2][dagger2] for dependency injection
* [Retrofit][retrofit] for REST api communication
* [Glide][glide] for image loading
* [Timber][timber] for logging
* [espresso][espresso] for UI tests
* [mockito][mockito] for mocking in tests

## Refernces

* https://github.com/firebase/FirebaseUI-Android





