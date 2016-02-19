# Introduction #

This page covers anything to do with drive train. That means speed controllers(victor,jaguar,talon), joysticks(tank drive, arcade drive), etc.

# Adjust joystick inputs using piece wise linear function #

Say you want to apply dead-band, prevent back-lash, and ramp speed slowly when robot is barely moving(fine grain control).  This will do it all...
http://www.chiefdelphi.com/forums/showpost.php?p=1438321&postcount=15


# Adjust Joystick reading before sending to Speed Controller #

http://www.chiefdelphi.com/forums/showthread.php?t=125816

**Refer to the graph when reading this description. Note you need to download pic to view it.**
![https://code.google.com/p/frc2876/source/browse/trunk/wiki_attachments/sensitivity_adjustment.gif](https://code.google.com/p/frc2876/source/browse/trunk/wiki_attachments/sensitivity_adjustment.gif)


> Let X be a joystick axis with value ranging from -1 to +1.

> Plot y=x and you get a straight line with slope (gain) = 1

> Now plot y = x<sup>3</sup> and you get a cubic output, also ranging from -1 to +1.

> The gain of this cubic curve is less than 1 for all x values between -sqrt(1/3) and +sqrt(1/3), and greater than 1 outside that range.  At x=0 the gain is zero (essentially giving a small deadband), and at x=1 the gain is 3.

> Now plot y = a(x<sup>3</sup>) + (1-a)x, where "a" is a constant in the range 0<=a<=1.  You get a family of curves lying between the curve y=x and y=x<sup>3</sup>.  When a=0 you get y=x, and when a=1 you get y=x<sup>3</sup>. When 0<a<1, you get a "blend" in-between y=x and y=x<sup>3</sup>.  The range of y is still -1 to +1 for all curves in the family.

> The gain of each curve is given by:
> > dy/dx = 3a(x<sup>2</sup>) + (1-a)


> Set this gain equal to 1 and solve for x:
> > 3a(x<sup>2</sup>) + (1-a) = 1  =>  x = +/- sqrt(1/3) = +/- 0.577


> ... so the "gain crossover" between y=x and y=a(x<sup>3</sup>)+(1-a)x always occurs at the same x value, namely 0.577.

> The gain of y=a(x<sup>3</sup>)+(1-a)x at x=0 is (1-a), and the gain at x=1 is 1+2a:

> x=0		gain = 1-a
> x=0.577		gain = 1
> x=1		gain = 1+2a

> You can set "a" equal to a hard-coded constant in your software, or you can use (for example) the throttle on a joystick to vary the value from 0 to 1 so the driver can select the desired sensitivity.



# Filtering joystick readings #

Coding is in labview, but the theory is applicable to any language.
http://thinktank.wpi.edu/article/140