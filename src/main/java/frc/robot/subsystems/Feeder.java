package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Feeder extends SubsystemBase {
    private final WPI_TalonSRX motor = new WPI_TalonSRX(3);

    

    public Command spin(double speed) {
        return this.runOnce(() -> motor.set(speed));
    }

    public Command stop() {
        return this.runOnce(() -> motor.set(0));
    }
}
