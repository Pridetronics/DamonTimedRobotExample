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

//import edu.wpi.first.wpilibj.Spark;
//import edu.wpi.first.wpilibj.SpeedController;

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
  // Not using autonomous

 
  @Override
  public void teleopInit()
  {

    _leftFollow.follow(_leftLead);
    _rightFollow.follow(_rightLead);
      // Sets what the "follow" motor to follow "lead" motor

		/* Disable motor controllers */
		_rightLead.set(ControlMode.PercentOutput, 0);
		_leftLead.set(ControlMode.PercentOutput, 0);
    
      // Sets the percentage of speed. 
      // Always set to 0.
    
    
		/* Set Neutral mode */
		_leftLead.setNeutralMode(NeutralMode.Coast);
		_rightLead.setNeutralMode(NeutralMode.Coast);
      // Defines how it stops.
      // If set to COAST, it will gradually slow down when there's no throttle.
      // If set to BREAK, it will stop immediately.
    
    
		/* Configure output direction */
		_leftLead.setInverted(false);
    _rightLead.setInverted(true);
      // Inverts the wheel direction.
      // Important to do so that way the wheels don't fight each other
      // If FALSE, the wheels are not inverted. If TRUE, they're inverted.
		
    System.out.println("This is a basic arcade drive using Arbitrary Feed Forward.");
      // Prints out whatever is in the double quotation marks.
  }
  
  @Override
  public void teleopPeriodic() 
  {
    SmartDashboard.putString("Robot ID:", "Lucky's robot");
        // Gamepad processing
        // When the SHUFFLEboard (not dashboard) pops up, it will print out the TWO strings.
        // There needs to be TWO strings.
		double forward = -1 * _gamepad.getY(); 		// Left Stick Y
		double turn = -1 *_gamepad.getX();				// Left Stick Y
		
		// double throttleL = _gamepad.getZ();			// Front Lower Left Throttle
		// double throttleR = _gamepad.getThrottle();	// Front Lower Right Throttle
		
		// This will carry a calculated value to set the Center Motor value
		// double centerMotorValue = 0;
		
		// Use the below Deadband function to filter out idle noise on the inputs
		forward = Deadband(forward);
		turn = Deadband(turn);
		// throttleR = Deadband(throttleR);
		// throttleL = Deadband(throttleL);
		
		// Calculate the difference between the throttles, and use that for the center motor (quick and dirty, but it works)
		//centerMotorValue = (0 - throttleL) + (throttleR);

		//Basic Arcade Drive using PercentOutput along with Arbitrary FeedForward supplied by turn
    _leftLead.set(ControlMode.PercentOutput, forward, DemandType.ArbitraryFeedForward, +turn);
      // The positive turn is biased towards the left side.

		_rightLead.set(ControlMode.PercentOutput, forward, DemandType.ArbitraryFeedForward, -turn);
      // The negative turn is biased to the right side.
    // If asked to turn left or right
    // The direction steered will be controled by the motor biased to the direction.

		// _centerMotor.set(centerMotorValue);

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() 
  {
    // There's nothing in here...
    // What does this area do? I forgot if we went over it or not.
  }

  double Deadband(double value) 
  {
    // The dead band is used to clean up the code a bit
    // Stops the robot from "twitching" when it's driving.
		/* Upper deadband */
		if (value >= +0.05) 
			return value;
    // The value will return if the throttle is >= to 0.05   

		/* Lower deadband */
		if (value <= -0.05)
      return value;
    // The value will also return if it's <= -0.05.
		
		/* Outside deadband */
    return 0;
    // And it will return if the value is at 0.
    // Desclaimer: I know what the DEADBAND is used for, but I don't know if what I commented was correct.
    
	}
}
