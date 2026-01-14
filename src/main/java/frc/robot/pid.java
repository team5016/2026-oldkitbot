package frc.robot;

public class pid {
    private double kP = 0.1;
    private double kI = 0.1;
    private double kD = 0.1;
    private double totalError = 0.0;
    private double integralLimit = 1.0;
    private double lastError = 0.0;
    private double derivative;
    private double i;

    public double PID(double error, double deltaTime) {
        if (deltaTime <= 0) {
            return 0.0;
        }
        i = totalError + error * deltaTime;
        totalError = Math.max(-integralLimit, Math.min(integralLimit, i));
        if (totalError > integralLimit) {
            totalError = integralLimit;
        }
        if (totalError < -integralLimit) {
            totalError = -integralLimit;
        }
        derivative = ((error - lastError) / deltaTime);
        lastError = error;
        return ((kP * error) + (kI * totalError) + kD * (derivative));
    }
}
