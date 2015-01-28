package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

public class Intake {

	private static Joystick joystick;
	
	private static Solenoid arm;
	private static Solenoid wrist;
	
	private static CANTalon phalangeLeft;
	private static CANTalon phalangeRight;
	
	private static final double inSpeed = 0.0;		//positive
	private static final double outSpeed = 0.0;		//positive
	
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
	
	private void setArm() {
		if (arm.get()) {
			arm.set(false);
		}
		if (!arm.get()) {
			arm.set(true);
		}
	}
	

	private void setWrist() {
		if (wrist.get()) {
			wrist.set(true);
		}
		if (!wrist.get()) {
			wrist.set(false);
		}
	}
	
	private void setPhalange(double output) {
		phalangeLeft.set(output);
		phalangeRight.set(output);
	}
	
	private void inPhalange() {
		setPhalange(inSpeed);
	}
	
	private void outPhalange() {
		setPhalange(-outSpeed);
	}
	
	private void breakPhalanges() {			//named in honor of the maturity of the UberNoobs of the 2015 FRC season
		setPhalange(0.0);
	}
	
	private void reset() {
		openArm();
		openWrist();
		breakPhalanges();
	}
	
	public void run() {
		
		if (!joystick.getRawButton(RESET)) {
		
			if (joystick.getRawButton(TOGGLEARM)) {
				setArm();
			}
			
			if (joystick.getRawButton(TOGGLEWRIST)) {
				setWrist();
			}

			if (joystick.getRawButton(INPHALANGE) && joystick.getRawButton(OUTPHALANGE)) {
				breakPhalanges();
			} else {
				
				if (joystick.getRawButton(OUTPHALANGE)) {
					outPhalange();
				}
				
				if (joystick.getRawButton(INPHALANGE)) {
					inPhalange();
				}
				
			}
			
		} else {
			reset();
		}
		
	}
	
	
	
}