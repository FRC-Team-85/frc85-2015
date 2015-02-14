package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

public class Autonomous {
	
	private int tenFootDrive = 2292;
	private int YELLOWTOTEDRIVE = 1547;
	//private int TO AUTO ZONE EDGE DRIVE (ST + R) = 1337;
	
	private boolean isDoneCalculating = false;
	
	private double MAXSPEED = .6;
	private double BASE = 1.0 - MAXSPEED;
	private int ACCELERATIONCOUNT = 720; //encoder counts
	
	private boolean shortDrive = false;
	private int goal;
	private int deccelerationCount;

	private int _procedure;
	private Drive _drive;
	
	
	public Autonomous(int procedureID, Drive drive) {
		_procedure = procedureID;
		_drive = drive;
		
		_drive.resetEncoders();
	}
	
	public void runAuto() {
		switch(_procedure) {
		case 0:
			driveLinear(tenFootDrive);
			break;
		}
		/*
		case 1:
			//intake,up
			//turn
			//drive
			break;
		
		
		 */
	}
	/*
	private void linearDrive(int target) {	//linear
		//6 inch diameter
		if (!isDoneCalculating) {
		//	shortDrive = false;
			_drive.resetEncoders();
			if (target <= (2 * ACCELERATIONCOUNT)) {
		//		shortDrive = true;
				goal = target - 180;//tempoffset
			} else {
		//		shortDrive = false;
				goal = target -180;
			}
			isDoneCalculating = true;
			deccelerationCount = goal - accelerationCount;
			_drive.setBrakeMode(false);
		} else if (isDoneCalculating) {
			
			double currentCount = ((_drive.getLeftEncoders() + _drive.getRightEncodrs()) / 2);
			double speed = 0.0;
			
			if (currentCount <= (accelerationCount) && currentCount >= 0) {	//triangle one
				speed = MAXSPEED * currentCount / accelerationCount + BASE;
			} else if (currentCount > deccelerationCount && currentCount <= goal) {	//triangle two
				_drive.setBrakeMode(true);
				speed = 1.0 * (goal - currentCount) / accelerationCount;
			} else if (currentCount > accelerationCount && currentCount <= deccelerationCount){	//rectangle
				speed = 1.0;
			} else {	//outside
				speed = 0.0;
			//	return true;
			}
	
			if (speed <= .1) {
				speed = 0.1;
			}
			
				
				if (shortDrive) {
					
					if (currentCount < goal) {
						speed = 0.4;
					} else {
						_drive.setBrakeMode(true);
						speed = 0.0;
					}
					
				}

			_drive.setMotors(speed, speed, speed, speed);
			
		}

		//return false;
		
	}*/
	
	/*public int countsToDrive() {
		
	}*/
	
	public void driveLinear(int target) {
		if(!isDoneCalculating) {
			_drive.resetEncoders();
			
			if(target <=  2 * ACCELERATIONCOUNT) {
				shortDrive = true;
			}
			
			goal = target - 180;
			deccelerationCount = goal - ACCELERATIONCOUNT;
			isDoneCalculating = true;
			_drive.setBrakeMode(false);
			
		} else {
			
			double currentCount = ((_drive.getLeftEncoders() + _drive.getRightEncodrs()) / 2);
			double speed = 0.0;
			
			if (currentCount <= ACCELERATIONCOUNT && currentCount >= 0) {	//triangle one
				speed = BASE + MAXSPEED * currentCount / ACCELERATIONCOUNT;
			} else if (currentCount > deccelerationCount && currentCount <= goal) {	//triangle two
				_drive.setBrakeMode(true);
				speed = 1.0 * (goal - currentCount) / ACCELERATIONCOUNT;
			} else if (currentCount > ACCELERATIONCOUNT && currentCount <= deccelerationCount){	//rectangle
				speed = 1.0;
			} else {	//outside
				speed = 0.0;
			//	return true;
			}
			
			_drive.setMotors(speed, speed, speed, speed);
			
		}
		
	}
}
