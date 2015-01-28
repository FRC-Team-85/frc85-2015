package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

public class Intake {

	private static Joystick joystick;
	
	private static Solenoid arms;
	
	private static Solenoid wrists;
	
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
			int armsAddress
			int wristsAddress
			int leftPhalangeAddress,int rightPhalangeAddress) {
		joystick = intakeJoystick;
		bothArms = new Solenoid(armsAddress);
		bothWrists = new Solenoid(wristsAddress);
		phalangeLeft = new CANTalon(phalangeLeftAddress);
		phalangeRight = new CANTalon (phalangeRightAddress);
	}
	
	private boolean armsOpenOrClosed() {
		boolean armClosed;
		armClosed = (armLeft.get() && armRight.get()) ? true : false;
		return armClosed;
			boolean armOpen;
			armOpen = (!armLeft.get() && !armRight.get()) ? true : false;
			return armOpen;
	}
	
	private boolean wristsOpenOrClosed() {
		boolean wristClosed;
		wristClosed = (wristLeft.get() && wristRight.get()) ? true : false;
		return wristClosed;
			boolean wristOpen;
			wristOpen = (!wristLeft.get() && !wristRight.get()) ? true : false;
			return wristOpen;
	}
	
	private void setArm(boolean input) {
		arms.set(input);
	}

	private void setWrist(boolean input) {
		wrists.set(input);
	}

	private void openOrCloseArms() {
		if (!armsOpen()) {
			setArms(false);
			private void closeArm() {
				if (!armsClosed()) {
					setArm(true);
		}
	}
	


	private void wristsOpenOrClosed() {
		if (!wristsOpen()) {
			setWrists(false);
		}
	}

	

		
	private void toggleArms() { // preference is if neither open or closed then open arm to avoid accidents
		if (!armsOpen()) {
			armsClosed();
		} else {
			closeArms();
		}
	}
	
	private void toggleWrists() { 			// preference is if neither open or closed then open wrist to avoid accidents
		if (!wristsOpen()) {
			openWrists();
		} else {
			closeWrists();
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
		openArms();
		openWrists();
		breakPhalanges();
	}
	
	public void run() {
		
		if (!joystick.getRawButton(RESET)) {
		
			if (joystick.getRawButton(TOGGLEARM)) {
				toggleArm();
			}
			
			if (joystick.getRawButton(TOGGLEWRIST)) {
				toggleWrist();
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