/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.PowerDistributionPanel; 

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	private PowerDistributionPanel PDP = new PowerDistributionPanel();
	
    private Joystick _driveController, _operatorController;
    
    private CameraServer _camera;

    private Drive _drive;
    private Intake _intake;
    private Elevator _elevator;
    
    private Autonomous _auto;
    private boolean autoCheck = false;
    
    private Gyro _gyro;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        _driveController = new Joystick(Addresses.DRIVE_CONTROLLER);
        _operatorController = new Joystick(Addresses.OPERATOR_CONTROLLER);
        
        _drive = new Drive(_driveController);
        _intake = new Intake(_operatorController);
        _elevator = new Elevator(_operatorController, _intake);
        _gyro = new Gyro(Addresses.GYRO_CHANNEL);
        _gyro.initGyro();
        
        try {
        _camera = CameraServer.getInstance();
        _camera.startAutomaticCapture("cam0");
        } catch(Exception ex) {
        	System.out.println("Camera Error");
        }
        
        _drive.resetEncoders();
        
    }
    
    public void autonomousInit() {
    	_auto = new Autonomous(0, _drive, _intake, _elevator, _gyro);
        _gyro.reset();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	if (!autoCheck) {
    		if (_elevator.atBottom()) {
    			autoCheck = true;
    		} else {
    			_elevator.moveTo(_elevator.getCurrentCount() - 100);
    		}
    	} else {
    		_auto.runAuto();
    		_drive.displayEncoderCount();
    		SmartDashboard.putNumber("Elevator Encoder", _elevator.getCurrentCount());

    		SmartDashboard.putNumber("TIMER", _auto._timer.get());
    	}	
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	_drive.setBrakeMode(false);
    	_drive.drive();
    	_intake.run();
    	_elevator.runLift();
    	//pneumaticsControlls();
    	getAMPs();
    	_drive.displayEncoderCount();

		SmartDashboard.putNumber("GYRO ANGLE", _gyro.getAngle());
    }
    
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
    //0-3
    //12-15
    
    private void getAMPs() {
    	SmartDashboard.putNumber("Channel 00 AMPs",PDP.getCurrent(0));
    	SmartDashboard.putNumber("Channel 01 AMPs",PDP.getCurrent(1));
    	SmartDashboard.putNumber("Channel 02 AMPs",PDP.getCurrent(2));
    	SmartDashboard.putNumber("Channel 03 AMPs",PDP.getCurrent(3));

    	SmartDashboard.putNumber("Channel 12 AMPs",PDP.getCurrent(12));
    	SmartDashboard.putNumber("Channel 13 AMPs",PDP.getCurrent(13));
    	SmartDashboard.putNumber("Channel 14 AMPs",PDP.getCurrent(14));
    	SmartDashboard.putNumber("Channel 15 AMPs",PDP.getCurrent(15));
    }
    
}
