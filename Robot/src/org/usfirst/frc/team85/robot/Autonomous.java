package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

public class Autonomous {
	
	private int TENFOOTDRIVE = 2292;
	private int YELLOWTOTEDRIVE = 1547;
	private int ONETOTE = 516;
	//private int TO AUTO ZONE EDGE DRIVE (ST + R) = 1337;
	
	private boolean isDoneCalculating = false;
	
	private double MAXSPEED = .6;
	private double BASE = 1.0 - MAXSPEED;
	private int ACCELERATIONCOUNT = 720; //encoder counts
	
	private boolean shortDrive = false;
	private int goal;
	private int deccelerationCount;
	private int STAGE = 0;

	private int _procedure;
	private Drive _drive;
	private Intake _intake;
	private Elevator _elevator;
	private Timer _timer;
	
	
	public Autonomous(int procedureID, Drive drive) {
		_procedure = procedureID;
		_drive = drive;
		
		_timer.start();
		_drive.resetEncoders();
	}
	
	public void runAuto() {
		switch(_procedure) {
		case 0:
			driveLinear(TENFOOTDRIVE);
			break;
		case 1:
			switch(STAGE) {
			case 0:
				setPneumatics(true);
				break;
			case 1:
				driveLinear(ONETOTE);
				break;
			case 2:
				setPneumatics(true);
				break;
			}
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
	
	private void setPneumatics(boolean bool) {
		_intake.setWrists(!bool);
		_intake.setArms(bool);
		
		if(_timer.get() > 1.0) {
			STAGE++;
			//_timer.stop();
			//_timer.reset();
		}
	}
}
