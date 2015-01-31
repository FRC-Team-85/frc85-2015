package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

public class Intake {

	private static Joystick _operatorController;
	
	private static Solenoid _arm;
	private static Solenoid _wrist;
	private static CANTalon _phalangeLeft;
	private static CANTalon _phalangeRight;
	
	private static final double INSPEED = 0.0;	//one of these should be neg
	private static final double OUTSPEED = 0.0;

	private static boolean _armToggleHeld;
	private static boolean _wristToggleHeld;
	
	public Intake (Joystick opController) {
		_operatorController = opController;
		_arm = new Solenoid(Addresses.PNEUMATIC_CONTROLLER_CID, Addresses.ARM_SOLENOID_CHANNEL);
		_wrist = new Solenoid(Addresses.PNEUMATIC_CONTROLLER_CID, Addresses.WRIST_SOLENOID_CHANNEL);
		_phalangeLeft = new CANTalon(Addresses.LEFT_PHALANGE_MOTOR);
		_phalangeRight = new CANTalon(Addresses.RIGHT_PHALANGE_MOTOR);
	}
	
	private void setPhalange(double output) {
		_phalangeLeft.set(output);
		_phalangeRight.set(output);
	}
	
	private void reset() {
		_arm.set(false);
		_wrist.set(false);
		setPhalange(0.0);
	}
	
	public void run() {

		if (_operatorController.getRawButton(Addresses.RESET)) {
			reset();
			return;
		}
		
		if (_operatorController.getRawButton(Addresses.TOGGLEARM) && !_armToggleHeld) {
			_arm.set(!_arm.get());
			_armToggleHeld = true;
		} else { _armToggleHeld = false;}
			
		if (_operatorController.getRawButton(Addresses.TOGGLEWRIST) && !_wristToggleHeld) {
			_wrist.set(!_wrist.get());
			_wristToggleHeld = true;
		} else { _wristToggleHeld = false;}

		if (!_operatorController.getRawButton(Addresses.INPHALANGE) && !_operatorController.getRawButton(Addresses.OUTPHALANGE)) {
			setPhalange(0.0);
		} else {		
			
			if (_operatorController.getRawButton(Addresses.OUTPHALANGE)) {
				setPhalange(OUTSPEED);
			}
			
			if (_operatorController.getRawButton(Addresses.INPHALANGE)) {
				setPhalange(INSPEED);
			}
				
		}
		
	}
	
	
	
}