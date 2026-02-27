// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Intake;
import frc.robot.commands.Shoot;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeAndFlywheel;
import frc.robot.subsystems.Feeder;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final DriveSubsystem robotDrive = new DriveSubsystem();
  private final IntakeAndFlywheel intakeAndFlywheel = new IntakeAndFlywheel();
  private final Feeder feeder = new Feeder();
  private final Intake intakeCommmand = new Intake(intakeAndFlywheel, feeder);
  private final Shoot shootCommand = new Shoot(intakeAndFlywheel, feeder);
  private final CommandXboxController driverController = new CommandXboxController(
      OperatorConstants.driverControllerPort);
  private final CommandXboxController operatorController = new CommandXboxController(
      OperatorConstants.driverControllerPort);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();

    // CameraServer.startAutomaticCapture(); // adds to dashboard
  }

  /**
   * Use this method to define your trigger->command mappings.
   */
  private void configureBindings() {
    // Driver
    robotDrive.setDefaultCommand(
        Commands.run(() -> robotDrive.tankDrive(-driverController.getLeftY(), -driverController.getRightY()),
            robotDrive));

    // Operator
    double intakeShootSpeed = 0.5;
    double feederSpeed = 1.0;
    operatorController.a().onTrue(intakeAndFlywheel.spin(intakeShootSpeed));
    operatorController.a().onFalse(intakeAndFlywheel.stop());

    operatorController.b().onTrue(intakeAndFlywheel.spin(intakeShootSpeed).andThen(feeder.spin(feederSpeed)));
    operatorController.b().onFalse(intakeAndFlywheel.stop().alongWith(feeder.stop()));
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