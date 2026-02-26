// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import edu.wpi.first.net.PortForwarder;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.XboxController;

/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private final RobotContainer m_robotContainer;
  private final XboxController opController = new XboxController(0);
  private final XboxController driController = new XboxController(1);

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  public Robot() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
  }

  @Override
  public void robotInit() {
    // Limelight port forwarding when laptop is USB connected to robot
    for (int port = 5800; port <= 5807; port++) {
            PortForwarder.add(port, "limelight.local", port);
    }
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      CommandScheduler.getInstance().schedule(m_autonomousCommand);
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    if(opController.getLeftTriggerAxis() > 0.25){//change the value to change sensitivity (lower is more sensitive)
      CommandScheduler.getInstance().schedule(m_robotContainer.intakeCommand());
    }else if(opController.getRightTriggerAxis() > 0.25){
      CommandScheduler.getInstance().schedule(m_robotContainer.shootCommand());
    }else{
      m_robotContainer.intakeCommand().cancel();
    }
    
    if(driController.getLeftY()>0.25){
      CommandScheduler.getInstance().schedule(m_robotContainer.driveLeftCommand(driController.getLeftY()));
    }else if (driController.getLeftY()<-0.25){
      CommandScheduler.getInstance().schedule(m_robotContainer.driveLeftCommand(driController.getLeftY()));
    }else{
      m_robotContainer.driveLeftCommand(0.0).cancel();
    }

    if(driController.getRightY()>0.25){
      CommandScheduler.getInstance().schedule(m_robotContainer.driveRightCommand(driController.getRightY()));
    }else if (driController.getRightY()<-0.25){
      CommandScheduler.getInstance().schedule(m_robotContainer.driveRightCommand(driController.getRightY()));
    }else{
      m_robotContainer.driveRightCommand(0.0).cancel();
    }

  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
