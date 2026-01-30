// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.GetAprilTagAngleCommand;
import frc.robot.subsystems.AlgaeArmSubsystem;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.SwerveSubsystem;
import swervelib.SwerveInputStream;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
	// The robot's subsystems and commands are defined here...
	private final SwerveSubsystem m_SwerveSubsystem = new SwerveSubsystem();
	private final ShooterSubsystem m_ShooterSubsystem = new ShooterSubsystem();
	private final AlgaeArmSubsystem m_ArmSubsystem = new AlgaeArmSubsystem();
	SwerveInputStream driveAngularVelocity;
	SwerveInputStream driveDirectAngle;
	SwerveInputStream driveRobotOriented;


	// Replace with CommandPS4Controller or CommandJoystick if needed
	private final CommandXboxController m_driverController = new CommandXboxController(OperatorConstants.kDriverControllerPort);
	private final XboxController m_mechanismController = new XboxController(OperatorConstants.kMechanismControllerPort);

	/** The container for the robot. Contains subsystems, OI devices, and commands. */
	public RobotContainer() {
		// Configure the trigger bindings
		configureSwerveInputs();
		configureBindings();
	}

	/*
	 * configures the inputs and math for swerve drive
	 */
	private void configureSwerveInputs() {
		driveAngularVelocity = SwerveInputStream.of(m_SwerveSubsystem.getDrive(),
													() -> m_driverController.getLeftY() * -1,
													() -> m_driverController.getLeftX() * -1)
												.withControllerRotationAxis(m_driverController::getRightX)
												.deadband(0.5)
												.scaleTranslation(0.8)
												.allianceRelativeControl(true);
		driveDirectAngle = driveAngularVelocity.copy().withControllerHeadingAxis(
																					m_driverController::getRightX,
																					m_driverController::getRightY
																				)
																				.headingWhile(true);
		driveRobotOriented = driveAngularVelocity.copy()
														.robotRelative(false)
														.allianceRelativeControl(true);
	}

	/**
	 * Use this method to define your trigger->command mappings. Triggers can be created via the
	 * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
	 * predicate, or via the named factories in {@link
	 * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
	 * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
	 * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
	 * joysticks}.
	 */
	private void configureBindings() {
		Command driveDirectAngleCommand = m_SwerveSubsystem.driveFieldOriented(driveDirectAngle);
		m_SwerveSubsystem.setDefaultCommand(driveDirectAngleCommand);
		m_driverController.a()
							.whileTrue(new InstantCommand(() -> m_ArmSubsystem.forword()))
							.onFalse(new InstantCommand(() -> m_ArmSubsystem.stop()));
		
		m_driverController.b()
							.whileTrue(new InstantCommand(() -> m_ArmSubsystem.backword()))
							.onFalse(new InstantCommand(() -> m_ArmSubsystem.stop()));
		
		new JoystickButton(m_mechanismController, XboxController.Button.kLeftBumper.value)
													.whileTrue(new InstantCommand(() -> m_ShooterSubsystem.feed()))
													.onFalse(new InstantCommand(() -> m_ShooterSubsystem.stop()));
		new JoystickButton(m_mechanismController, XboxController.Button.kRightBumper.value)
													.whileTrue(new InstantCommand(() -> m_ShooterSubsystem.shoot()))
													.onFalse(new InstantCommand(() -> m_ShooterSubsystem.stop()));

		// Bind to a button - get angle to tag ID 2 and print it
		m_driverController.x()
			.onTrue(new GetAprilTagAngleCommand(m_Limelight, 2,
				(angle) -> System.out.println("Angle to tag 2: " + angle)));
	}

	/**
	 * Use this to pass the autonomous command to the main {@link Robot} class.
	 *
	 * @return the command to run in autonomous
	 */
	public Command getAutonomousCommand() {
		// An example command will be run in autonomous
		return m_SwerveSubsystem.getAutonamasCommand("basicAuto");
	}


    // Add the subsystem
    private final LimelightSubsystem m_Limelight = new LimelightSubsystem();
}
