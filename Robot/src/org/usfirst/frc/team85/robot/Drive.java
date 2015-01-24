package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

public class Drive {

	private CANTalon _frontLeftMotor;
	private CANTalon _frontRightMotor;
	private CANTalon _backLeftMotor;
	private CANTalon _backRightMotor;
	
	private Encoder _frontLeftEncoder;
	private Encoder _frontRightEncoder;
	private Encoder _backLeftEncoder;
	private Encoder _backRightEncoder;
	
	private double _frontLeftOutput;
	private double _frontRightOutput;
	private double _backLeftOutout;
	private double _backRighOutput;
	
	public Drive(Joystick drivecontroller) {
		
		_frontLeftMotor = new CANTalon(Addresses.LEFT_FRONT_MOTOR);
		_frontRightMotor = new CANTalon(Addresses.RIGHT_FRONT_MOTOR);
		_backLeftMotor = new CANTalon(Addresses.LEFT_REAR_MOTOR);
		_backRightMotor = new CANTalon(Addresses.RIGHT_REAR_MOTOR);
	
		
		_frontLeftEncoder = new Encoder(Addresses.FRONT_LEFT_ENCODER_CHANNEL_A, 
				Addresses.FRONT_LEFT_ENCODER_CHANNEL_B);
		/*_frontRightEncoder = new Encoder(Addresses.FRONT_RIGHT_ENCODER_CHANNEL_A,
				Addresses.FRONT_RIGHT_ENCODER_CHANNEL_B);
		_backLeftEncoder = new Encoder(Addresses.BACK_LEFT_ENCODER_CHANNEL_A,
				Addresses.BACK_LEFT_ENCODER_CHANNEL_B);
		_backRightEncoder = new Encoder(Addresses.BACK_RIGHT_ENCODER_CHANNEL_A,
				Addresses.BACK_RIGHT_ENCODER_CHANNEL_B);
		*/
	System.out.print(_frontLeftEncoder.get());
	}
	
}
