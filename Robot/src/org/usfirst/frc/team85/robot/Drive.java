package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive {
	
	private RobotDrive _drive;
	
	private Joystick _controller;

	private CANTalon _frontLeftMotor;
	private CANTalon _frontRightMotor;
	private CANTalon _backLeftMotor;
	private CANTalon _backRightMotor;
	
	private Encoder _frontLeftEncoder;
	private Encoder _frontRightEncoder;
	private Encoder _backLeftEncoder;
	private Encoder _backRightEncoder;
	
	public Drive(Joystick drivecontroller) {
		
		_controller = drivecontroller;
		
		_frontLeftMotor = new CANTalon(Addresses.LEFT_FRONT_MOTOR);
		_frontRightMotor = new CANTalon(Addresses.RIGHT_FRONT_MOTOR);
		_backLeftMotor = new CANTalon(Addresses.LEFT_REAR_MOTOR);
		_backRightMotor = new CANTalon(Addresses.RIGHT_REAR_MOTOR);
	
		
		_frontLeftEncoder = new Encoder(Addresses.FRONT_LEFT_ENCODER_CHANNEL_A, 
				Addresses.FRONT_LEFT_ENCODER_CHANNEL_B);
		_frontRightEncoder = new Encoder(Addresses.FRONT_RIGHT_ENCODER_CHANNEL_A,
				Addresses.FRONT_RIGHT_ENCODER_CHANNEL_B);
		_backLeftEncoder = new Encoder(Addresses.BACK_LEFT_ENCODER_CHANNEL_A,
				Addresses.BACK_LEFT_ENCODER_CHANNEL_B);
		_backRightEncoder = new Encoder(Addresses.BACK_RIGHT_ENCODER_CHANNEL_A,
				Addresses.BACK_RIGHT_ENCODER_CHANNEL_B);
		
		_drive = new RobotDrive(_frontLeftMotor, _backLeftMotor, _frontRightMotor, _backRightMotor);
	}
	
	public void drive() {
		if(_controller.getRawButton(1)) {
			resetEncoders();
		}
		if(_controller.getRawButton(2)) {
			SmartDashboard.putString("DB/String 0", getAveEncoders() + "");
		}
		if(Math.abs(_controller.getX()) <= .05 && Math.abs(_controller.getY()) <= .05 && Math.abs(_controller.getTwist()) <= .05) {
			setMotors(0.0, 0.0, 0.0, 0.0);
			return;
		}
	
		_drive.mecanumDrive_Cartesian(scaleDrive(_controller.getX()), scaleDrive(_controller.getY()), scaleDrive(_controller.getTwist()), 0);	
	
	}
	
	private double scaleDrive(double speed) {	//LR100%, L_50%,_R50% __80%
		double scale = speed;
		boolean left = _controller.getRawButton(Addresses.MOD_A);
		boolean right = _controller.getRawButton(Addresses.MOD_B);
		if (left && right) {
			return scale;
		} else if (left || right) {
			return scale * 0.50;
		} else {
			return scale * 0.8;
		}
	/*
		//L_35%,__70%,	LR50%,_R100%
		double scale = speed;
		if (_controller.getRawButton(Addresses.MOD_A)) {
			scale *= 0.5;
		}
		if (_controller.getRawButton(Addresses.MOD_B)) {
			return scale;
		}
		return scale * .7;
	*/
	}
	
	public void setMotors(double frontLeftSpeed, double frontRightSpeed, double backLeftSpeed, double backRightSpeed) {
		_frontLeftMotor.set(frontLeftSpeed);
		_frontRightMotor.set(frontRightSpeed);
		_backLeftMotor.set(backLeftSpeed);
		_backRightMotor.set(backRightSpeed);
	}
	
	public void resetEncoders() {
		_frontLeftEncoder.reset();
		_frontRightEncoder.reset();
		_backLeftEncoder.reset();
		_backRightEncoder.reset();
	}
	
	public int getLeftEncoders() {
		return (_frontLeftEncoder.get() + _backLeftEncoder.get()) / 2;
	}
	
	public int getRightEncoders() {
		return (_frontRightEncoder.get() + _backLeftEncoder.get()) / 2;
	}
	
	public void setBrakeMode(boolean set) {
		_frontLeftMotor.enableBrakeMode(set);
		_backLeftMotor.enableBrakeMode(set);
		_frontRightMotor.enableBrakeMode(set);
		_backRightMotor.enableBrakeMode(set);
	}
	
	public void displayEncoderCount(){

    	SmartDashboard.putNumber("Ave. Encoder", (getLeftEncoders() + getRightEncoders()) / 2);
    	SmartDashboard.putNumber("FrontLeftEncoder", _frontLeftEncoder.get());
    	SmartDashboard.putNumber("BackLeftEncoder", _backLeftEncoder.get());
    	SmartDashboard.putNumber("FrontRightEncoder", _frontRightEncoder.get());
    	SmartDashboard.putNumber("BackRightEncoder", _backRightEncoder.get());
	}
	public int getAveEncoders() {
		return (getLeftEncoders() + getRightEncoders()) / 2;
	}
	
	public void autoMech(double x) {
		_drive.mecanumDrive_Cartesian (x, 0, 0, 0);
	}
	
}
