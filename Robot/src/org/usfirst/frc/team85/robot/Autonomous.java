package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

public class Autonomous {

	private int _procedure;
	private Drive _drive;
	
	private double _countsToDrive;
	
	public Autonomous(int procedureID, Drive drive) {
		_procedure = procedureID;
		_drive = drive;
		
		_drive.resetEncoders();
	}
	
	public void runAuto() {
		switch(_procedure) {
		case 0:
			//simpleDrive();
		}
	}
	private void simpleDrive(int distance) {
		
	}
	
	/*public int countsToDrive() {
		
	}*/
}
