//Tm Bennett/Hunter

package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeAndShooter;
import frc.robot.subsystems.Feeder;
/** An example command that uses an example subsystem. */
public class Intake extends Command {
  @SuppressWarnings("PMD.UnusedPrivateField")
    private final IntakeAndShooter Intake;
    private final Feeder f = new Feeder();
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public Intake(IntakeAndShooter subsystem, Feeder s) {
    Intake = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem,s);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Intake.spin(1);
    f.spin(-1);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Intake.stop();
    f.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
