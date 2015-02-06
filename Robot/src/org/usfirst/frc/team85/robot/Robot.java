/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    private Joystick _driveController, _operatorController;
    
    private CameraServer _camera;

    private Drive _drive;
    private Intake _intake;
    private Elevator _elevator;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        _driveController = new Joystick(Addresses.DRIVE_CONTROLLER);
        _operatorController = new Joystick(Addresses.OPERATOR_CONTROLLER);
        
        _drive = new Drive(_driveController);
        _intake = new Intake(_operatorController);
        _elevator = new Elevator(_operatorController);
        
        try {
        _camera = CameraServer.getInstance();
        _camera.startAutomaticCapture("cam0");
        } catch(Exception ex) {
        	System.out.println("Camera Error");
        }
        
        _drive.resetEncoders();
        
    }
    
    public void autonomousInit() {
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
             
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	_drive.drive();
    	_intake.run();
    	_elevator.runLift();
    	//pneumaticsControlls();
    }
    
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
