/**
 * 
 */
package EV3RubikSolver;

import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;

/**
 * @author TIMYY
 *
 */
public class Robot {
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
}
