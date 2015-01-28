package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

public class Elevator {

	private static DigitalInput topSwitch;
	private static DigitalInput bottomSwitch;
	
	private static Joystick controller;
	
	private double speed;
	
	private static CANTalon rightBeltMotor;
	private static CANTalon leftBeltMotor;
	
	public Elevator () {
		/*topSwitch = new DigitalInput(topSwitchChannel);
		bottomSwitch = new DigitalInput(bottomSwitchChannel);
		rightBeltMotor = new CANTalon(beltMotorOneAddress);
		leftBeltMotor = new CANTalon(beltMotorTwoAddress);
	*/
	}
	
	public void runLift() {
		//runMotors();
		
	}
	

	private void setMotors() {
		if(bottomSwitch.get() && controller.getX() <= 0.0 || topSwitch.get() && controller.getX() >= 0) {
			rightBeltMotor.set(0.0);
			leftBeltMotor.set(0.0);
			return;
		}
		speed = controller.getX();
		rightBeltMotor.set(speed);
		leftBeltMotor.set(speed);
	}
	
}
