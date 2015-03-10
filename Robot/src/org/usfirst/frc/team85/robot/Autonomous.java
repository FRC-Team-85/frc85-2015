package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous {
	
	private int SIXFOOTDRIVE = 1375;
	private int YELLOWTOTEDRIVE = 1547;
	private int ONETOTE = 516;
	private int DRIVEINTOAUTO = 1547;
	private int WALLTOAUTO = 0;
	private int TOTETOAUTO = 0;//was 1900
	private int CANPICKUP = 600;
	private int CANPICKEDUP = 620;
	//private int TO AUTO ZONE EDGE DRIVE (ST + R) = 1337;
	
	private boolean isDoneCalculating = false;
	
	private final static double RAMPSPEED = .3;
	private final static double BASE = .8 - RAMPSPEED; //Changed from 1.0 to .7
	private final static int ACCELERATIONCOUNT = 720; //encoder counts
	
	private final static double RAMPTURNSPEED = .4;
	private final static double TURNBASE = .6 - RAMPTURNSPEED;
	private final static int TURNACCELERATIONCOUNT = 30;
	
	private boolean shortDrive = false;
	private int goal;
	private int absTurnGoal;
	private int deccelerationCount;
	private int STAGE;
	private int SUBSTAGE = 0;

	private int _procedure;
	private Drive _drive;
	private Intake _intake;
	private Elevator _elevator;
	public Timer _timer;
	private Gyro _gyro;
	
	private boolean _alreadyRestarted = false;
	private boolean _overRamp;
	
	public Autonomous(Drive drive, Intake intake, Elevator elevator,Gyro gyro) {
		_procedure = (int)SmartDashboard.getNumber("DB/Slider 0", 99);
		_overRamp = SmartDashboard.getBoolean("DB/Button 0", false);
		TOTETOAUTO = (int)(1000 * SmartDashboard.getNumber("DB/Slider 1", 0));
		
		_drive = drive;
		_intake = intake;
		_elevator = elevator;
		_timer = new Timer();
		_gyro = gyro;
		
		_timer.start();
		_drive.resetEncoders();
		STAGE = 0;
	}
	
	public void runAuto() {
		SmartDashboard.putNumber("Stage", STAGE);
		SmartDashboard.putNumber("Substage", SUBSTAGE);
		SmartDashboard.putNumber("procedure", _procedure);
		switch(_procedure) {
		case 0://Do nothing, no plastic ramp
			switch(STAGE) {//This is important don't delete
			case 0:
				driveLinear(YELLOWTOTEDRIVE);
				break;
			}
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
				if(_timer.get() <= .15) {//was .1
					_intake.setArms(true);
				} else {
					STAGE++;
				}
				break;
			case 3:
				if(Math.abs(_elevator.getCurrentCount() - (CANPICKEDUP)) >= _elevator._positionTolerance) {
					_elevator.moveTo(CANPICKEDUP);
				} else {
					_elevator.stop();
					STAGE++;
				}
				break;
			case 4:
				turn(80);
				break;
			case 5:
				driveLinear(TOTETOAUTO);
				_timer.reset();
				break;
			case 6://put timer pause here
				if(Math.abs(_elevator.getCurrentCount() - (CANPICKEDUP + 200)) >= _elevator._positionTolerance) {
					_elevator.moveTo(CANPICKEDUP + 200);
				} else {
					_elevator.stop();
					STAGE++;
				}
				break;
			case 7:
				_elevator.setHook(true);
				if(Math.abs(_elevator.getCurrentCount() - (600)) >= _elevator._positionTolerance) {
					_elevator.moveTo(600);
				} else {
					_elevator.stop();
					STAGE++;
				}
				break;
			case 8:
				_intake.setArms(false);
				break;
			}
			break;
		case 2://can
			switch(STAGE) {
			case 0:
				if(_timer.get() < .5) {
					_intake.setArms(true);
				} else {
					STAGE++;
				}
				break;
			case 1:
				if(Math.abs(_elevator.getCurrentCount() - 200) >= _elevator._positionTolerance) {
					_elevator.moveTo(200);
				} else {
					_elevator.stop();
					STAGE++;
				}
				break;
			case 2:
				driveLinear(WALLTOAUTO);
				_timer.reset();	
				break;
			case 3:
				if(_timer.get() > .5) {
					if(_elevator.atBottom()) {
						_elevator.moveTo(_elevator.getCurrentCount() - 200);
					} else {
						_intake.setArms(false);
					}
				}
				break;
			}
			break;
		case 3://get ready, set,
			_intake.setArms(true);
			_elevator.moveTo(_elevator.posLoad);
			break;
		case 4:
			switch (STAGE) {
			case 0:
				turn(90);
				break;
			}
			break;
		/*
		case 2://one tote
			switch(STAGE) {
			case 0:
				pickUpTote;
				break;
			case 1:
				turnRight90();
				break;
			case 2:
				driveLinear(YELLOWTOTEDRIVE);
				break;
			}
			break;
		*/
		default:
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
					speed = BASE + RAMPSPEED * currentCount / ACCELERATIONCOUNT;
				} else if (currentCount > deccelerationCount && currentCount <= goal) {	//triangle two
					_drive.setBrakeMode(true);
					speed = (BASE + RAMPSPEED) * (goal - currentCount) / ACCELERATIONCOUNT;
				} else if (currentCount > ACCELERATIONCOUNT && currentCount <= deccelerationCount){	//rectangle
					speed = (BASE + RAMPSPEED);
				} else {	//outside
					speed = 0.0;
					STAGE++;
					isDoneCalculating = false;
					_timer.reset();
				}
				
			} else {
				
				if(currentCount <= goal) {
					speed = BASE;
				} else if(currentCount > goal) {
					speed = 0.0;
					_drive.setBrakeMode(true);
					STAGE++;
					isDoneCalculating = false;
					_timer.reset();
				}
			}
			
			if (_overRamp) {
				//for decreasing speed
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
			if(_timer.get() <= .5) {
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
			if(_timer.get() >= .2) {
				SUBSTAGE++;
			}
			break;
		case 3:
			if (Math.abs(_elevator.getCurrentCount() - CANPICKUP) >= _elevator._positionTolerance) {
				_elevator.moveTo(CANPICKUP);
				if(_elevator.getCurrentCount() <= (_elevator.posHookA - 600)) {
					setPneumatics(false);
				}
			} else {
				SUBSTAGE++;
			}
			break;
		case 4:
			SUBSTAGE = 0;
			STAGE++;
			break;
		}
	}
	
	private void turn(int turningGoal) {//only works for big enough turns
		if (!isDoneCalculating) {
			_gyro.reset();
			isDoneCalculating = true;
			_drive.setBrakeMode(false);
			absTurnGoal = Math.abs(turningGoal) - 20;
		} else {
			double speed;
			double relativeAngle = absTurnGoal - Math.abs(_gyro.getAngle());
			double absAngle = Math.abs(_gyro.getAngle());
			SmartDashboard.putNumber("GYRO ANGLE", _gyro.getAngle());
		
			if (absAngle <= TURNACCELERATIONCOUNT) {	//triangle one
				speed = TURNBASE + (RAMPTURNSPEED * absAngle / TURNACCELERATIONCOUNT);
			} else if (absAngle > (absTurnGoal - TURNACCELERATIONCOUNT) && absAngle <= absTurnGoal) {	//triangle two
				_drive.setBrakeMode(true);
				speed = TURNBASE + (RAMPTURNSPEED * absAngle / TURNACCELERATIONCOUNT);
			} else if (absAngle > TURNACCELERATIONCOUNT && absAngle <= (absTurnGoal-TURNACCELERATIONCOUNT)){	//rectangle
				speed = TURNBASE + RAMPTURNSPEED;
			} else {	//outside
				speed = 0.0;
				STAGE++;
				_timer.reset();
				isDoneCalculating = false;
			}
		
			if (relativeAngle < 0) {//overshoot
				speed = -speed;
			}
			
			speed *= .8;
			
			if (turningGoal > 0) {
				_drive.setMotors(speed, -speed, speed, -speed);
			} else {
				_drive.setMotors(-speed, speed, -speed, speed);
			}
			
		}
		
	}
	
	public void strafe() {
		
	}
	
}
