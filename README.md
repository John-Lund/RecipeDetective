# **Recipe Detective**

### **Overview**

This is the final project I submitted for my Udacity Android Nanodegree.  It's a recipe finding app
that uses the Yummly api for its data - https://developer.yummly.com/documentation  The user can enter
a list of ingredients in the search activity edit text and the app will request recipes that contain
those ingredients.  Recipes can be stored in a Room-powered "favourites" data base and reviews written by
users can be shared via a Firebase Realtime Database.  The app uses a simple Firebase Auth and viewmodels,
live data and a respository. It also has a home screen widget to display ingredients from recipes viewed
in favourite details.

If you want to see this app fully working, you'll need to get a Yummly key and app ID and insert them where
indicated by a "todo" I left in there.  Without an app ID and key, everything will work apart from the search. 


### **Sample UI images**

![image](https://user-images.githubusercontent.com/36385109/54075345-39936780-4296-11e9-8564-3d6e09681d1e.png)

![image](https://user-images.githubusercontent.com/36385109/54075402-d229e780-4296-11e9-97fd-9a89635eb860.png)

![image](https://user-images.githubusercontent.com/36385109/54075408-e53cb780-4296-11e9-9227-b6e3cbd3a2a6.png)

![image](https://user-images.githubusercontent.com/36385109/54075417-f5ed2d80-4296-11e9-8dfb-d4f6103a69fe.png)

![image](https://user-images.githubusercontent.com/36385109/54075424-030a1c80-4297-11e9-8771-283479e8c9ee.png)

![image](https://user-images.githubusercontent.com/36385109/54075429-10bfa200-4297-11e9-8dd6-30e78b1c5df6.png)

![image](https://user-images.githubusercontent.com/36385109/54075433-20d78180-4297-11e9-8ff6-b3d99206aae1.png)



















### **Licensing**

MIT License

Copyright (c) 2019 John Lund

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
