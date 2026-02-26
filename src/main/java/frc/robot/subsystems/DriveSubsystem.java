// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class DriveSubsystem extends SubsystemBase {
  private final WPI_TalonSRX frontRight = new WPI_TalonSRX(4);
  private final WPI_TalonSRX rearRight = new WPI_TalonSRX(5);
  private final WPI_TalonSRX frontLeft = new WPI_TalonSRX(6);
  private final WPI_TalonSRX rearLeft = new WPI_TalonSRX(7);

  // The robot's drive
  private final DifferentialDrive robotDrive;

  /** Creates a new DriveSubsystem. */
  public DriveSubsystem() {
    
    rearRight.follow(frontRight);
    rearLeft.follow(frontLeft);    
    robotDrive = new DifferentialDrive(frontLeft::set, frontRight::set);
    
    SendableRegistry.addChild(robotDrive, frontLeft);
    SendableRegistry.addChild(robotDrive, frontRight);

    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    frontRight.setInverted(true);
    rearRight.setInverted(true);
  }

  /**
   * Drives the robot using arcade controls.
   *
   * @param fwd the commanded forward movement
   * @param rot the commanded rotation
   */
  public void arcadeDrive(double fwd, double rot) {
    robotDrive.arcadeDrive(fwd, rot);
  }

  /**
   * Sets the max output of the drive. Useful for scaling the drive to drive more slowly.
   *
   * @param maxOutput the maximum output to which the drive will be constrained
   */
  public void setMaxOutput(double maxOutput) {
    robotDrive.setMaxOutput(maxOutput);
  }
 
}