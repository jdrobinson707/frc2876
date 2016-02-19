# General Info about Encoders #
http://www.anaheimautomation.com/manuals/forms/encoder-guide.php

# Wiring US Digital E4P #
http://www.chiefdelphi.com/forums/showthread.php?t=112180&highlight=wiring+encoders
```
orange- 5V
blue- chanle A
black- GND
yellow - chanle B
```

# General Formula for Getting Distance from Encoders #
```
DRIVE_ENCODER_PULSE_PER_ROT = PULSE_PER_ROTATION * GEAR_RATIO

Distance Per Pulse = ((Math.PI * 2 * DRIVE_WHEEL_RADIUS) / DRIVE_ENCODER_PULSE_PER_ROT)
```

These are the encoder values for e-4p:
Pulse per rotation came from the e-4p documentation.
Drive wheel radius came from documentation or measuring.
Gear ratio came from mechanical team. Maybe from the tuff box documentation.

# How to use getRate and setDistancePerPulse #

http://www.chiefdelphi.com/forums/showthread.php?t=114522

Sample code showing how to use encoders

http://www.chiefdelphi.com/forums/showthread.php?s=d40395438fb7b7ebafa4defd0c6e59ac&t=112700&highlight=encoders
```
 //DIO Port of Encoder
	public static final int rightDriveEncoderChannelA = 3;
	public static final int rightDriveEncoderChannelB = 4;

	//Wheel Radius
	public static final int driveWheelRadius=2;//wheel radius in inches
	public static final int pulsePerRotation = 360; //encoder pulse per rotation
	public static final double gearRatio = 1/1; //ratio between wheel and encoder
	public static final double driveEncoderPulsePerRot= pulsePerRotation*gearRatio; //pulse per rotation * gear ratio
	public static final double driveEncoderDistPerTick=(Math.PI*2*driveWheelRadius)/driveEncoderPulsePerRot;
	public static final int driveEncoderMinRate=10; 
	public static final int driveEncoderMinPeriod=10;
        public static final boolean rightDriveTrainEncoderReverse=false;

	//declare sensors
	Encoder rightEncoder;

         //instantiate encoder
        rightEncoder = new Encoder(rightDriveEncoderChannelA, rightDriveEncoderChannelB, rightDriveTrainEncoderReverse, CounterBase.EncodingType.k4X);
    	rightEncoder.setDistancePerPulse(driveEencoderDistPerTick);
    	rightEncoder.setMaxPeriod(driveEncoderMinPeriod);//min period before reported stopped
    	rightEncoder.setMinRate(driveEncoderMinRate);//min rate before reported stopped
    	rightEncoder.start();
```

# 2012 bball robot values #
```
	public static final int driveWheelRadius=3;//wheel radius in inches
	public static final int pulsePerRotation = 360; //encoder pulse per rotation
	public static final double gearRatio = 26/12; //ratio between wheel and 
```

## 2014 ##
```
PULSE_PER_ROTATION = 250
GEAR_RATIO = 12/5
DRIVE_WHEEL_RADIUS = 2

Drive encoder pulse per rotation = 250*(12/5) = 600
Distance per pulse = ((3.14*2*2)/ 600= 0.020933333
```