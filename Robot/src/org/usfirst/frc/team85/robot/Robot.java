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
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        _driveController = new Joystick(Addresses.DRIVE_CONTROLLER);
        _operatorController = new Joystick(Addresses.OPERATOR_CONTROLLER);
        
        _drive = new Drive(_driveController);
        _intake = new Intake(_operatorController);
        
        _camera = CameraServer.getInstance();
        _camera.startAutomaticCapture("cam0");
        
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
    	solenoid1.get();
    	//_intake.run();
    	//pneumaticsControlls();
    }
    
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
    Solenoid solenoid1 = new Solenoid(5, 0);
    Solenoid solenoid2 = new Solenoid(5, 1);
    Solenoid solenoid3 = new Solenoid(5, 2);
    Solenoid solenoid4 = new Solenoid(5, 3);
    Solenoid solenoid5 = new Solenoid(5, 4);
    //Solenoid solenoid6 = new Solenoid(5, 5);
    
    boolean valve1 = false, valve2 = false, valve3 = false, valve4 = false, valve5 = false, valve6 = false;
    
    private void pneumaticsControlls() {
    	
    	if(_operatorController.getRawButton(1) && !valve1) {
    		solenoid1.set(!solenoid1.get());
    		valve1 = true;
    	} else {valve1 = false;}
    	
    	if(_operatorController.getRawButton(2) && !valve2) {
    		solenoid2.set(!solenoid2.get());
    		valve2 = true;
    	} else {valve2 = false;}
    	
    	if(_operatorController.getRawButton(3) && !valve3) {
    		solenoid3.set(!solenoid3.get());
    		valve3 = true;
    	} else {valve3 = false;}
    	
    	if(_operatorController.getRawButton(4) && !valve4) {
    		solenoid4.set(!solenoid4.get());
    		valve4 = true;
    	} else {valve4 = false;}
    	
    	if(_operatorController.getRawButton(5) && !valve5) {
    		solenoid5.set(!solenoid5.get());
    		valve5 = true;
    	} else {valve5 = false;}
    	
    	/*if(_operatorController.getRawButton(6) && !valve6) {
    		solenoid6.set(!solenoid6.get());
    		valve6 = true;
    	} else {valve6 = false;}*/
    }
}
