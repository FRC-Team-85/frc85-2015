/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.usfirst.frc.team85.robot;


/**
 *
 * @author mattmichielsen
 */
public final class Addresses {
	
		//Drive
	
    public static final int LEFT_FRONT_MOTOR = 1;
    public static final int LEFT_REAR_MOTOR = 2;
    public static final int RIGHT_FRONT_MOTOR = 3;
    public static final int RIGHT_REAR_MOTOR = 4;
    
    public static final int FRONT_LEFT_ENCODER_CHANNEL_A = 0;
    public static final int FRONT_LEFT_ENCODER_CHANNEL_B = 1;
    public static final int FRONT_RIGHT_ENCODER_CHANNEL_A = 2;
    public static final int FRONT_RIGHT_ENCODER_CHANNEL_B = 3;
    public static final int BACK_LEFT_ENCODER_CHANNEL_A = 4;
    public static final int BACK_LEFT_ENCODER_CHANNEL_B = 5;
    public static final int BACK_RIGHT_ENCODER_CHANNEL_A = 6;
    public static final int BACK_RIGHT_ENCODER_CHANNEL_B = 7;
    
    public static final int SCYTHE_SOLENOID_CHANNEL = 4;
    
    	//Elevator
    
    public static final int ELEVATOR_ENCODER_CHANNEL_A = 8;
    public static final int ELEVATOR_ENCODER_CHANNEL_B = 9;

    public static final int TOPSWITCH_CHANNEL = 2;
    public static final int BOTTOMSWITCH_CHANNEL = 1;

    public static final int HOOK_A_SOLENOID_CHANNEL = 2;
    public static final int HOOK_B_SOLENOID_CHANNEL = 3;
    
    public static final int LEFT_BELT_MOTOR = 8;
    public static final int RIGHT_BELT_MOTOR = 9;
    
    	//Intake
    public static final int WRIST_SOLENOID_CHANNEL = 0;
    public static final int ARM_SOLENOID_CHANNEL = 1;
    
    public static final int LEFT_INTAKE_MOTOR = 6;
    public static final int RIGHT_INTAKE_MOTOR = 7;
    
    	//General
    
    public static final int DRIVE_CONTROLLER = 0;
    public static final int OPERATOR_CONTROLLER = 1;
    
    public static final int PNEUMATIC_CONTROLLER_CID = 5;
    
    public static final int GYRO_CHANNEL = 0;
    
    	//DriveController
    
    public static final int CHARGE = 1;
    public static final int REAP = 4;
    
    public static final int SLOW_A = 5;
    public static final int FAST_B = 6;
    
    	//OperatorControllerIntake
    
    public static final int ARM_IN = 5;
	public static final int ARM_OUT = 7;			
	public static final int WRIST_IN = 8;
	public static final int WRIST_OUT = 6;
	
		//OperatorControllerElevator

	public static int BUTTON_BOTTOM = 1;
	public static int BUTTON_LOAD = 2;
	public static final int BUTTON_HOOK_A = 3;
	public static final int BUTTON_HOOK_B = 4;
	
	public static int HOOK_AXIS = 5;
	
	public static final int HOOK_BUTTON = 12;

}
