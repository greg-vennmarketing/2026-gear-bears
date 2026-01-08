package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterSubsystem extends SubsystemBase{
    SparkMax motor = new SparkMax(43, SparkLowLevel.MotorType.kBrushed);

    public ShooterSubsystem() {

    }

    public void shoot() {
        motor.set(3);
    }
    public void feed() {
        motor.set(0.5);
    }
    public void stop() {
        motor.set(0);
    }
}
