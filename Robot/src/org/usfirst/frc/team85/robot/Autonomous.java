package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous {
	
	private int SIXFOOTDRIVE = 1375;
	private int YELLOWTOTEDRIVE = 1547;
	private int ONETOTE = 516;
	private int DRIVEINTOAUTO = 1547;
	private int WALLTOAUTO;// = 2750;
	private int TOTETOAUTO = 0;//was 1900 => try 2500
	private int FORWARD = 0;
	private int CANPICKUP = 600;
	private int CANPICKEDUP = 620;
	//private int TO AUTO ZONE EDGE DRIVE (ST + R) = 1337;
	
	private boolean isDoneCalculating = false;
	
	private final static double RAMPSPEED = .3;
	private static double BASE = .8 - RAMPSPEED; //Changed from 1.0 to .7
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
	
	private double HOWMANYSEC;
	private int ISPOSSTRAFE;

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

		_overRamp = SmartDashboard.getBoolean("DB/Button 1", false);
		FORWARD = (int)(1000 * SmartDashboard.getNumber("DB/Slider 1", 0));
		//testing
		WALLTOAUTO = (int)(1000 * SmartDashboard.getNumber("DB/Slider 1", 0));
		//testing
		TOTETOAUTO = (int)(1000 * SmartDashboard.getNumber("DB/Slider 1", 0));
		HOWMANYSEC = Math.abs(SmartDashboard.getNumber("DB/Slider 2", 0));
		
		if (SmartDashboard.getNumber("DB/Slider 2") > 0) {
			ISPOSSTRAFE = 1;
		} else {
			ISPOSSTRAFE = -1;
		}
		
		_drive = drive;
		_intake = intake;
		_elevator = elevator;
		_timer = new Timer();
		_gyro = gyro;
		
		_timer.start();
		_drive.resetEncoders();
		STAGE = 0;
		
		SmartDashboard.putString("DB/String 0", "0: Drive Forward");
		SmartDashboard.putString("DB/String 1", "1: Tote and Can");
		SmartDashboard.putString("DB/String 2", "2: Pick up Can");
		SmartDashboard.putString("DB/String 3", "3: Loading Station");
		SmartDashboard.putString("DB/String 4", "4: Turn");
		SmartDashboard.putString("DB/String 5", "Play dead");
	}
	
	public void runAuto() {
		SmartDashboard.putNumber("Stage", STAGE);
		SmartDashboard.putNumber("Substage", SUBSTAGE);
		SmartDashboard.putNumber("procedure", _procedure);
		switch(_procedure) {
		case 0://Do nothing, no plastic ramp
			switch(STAGE) {//This is important don't delete
			case 0:
				driveLinear(FORWARD,false);
				break;
			}
			break;
		case 1://Pick up tote and can
			switch(STAGE) {
			case 0:
				pickUpTote();
				break;
			case 1:
				driveLinear(ONETOTE,false);
				break;
			case 2:
				if(_timer.get() <= .15) {//was .1
					_intake.setArms(true);
				} else {
					STAGE++;
				}
				break;
			case 3:
				elevatorMove(CANPICKEDUP);
				break;
			case 4:
				turn(80);
				break;
			case 5:
				driveLinear(TOTETOAUTO,false);
				_timer.reset();
				break;
			case 6:
				if (_timer.get() >= 0.25) {
					STAGE++;
				} 
				break;
			case 7://put timer pause here
				elevatorMove(CANPICKEDUP + 200);
				break;
			case 8:
				_elevator.setHook(true);
				elevatorMove(600);
				break;
			case 9:
				_intake.setArms(false);
				break;
			}
			break;
		case 2://can forward only
			switch(STAGE) {
			case 0:
				if(_timer.get() < .5) {
					_intake.setArms(true);
				} else {
					STAGE++;
				}
				break;
			case 1:
				elevatorMove(300);
				break;
			case 2:
				driveLinear(WALLTOAUTO, _overRamp);
				break;
			case 3:
				if(!_elevator.atBottom()) {
					_elevator.moveTo(_elevator.getCurrentCount() - 200);
				} else {
					_intake.setArms(false);
				}
				break;
			}
			break;
		case 3://can and backwards
			switch(STAGE) {
			case 0:
				if(_timer.get() < .5) {
					_intake.setArms(true);
				} else {
					STAGE++;
				}
				break;
			case 1:
				elevatorMove(300);
				break;
			case 2:
				driveLinear(WALLTOAUTO, _overRamp);
					break;
			case 3:
				if(!_elevator.atBottom()) {
					_elevator.moveTo(_elevator.getCurrentCount() - 200);
				} else {
					_intake.setArms(false);
					STAGE++;
				}
				break;
			case 4:
				driveLinear(-WALLTOAUTO+500, _overRamp);
				break;
			case 5:
				
				//turn(-120);
				break;
			}
			break;
			
		case 4://RIP can
			switch (STAGE) {
			case 0:
				if(_timer.get() > 0.5) {
					_timer.reset();
					STAGE++;
				} else {
					_intake.reap(true);
				}
				break;
			case 1:
				if(_timer.get() < HOWMANYSEC) {
					_drive.autoMech(ISPOSSTRAFE);
				}	else {
					STAGE++;
				}
				break;
			case 2:
				_intake.reap(false);
				STAGE++;
				break;
			}
			break;
			
			
		case 5:
			switch(STAGE) {
			case 0:
				turn(-80);
				break;
			case 1:
				driveLinear(800,false);
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
			
					//Griffin's request: 10+
			
		case 10:	//3 tote auto
			switch(STAGE) {
			case 0:
				pickUpTote();
				break;
			case 1:
				if (_elevator.atBottom()) {
					STAGE++;
				} else {
					_elevator.moveTo(_elevator.getCurrentCount() - 200);
				}
				break;
			case 2:															//timer here?
				driveLinear(YELLOWTOTEDRIVE,false);
				break;
			case 3:
				pickUpTote();
				break;
			case 4:	//fall through
				driveLinear(YELLOWTOTEDRIVE,false);
			case 5:
				if (_elevator.atBottom()) {
					STAGE++;
				} else {
					_elevator.moveTo(_elevator.getCurrentCount() - 200);
				}
				break;
			case 6:
				pickUpTote();
			case 7:
				turn(90);
				break;
			case 8:
				driveLinear(DRIVEINTOAUTO,false);		//not sure about this dist
												//fall through
			case 9:
				
				_elevator.setHook(true);
				elevatorMove(_elevator.posHookA + 100);
				break;
			case 10:
				if (_elevator.atBottom()) {
					STAGE++;
				} else {
					_elevator.moveTo(_elevator.getCurrentCount() - 200);
				}
				break;
			case 11:
				_intake.setArms(false);
				STAGE++;
				break;
			}
			break;
			
					//PARTY MODE: 100+
			
		case 100:	//Uma Uma Dance
			elevatorMove(3700);
			switch(STAGE) {
			case 0:
				turn(65);
				break;
			case 1:
				turn(-65);
				break;
			case 2:
				STAGE = 0;
				break;
			}
			if (Math.abs(_gyro.getAngle()) > 55) {
				_intake.setWrists(true);
			} else {
				_intake.setWrists(false);
			}
			break;
			
		case 101:	//Spin open
			switch(STAGE) {
			case 0:
				turn(360*5);
				break;
			case 1:
				turn(360*-5);
				break;
			case 2:
				STAGE = 0;
				break;
			}
			if (Math.abs(_gyro.getAngle()) > ((360*5)-15)) {
				_intake.setArms(false);
			} else {
				_intake.setArms(true);
			}
			break;
			
		case 102:	//Cotton EYEd JOE
			
			if (SUBSTAGE == 0) {
				SUBSTAGE = 1;
			}
			

			_elevator.moveTo(_elevator.getCurrentCount() - 200);
			_intake.setWrists(true);
			switch(STAGE) {
			case 0:
				driveLinear(200,false);
				break;
			case 1:
				driveLinear(-200,false);
				break;
			case 2:
				driveLinear(-200,false);
				break;
			case 3:
				driveLinear(200,false);
				break;
			case 4:
				turn(45*SUBSTAGE);
				_elevator.moveTo(_elevator.getCurrentCount() + 200);
				break;
			case 5:
				turn(-45*SUBSTAGE);
				_elevator.moveTo(_elevator.getCurrentCount() - 200);
				break;
			case 6:
				turn(-45*SUBSTAGE);
				_elevator.moveTo(_elevator.getCurrentCount() + 200);
				break;
			case 7:
				turn(45*SUBSTAGE);
				_elevator.moveTo(_elevator.getCurrentCount() - 200);
				break;
			case 8://strafe clap strafe clap
				_intake.setWrists(false);
				turn(350*SUBSTAGE);
				break;
			case 9:
				STAGE = 0;
				SUBSTAGE *= -1;
			}
			
		default:
			doNothing();
			break;
			
		}
		
	}
	
	private void doNothing() {
		String doItYourself = "This is actually a test that Tyler and Brian made, you should know what the actual code for this method is... UBERNOOBS";			/*normal Noobs as well*/
		doItYourself.length();
	}
	
	public void elevatorMove(int target) {
		if(Math.abs(_elevator.getCurrentCount() - target) >= _elevator._positionTolerance) {
			_elevator.moveTo(target);
		} else {
			_elevator.stop();
			STAGE++;
		}
	}
	
	public void driveLinear(int target, boolean overRamp) {
		if(target==0) {
			return;
		}
		if(!isDoneCalculating) {
			_drive.resetEncoders();
			
			if(Math.abs(target) <=  2 * ACCELERATIONCOUNT) {
				shortDrive = true;
			}
			
			goal = Math.abs(Math.abs(target) - 180);
			deccelerationCount = goal - ACCELERATIONCOUNT;
			isDoneCalculating = true;
			_drive.setBrakeMode(false);
			
			if(overRamp) {
				BASE = .4;
				goal += 0;
			}
			
		} else {
			
			double currentCount = Math.abs((_drive.getLeftEncoders() + _drive.getRightEncoders()) / 2);
			double speed = 0.0;
			
			if(!shortDrive) {	
				if (currentCount <= ACCELERATIONCOUNT && currentCount >= 0) {	//triangle one
					speed = BASE + RAMPSPEED * currentCount / ACCELERATIONCOUNT;
				} else if (currentCount > deccelerationCount && currentCount <= goal) {	//triangle two
					_drive.setBrakeMode(true);
					speed = BASE + RAMPSPEED * (goal - currentCount) / ACCELERATIONCOUNT;
				} else if (currentCount > ACCELERATIONCOUNT && currentCount <= deccelerationCount){	//rectangle
					speed = (BASE + RAMPSPEED);
				} else {	//outside
					
					speed = 0.0;
					STAGE++;
					BASE = .5;
					isDoneCalculating = false;
					_timer.reset();
				}
				
			} else {
				
				if(currentCount <= goal) {
					speed = BASE;
				} 
				
				if(currentCount > goal) {
					speed = 0.0;
					_drive.setBrakeMode(true);
					STAGE++;
					BASE = .5;
					isDoneCalculating = false;
					_timer.reset();
				}
			}
			
			if (target < 0) {
				speed *= -1;
			}
			
			if (_overRamp && currentCount > 2500) {
				speed = 0.0;
				STAGE++;
				isDoneCalculating = false;
				_timer.reset();
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
			double relativeAngle = absTurnGoal - Math.abs(_gyro.getAngle() * 0.25);
			double absAngle = Math.abs(_gyro.getAngle() * 0.25);
			SmartDashboard.putNumber("GYRO ANGLE", _gyro.getAngle() * 0.25);
		
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
