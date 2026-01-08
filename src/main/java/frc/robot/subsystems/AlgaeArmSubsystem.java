package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class AlgaeArmSubsystem extends SubsystemBase{
    SparkMax motor = new SparkMax(44, SparkLowLevel.MotorType.kBrushed);
    RelativeEncoder encoder = motor.getEncoder();

    public AlgaeArmSubsystem() {
        encoder.setPosition(1.0);
    }

    public void forword() {
        if (encoder.getPosition() < 0.1) {
            motor.set(0.5);
        }
        if (encoder.getPosition() == 0.1) {
            motor.set(0.0);
            return;
        }
        motor.set(-0.5);
    }

    public void backword() {
        if (encoder.getPosition() > 0.9) {
            motor.set(-0.5);
            return;
        }
        if (encoder.getPosition() == 0.9) {
            motor.set(0.0);
            return;
        }
        motor.set(0.5);
    }

    public void stop() {
        motor.set(0);
    }
}
