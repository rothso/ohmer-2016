
package org.teamresistance.auto;

import org.teamresistance.Constants;
import org.teamresistance.IO;
import org.teamresistance.auto.defense.DefenseCheval;
import org.teamresistance.auto.defense.DefenseDrawbridge;
import org.teamresistance.auto.defense.DefenseMoat;
import org.teamresistance.auto.defense.DefensePortcullis;
import org.teamresistance.auto.defense.DefenseRamparts;
import org.teamresistance.auto.defense.DefenseRockWall;
import org.teamresistance.auto.defense.DefenseRoughTerrain;
import org.teamresistance.util.state.State;
import org.teamresistance.util.state.StateTransition;
import org.teamresistance.util.Time;
import org.teamresistance.auto.AutoConstants;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveToTower extends State {
	
	//	Cheval, Drawbridge, Moat, Portcullis, Ramparts, RockWall, Rough terrain
	final private static boolean[] ORIENTATION = 
		{false, true, false, true, false, true, false};
	
	
	private double distance;
	private int startAngle = 90;
	private int endAngle;
	private double elapsed = 0;
	private double speed;
	private boolean turned = false;
	private boolean orient;
	
	public DriveToTower(int position, int goal, int defense) {
		distance = AutoConstants.DISTANCES[position-2][goal];
		SmartDashboard.putNumber("Travel time", distance);
		startAngle = AutoConstants.START_ANGLES[position-2][goal];
		//SmartDashboard.putNumber("angle", startAngle);
		endAngle = AutoConstants.END_ANGLES[position-2][goal];
		speed = AutoConstants.COURTYARD_SPEED;
		SmartDashboard.putNumber("Speed: ", speed);
		orient = ORIENTATION[defense];
		
		/*if(orient) {
			
			//add 180 and wrap to -180
			startAngle += 180;
			wrap(startAngle);
			endAngle += 180;
			wrap(endAngle);
			
		}*/
		
		//IO.imu.turnTo(startAngle, AutoConstants.ANGLE_ERROR_THRESHOLD);
		SmartDashboard.putNumber("Angle", startAngle);
	}
	
	public void wrap(double angle) {
		speed*= -1;
		if(angle>180) {
			double difference = angle-180;
			angle = -180+difference;
		}
	}

	@Override
	public void onEntry(StateTransition e) {

	}

	private boolean firstTurn = false;
	private boolean driven = false;
	private boolean lastTurn = false;
	private double time = 0;
	
	@Override
	public void update() {
		elapsed += Time.getDelta();
		System.out.println("StartAngle = "+startAngle);
		SmartDashboard.putNumber("startAngle = ",startAngle);
		if(firstTurn==false) {
			if(!IO.imu.isStraight(AutoConstants.ANGLE_ERROR_THRESHOLD, startAngle)) {
				IO.imu.turnTo(startAngle, AutoConstants.ANGLE_ERROR_THRESHOLD);
			} else {
				firstTurn = true;
				time = elapsed;
			}
		} else {
			if(driven==false) {
				if((elapsed-time)<distance) {
					IO.robotDrive.arcadeDrive(speed,0.0);
					//if(!IO.imu.isStraight(AutoConstants.ANGLE_ERROR_THRESHOLD, startAngle)) {
					//	IO.imu.turnTo(startAngle, AutoConstants.ANGLE_ERROR_THRESHOLD);
					//}
					
				} 
					//else {
				//	driven = true;
				}
			//}else {
			//	if(!lastTurn){
				//	if(!IO.imu.isStraight(AutoConstants.ANGLE_ERROR_THRESHOLD, startAngle)) {
					//	IO.imu.turnTo(startAngle, AutoConstants.ANGLE_ERROR_THRESHOLD);
					//} else {
						//lastTurn = true; //change state here. 
					//}
				
			}
			
		//}
		
			
	}
		//else {
			//IO.imu.turnTo(endAngle, AutoConstants.ANGLE_ERROR_THRESHOLD);
			//need to figure out some way of making it go to a targeting state rather than looping
			//while(IO.snorflerSolenoid.get()) {
			//	IO.snorflerSolenoid.set(false);
			//}
			//while(IO.ballSensor.get()) {
			//	IO.snorflerMotor.set(Constants.SNORFLE_DUMP_SPEED);
			//}
			
	//	}
//	}

	@Override
	public void onExit(StateTransition e) {
		
	}

}