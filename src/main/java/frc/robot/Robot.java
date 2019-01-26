/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;

import edu.wpi.first.wpilibj.Joystick;


public class Robot extends TimedRobot 
{
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  TalonSRX _leftLead = new TalonSRX(0);
  TalonSRX _leftFollow = new TalonSRX(1);

  TalonSRX _rightLead = new TalonSRX(2);
  TalonSRX _rightFollow = new TalonSRX(3);

  //Spark _centerMotor = new Spark(0);
	
	Joystick _gamepad = new Joystick(0);

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() 
  {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() 
  {

  }

 
  @Override
  public void autonomousInit() 
  {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() 
  {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

 
  @Override
  public void teleopInit()
  {

    _leftFollow.follow(_leftLead);
    _rightFollow.follow(_rightLead);

		/* Disable motor controllers */
		_rightLead.set(ControlMode.PercentOutput, 0);
		_leftLead.set(ControlMode.PercentOutput, 0);
		
		/* Set Neutral mode */
		_leftLead.setNeutralMode(NeutralMode.Coast);
		_rightLead.setNeutralMode(NeutralMode.Coast);
		
		/* Configure output direction */
		_leftLead.setInverted(false);
		_rightLead.setInverted(true);
		
		System.out.println("This is a basic arcade drive using Arbitrary Feed Forward.");
  }
  
  @Override
  public void teleopPeriodic() 
  {

    		// Gamepad processing
		double forward = -1 * _gamepad.getY(); 		// Left Stick Y
		double turn = -1 *_gamepad.getX();				// Left Stick Y
		
		double throttleL = _gamepad.getZ();			// Front Lower Left Throttle
		double throttleR = _gamepad.getThrottle();	// Front Lower Right Throttle
		
		// This will carry a calculated value to set the Center Motor value
		double centerMotorValue = 0;
		
		// Use the below Deadband function to filter out idle noise on the inputs
		forward = Deadband(forward);
		turn = Deadband(turn);
		throttleR = Deadband(throttleR);
		throttleL = Deadband(throttleL);
		
		// Calculate the difference between the throttles, and use that for the center motor (quick and dirty, but it works)
		centerMotorValue = (0 - throttleL) + (throttleR);

		// Basic Arcade Drive using PercentOutput along with Arbitrary FeedForward supplied by turn
		_leftLead.set(ControlMode.PercentOutput, forward, DemandType.ArbitraryFeedForward, +turn);
		_rightLead.set(ControlMode.PercentOutput, forward, DemandType.ArbitraryFeedForward, -turn);
		
		//_centerMotor.set(centerMotorValue);

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() 
  {

  }

  double Deadband(double value) 
  {
		/* Upper deadband */
		if (value >= +0.05) 
			return value;
		
		/* Lower deadband */
		if (value <= -0.05)
			return value;
		
		/* Outside deadband */
		return 0;
	}
}
