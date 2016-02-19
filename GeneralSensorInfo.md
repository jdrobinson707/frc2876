General Sensor Information


# Generic advice about choosing and using sensors #
http://www.chiefdelphi.com/forums/showthread.php?t=114466

Some useful posts in this thread: 3, 6, 7

# How to use a PNP, NPN, inductive sensor #
http://www.chiefdelphi.com/forums/showthread.php?t=103308

# How to de-noise a sensor #
http://www.chiefdelphi.com/forums/showthread.php?t=114539&page=2

From the thread a simple IIR filter
```
ave = 0.5*(input+ave);
output = truncate(ave+0.03);
```
For digital signals this filter will debounce in one direction. It requires five '1' inputs in a row to go from 0->1.  But only one 0 input to go from 1->0. The .03 number controls how many samples are included in the average. In this example 5 samples are included in the average. Adjust .03 to include more or less samples.

To debounce in both directions this will do it:
```
if(output==0) {
  output=truncate(ave+0.03);
} else {
  output=truncate(ave+1-0.03);
}
```

# How to detect a motor has stalled #
http://www.chiefdelphi.com/forums/showthread.php?t=114680