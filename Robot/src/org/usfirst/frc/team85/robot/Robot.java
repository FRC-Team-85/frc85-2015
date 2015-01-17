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
    private Joystick _rightStick;
    private Joystick _leftStick;
        
    private RobotDrive _drive;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        _driveController = new Joystick(Addresses.DRIVE_CONTROLLER);
        _drive = new RobotDrive(new CANTalon(Addresses.LEFT_FRONT_MOTOR), new CANTalon(Addresses.LEFT_REAR_MOTOR), 
                new CANTalon(Addresses.RIGHT_FRONT_MOTOR), new CANTalon(Addresses.RIGHT_REAR_MOTOR));
        _rightStick = new Joystick();
        _leftStick = new Joystick();
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
        _drive.tankDrive(_leftStick, _rightStick);
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
