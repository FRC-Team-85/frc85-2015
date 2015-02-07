package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

public class Elevator {

	private static Joystick _controller;
	
	private static DigitalInput _topSwitch;
	private static DigitalInput _bottomSwitch;
	
	private static CANTalon _rightBeltMotor;
	private static CANTalon _leftBeltMotor;
	
	private static Encoder _elevatorCounter;
	
	private double _count;
	//private double convertedCount;
	//private double radius = 0.0;// in ___   inch, cm, m, etc

	private static Solenoid _locks;
	private static boolean _islockToggleHeld;
	
	private static Solenoid _hookA;
	private static final double HOOKAPOS = 0.0; //from the bottom
	
	private static Solenoid _hookB;
	private static final double HOOKBPOS = 0.0; //from the bottom
	
	private static int _totesOnElevator = 0;
	
	private static int _accelDistance = 180;
	private static int _positionTolerance = 10;
	
	private static double _fastSpeed = .4;
	private static double _slowSpeed = .2;
	private static int _goalPos1 = 100;
	private static int _goalPos2 = 200;
	private static int _goalPos3 = 300;
	private static int _goalPos4 = 400;
	
	public Elevator (Joystick opController) {
	
	  	_controller = opController;
	  	
		_topSwitch = new DigitalInput(Addresses.TOPSWITCH_CHANNEL);
		_bottomSwitch = new DigitalInput(Addresses.BOTTOMSWITCH_CHANNEL);
		_rightBeltMotor = new CANTalon(Addresses.LEFT_BELT_MOTOR);
		_leftBeltMotor = new CANTalon(Addresses.RIGHT_BELT_MOTOR);
	
		_elevatorCounter = new Encoder(Addresses.ELEVATOR_ENCODER_CHANNEL_A,Addresses.ELEVATOR_ENCODER_CHANNEL_B);
		
		_locks = new Solenoid(Addresses.PNEUMATIC_CONTROLLER_CID, Addresses.LOCKS_SOLENOID_CHANNEL);
		_hookA = new Solenoid(Addresses.PNEUMATIC_CONTROLLER_CID, Addresses.HOOK_A_SOLENOID_CHANNEL);
		_hookB = new Solenoid(Addresses.PNEUMATIC_CONTROLLER_CID, Addresses.HOOK_B_SOLENOID_CHANNEL);
		
	}
	
	public void runLift() {
		//moveElevator();
		runMotors(_controller.getY());
		hookSafety(_elevatorCounter.get());
		//locks.set(controller.getRawButton(Addresses.LOCKTOGGLE));
	}
	

	private void runMotors(double speed) {
		if(!_bottomSwitch.get() && speed <= 0.0 || !_topSwitch.get() && speed >= 0.0) {
			_rightBeltMotor.set(0.0);
			_leftBeltMotor.set(0.0);
			return;
		}
		_rightBeltMotor.set(speed);
		_leftBeltMotor.set(speed);
	}
	
	private void hookSafety(int count) {
		_hookA.set(!(count >= _HOOKAPOS));
		_hookB.set(!(count >= _HOOKBPOS));
	}
	
	private void moveElevator() {
		double override = _controller.getY();
		if(override != 0.0) {
			runMotors(override);
		} else if(_controller.getRawButton(1)){
			moveTo(_goalPos1);
		} else if(_controller.getRawButton(2)) {
			moveTo(_goalPos2);
		} else if(_controller.getRawButton(3)) {
			moveTo(_goalPos3);
		} else if(_controller.getRawButton(4)) {
			moveTo(_goalPos4);
		} else {
			runMotors(0.0);	//Only test, can be replaced
		}
	}
	
	private void moveTo(int goal) {
		
		int currentPos = _elevatorCounter.get();
		double speed;
		int relativeDist = goal - currentPos;
		
		if(Math.abs(relativeDist) < 10) {
			speed = 0;
		} else if (Math.abs(relativeDist) < 180) {
			speed = _slowSpeed;
		} else {
			speed = _fastSpeed;
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
}
