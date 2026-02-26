// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.DefaultDrive;
import frc.robot.commands.Intake;
import frc.robot.commands.Shoot;
import frc.robot.commands.DriveLeft;
import frc.robot.commands.DriveRight;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeAndShooter;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.TankDrive;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final DriveSubsystem robotDrive = new DriveSubsystem();
  private final IntakeAndShooter IandS = new IntakeAndShooter();
  private final Feeder f = new Feeder();
   private final TankDrive t = new TankDrive();
  private final Intake intakeCom = new Intake(IandS,f);
  private final Shoot shoot = new Shoot(IandS,f);
  private final DriveLeft left = new DriveLeft(t);
  private final DriveRight right = new DriveRight(t);
  private final CommandXboxController driverController =
      new CommandXboxController(OperatorConstants.driverControllerPort);
        private final CommandXboxController operatorController =
      new CommandXboxController(OperatorConstants.driverControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();

    //CameraServer.startAutomaticCapture(); // adds to dashboard
  }
  public Command driveLeftCommand(Double s){
    left.setSpeed(s);
    return left;
  }
  public Command driveRightCommand(Double s){
    right.setSpeed(s);
    return right;
  }
  public Command intakeCommand(){
    return intakeCom;
  }
  public Command shootCommand(){
    return shoot;
  }

  /**
   * Use this method to define your trigger->command mappings.
   */
  private void configureBindings() {

    // Configure default commands
    // Set the default drive command to split-stick arcade drive
    robotDrive.setDefaultCommand(
        // A split-stick arcade command, with forward/backward controlled by the left
        // hand, and turning controlled by the right.
        new DefaultDrive(
            robotDrive,
            () -> -driverController.getLeftY(),
            () -> -driverController.getRightX()));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    try {
      // Fixed: removed "new" keyword and changed path name (no spaces)
      PathPlannerPath path = PathPlannerPath.fromPathFile("Safe otherside pickup");
      
      return AutoBuilder.followPath(path);
    } catch (Exception e) {
      System.err.println("Failed to load PathPlanner path: " + e.getMessage());
      e.printStackTrace();
      return Commands.none();
    }
  }
}