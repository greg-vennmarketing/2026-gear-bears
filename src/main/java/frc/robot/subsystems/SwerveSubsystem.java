package frc.robot.subsystems;

import java.io.File;
import java.io.IOException;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import swervelib.SwerveDrive;
import swervelib.math.SwerveMath;
import swervelib.parser.SwerveParser;
import swervelib.telemetry.SwerveDriveTelemetry;
import swervelib.telemetry.SwerveDriveTelemetry.TelemetryVerbosity;

public class SwerveSubsystem extends SubsystemBase{
    public double maxSpeedMeters = 3; 
    File directory = new File(Filesystem.getDeployDirectory(), "swerve"); // grabs the JSON packages for swerve
    private SwerveDrive swerveDrive;
    public SwerveSubsystem() {    
        try { // you need a try catch statement because SwerveParser throws an exception that must be catched
            swerveDrive = new SwerveParser(directory).createSwerveDrive(maxSpeedMeters);
        } catch(IOException e) {
            e.printStackTrace();
        }
        SwerveDriveTelemetry.verbosity = TelemetryVerbosity.HIGH;
    }

    public Command getDriveCommand(DoubleSupplier xTranslation, DoubleSupplier yTranslation, DoubleSupplier xHeading, DoubleSupplier yHeading) {
        return run(() -> {
            Translation2d velocity = SwerveMath.scaleTranslation(new Translation2d(
                                                                                xTranslation.getAsDouble(),
                                                                                yTranslation.getAsDouble()
                                                                                ), 0.8);
            swerveDrive.driveFieldOriented(swerveDrive.swerveController.getTargetSpeeds(velocity.getX(), velocity.getY(),
                                                                                        xHeading.getAsDouble(),
                                                                                        yHeading.getAsDouble(),
                                                                                        swerveDrive.getOdometryHeading().getRadians(),
                                                                                        swerveDrive.getMaximumChassisVelocity()));
        });
    }

    public void driveFieldOriented(ChassisSpeeds velocity) {
        swerveDrive.driveFieldOriented(velocity);
    }

    public Command driveFieldOriented(Supplier<ChassisSpeeds> velocity) {
        return run(() -> {
            swerveDrive.driveFieldOriented(velocity.get());
        });
    }

    public SwerveDrive getDrive() {
        return swerveDrive;
    }

    public void resetOdometry(Pose2d initialHolonomicPos) {
        swerveDrive.resetOdometry(initialHolonomicPos);
    }
}
