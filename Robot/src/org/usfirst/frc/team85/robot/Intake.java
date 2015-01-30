package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

public class Intake {

	private static Joystick joystick;
	
	private static Solenoid arm;
	private static Solenoid wrist;
	private static CANTalon phalangeLeft;
	private static CANTalon phalangeRight;
	
	private static final double INSPEED = 0.0;	//one of these should be neg
	private static final double OUTSPEED = 0.0;
	
	private static final int RESET = 1;
	private static final int TOGGLEARM = 2;
	private static final int TOGGLEWRIST = 3;
	private static final int INPHALANGE = 4;
	private static final int OUTPHALANGE = 5; 		//config not set
	
	public Intake (Joystick intakeJoystick,
			int ArmAddress,
			int WristAddress,
			int leftPhalangeAddress, int rightPhalangeAddress ) {
		joystick = intakeJoystick;
		arm = new Solenoid(ArmAddress);
		wrist = new Solenoid(WristAddress);
		phalangeLeft = new CANTalon(leftPhalangeAddress);
		phalangeRight = new CANTalon(rightPhalangeAddress);
	}
	
	private void setPhalange(double output) {
		phalangeLeft.set(output);
		phalangeRight.set(output);
	}
	
	private void reset() {
		arm.set(true);
		wrist.set(true);
		setPhalange(0.0);
	}
	
	public void run() {

		if (joystick.getRawButton(RESET)) {
			reset();
			return;
		}
		
		if (joystick.getRawButton(TOGGLEARM)) {
			arm.set(!arm.get());
		}
			
		if (joystick.getRawButton(TOGGLEWRIST)) {
			wrist.set(!wrist.get());
		}

		if (joystick.getRawButton(INPHALANGE) && joystick.getRawButton(OUTPHALANGE)) {
			setPhalange(0.0);
		} else {		
			
			if (joystick.getRawButton(OUTPHALANGE)) {
				setPhalange(OUTSPEED);
			}
			
			if (joystick.getRawButton(INPHALANGE)) {
				setPhalange(INSPEED);
			}
				
		}
		
	}
	
	
	
}