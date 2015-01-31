package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

public class Elevator {

	private static Joystick controller;
	
	private static DigitalInput topSwitch;
	private static DigitalInput bottomSwitch;
	
	private static CANTalon rightBeltMotor;
	private static CANTalon leftBeltMotor;
	
	private static Encoder elevatorCounter;
	
	private double count;
	//private double convertedCount;
	//private double radius = 0.0;// in ___   inch, cm, m, etc

	private static Solenoid locks;
	private static boolean islockToggleHeld;
	
	private static Solenoid hookA;
	private static final double HOOKAPOS = 0.0; //from the bottom
	
	private static Solenoid hookB;
	private static final double HOOKBPOS = 0.0; //from the bottom
	
	private static int totesOnElevator = 0;
	
	public Elevator (Joystick opController) {
	
	  	controller = opController;
	  	
		topSwitch = new DigitalInput(Addresses.TOPSWITCH_CHANNEL);
		bottomSwitch = new DigitalInput(Addresses.BOTTOMSWITCH_CHANNEL);
		rightBeltMotor = new CANTalon(Addresses.LEFT_BELT_MOTOR);
		leftBeltMotor = new CANTalon(Addresses.RIGHT_BELT_MOTOR);
	
		elevatorCounter = new Encoder(Addresses.ELEVATOR_ENCODER_CHANNEL_A,Addresses.ELEVATOR_ENCODER_CHANNEL_B);
		
		locks = new Solenoid(Addresses.PNEUMATIC_CONTROLLER_CID, Addresses.LOCKS_SOLENOID_CHANNEL);
		hookA = new Solenoid(Addresses.PNEUMATIC_CONTROLLER_CID, Addresses.HOOK_A_SOLENOID_CHANNEL);
		hookB = new Solenoid(Addresses.PNEUMATIC_CONTROLLER_CID, Addresses.HOOK_B_SOLENOID_CHANNEL);
		
	}
	
	public void runLift() {
		runMotors(controller.getY());
		checkCount();
		hookSafety(elevatorCounter.get());
		toggleLocks();
	}
	

	private void runMotors(double speed) {
		if(bottomSwitch.get() && speed <= 0.0 || topSwitch.get() && speed >= 0) {
			rightBeltMotor.set(0.0);
			leftBeltMotor.set(0.0);
			return;
		}
		rightBeltMotor.set(speed);
		leftBeltMotor.set(speed);
	}
	
	private void checkCount() {
		if (bottomSwitch.get()) {
			elevatorCounter.reset();
		}
		count = elevatorCounter.get();
		//convertedCount = Math.PI * 2 * radius * count / 360;
		System.out.println("Count: " + count);
	}
	
	private void toggleLocks() {
		if (!islockToggleHeld) {
			locks.set(!locks.get());
			islockToggleHeld = true;
		} else {islockToggleHeld = false;}
	}
	
	private void hookSafety(int count) {
		
		if(count >= HOOKAPOS && count <= HOOKBPOS) {
			hookA.set(false);
			hookB.set(controller.getRawButton(Addresses.HOOK_A_SET));
		} else if(count >= HOOKBPOS) {
			hookA.set(false);
			hookB.set(false);
		} else {
			hookA.set(controller.getRawButton(Addresses.HOOK_A_SET));
			hookB.set(controller.getRawButton(Addresses.HOOK_B_SET));
		}
	}
	
	private void autoStore() {
		if(controller.getRawButton(Addresses.AUTO_STORE)) {
			switch(totesOnElevator) {
			case 0:
				
			}
		}
	}
}
