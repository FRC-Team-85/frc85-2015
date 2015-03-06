package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

public class Intake {

	private static Joystick _operatorController;
	
	private static Solenoid _arm;
	private static Solenoid _wrist;
	private static CANTalon _leftMotor;
	private static CANTalon _rightMotor;
	
	private static final double INSPEED = .5;
	
	public Intake (Joystick opController) {
		_operatorController = opController;
		_arm = new Solenoid(Addresses.PNEUMATIC_CONTROLLER_CID, Addresses.ARM_SOLENOID_CHANNEL);
		_wrist = new Solenoid(Addresses.PNEUMATIC_CONTROLLER_CID, Addresses.WRIST_SOLENOID_CHANNEL);
		_leftMotor = new CANTalon(Addresses.LEFT_INTAKE_MOTOR);
		_rightMotor = new CANTalon(Addresses.RIGHT_INTAKE_MOTOR);
	}
	
	public void run() {
		if (_operatorController.getRawButton(Addresses.ARM_IN)) {
			_arm.set(false);
		}
		
		if (_operatorController.getRawButton(Addresses.ARM_OUT)) {
			_arm.set(true);
		}
		
		if (_operatorController.getRawButton(Addresses.WRIST_IN)) {
			_wrist.set(false);
			setIntakeMotors(INSPEED);
		}
		else
		{
			setIntakeMotors(0.0);
		}
		
		if (_operatorController.getRawButton(Addresses.BUTTON_LOAD) || _operatorController.getRawButton(Addresses.WRIST_OUT)) {
			_wrist.set(true);
		}
	}
	
	private void setIntakeMotors(double output) {
		_leftMotor.set(output);
		_rightMotor.set(output);
	}
	
	public void pnumaticsOff() {
		_wrist.set(false);
		_arm.set(false);
		setIntakeMotors(0.0);
	}
	
	public void setWrists(boolean bool) {
		_wrist.set(bool);
		if(!bool) {
			setIntakeMotors(INSPEED);
		} else {
			setIntakeMotors(0.0);
		}
	}
	
	public void setArms(boolean bool) {
		_arm.set(bool);
	}
}