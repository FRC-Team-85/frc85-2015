package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

public class Elevator {

	private static Joystick controller;
	
	private static DigitalInput topSwitch;
	private static DigitalInput bottomSwitch;
	
	private double speed;
	
	private static CANTalon rightBeltMotor;
	private static CANTalon leftBeltMotor;
	
	private static Encoder elevatorCounter;
	
	private double count;
	private double convertedCount;
	private double radius = 0.0;// in ___   inch, cm, m, etc

	private static Solenoid locks;
	
	private static Solenoid hookA;
	private static final double HOOKAPOS = 0.0; //from the bottom
	
	private static Solenoid hookB;
	private static final double HOOKBPOS = 0.0; //from the bottom
	
	public Elevator (Joystick elevatorController,
			int topSwitchChannel, int bottomSwitchChannel,
			int beltMotorOneAddress, int beltMotorTwoAddress,
			int channelA, int channelB,
			int lockChannel,
			int hookAChannel, int hookBChannel) {
	
	  	controller = elevatorController;
	  	
		topSwitch = new DigitalInput(topSwitchChannel);
		bottomSwitch = new DigitalInput(bottomSwitchChannel);
		rightBeltMotor = new CANTalon(beltMotorOneAddress);
		leftBeltMotor = new CANTalon(beltMotorTwoAddress);
	
		elevatorCounter = new Encoder(channelA,channelB);
		
		locks = new Solenoid(lockChannel);
		hookA = new Solenoid(hookAChannel);
		hookB = new Solenoid(hookBChannel);
		
	}
	
	public void runLift() {
		runMotors();
		checkCount();
	}
	

	private void runMotors() {
		if(bottomSwitch.get() && controller.getX() <= 0.0 || topSwitch.get() && controller.getX() >= 0) {
			rightBeltMotor.set(0.0);
			leftBeltMotor.set(0.0);
			return;
		}
		speed = controller.getX();
		rightBeltMotor.set(speed);
		leftBeltMotor.set(speed);
	}
	
	private void checkCount() {
		if (bottomSwitch.get()) {
			elevatorCounter.reset();
		}
		count = elevatorCounter.get();
		convertedCount = Math.PI * 2 * radius * count / 360;
		System.out.println("Count: " + count);
		System.out.println("Up " + convertedCount + " in/cm/units");
	}
	
}
