package frc.robot.commands;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.Command;

public class ServoMove extends Command {
    private final Servo serv;
    private final double angle;
    public ServoMove(Servo S, double A){
        serv = S;
        angle = A;
    }

    @Override
    public void initialize(){
        serv.setAngle(angle);
    }
}
