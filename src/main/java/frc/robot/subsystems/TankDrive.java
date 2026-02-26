// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class TankDrive extends SubsystemBase {
  private final WPI_TalonSRX frontRight = new WPI_TalonSRX(4);
  private final WPI_TalonSRX rearRight = new WPI_TalonSRX(5);
  private final WPI_TalonSRX frontLeft = new WPI_TalonSRX(6);
  private final WPI_TalonSRX rearLeft = new WPI_TalonSRX(7);

  public TankDrive(){
    rearLeft.follow(frontLeft);
    rearRight.follow(frontRight);
  }

  
  public void setLeftSpeed(double speed) {
    frontLeft.set(speed);
  }
  public void setRightSpeed(double speed) {
    frontRight.set(speed);
  }
}