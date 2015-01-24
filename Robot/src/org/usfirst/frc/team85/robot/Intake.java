package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

public class Intake {

	private Joystick joystick;
	
	private Solenoid armLeft;
	private Solenoid armRight;
	
	private Solenoid wristLeft;
	private Solenoid wristRight;
	
	private CANTalon phalangeLeft;
	private CANTalon phalangeRight;
	
	private double inSpeed;// 0.0
	private double outSpeed;// -0.0
	
	public Intake (Joystick intakeJoystick,
			int leftArmAddress,int rightArmAddress,
			int leftWristAddress,int rightWristAddress,
			int leftPhalangeAddress,int rightPhalangeAddress) {
		joystick = intakeJoystick;
		armLeft = new Solenoid(leftArmAddress);
		armRight = new Solenoid(rightArmAddress);
		wristLeft = new Solenoid(leftWristAddress);
		wristRight = new Solenoid(rightWristAddress);
		phalangeLeft = new CANTalon(leftPhalangeAddress);
		phalangeRight = new CANTalon(rightPhalangeAddress);
	}
	
	private boolean isArmClosed() {
		boolean armClosed;
		armClosed = (armLeft.get() && armRight.get()) ? true : false;
		return armClosed;
	}
	
	private boolean isArmOpen() {
		boolean armOpen;
		armOpen = (!armLeft.get() && !armRight.get()) ? true : false;
		return armOpen;
	}

	private boolean isWristClosed() {
		boolean wristClosed;
		wristClosed = (wristLeft.get() && wristRight.get()) ? true : false;
		return wristClosed;
	}
	
	private boolean isWristOpen() {
		boolean wristOpen;
		wristOpen = (!wristLeft.get() && !wristRight.get()) ? true : false;
		return wristOpen;
	}

	private void setArm(boolean input) {
		armLeft.set(input);
		armRight.set(input);
	}

	private void setWrist(boolean input) {
		wristLeft.set(input);
		wristRight.set(input);
	}

	private void openArm() {
		if (!isArmOpen()) {
			setArm(false);
		}
	}
	
	private void closeArm() {
		if (!isArmClosed()) {
			setArm(true);
		}
	}

	private void openWrist() {
		if (!isWristOpen()) {
			setWrist(false);
		}
	}

	private void closeWrist() {
		if (!isWristClosed()) {
			setWrist(true);
		}
	}
		
	private void toggleArm() { // preference is if neither open or closed then open arm to avoid accidents
		if (!isArmOpen()) {
			openArm();
		} else {
			closeArm();
		}
	}
	
	private void toggleWrist() { // preference is if neither open or closed then open wrist to avoid accidents
		if (!isWristOpen()) {
			openWrist();
		} else {
			closeWrist();
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
		setPhalange(outSpeed);
	}
	
	public void run() {
		//all that stuff
	}
	
}