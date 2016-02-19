# Introduction #

From
http://www.chiefdelphi.com/forums/showpost.php?p=1286318&postcount=15
Good pic attached to that post

When the robot starts up the gyro runs a bias measurement and calculation. During this time the robot must be absolutely still. This process is measuring the offset in the device, which when integrated translates to drift.

We tell our drive team to start up the robot at the last possible moment and make certain that the robot is not bumped or disturbed during this initialization time. The compressor cannot be running during this time if you want the best result.

Kevin Watson has some really good code and information about this on his website kevin.org. Look in the FRC code section. It is code from the old IFI controller, but it is very educational.

http://kevin.org/frc/ Look in the frc\_gyro.zip file.

Another helpful trick is to mount the gyro on a mass and mount that in foam. We use a one pound piece of aluminum and some foam that hard disk drives are often packaged in. This acts as a low pass filter and helps smooth out the data. See the attached photo for our arrangement.

Keep working with the gyro and accelerometer. You can do some really cool stuff once you understand these devices. Have fun!


---
