# Introduction #

Collection of info about sensors and filtering readings.


---

From
http://www.chiefdelphi.com/forums/showthread.php?t=118360

Post #6 has links to white paper and other places for info on filters.

Another post #7 has example

Here's a very simple low-pass IIR filter:

```
filtered_value = a*filtered_value + (1-a)*new_value
```

"new\_value" is the new reading from the accelerometer.

"filtered\_value" is the filtered version of the accelerometer reading.

"a" is a tuning constant which tunes the strength of the filter. "a" has a range of 0 to 1.

When a=0, there's no filtering -- the filtered\_value always equals the accelerometer reading.

As "a" approaches 1, the filtering gets more aggressive -- the filtered\_value has less noise (but more phase lag).