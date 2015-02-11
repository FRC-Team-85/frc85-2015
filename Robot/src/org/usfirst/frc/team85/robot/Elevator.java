package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator {

	private static Joystick _controller;
	
	private static AnalogInput _topSwitch;
	private static AnalogInput _bottomSwitch;
	
	private static CANTalon _rightBeltMotor;
	private static CANTalon _leftBeltMotor;
	
	private static Encoder _elevatorCounter;
	
	private double _count;
	//private double convertedCount;
	//private double radius = 0.0;// in ___   inch, cm, m, etc

	private static Solenoid _locks;
	private static boolean _islockToggleHeld;
	
	private static Solenoid _hookA;
	private static final int HOOKAPOS = 2250; //from the bottom
	
	private static Solenoid _hookB;
	private static final double HOOKBPOS = 0.0; //from the bottom
	
	private static int _totesOnElevator = 0;
	
	private static int _accelDistance = 180;
	private static int _positionTolerance = 10;
	
	private static final double voltageLimit = 2.5;

	private static double _fastSpeed = 1;
	private static double _slowSpeed = .4;
	
	//Positions for elevator
	private static int _posBottom = 0;
	private static int _posLoad = 220; 
	private static int _posHookA = 2160;
	private static int _posHookB = 3250; 

	private static int SOFT_HEIGHT_LIMIT_HIGH = 3700;	//3700
	private static int SOFT_HEIGHT_LIMIT_LOW = 200;		// 200
	private static double SOFT_LIMIT_SCALE = 0.25;
	
	public Elevator (Joystick opController) {
	
	  	_controller = opController;
	  	
		_topSwitch = new AnalogInput(Addresses.TOPSWITCH_CHANNEL);
		_bottomSwitch = new AnalogInput(Addresses.BOTTOMSWITCH_CHANNEL);
		_rightBeltMotor = new CANTalon(Addresses.LEFT_BELT_MOTOR);
		_leftBeltMotor = new CANTalon(Addresses.RIGHT_BELT_MOTOR);
	
		_elevatorCounter = new Encoder(Addresses.ELEVATOR_ENCODER_CHANNEL_A,Addresses.ELEVATOR_ENCODER_CHANNEL_B);
		
		_locks = new Solenoid(Addresses.PNEUMATIC_CONTROLLER_CID, Addresses.LOCKS_SOLENOID_CHANNEL);
		_hookA = new Solenoid(Addresses.PNEUMATIC_CONTROLLER_CID, Addresses.HOOK_A_SOLENOID_CHANNEL);
		_hookB = new Solenoid(Addresses.PNEUMATIC_CONTROLLER_CID, Addresses.HOOK_B_SOLENOID_CHANNEL);
	}
	
	public void runLift() {
		if (checkLimit(_bottomSwitch))
		{
			_elevatorCounter.reset();
		}
		
		int encoderCount = _elevatorCounter.get();
		hookSafety(encoderCount);
		moveElevator(encoderCount);
		//locks.set(controller.getRawButton(Addresses.LOCKTOGGLE));
		SmartDashboard.putInt("Elevator Encoder", encoderCount);
		SmartDashboard.putNumber("Top Limit Switch", _topSwitch.getVoltage());
		SmartDashboard.putNumber("Bottom Limit Switch", _bottomSwitch.getVoltage());
	}
	

	private void runMotors(double speed, int currentPosition) {
		if(checkLimit(_bottomSwitch) && speed > 0.0 || checkLimit(_topSwitch) && speed < 0.0) {
			speed = 0;
		} else if (currentPosition <= SOFT_HEIGHT_LIMIT_LOW && speed > _slowSpeed || currentPosition >= SOFT_HEIGHT_LIMIT_HIGH && speed < -_slowSpeed) {
			speed *= SOFT_LIMIT_SCALE;
		}
		
		SmartDashboard.putNumber("Elevator motor speed", speed);
		if(Math.abs(speed) >= .05) {
			_locks.set(false);
		}
		_leftBeltMotor.set(speed);
		_rightBeltMotor.set(speed);
	}
	
	private void hookSafety(int count) {
		int POV = _controller.getPOV();
		
		_hookA.set(count >= HOOKAPOS || _controller.getRawButton(Addresses.HOOK_BUTTON));
		
		
		if(POV == 0) {
			_locks.set(true);
		} else if(POV == 180) {
			_locks.set(false);
		}
		
		
		/*if(count >= HOOKBPOS) {
			_hookB.set(true);
		} else if(_controller.getRawAxis(Addresses.HOOK_B_AXIS) > 0.0) {
			_hookB.set(false);
		} else if(_controller.getRawAxis(Addresses.HOOK_B_AXIS) < 0.0) {
			_hookB.set(true);
		}*/
		
	}
	
	private void moveElevator(int encoderCount) {
		if(_controller.getRawButton(Addresses.BUTTON_BOTTOM)) {
			moveTo(_posBottom, encoderCount);
		} else if(_controller.getRawButton(Addresses.BUTTON_LOAD)) {
			moveTo(_posLoad, encoderCount);
		} else if(_controller.getRawButton(Addresses.BUTTON_HOOK_A)) {
			moveTo(_posHookA, encoderCount);
		} else if(_controller.getRawButton(Addresses.BUTTON_HOOK_B)) {
			moveTo(_posHookB, encoderCount);
		} else {
			runMotors(_controller.getY(), encoderCount);
		}
	}
	
	private void moveTo(int goal, int currentPos) {
		double speed;
		int relativeDist = goal - currentPos;
		SmartDashboard.putInt("Goal Position", goal);
		SmartDashboard.putInt("Relative Distance", relativeDist);
		if(Math.abs(relativeDist) < 10) {
			speed = 0;
		} else if (Math.abs(relativeDist) < 180) {
			speed = _slowSpeed;
		} else {
			speed = _fastSpeed;
		}
		
		if(relativeDist > 0) {
			speed = -speed;
		}
		
		runMotors(speed, currentPos);
	}
	
	/*private void autoStore() {
		if(controller.getRawButton(Addresses.AUTO_STORE)) {
			switch(totesOnElevator) {
			case 0:
				
			}
		}

	}*/
	
	private boolean checkLimit(AnalogInput input) {
		return !(input.getVoltage() >= voltageLimit);
	}
}
