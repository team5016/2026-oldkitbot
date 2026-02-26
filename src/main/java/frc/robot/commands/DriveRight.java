//Tm Bennett/Hunter

package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeAndShooter;
import frc.robot.subsystems.TankDrive;
import frc.robot.subsystems.Feeder;
/** An example command that uses an example subsystem. */
public class DriveRight extends Command {
  @SuppressWarnings("PMD.UnusedPrivateField")
    private final TankDrive t;
    private double speed = 0;
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public DriveRight(TankDrive subsystem) {
    t = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    t.setRightSpeed(speed);

  }

  public void setSpeed(double s){
    speed = s;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
 
    t.setRightSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
