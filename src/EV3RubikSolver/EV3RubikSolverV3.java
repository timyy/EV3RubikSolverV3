/**
 * 
 */
package EV3RubikSolver;

import java.io.IOException;

import lejos.hardware.*;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

import java.io.*;

/**
 * @author TIMYY
 * 
 *
 */
public class EV3RubikSolverV3 {


	//Define Sensors
	static EV3UltrasonicSensor distance=new EV3UltrasonicSensor(SensorPort.S1);
	// static LightSensor light = new LightSensor(SensorPort.S2);
	static EV3ColorSensor color = new EV3ColorSensor(SensorPort.S3);
	
	//Define Motors
	static RegulatedMotor paw = Motor.A;
	static RegulatedMotor bottom = Motor.C;
	static RegulatedMotor monitor = Motor.B;

	//if the paw has the 3:1 reducer, set it as true 
	static boolean HasReducer = true;
	//the motor angle for paw to hold the cube
	static int PawHoldPosition = 56;
	//the motor angle for paw to rotate the cube
	static int PawTurnOverPosition = 110;
	//the motor angle for base to rotate 90 degree
	static int BaseOneQuarter = 315;
	//the fix angle for base
	static int BaseRotateFix = 40;
	//the fix angle of base position fix
	static int FixBasePositionOffset = 16;
	//the init position of color sensor motor(this will be set automatically)
	static int ColorMotorBaseAngle = 0;
	//add offset positions for color sensor motor
	static int ColorMotorOffset1 = 33;
	static int ColorMotorOffset2 = 9;
	static int ColorMotorOffset3 = 18;
	static int ColorReadPostion1 = 162;
	static int ColorReadPostion2 = 154;
	//A flag to check if the cube is on the base
	static boolean hasError = false;
	
	 
	/**
	 * @param args
	 */
    public static void main (String[] arg) throws Exception
    {
    //	light.setFloodlight(false);
    	bottom.setSpeed(1000);
    	
    	boolean hasCube = true;  //flag used to get the distance
    	boolean isChaotic = true; //flag used to record a chaotic cube  
    	
    	while(!Button.ESCAPE.isDown())
    	{
    		//Wait for the distance being in the correct range: 12~16
    		int CheckStatusTimes=0;
			LCD.clear();
			boolean previousStatus = true;
			boolean currentStatus = true;
			while(CheckStatusTimes++ < 10)
			{
				// Modify by timyy 2015/09/24
				// EV3 0.9 版本问题，改了。 
				SampleProvider sp = distance.getDistanceMode();
			    int sampleSize = sp.sampleSize();
			    float[] sample = new float[sampleSize];
			      /*
			       * Get a fresh sample from the filter. (The filter gets it from its
			       * source, the redMode)
			       */
			      sp.fetchSample(sample, 0);
			      /* Display individual values in the sample. */
			      // for (int i = 0; i < sampleSize; i++) {
			      float n = sample[0];
					LCD.drawString("distance=" + n + "   ", 0, 0);
					currentStatus = (n>=12 && n<=16);
					if(currentStatus != previousStatus)
					{
						CheckStatusTimes = 0;
						previousStatus = currentStatus;
					}
					// }
				Thread.sleep(100);
			}
			hasCube = currentStatus;
			if(!hasCube)
			{
				//if the cube is take away, we consume it is been upset
				isChaotic = true;
			}
    		
	    	if(hasCube && isChaotic)
	    	{
	    		//The cube is read, init the error status
	   			hasError = false;
	   			
	   			//Play some sound to notice the "Start"
				Thread.sleep(1000);
				Sound.twoBeeps();
				Thread.sleep(1000);
				Sound.playSample(new File("Start.wav"));

				//Fix angles
				Robot.FixBasePosition();
	   			Robot.FixColorSensorPosition();
	   			
	   			//Read Colors
	   			Robot.ReadAllSide();
	   			
	   			if(!hasError)
	   			{
		   			//Send 255 to start calculate
	   				
		   			// BlueTooth.WriteBytes(new byte[]{(byte)255});
		   			
		   			//The first return byte is the length of steps
		   			// byte[] readStepCount = BlueTooth.ReadBytes();
		   			int stepCount = readStepCount[0];
		   			LCD.drawString("steps=" + stepCount, 1, 1);
		   			
		   			//Start to action
		   			for(int i=0; i<stepCount; i++)
		   			{
		   				if(!Robot.CheckCubeReady()) break;
		   				
		   				//Fix angle every 10 steps
		   				if(i % 10 == 0)
		   				{
		   					Robot.FixBasePosition();
		   				}
		   				
		   				//Send request for the i step
		   				BlueTooth.WriteBytes(new byte[]{(byte)i});
		   				
		   				//Get result
		   				int step = BlueTooth.ReadBytes()[0];
		   				if(step==10)
		   				{
		   					//Rotate paw
		   					Robot.RotatePaw();
		   				}
		   				else if(step>=20 && step<30)
		   				{
		   					//Rotate Bottom
		   					int count = step - 20;
		   					if(count == 3) count = -1;
		   					Robot.RotateBottom(count);
		   				}
		   				else if(step>=30 && step<40)
		   				{
		   					//Rotate Bottom Side
		   					int count = step - 30;
		   					if(count == 3) count = -1;
		   					Robot.RotateBottomSide(count);
		   				}
		   			}
		   			
		   			//Send 254 to reset the pc data
		   			BlueTooth.WriteBytes(new byte[]{(byte)254});
	   			}
	   			if(!hasError)
	   			{
		   			//The cube has been solved
		   			isChaotic = false;
		   			
					Sound.playSample(new File("End.wav"));
					Thread.sleep(1000);
					Robot.RotateBottom(8);   				
	   			}
	    	}
    		Thread.sleep(500);
    	}
    }
    
    public static class Robot
    {
    	//Rotate bottom side of cube
		public static void RotateBottomSide(int nQuarter) throws Exception 
		{
			paw.setSpeed(400);
			int nFixAngle = BaseRotateFix * ( nQuarter > 0 ? 1 : -1);
			int nPawHoldPosition = PawHoldPosition;
			if(HasReducer) nPawHoldPosition = -3 * nPawHoldPosition;
			paw.rotateTo(nPawHoldPosition);
			bottom.rotate(nQuarter * BaseOneQuarter + nFixAngle);
			bottom.rotate(-nFixAngle);
			paw.rotateTo(0);
		}
		
		//Rotate the whole cube from bottom, without hold the arm
		public static void RotateBottom(int nQuarter)throws Exception 
		{
			bottom.rotate(nQuarter * BaseOneQuarter);
		}
		
		//Rotate the whole cube from paw
		public static void RotatePaw()throws Exception 
		{
			int nPawHoldPosition = PawHoldPosition - 8;
			if(HasReducer) nPawHoldPosition = -3 * nPawHoldPosition;
			
			int nPawTurnOverPosition = PawTurnOverPosition;
			if(HasReducer) nPawTurnOverPosition = -3 * nPawTurnOverPosition;
			
			paw.setSpeed(1000);
			paw.rotateTo(nPawHoldPosition);
			paw.setSpeed(300);
			paw.rotateTo(nPawTurnOverPosition);
			paw.setSpeed(400);
			paw.rotateTo(nPawHoldPosition);
			paw.setSpeed(1000);
			paw.rotateTo(0);
		}
		
		//Fix the position of cube base
		public static void FixBasePosition() throws Exception
		{
			int step = 3;
			int tolerance = 4;
			light.setFloodlight(false);
			bottom.rotate(-50);
			int angle = 0, minLight = 10000;
			int realtimeLight = ReadLightDifference();
			while(realtimeLight < minLight + tolerance)		
			{
				bottom.rotate(step);
				realtimeLight = ReadLightDifference();
				if(realtimeLight < minLight)
				{
					minLight = realtimeLight;
					angle = 0;
				}
				else
				{
					angle += step;
				}
			}
			bottom.rotate(- angle/2 - FixBasePositionOffset);
		}
		
		//Read the light difference between light on and light off
		private static int ReadLightDifference() throws Exception
		{
			int l1 = 0, l2 = 0;
			l1 = light.readValue();
			light.setFloodlight(true);
			Thread.sleep(20);
			l2 = light.readValue();
			light.setFloodlight(false);
			return l1-l2;
		}
		
		//Fix color sensor position
		public static void FixColorSensorPosition() throws Exception
		{
			int tolerance = 5;
			ColorMotorBaseAngle = -25;
			monitor.rotateTo(ColorMotorBaseAngle);
			Thread.sleep(100);
			monitor.setSpeed(50);
			int r = color.getRawRed();
			int g = color.getRawGreen();
			int b = color.getRawBlue();
			int baseColor = r + g + b;
			int TargetExists = 0;
			while(TargetExists < baseColor + tolerance && ColorMotorBaseAngle > -50)
			{
				monitor.rotateTo(ColorMotorBaseAngle--);
				r = color.getRawRed();
				g = color.getRawGreen();
				b = color.getRawBlue();
				TargetExists = r + g + b;
			}
			monitor.rotateTo(ColorMotorBaseAngle + 32);
		}
		
		//Read each side colors of the cube 
		public static void ReadAllSide() throws Exception
		{
			//Rotate the 6 sides in sequence
			int nSideIndex=0;
			ReadOneSide(nSideIndex++);

			if(CheckCubeReady()) RotatePaw();
			ReadOneSide(nSideIndex++);
			
			if(CheckCubeReady()) RotatePaw();
			ReadOneSide(nSideIndex++);
			
			if(CheckCubeReady()) RotatePaw();
			ReadOneSide(nSideIndex++);
			
			RotateBottom(-1);
			if(CheckCubeReady()) RotatePaw();
			ReadOneSide(nSideIndex++);
			
			if(CheckCubeReady()) RotatePaw();
			if(CheckCubeReady()) RotatePaw();
			ReadOneSide(nSideIndex++);
		}
		
		//Read one side by the index
		public static void ReadOneSide(int nSideIndex) throws Exception
		{
			//Add a delay time for the motor to be stable
			int delay=120; 
			int[][] idx={
					{4,6,7,8,5,2,1,0,3},
					{4,0,3,6,7,8,5,2,1},
					{4,2,1,0,3,6,7,8,5},
					{4,8,5,2,1,0,3,6,7},
					{4,2,1,0,3,6,7,8,5},
					{4,2,1,0,3,6,7,8,5}};
			int[] idx2={5,1,4,3,2,0};
			
			int i=0;
			monitor.setSpeed(200);
			bottom.setSpeed(1000);
			
			//Read Center Color
			monitor.rotateTo(ColorMotorBaseAngle - ColorMotorOffset1);
			Thread.sleep(delay);
			
			SendColorToPC(idx2[nSideIndex], idx[nSideIndex][i++]);
			
			//Read Borders
			for(int jj=0;jj<4;jj++)
			{
				monitor.rotateTo(ColorMotorBaseAngle - ColorMotorOffset2);
				Thread.sleep(delay);
				SendColorToPC(idx2[nSideIndex], idx[nSideIndex][i++]);
			
				monitor.rotateTo(ColorMotorBaseAngle - ColorMotorOffset3);
				bottom.rotate(-ColorReadPostion1);
				Thread.sleep(delay);
				SendColorToPC(idx2[nSideIndex], idx[nSideIndex][i++]);
				bottom.rotate(-ColorReadPostion2);
			}
			monitor.rotateTo(ColorMotorBaseAngle + 32);
		}
		
		//Send colors to PC
		public static void SendColorToPC(int center, int n) throws Exception
		{
			//get the x,y of n
			int y = n % 3;
			int x = (n - y) / 3;

			//send to PC by bluetooth
			byte[] data = new byte[9]; 
			data[0] = (byte)center;
			data[1] = (byte)x;
			data[2] = (byte)y;
			data[3] = (byte)color.getRed();
			data[4] = (byte)color.getGreen();
			data[5] = (byte)color.getBlue();
			data[6] = (byte)(color.getRawRed() / 3);
			data[7] = (byte)(color.getRawGreen() / 3);
			data[8] = (byte)(color.getRawBlue() / 3);
			BlueTooth.WriteBytes(data);
		}
		
		//check if the cube is still on the base
		public static boolean CheckCubeReady() throws Exception
		{
			//if already error, return directly to avoid play *.wav again
			if(hasError) return false;
			
			int d = distance.getDistanceMode();
			int errorCount = 0;
			while((d<12 || d>16) && errorCount < 10)
			{
				errorCount++;
				Thread.sleep(20);
			}
			if(errorCount >= 10)
			{
				//The cube is break out;
				hasError = true;
				Sound.playSample(new File("Error.wav"));
			}
			return !hasError;
		}
    }
}




/* 
import java.io.IOException;
import lejos.nxt.*;
import lejos.nxt.comm.*;
import java.io.*;
import javax.bluetooth.*;

// Author: 动力老男孩
// Site: http://www.diy-robots.com
// Location: Beijing
// Update history:
// 2010/01/15 - Motor actions to rotate cube
// 2010/01/16 - Fix position for cube base and color sensor motor
// 2010/01/24 - Add codes to scan 6 sides of cube
// 2010/02/02 - Add bluetooth connect to PC
// 2010/04/01 - Add Error control and playing sound
// 2010/05/01 - Rewrite some codes to make it more beautiful


public class RubikSolverV2 {
	//Define Sensors
	static UltrasonicSensor distance=new UltrasonicSensor(SensorPort.S1);
	static LightSensor light = new LightSensor(SensorPort.S2);
	static ColorSensor color = new ColorSensor(SensorPort.S3);
	
	//Define Motors
	static Motor paw = Motor.A;
	static Motor bottom = Motor.C;
	static Motor monitor = Motor.B;

	//if the paw has the 3:1 reducer, set it as true 
	static boolean HasReducer = true;
	//the motor angle for paw to hold the cube
	static int PawHoldPosition = 56;
	//the motor angle for paw to rotate the cube
	static int PawTurnOverPosition = 110;
	//the motor angle for base to rotate 90 degree
	static int BaseOneQuarter = 315;
	//the fix angle for base
	static int BaseRotateFix = 40;
	//the fix angle of base position fix
	static int FixBasePositionOffset = 16;
	//the init position of color sensor motor(this will be set automatically)
	static int ColorMotorBaseAngle = 0;
	//add offset positions for color sensor motor
	static int ColorMotorOffset1 = 33;
	static int ColorMotorOffset2 = 9;
	static int ColorMotorOffset3 = 18;
	static int ColorReadPostion1 = 162;
	static int ColorReadPostion2 = 154;
	//A flag to check if the cube is on the base
	static boolean hasError = false;
	
    public static void main (String[] arg) throws Exception
    {
    	light.setFloodlight(false);
    	bottom.setSpeed(1000);
    	
    	BlueTooth.Connect();
    	boolean hasCube = true;  //flag used to get the distance
    	boolean isChaotic = true; //flag used to record a chaotic cube  
    	
    	while(!Button.ESCAPE.isPressed())
    	{
    		//Wait for the distance being in the correct range: 12~16
    		int CheckStatusTimes=0;
			LCD.clear();
			boolean previousStatus = true;
			boolean currentStatus = true;
			while(CheckStatusTimes++ < 10)
			{
				int n = distance.getDistance();
				LCD.drawString("distance=" + n + "   ", 0, 0);
				currentStatus = (n>=12 && n<=16);
				if(currentStatus != previousStatus)
				{
					CheckStatusTimes = 0;
					previousStatus = currentStatus;
				}
				Thread.sleep(100);
			}
			hasCube = currentStatus;
			if(!hasCube)
			{
				//if the cube is take away, we consume it is been upset
				isChaotic = true;
			}
    		
	    	if(hasCube && isChaotic)
	    	{
	    		//The cube is read, init the error status
	   			hasError = false;
	   			
	   			//Play some sound to notice the "Start"
				Thread.sleep(1000);
				Sound.twoBeeps();
				Thread.sleep(1000);
				Sound.playSample(new File("Start.wav"));

				//Fix angles
				Robot.FixBasePosition();
	   			Robot.FixColorSensorPosition();
	   			
	   			//Read Colors
	   			Robot.ReadAllSide();
	   			
	   			if(!hasError)
	   			{
		   			//Send 255 to start calculate
		   			BlueTooth.WriteBytes(new byte[]{(byte)255});
		   			
		   			//The first return byte is the length of steps
		   			byte[] readStepCount = BlueTooth.ReadBytes();
		   			int stepCount = readStepCount[0];
		   			LCD.drawString("steps=" + stepCount, 1, 1);
		   			
		   			//Start to action
		   			for(int i=0; i<stepCount; i++)
		   			{
		   				if(!Robot.CheckCubeReady()) break;
		   				
		   				//Fix angle every 10 steps
		   				if(i % 10 == 0)
		   				{
		   					Robot.FixBasePosition();
		   				}
		   				
		   				//Send request for the i step
		   				BlueTooth.WriteBytes(new byte[]{(byte)i});
		   				
		   				//Get result
		   				int step = BlueTooth.ReadBytes()[0];
		   				if(step==10)
		   				{
		   					//Rotate paw
		   					Robot.RotatePaw();
		   				}
		   				else if(step>=20 && step<30)
		   				{
		   					//Rotate Bottom
		   					int count = step - 20;
		   					if(count == 3) count = -1;
		   					Robot.RotateBottom(count);
		   				}
		   				else if(step>=30 && step<40)
		   				{
		   					//Rotate Bottom Side
		   					int count = step - 30;
		   					if(count == 3) count = -1;
		   					Robot.RotateBottomSide(count);
		   				}
		   			}
		   			
		   			//Send 254 to reset the pc data
		   			BlueTooth.WriteBytes(new byte[]{(byte)254});
	   			}
	   			if(!hasError)
	   			{
		   			//The cube has been solved
		   			isChaotic = false;
		   			
					Sound.playSample(new File("End.wav"));
					Thread.sleep(1000);
					Robot.RotateBottom(8);   				
	   			}
	    	}
    		Thread.sleep(500);
    	}
    	
    	BlueTooth.Disconnect();
    }
    
    public static class BlueTooth
    {
    	static BTConnection btc ;
    	static DataInputStream dis;
    	static DataOutputStream dos;
    	
    	public static void Connect() throws Exception 
    	{
    		LCD.clear();
    		LCD.drawString("Waiting BTC...",0,0);
            btc = Bluetooth.waitForConnection();
    		LCD.drawString("Connected",0,2);
    		LCD.refresh();	
    		dis = btc.openDataInputStream();
    		dos = btc.openDataOutputStream();
    	}
    	
    	public static void Disconnect() throws Exception 
    	{
			if(btc!=null)
			{
				WriteBytes(new byte[]{(byte)255,(byte)255,(byte)255});
				Thread.sleep(100);
				dos.close();
				dis.close();
				btc.close();
			}
    	}
    	
    	public static void WriteBytes(byte[] data) throws Exception 
    	{
    		for(int i=0;i<data.length;i++)
    		{
    			dos.writeByte(data[i]);
    		}
    		dos.flush();
    	}
    	
    	public static byte[] ReadBytes() throws Exception
    	{
    		byte[] buffer = new byte[255];
    		int length = btc.read(buffer, buffer.length);
    		if(length==-2)
    		{
    			//lost data, re-sync
    			btc.read(null, 255);
    			return new byte[0];
    		}
    		else
    		{
	    		byte[] data = new byte[length];
	    		for(int i=0;i<length;i++)
	    		{
	    			data[i] = buffer[i];
	    		}
	    		return data;
    		}
    	}
    }
 
    public static class Robot
    {
    	//Rotate bottom side of cube
		public static void RotateBottomSide(int nQuarter) throws Exception 
		{
			paw.setSpeed(400);
			int nFixAngle = BaseRotateFix * ( nQuarter > 0 ? 1 : -1);
			int nPawHoldPosition = PawHoldPosition;
			if(HasReducer) nPawHoldPosition = -3 * nPawHoldPosition;
			paw.rotateTo(nPawHoldPosition);
			bottom.rotate(nQuarter * BaseOneQuarter + nFixAngle);
			bottom.rotate(-nFixAngle);
			paw.rotateTo(0);
		}
		
		//Rotate the whole cube from bottom, without hold the arm
		public static void RotateBottom(int nQuarter)throws Exception 
		{
			bottom.rotate(nQuarter * BaseOneQuarter);
		}
		
		//Rotate the whole cube from paw
		public static void RotatePaw()throws Exception 
		{
			int nPawHoldPosition = PawHoldPosition - 8;
			if(HasReducer) nPawHoldPosition = -3 * nPawHoldPosition;
			
			int nPawTurnOverPosition = PawTurnOverPosition;
			if(HasReducer) nPawTurnOverPosition = -3 * nPawTurnOverPosition;
			
			paw.setSpeed(1000);
			paw.rotateTo(nPawHoldPosition);
			paw.setSpeed(300);
			paw.rotateTo(nPawTurnOverPosition);
			paw.setSpeed(400);
			paw.rotateTo(nPawHoldPosition);
			paw.setSpeed(1000);
			paw.rotateTo(0);
		}
		
		//Fix the position of cube base
		public static void FixBasePosition() throws Exception
		{
			int step = 3;
			int tolerance = 4;
			light.setFloodlight(false);
			bottom.rotate(-50);
			int angle = 0, minLight = 10000;
			int realtimeLight = ReadLightDifference();
			while(realtimeLight < minLight + tolerance)		
			{
				bottom.rotate(step);
				realtimeLight = ReadLightDifference();
				if(realtimeLight < minLight)
				{
					minLight = realtimeLight;
					angle = 0;
				}
				else
				{
					angle += step;
				}
			}
			bottom.rotate(- angle/2 - FixBasePositionOffset);
		}
		
		//Read the light difference between light on and light off
		private static int ReadLightDifference() throws Exception
		{
			int l1 = 0, l2 = 0;
			l1 = light.readValue();
			light.setFloodlight(true);
			Thread.sleep(20);
			l2 = light.readValue();
			light.setFloodlight(false);
			return l1-l2;
		}
		
		//Fix color sensor position
		public static void FixColorSensorPosition() throws Exception
		{
			int tolerance = 5;
			ColorMotorBaseAngle = -25;
			monitor.rotateTo(ColorMotorBaseAngle);
			Thread.sleep(100);
			monitor.setSpeed(50);
			int r = color.getRawRed();
			int g = color.getRawGreen();
			int b = color.getRawBlue();
			int baseColor = r + g + b;
			int TargetExists = 0;
			while(TargetExists < baseColor + tolerance && ColorMotorBaseAngle > -50)
			{
				monitor.rotateTo(ColorMotorBaseAngle--);
				r = color.getRawRed();
				g = color.getRawGreen();
				b = color.getRawBlue();
				TargetExists = r + g + b;
			}
			monitor.rotateTo(ColorMotorBaseAngle + 32);
		}
		
		//Read each side colors of the cube 
		public static void ReadAllSide() throws Exception
		{
			//Rotate the 6 sides in sequence
			int nSideIndex=0;
			ReadOneSide(nSideIndex++);

			if(CheckCubeReady()) RotatePaw();
			ReadOneSide(nSideIndex++);
			
			if(CheckCubeReady()) RotatePaw();
			ReadOneSide(nSideIndex++);
			
			if(CheckCubeReady()) RotatePaw();
			ReadOneSide(nSideIndex++);
			
			RotateBottom(-1);
			if(CheckCubeReady()) RotatePaw();
			ReadOneSide(nSideIndex++);
			
			if(CheckCubeReady()) RotatePaw();
			if(CheckCubeReady()) RotatePaw();
			ReadOneSide(nSideIndex++);
		}
		
		//Read one side by the index
		public static void ReadOneSide(int nSideIndex) throws Exception
		{
			//Add a delay time for the motor to be stable
			int delay=120; 
			int[][] idx={
					{4,6,7,8,5,2,1,0,3},
					{4,0,3,6,7,8,5,2,1},
					{4,2,1,0,3,6,7,8,5},
					{4,8,5,2,1,0,3,6,7},
					{4,2,1,0,3,6,7,8,5},
					{4,2,1,0,3,6,7,8,5}};
			int[] idx2={5,1,4,3,2,0};
			
			int i=0;
			monitor.setSpeed(200);
			bottom.setSpeed(1000);
			
			//Read Center Color
			monitor.rotateTo(ColorMotorBaseAngle - ColorMotorOffset1);
			Thread.sleep(delay);
			
			SendColorToPC(idx2[nSideIndex], idx[nSideIndex][i++]);
			
			//Read Borders
			for(int jj=0;jj<4;jj++)
			{
				monitor.rotateTo(ColorMotorBaseAngle - ColorMotorOffset2);
				Thread.sleep(delay);
				SendColorToPC(idx2[nSideIndex], idx[nSideIndex][i++]);
			
				monitor.rotateTo(ColorMotorBaseAngle - ColorMotorOffset3);
				bottom.rotate(-ColorReadPostion1);
				Thread.sleep(delay);
				SendColorToPC(idx2[nSideIndex], idx[nSideIndex][i++]);
				bottom.rotate(-ColorReadPostion2);
			}
			monitor.rotateTo(ColorMotorBaseAngle + 32);
		}
		
		//Send colors to PC
		public static void SendColorToPC(int center, int n) throws Exception
		{
			//get the x,y of n
			int y = n % 3;
			int x = (n - y) / 3;

			//send to PC by bluetooth
			byte[] data = new byte[9]; 
			data[0] = (byte)center;
			data[1] = (byte)x;
			data[2] = (byte)y;
			data[3] = (byte)color.getRed();
			data[4] = (byte)color.getGreen();
			data[5] = (byte)color.getBlue();
			data[6] = (byte)(color.getRawRed() / 3);
			data[7] = (byte)(color.getRawGreen() / 3);
			data[8] = (byte)(color.getRawBlue() / 3);
			BlueTooth.WriteBytes(data);
		}
		
		//check if the cube is still on the base
		public static boolean CheckCubeReady() throws Exception
		{
			//if already error, return directly to avoid play *.wav again
			if(hasError) return false;
			
			int d = distance.getDistance();
			int errorCount = 0;
			while((d<12 || d>16) && errorCount < 10)
			{
				errorCount++;
				Thread.sleep(20);
			}
			if(errorCount >= 10)
			{
				//The cube is break out;
				hasError = true;
				Sound.playSample(new File("Error.wav"));
			}
			return !hasError;
		}
    }
}

**/