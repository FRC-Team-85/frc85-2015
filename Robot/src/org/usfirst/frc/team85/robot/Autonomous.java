package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous {
	
	private int SIXFOOTDRIVE = 1375;
	private int YELLOWTOTEDRIVE = 1547;
	private int ONETOTE = 516;
	private int TOTETOAUTO = 1900;
	//private int TO AUTO ZONE EDGE DRIVE (ST + R) = 1337;
	
	private boolean isDoneCalculating = false;
	
	private double MAXSPEED = .6;
	private double BASE = .8 - MAXSPEED; //Changed from 1.0 to .7
	private int ACCELERATIONCOUNT = 720; //encoder counts
	
	private boolean shortDrive = false;
	private int goal;
	private int deccelerationCount;
	private int STAGE = 0;
	private int SUBSTAGE = 0;

	private int _procedure;
	private Drive _drive;
	private Intake _intake;
	private Elevator _elevator;
	public Timer _timer;
	//private Gyro _gyro;
	
	private boolean _alreadyRestarted = false;
	
	
	public Autonomous(int procedureID, Drive drive, Intake intake, Elevator elevator) {
		_procedure = 1;
		_drive = drive;
		_intake = intake;
		_elevator = elevator;
		_timer = new Timer();
		//_gyro = new Gyro(Addresses.GYRO_CHANNEL);
		
		_timer.start();
		_drive.resetEncoders();
	}
	
	public void runAuto() {
		switch(_procedure) {
		case 0://Do nothing, no plastic ramp
			driveLinear(YELLOWTOTEDRIVE);
			break;
		case 1://Pick up tote and can
			switch(STAGE) {
			case 0:
				pickUpTote();
				break;
			case 1:
				driveLinear(ONETOTE);
				break;
			case 2:
				if(_timer.get() <= .5) {
					setPneumatics(true);
				} else {
					STAGE++;
				}
				break;
			case 3:
				if(Math.abs(_elevator.getCurrentCount() - (_elevator.posBottom + 50)) >= _elevator._positionTolerance) {
					_elevator.moveTo(_elevator.posBottom + 50);
				} else {
					STAGE++;
				}
				break;
			case 4:
				//turnRight90();
				break;
			case 5:
				driveLinear(TOTETOAUTO);
				break;
			}
			break;
		case 2:
			
			break;
		}
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
			
			double currentCount = ((_drive.getLeftEncoders() + _drive.getRightEncoders()) / 2);
			double speed = 0.0;
			
			if(!shortDrive) {	
				if (currentCount <= ACCELERATIONCOUNT && currentCount >= 0) {	//triangle one
					speed = BASE + MAXSPEED * currentCount / ACCELERATIONCOUNT;
				} else if (currentCount > deccelerationCount && currentCount <= goal) {	//triangle two
					_drive.setBrakeMode(true);
					speed = 1.0 * (goal - currentCount) / ACCELERATIONCOUNT;
				} else if (currentCount > ACCELERATIONCOUNT && currentCount <= deccelerationCount){	//rectangle
					speed = 1.0;
				} else {	//outside
					speed = 0.0;
					STAGE++;
					_timer.reset();
				}
				
			} else {
				
				if(currentCount <= goal) {
					speed = BASE;
				} else if(currentCount > goal) {
					speed = 0.0;
					_drive.setBrakeMode(true);
					STAGE++;
					_timer.reset();
				}
			}
		
			_drive.setMotors(speed, speed, speed, speed);
			
		}
		
	}
	
	private void setPneumatics(boolean bool) {
		_intake.setWrists(!bool);
		_intake.setArms(bool);
	}
	
	private void pickUpTote() {
		switch(SUBSTAGE) {
		case 0:
			if(_timer.get() <= 1.00) {
				setPneumatics(true);
			} else {
				SUBSTAGE++;
			}
			break;
		case 1:
			if(Math.abs(_elevator.getCurrentCount() - _elevator.posHookA) >= _elevator._positionTolerance) {
				_elevator.moveTo(_elevator.posHookA);
			} else {
				_timer.reset();
				SUBSTAGE++;
			}
			break;
		case 2:
			if(!_alreadyRestarted) {
				_intake.setWrists(true);
				_alreadyRestarted = true;
			}
			if(_timer.get() >= .5) {
				SUBSTAGE++;
			}
			break;
		case 3:
			if(Math.abs(_elevator.getCurrentCount() - _elevator.posBottom) >= _elevator._positionTolerance) {
				_elevator.moveTo(_elevator.posBottom);
				if(_elevator.getCurrentCount() <= (_elevator.posHookA - 600)) {
					setPneumatics(false);
				}
			} else {
				SUBSTAGE++;
			}
			break;
		default:
			break;
		}
	}
	/*
	private void turnRight90() {
		double currentAngle = _gyro.getAngle();
		double speed;
		SmartDashboard.putNumber("GyroAngle", _gyro.getAngle());
		double TURNCOUNT = 30;//degrees
		
		if (currentAngle <= TURNCOUNT) {	//triangle one
			speed = Math.abs(BASE + MAXSPEED * currentAngle / TURNCOUNT);
		} else if (currentAngle > (90-TURNCOUNT) && currentAngle <= goal) {	//triangle two
			_drive.setBrakeMode(true);
			speed = 1.0 * (goal - currentAngle) / TURNCOUNT;
		} else if (currentAngle > TURNCOUNT && currentAngle <= (90-TURNCOUNT)){	//rectangle
			speed = 1.0;
		} else {	//outside
			speed = 0.0;
			STAGE++;
			_timer.reset();
		}
		
		_drive.setMotors(speed, -speed, speed, -speed);
		
	}
	*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
