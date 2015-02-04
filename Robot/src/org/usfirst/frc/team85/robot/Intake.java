package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

public class Intake {

	private static Joystick _operatorController;
	
	private static Solenoid _arm;
	private static Solenoid _wrist;
	private static CANTalon _leftMotor;
	private static CANTalon _rightMotor;
	
	private static final double INSPEED = 0.0;

	private static boolean _isArmToggleHeld;
	
	private boolean _armOutput = false;
	
	public Intake (Joystick opController) {
		_operatorController = opController;
		_arm = new Solenoid(Addresses.PNEUMATIC_CONTROLLER_CID, Addresses.ARM_SOLENOID_CHANNEL);
		_wrist = new Solenoid(Addresses.PNEUMATIC_CONTROLLER_CID, Addresses.WRIST_SOLENOID_CHANNEL);
		_leftMotor = new CANTalon(Addresses.LEFT_PHALANGE_MOTOR);
		_rightMotor = new CANTalon(Addresses.RIGHT_PHALANGE_MOTOR);
	}
	
	private void setPhalange(double output) {
		_leftMotor.set(output);
		_rightMotor.set(output);
	}
	
	public void run() {
		
		if (_operatorController.getRawButton(Addresses.TOGGLEARM)) {
			//_armOutput = !_armOutput;
			_arm.set(true);
			//_isArmToggleHeld = true;
		} else {_arm.set(false);}
		
		if (_operatorController.getRawButton(Addresses.TOGGLEWRIST)) {
			_wrist.set(true);
		} else {
			_wrist.set(false);
		}
		
		if (_operatorController.getRawButton(Addresses.INPHALANGE)) {
			setPhalange(INSPEED);
		} else {
			setPhalange(0.0);				
		}
		
	}
	
	
	
}