package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake {

	private static Joystick _operatorController;
	
	private static Solenoid _arm;
	private static Solenoid _wrist;
	private static CANTalon _leftMotor;
	private static CANTalon _rightMotor;
	private Solenoid _scythe;
	private boolean reapingAuthorized = false;
	private Timer _timer;
	private boolean _timerRestarted;
	
	private static final double INSPEED = .8;
	
	public Intake (Joystick opController, boolean partyTime) {
		_operatorController = opController;
		_arm = new Solenoid(Addresses.PNEUMATIC_CONTROLLER_CID, Addresses.ARM_SOLENOID_CHANNEL);
		_wrist = new Solenoid(Addresses.PNEUMATIC_CONTROLLER_CID, Addresses.WRIST_SOLENOID_CHANNEL);
		_leftMotor = new CANTalon(Addresses.LEFT_INTAKE_MOTOR);
		_rightMotor = new CANTalon(Addresses.RIGHT_INTAKE_MOTOR);
		_scythe = new Solenoid(Addresses.PNEUMATIC_CONTROLLER_CID, Addresses.SCYTHE_SOLENOID_CHANNEL);
		reapingAuthorized = partyTime;
		_timer = new Timer();
		_timerRestarted = false;
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
		
		reap();
		
		if(_operatorController.getRawButton(Addresses.SUCKING_SIDEWAYS)) {
			if(!_timerRestarted) {
				_timer.reset();
			}
			if(_timer.get() <= 1) {
				_leftMotor.set(INSPEED / 2);
				_rightMotor.set(-INSPEED / 2);
			}
			if(_timer.get() <= 2 && _timer.get() > 1) {
				setIntakeMotors(INSPEED / 2);
			}
			if(_timer.get() > 2) {
				setIntakeMotors(0.0);
			}
		} else {
			_timerRestarted = false;
		}
		SmartDashboard.putNumber("Intake Timer", _timer.get());
	}
	
	private void reap() {
		if (reapingAuthorized) {
			int POV = _operatorController.getPOV();
			if (POV == 0) {
				_scythe.set(true);
			}
			if (POV == 180) {
				_scythe.set(false);
			}
		} else {
			_scythe.set(false);
		}
	}
	
	public void reap(boolean reap) {
		if (reapingAuthorized) {
			_scythe.set(reap);
		} else {
			_scythe.set(false);
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