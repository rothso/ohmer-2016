package org.teamresistance.teleop;

import org.teamresistance.IO;
import org.teamresistance.teleop.driveModes.DirectDrive;
import org.teamresistance.teleop.driveModes.ScaledDrive;
import org.teamresistance.teleop.driveModes.Target;
import org.teamresistance.util.state.State;
import org.teamresistance.util.state.StateMachine;
import org.teamresistance.util.state.StateTransition;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Teleop extends State {
	
	private StateMachine driveModes;
	
	private NetworkTable gripTable;
	
	protected Teleop(StateMachine stateMachine, String name) {
		super(stateMachine, name);
	}

	@Override
	public void init() {
		driveModes = new StateMachine();
		driveModes.addState(ScaledDrive.class, "ScaledDrive");
		driveModes.addState(DirectDrive.class, "DirectDrive");
		driveModes.addState(Target.class, "Target");
		
		gripTable = NetworkTable.getTable("GRIP");
	}

	@Override
	public void onEntry(StateTransition e) {
		driveModes.setState("ScaledDrive"); 
	}

	@Override
	public void update() {
		driveModes.update();
		SmartDashboard.putNumber("Roll", IO.imu.getRoll());
		SmartDashboard.putNumber("Pitch", IO.imu.getPitch());
		SmartDashboard.putNumber("Yaw", IO.imu.getYaw());
		
		SmartDashboard.putNumber("Frame Rate", gripTable.getNumber("frameRate", -1));
	}

	@Override
	public void onExit(StateTransition e) {
		
	}
	
}