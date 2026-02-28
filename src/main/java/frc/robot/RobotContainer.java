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
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
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
  private final CommandPS4Controller driverController = new CommandPS4Controller(
      OperatorConstants.driverControllerPort);
  private final CommandXboxController operatorController = new CommandXboxController(
      OperatorConstants.operatorControllerPort);

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
    double driveScalingFactor = 0.75;
    /* Notes for programmers:
     *  this does not work:
     *     robotDrive.setDefaultCommand(robotDrive.runAsTank(-driverController.getLeftY(), -driverController.getRightY()));
     *  probably because 'runAsTank' is/was setting up a run command inside the subsystem using this.run
     * 
     *  this does work:
     *    robotDrive.setDefaultCommand(
            Commands.run(
              ()-> robotDrive.tankDrive(-driverController.getLeftY(), driverController.getRightY()), robotDrive)
            );
     *  probably because Commands.run only marks the subsystem as a dependency,
     *  it does not actually use a command tied to the subsystem itself
     */
    robotDrive.setMaxOutput(driveScalingFactor);
    robotDrive.setDefaultCommand(
      Commands.run(
        ()-> robotDrive.tankDrive(-driverController.getLeftY(), driverController.getRightY()), robotDrive)
        // () -> robotDrive.arcade(-driverController.getLeftY(), driverController.getRightX()), robotDrive)
    );

    // Debug: print controller axes periodically
    // Thread ctrlDebug = new Thread(() -> {
    //   try {
    //     while (!Thread.currentThread().isInterrupted()) {
    //       System.out.printf(
    //           "Driver LY=%.3f RY=%.3f LX=%.3f RX=%.3f",
    //           driverController.getLeftY(),
    //           driverController.getRightY(),
    //           driverController.getLeftX(),
    //           driverController.getRightX());
    //       Thread.sleep(200);
    //     }
    //   } catch (InterruptedException e) {
    //     System.out.print("Error caught");
    //     Thread.currentThread().interrupt();
    //   }
    // }, "ControllerDebug");
    // ctrlDebug.setDaemon(true);
    // ctrlDebug.start();

    // Operator
    double intakeAndShootSpeed = 1.0;
    double feederSpeed = 1.0; // positive is into hopper, negative is out of hopper
    double feederSpeedFactor = 0.2;

    //  Intake/feed to get Ball into hopper
    operatorController.a()
      .onTrue(intakeAndFlywheel.spin(intakeAndShootSpeed * 0.75).alongWith(feeder.spin(feederSpeed)))
      .onFalse(intakeAndFlywheel.stop().alongWith(feeder.stop()));

    //  Feed and shoot
    operatorController.b()
      .onTrue(feeder.spin(feederSpeed * -0.35).andThen(intakeAndFlywheel.spin(intakeAndShootSpeed)))
      .onFalse(intakeAndFlywheel.stop().alongWith(feeder.stop()));

    // Outtake only
    operatorController.rightBumper()
      .onTrue(intakeAndFlywheel.spin(intakeAndShootSpeed * -1.0))
      .onFalse(intakeAndFlywheel.stop());

    //  Intake only
    operatorController.leftBumper()
      .onTrue(intakeAndFlywheel.spin(intakeAndShootSpeed))
      .onFalse(intakeAndFlywheel.stop());

    //  Slow forward/reverse of feeder in case of jam (D-pad)
    operatorController.povUp() // Forward
      .onTrue(feeder.spin(feederSpeed * feederSpeedFactor))
      .onFalse(feeder.stop());
    operatorController.povDown() // Reverse
      .onTrue(feeder.spin(-1 * feederSpeed * feederSpeedFactor))
      .onFalse(feeder.stop());
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
