package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator {

	private static Joystick controller;
	
	private static AnalogInput topSwitch;
	private static AnalogInput bottomSwitch;
	
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
	
	private static int accelDistance = 180;
	private static int positionTolerance = 10;
	
	private static final double voltageLimit = 2.5;
	
	private static double fastSpeed = .4;
	private static double slowSpeed = .2;
	private static int goalPos1 = 100;
	private static int goalPos2 = 200;
	private static int goalPos3 = 300;
	private static int goalPos4 = 400;
	
	public Elevator (Joystick opController) {
	
	  	controller = opController;
	  	
		topSwitch = new AnalogInput(Addresses.TOPSWITCH_CHANNEL);
		bottomSwitch = new AnalogInput(Addresses.BOTTOMSWITCH_CHANNEL);
		rightBeltMotor = new CANTalon(Addresses.LEFT_BELT_MOTOR);
		leftBeltMotor = new CANTalon(Addresses.RIGHT_BELT_MOTOR);
	
		elevatorCounter = new Encoder(Addresses.ELEVATOR_ENCODER_CHANNEL_A,Addresses.ELEVATOR_ENCODER_CHANNEL_B);
		
		locks = new Solenoid(Addresses.PNEUMATIC_CONTROLLER_CID, Addresses.LOCKS_SOLENOID_CHANNEL);
		hookA = new Solenoid(Addresses.PNEUMATIC_CONTROLLER_CID, Addresses.HOOK_A_SOLENOID_CHANNEL);
		hookB = new Solenoid(Addresses.PNEUMATIC_CONTROLLER_CID, Addresses.HOOK_B_SOLENOID_CHANNEL);
	}
	
	public void runLift() {
		moveElevator();
		//runMotors(controller.getY());
		int encoderCount = elevatorCounter.get();
		hookSafety(encoderCount);
		//locks.set(controller.getRawButton(Addresses.LOCKTOGGLE));
		SmartDashboard.putInt("Elevator Encoder", encoderCount);
		SmartDashboard.putNumber("Top Limit Switch", topSwitch.getVoltage());
		SmartDashboard.putNumber("Bottom Limit Switch", bottomSwitch.getVoltage());
	}
	

	private void runMotors(double speed) {
		/*if(checkLimit(bottomSwitch) && speed <= 0.0 || checkLimit(topSwitch) && speed >= 0.0) {
			rightBeltMotor.set(0.0);
			leftBeltMotor.set(0.0);
		} else {*/
			rightBeltMotor.set(speed);
			leftBeltMotor.set(speed);
		//}
	}
	
	private void hookSafety(int count) {
		if(count >= HOOKAPOS) {
			hookA.set(true);
		} else if(controller.getRawAxis(Addresses.HOOK_A_AXIS) > 0 && count <= HOOKAPOS) {
			hookA.set(false);
		} else if(controller.getRawAxis(Addresses.HOOK_A_AXIS) < 0 && count <= HOOKAPOS) {
			hookA.set(true);
		}
		if(count >= HOOKBPOS) {
			hookB.set(true);
		} else if(controller.getRawAxis(Addresses.HOOK_B_AXIS) > 0 && count <= HOOKBPOS) {
			hookB.set(false);
		} else if(controller.getRawAxis(Addresses.HOOK_B_AXIS) < 0 && count <= HOOKBPOS) {
			hookB.set(true);
		}
	}
	
	private void moveElevator() {
		double override = controller.getY();
		if(override != 0.0) {
			runMotors(override);
		} else if(controller.getRawButton(1)){
			moveTo(goalPos1);
		} else if(controller.getRawButton(2)) {
			moveTo(goalPos2);
		} else if(controller.getRawButton(3)) {
			moveTo(goalPos3);
		} else if(controller.getRawButton(4)) {
			moveTo(goalPos4);
		} else {
			runMotors(0.0);	//Only test, can be replaced
		}
	}
	
	private void moveTo(int goal) {
		
		int currentPos = elevatorCounter.get();
		double speed;
		int relativeDist = goal - currentPos;
		
		if(Math.abs(relativeDist) < 10) {
			speed = 0;
		} else if (Math.abs(relativeDist) < 180) {
			speed = slowSpeed;
		} else {
			speed = fastSpeed;
		}
		
		if(relativeDist < 0) {
			speed = -speed;
		}
		runMotors(speed);
	}
	
	/*private void autoStore() {
		if(controller.getRawButton(Addresses.AUTO_STORE)) {
			switch(totesOnElevator) {
			case 0:
				
			}
		}
	}
	
	private void checkCount() {
		if (bottomSwitch.get()) {
			elevatorCounter.reset();
		}
		count = elevatorCounter.get();
		//convertedCount = Math.PI * 2 * radius * count / 360;
		System.out.println("Count: " + count);
	}*/
	
	private boolean checkLimit(AnalogInput input) {
		return !(input.getVoltage() >= voltageLimit);
	}
}
