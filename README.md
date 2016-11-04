Informer
========

Handle network errors with elegance by providing users with error messages they can interact with. 
Use a simple API for showing inline and Snackbar messages to give network errors consistency
across your app.

Usage
------

Setting up Informer is pretty straightforward. Check out the sample app for setup.

Configure
---------

Make sure you reference jitpack.io in your root build.gradle file
```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

Then add Informer to your dependencies
```groovy
dependencies {
    compile 'com.github.ryansimon:informer:1.0.0'
}
```
 
Todo
------

Add additional examples and more thorough documentation.

License
-------

```
Copyright 2016 Ryan Simon

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
