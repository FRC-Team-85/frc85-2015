package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

public class Autonomous {
	
	private int tenFootDrive = 2292;
	private int YELLOWTOTEDRIVE = 1547;
	//private int TO AUTO ZONE EDGE DRIVE (ST + R) = 1337;
	
	private boolean isDoneCalculating = false;
	
	private double MAXSPEED = 1.0;
	private int ACCELERATIONCOUNT = 360; //encoder counts
	
	private int accelerationCount;

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
			linearDrive(tenFootDrive);
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
	
	private boolean linearDrive(int goal) {	//linear
		//6 inch diameter
		if (!isDoneCalculating) {
			_drive.resetEncoders();
			if (goal < (2 * ACCELERATIONCOUNT)) {
				accelerationCount = (goal / 2);
			} else {
				accelerationCount = ACCELERATIONCOUNT;
			}
			isDoneCalculating = true;
		} else if (isDoneCalculating) {
			
			double currentCount = ((_drive.getLeftEncoders() + _drive.getRightEncodrs()) / 2);
			double speed = 0.0;
			int deccelerationCount = goal - accelerationCount;
			
			if (currentCount <= (accelerationCount)) {	//triangle one
				speed = MAXSPEED * currentCount / accelerationCount;
			} else if (currentCount > deccelerationCount && currentCount <= goal) {	//triangle two
				speed = MAXSPEED * (goal - currentCount) / accelerationCount;
			} else if (currentCount > accelerationCount && currentCount <= deccelerationCount){	//rectangle
				speed = MAXSPEED;
			} else {	//outside
				speed = 0.0;
				return true;
			}
			
			_drive.setMotors(speed, speed, speed, speed);
			
		}

		return false;
		
	}
	
	/*public int countsToDrive() {
		
	}*/
}
