package org.teamresistance.teleop.driveModes;

import org.teamresistance.IO;
import org.teamresistance.JoystickIO;
import org.teamresistance.util.Util;
import org.teamresistance.util.state.State;
import org.teamresistance.util.state.StateTransition;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AngleMatch extends State {

	private float targetAngle = 0.0f;
	private float angleDeadband = 5.0f;
	private float angleGain = 4.0f;

	private String previousStateName = null;
	
	public AngleMatch() {
		//SmartDashboard.putNumber("TargetAngle", targetAngle);
		//SmartDashboard.putNumber("AngleDeadband", angleDeadband);
		//SmartDashboard.putNumber("AngleGain", angleGain);
		//SmartDashboard.putNumber("IntegralGain", angleIntegralGain);
	}
	
	@Override
	public void onEntry(StateTransition e) {
		previousStateName = e.getInitialState().getName();
		SmartDashboard.putNumber("$$$$$Angle Match", 100);
	}

	@Override
	public void update() {
		if(!JoystickIO.btnScore.isDown()) {
			gotoState(previousStateName);
		}
		
		//targetAngle = (float) Math.toRadians(SmartDashboard.getNumber("TargetAngle"));
//		angleDeadband = (float) Math.toRadians(SmartDashboard.getNumber("AngleDeadband"));
//		angleGain = (float) SmartDashboard.getNumber("AngleGain");
		
		float currentAngle = getCurrentAngle();
		float error = targetAngle - currentAngle;
		SmartDashboard.putNumber("Error", error);
		
		if(Math.abs(error/(2 * Math.PI)) > 0.8) {
			if(error > 0) {
				error -= (float)(2 * Math.PI);
			} else {
				error += (float)(2 * Math.PI);
			}
		}
		
		if(Math.abs(error) < Math.toRadians(angleDeadband)) {
			error = 0.0f;
			gotoState("Target");
		}
		
		SmartDashboard.putNumber("Result", Util.clip((double)(error*angleGain), -1.0, 1.0));
		IO.robotDrive.arcadeDrive(0.0, Util.clip((double)(error*angleGain), -1.0, 1.0));
	}

	private float getCurrentAngle() {
		return (float)Math.toRadians(IO.imu.getYaw());
	}

	public void setTargetAngle(float angle) {
		targetAngle = (float)Math.toRadians(angle);
	}
	
	public void setAngleDeadband(float angleDeadband) {
		this.angleDeadband = (float)Math.toRadians(angleDeadband);
	}

	public void setAngleGain(float angleGain) {
		this.angleGain = angleGain;
	}	
}