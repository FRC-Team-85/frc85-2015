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

	private static boolean _alreadyRestarted = false;
	
	private static Timer _timer;
	private static Solenoid _hookA;
	private static final int HOOKAPOS = 2510; //Practice Bot 2400 Comp Bot 2400		+60
	
	private static Intake _intake;
	
	private static int _accelDistance = 180;
	public static int _positionTolerance = 5;
	
	private static final double voltageLimit = 2.5;

	private static double _fastSpeed = 1;
	private static double _slowSpeed = .4;
	
	//Positions for elevator
	public static int posSuckItIn = 1045;

	public static int posLoad = 460;
	public static int posHookA = 2240; //Practice Bot 2100 Comp bot 2180		+1" = 2240
	public static int posRide = 675; // practice bot 1240 comp bot 615			+60
	public static int toteTop = 500;

	private static int SOFT_HEIGHT_LIMIT_HIGH = 3700;	//3700
	private static int SOFT_HEIGHT_LIMIT_LOW = 200;		// 200
	private static double SOFT_LIMIT_SCALE = 0.25;
	
	public Elevator (Joystick opController, Intake intake) {
	
	  	_controller = opController;
	  	
		_topSwitch = new AnalogInput(Addresses.TOPSWITCH_CHANNEL);
		_bottomSwitch = new AnalogInput(Addresses.BOTTOMSWITCH_CHANNEL);
		_rightBeltMotor = new CANTalon(Addresses.LEFT_BELT_MOTOR);
		_leftBeltMotor = new CANTalon(Addresses.RIGHT_BELT_MOTOR);
	
		_elevatorCounter = new Encoder(Addresses.ELEVATOR_ENCODER_CHANNEL_A,Addresses.ELEVATOR_ENCODER_CHANNEL_B);
		
		_hookA = new Solenoid(Addresses.PNEUMATIC_CONTROLLER_CID, Addresses.HOOK_A_SOLENOID_CHANNEL);
		_timer = new Timer();
		
		_timer.start();
		_intake = intake;
	}
	
	public void runLift() {
		if (checkLimit(_bottomSwitch))
		{
			_elevatorCounter.reset();
		}
		
		int encoderCount = _elevatorCounter.get();
		hookSafety(encoderCount);
		moveElevator(encoderCount);
		
		SmartDashboard.putInt("Elevator Encoder", encoderCount);
		SmartDashboard.putNumber("Top Limit Switch", _topSwitch.getVoltage());
		SmartDashboard.putNumber("Bottom Limit Switch", _bottomSwitch.getVoltage());
		
	}
	

	private void runMotors(double speed, int currentPosition) {
		if(checkLimit(_bottomSwitch) && speed > 0.0 || checkLimit(_topSwitch) && speed < 0.0) {
			speed = 0.0;
		} else if (currentPosition <= SOFT_HEIGHT_LIMIT_LOW && speed > _slowSpeed || currentPosition >= SOFT_HEIGHT_LIMIT_HIGH && speed < -_slowSpeed) {
			speed *= SOFT_LIMIT_SCALE;
		}

		_leftBeltMotor.set(speed);
		_rightBeltMotor.set(speed);
	}
	
	private void hookSafety(int count) {
		int POV = _controller.getPOV();
		
		_hookA.set(count >= HOOKAPOS || _controller.getRawButton(Addresses.HOOK_BUTTON));
		
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
			moveTo(posLoad);
		} 
		else if(_controller.getRawButton(Addresses.BUTTON_LOAD)) {
			if(!_alreadyRestarted) {
				_timer.reset();
				_alreadyRestarted = true;
				_intake.setWrists(true);
			}
			if(_timer.get() >= 0) {
				moveTo(posSuckItIn);
			}
		} else if(_controller.getRawButton(Addresses.BUTTON_HOOK_A)) {
			_intake.setWrists(true);
			moveTo(posHookA);
		} else if(_controller.getRawButton(Addresses.BUTTON_HOOK_B)) {
			moveTo(posRide);
		} 
		
		else if(_controller.getRawButton(Addresses.TOTE_TOP)) {
			moveTo(toteTop);
		} 
		
		else {
			runMotors(_controller.getY(), encoderCount);
			_alreadyRestarted = false;
		}
	}
	
	public void moveTo(int goal) {
		double speed;
		int currentPos = _elevatorCounter.get();
		int relativeDist = goal - currentPos;
		SmartDashboard.putInt("Goal Position", goal);
		SmartDashboard.putInt("Relative Distance", relativeDist);
		if(Math.abs(relativeDist) < _positionTolerance) {
			speed = 0.0;
		} else if (Math.abs(relativeDist) < 160) {
			speed = _fastSpeed * Math.abs(relativeDist) / 160;
		} else {
			speed = _fastSpeed;
		}
		
		if(speed <= .2 && speed != 0.0) {
			speed = .2;//.2 before
		}
		
		if(relativeDist > 0) {
			speed = -speed;
		}
		hookSafety(currentPos);
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
	
	public int getCurrentCount() {
		return _elevatorCounter.get();
	}
	
	public void stop() {
		runMotors(0.0,_elevatorCounter.get());
	}
	
	public boolean atBottom() {
		return checkLimit(_bottomSwitch);
	}
	
	public boolean atTop() {
		return checkLimit(_topSwitch);
	}
	
	public void setHook(boolean set) {
		_hookA.set(set);
	}
	
	public void setBrakeMode(boolean set) {
		_rightBeltMotor.enableBrakeMode(set);
		_leftBeltMotor.enableBrakeMode(set);
	}
	
	
}
