/**
 * TIMYY modify
 * 2014 begin
 * 创建：2015-09-8
 */
package EV3RubikSolver;

import java.util.ArrayList;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Sound;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.Color;
import lejos.robotics.ColorAdapter;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;

/**
 * @author TIMYY
 * 
 */

public class Robot {
	// Define Sensors
	static EV3UltrasonicSensor distance = new EV3UltrasonicSensor(SensorPort.S1);
	// static LightSensor light = new LightSensor(SensorPort.S2);
	// static EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S3);
	static ColorAdapter colorAdapter = new ColorAdapter(new EV3ColorSensor(
			SensorPort.S2));

	// Define Motors
	static RegulatedMotor paw = Motor.A;
	static RegulatedMotor bottom = Motor.B;
	static RegulatedMotor colorMonitor = Motor.C;

	// if the paw has the 3:1 reducer, set it as true
	static boolean HasReducer = true;
	// the motor angle for paw to hold the cube
	static int nPawHoldPosition = 60;
	// the motor angle for paw to rotate the cube
	static int PawTurnOverPosition = 110;
	// the motor angle for base to rotate 90 degree
	static int BaseOneQuarter = -270; // 36:12 4个位置。 360/4 * (36/12)
	// the fix angle for base
	static int BaseRotateFix = 0; // 这个打算拧过一点，再回复。
	// the fix angle of base position fix
	static int FixBasePositionOffset = 16;
	// the init position of color sensor motor(this will be set automatically)
	static int ColorMotorBaseAngle = 0;
	// add offset positions for color sensor motor
	static int ColorMotorOffset1 = 730+4; // 中心位置，scan
	static int ColorMotorOffset2 = 620; // 边
	static int ColorMotorOffset3 = 585; // 角
	static int[][] ColorMotorOffset = {
		{ 734, 610, 570, 620, 570, 620, 570, 620, 570 },				// 5 RF 读起
		{ 734, 610, 570, 630, 570, 630, 570, 630, 570 },
		{ 734, 610, 570, 630, 570, 630, 570, 630, 570 },
		{ 734, 610, 570, 630, 570, 630, 570, 630, 570 },				
		{ 734, 610, 570, 630, 570, 630, 570, 630, 570 },				// 先读的DF
		{ 734, 610, 570, 630, 570, 630, 570, 630, 570 } };

	static int mSpinAngle =4; // 微调角度
	static int ColorReadPostion1 = 135; // 36:12 8个位置。 360/8 * (36/12)
	static int ColorReadPostion2 = 135;
	// A flag to check if the cube is on the base
	static boolean hasError = false;
	static int delay = 120;
	
	static RubikCube cube = new RubikCube();

	// init

	// init all motor to init position.

	public static void init() {
		Message("Reset scan");
		initColorMonitor();
		Message("Reset paw");
		initPaw();
		// paw.setSpeed(50);
		// paw.rotate(50);
		bottom.resetTachoCount();
		Message("Ready");
		cube.rest();

	}

	private static void Message(String s) {

		Brick brick = BrickFinder.getDefault();
		GraphicsLCD g = brick.getGraphicsLCD();
		g.setFont(Font.getLargeFont());
		g.drawString(s, 2, 10, 0);

	}

	private static void initColorMonitor() {
		// 40%功率开电机，直到电机转到正向极限位置，不再转动.

		colorMonitor.setSpeed((int) (colorMonitor.getMaxSpeed() * 0.4));
		colorMonitor.forward();

		while (colorMonitor.isMoving()) {
			Thread.yield();
		}

		// 从极限位置反向转动100度。
		colorMonitor.rotate(-100, false);

		// 20%
		colorMonitor.setSpeed((int) (colorMonitor.getMaxSpeed() * 0.2));
		colorMonitor.forward();

		while (colorMonitor.isMoving()) {
			Thread.yield();
		}
		// 传感器就位，重置位置计数，做为初始位置。
		colorMonitor.resetTachoCount();
		scanAway();
	}

	private static void scanAway() {
		//
		colorMonitor.setSpeed((int) (colorMonitor.getMaxSpeed() * 1.0));

		// 从极限位置反向转动340度。
		colorMonitor.rotateTo(-340);
	}

	// 初始化爪子电机。
	private static void initPaw() {
		// 20%开始。
		paw.setSpeed((int) (colorMonitor.getMaxSpeed() * 0.2));
		paw.backward();
		// 转到极限
		while (paw.isMoving()) {
			Thread.yield();
		}
		// 电机就位，重置位置计数，做为初始位置。
		paw.resetTachoCount();

		// 从极限位置转动10度,70%功率。
		paw.setSpeed((int) (colorMonitor.getMaxSpeed() * 0.7));
		paw.rotate(10);

	}

	// Rotate bottom side of cube
	public static void RotateBottomSide(int nQuarter) throws Exception {
		// paw.setSpeed(400);
		int nFixAngle = BaseRotateFix * (nQuarter > 0 ? 1 : -1);
		// int nPawHoldPosition = PawHoldPosition;
		// if(HasReducer) nPawHoldPosition = -3 * nPawHoldPosition;
		// paw.rotateTo(nPawHoldPosition);
		// bottom.rotate(nQuarter * BaseOneQuarter + nFixAngle);
		// bottom.rotate(-nFixAngle);
		// paw.rotateTo(0);

		paw.setSpeed((int) (colorMonitor.getMaxSpeed() * 0.60));
		paw.rotateTo(nPawHoldPosition);
		Thread.sleep(100);
		bottom.setSpeed((int) (colorMonitor.getMaxSpeed() * 1.0));
		bottom.rotate(nQuarter * BaseOneQuarter + nFixAngle);
		bottom.rotate(-nFixAngle);
		paw.rotateTo(10);

	}

	// Rotate the whole cube from bottom, without hold the arm
	public static void RotateBottom(int nQuarter) throws Exception {
		bottom.rotate(nQuarter * BaseOneQuarter);
	}

	// Rotate the whole cube from paw
	public static void RotatePaw() throws Exception {
		// int nPawHoldPosition = PawHoldPosition - 8;
		// if(HasReducer) nPawHoldPosition = -3 * nPawHoldPosition;
		//
		// int nPawTurnOverPosition = PawTurnOverPosition;
		// if(HasReducer) nPawTurnOverPosition = -3 * nPawTurnOverPosition;
		//
		// paw.setSpeed(1000);
		// paw.rotateTo(nPawHoldPosition);
		// paw.setSpeed(300);
		// paw.rotateTo(nPawTurnOverPosition);
		// paw.setSpeed(400);
		// paw.rotateTo(nPawHoldPosition);
		// paw.setSpeed(1000);
		// paw.rotateTo(0);
		paw.setSpeed((int) (colorMonitor.getMaxSpeed() * 0.60));
		paw.rotateTo(nPawHoldPosition);
		Thread.sleep(100);
		paw.setSpeed((int) (colorMonitor.getMaxSpeed() * 0.90));
		paw.rotateTo(195);
		paw.setSpeed((int) (colorMonitor.getMaxSpeed() * 0.75));
		paw.rotate(-15);
		Thread.sleep(70);
		paw.setSpeed((int) (colorMonitor.getMaxSpeed() * 0.60));
		paw.rotateTo(nPawHoldPosition);
		Thread.sleep(100);
		paw.rotateTo(10);
		Thread.sleep(100);
	}

	// Fix color sensor position
	public static void FixColorSensorPosition() throws Exception {
		int tolerance = 5;
		ColorMotorBaseAngle = -25;
		colorMonitor.rotateTo(ColorMotorBaseAngle);
		Thread.sleep(100);
		colorMonitor.setSpeed(50);

		Color color = colorAdapter.getColor();

		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		int baseColor = r + g + b;
		int TargetExists = 0;
		while (TargetExists < baseColor + tolerance
				&& ColorMotorBaseAngle > -50) {
			colorMonitor.rotateTo(ColorMotorBaseAngle--);
			r = color.getRed();
			g = color.getGreen();
			b = color.getBlue();
			TargetExists = r + g + b;
		}
		colorMonitor.rotateTo(ColorMotorBaseAngle + 32);
	}

	// Read each side colors of the cube
	public static void ReadAllSide() throws Exception {
		// Rotate the 6 sides in sequence
		int nSideIndex = 0;
		ReadOneSide(nSideIndex++);

		if (CheckCubeReady())
			RotatePaw();
		ReadOneSide(nSideIndex++);

		if (CheckCubeReady())
			RotatePaw();
		ReadOneSide(nSideIndex++);

		if (CheckCubeReady())
			RotatePaw();
		ReadOneSide(nSideIndex++);

		RotateBottom(-1);    // 
		if (CheckCubeReady())
			RotatePaw();
		ReadOneSide(nSideIndex++);  // 翻了两次，0对面，2

		if (CheckCubeReady())
			RotatePaw();
		if (CheckCubeReady())
			RotatePaw();
		ReadOneSide(nSideIndex++);   // 最后是0
	}

	/**
	 * Read one side by the index
	 * 
	 * @param nSideIndex
	 * @throws Exception
	 */
	public static void ReadOneSide(int nSideIndex) throws Exception {
		int[][] idx = { 
				{ 4, 7, 8, 5, 2, 1, 0, 3, 6 },				// 5 RF 读起
				{ 4, 3, 6, 7, 8, 5, 2, 1, 0 },
				{ 4, 1, 0, 3, 6, 7, 8, 5, 2 },
				{ 4, 5, 2, 1, 0, 3, 6, 7, 8 },				
				{ 4, 7, 8, 5, 2, 1, 0, 3, 6},				// 先读的DF
				{ 4, 1, 0, 3, 6, 7, 8, 5, 2 } };
	
// 
//		int[][] idx1 = { { 4, 6, 7, 8, 5, 2, 1, 0, 3 },
//				{ 4, 0, 3, 6, 7, 8, 5, 2, 1 }, { 4, 2, 1, 0, 3, 6, 7, 8, 5 },
//				{ 4, 8, 5, 2, 1, 0, 3, 6, 7 }, { 4, 2, 1, 0, 3, 6, 7, 8, 5 },
//				{ 4, 2, 1, 0, 3, 6, 7, 8, 5 } };
		int[] idx2 = { 5, 1, 4, 3, 2, 0 }; //最后必须是0，因为转顶面上来

		int i = 0;
		colorMonitor.setSpeed((int) (colorMonitor.getMaxSpeed() * 0.8));
		bottom.setSpeed((int) (colorMonitor.getMaxSpeed() * 0.80));

		// Read Center Color
		colorMonitor.rotateTo(ColorMotorBaseAngle - ColorMotorOffset[nSideIndex][0]);
		Color color = ReadOnePiece();
		SendColorToPC(idx2[nSideIndex], idx[nSideIndex][i++], color);

		// Read Borders
		for (int jj = 0; jj < 4; jj++) {
			// read border
			colorMonitor.rotateTo(ColorMotorBaseAngle - ColorMotorOffset[nSideIndex][1+jj*2]);
			color = ReadOnePiece();
			SendColorToPC(idx2[nSideIndex], idx[nSideIndex][i++], color);

			// rotate to corner
			bottom.rotate(ColorReadPostion1);
			// read corner
			colorMonitor.rotateTo(ColorMotorBaseAngle - ColorMotorOffset[nSideIndex][2+jj*2]);
			color = ReadOnePiece();
			SendColorToPC(idx2[nSideIndex], idx[nSideIndex][i++], color);

			// rotate to border
			bottom.rotate(ColorReadPostion2);
		}

		// colorMonitor.rotateTo(ColorMotorBaseAngle + 32);
		// bottom.rotateTo(0);
		scanAway();
	}

	// Read one side by the index
	private static Color ReadOnePiece() throws Exception {
		Color[] colors = new Color[10];
		int maxColor = 0;
		int tmpColor = 0;
		int maxi = 0;
		

		for (int i = 0; i < 1; i++) {
			// Add a delay time for the motor to be stable
			if(i>0) colorMonitor.rotate(mSpinAngle);
			Thread.sleep(delay);
			colors[i] = colorAdapter.getColor();
			tmpColor = colors[i].getRed() + colors[i].getGreen()
					+ colors[i].getBlue();
			if (maxColor < tmpColor) {
				maxColor = tmpColor;
				maxi = i;
			}
			// System.out.print(i + " " + maxColor + " " + maxi + "--");
		}
		return colors[maxi];
	}

	// Send colors to PC
	// Read cube color , build a virtual cube.
	public static void SendColorToPC(int center, int n, Color color)
			throws Exception {
		// Add a delay time for the motor to be stable
		int delay = 120;

		// get the x,y of n
		int y = n % 3;
		int x = (n - y) / 3;

		// send to PC by bluetooth
		byte[] data = new byte[9];
		data[0] = (byte) center;
		data[1] = (byte) x;
		data[2] = (byte) y;
		data[3] = (byte) color.getRed();
		data[4] = (byte) color.getGreen();
		data[5] = (byte) color.getBlue();
		// data[6] = (byte)(color.getRawRed() / 3);
		// data[7] = (byte)(color.getRawGreen() / 3);
		// data[8] = (byte)(color.getRawBlue() / 3);
		// BlueTooth.WriteBytes(data);

		cube.AddColorItem(center, x, y, color.getRed(), color.getGreen(),
				color.getBlue());
		String str = "center=, " + center + ", n= , " + n + ", r= , " + color.getRed()
				+ " , g= , " + color.getGreen() + " , b= ," + color.getBlue();
		System.out.print(str);
		System.out.println();
	}

	/**
	 * 解Robot
	 * 
	 * @throws Exception
	 */
	public static void SolveRubik() throws Exception {
		String ResultSteps;
		ArrayList<MoveStep> moveSteps;
		int count = 0;

		ResultSteps = cube.SolveRubik();
		System.out.print("ResultSteps:" + ResultSteps);
		System.out.println();
		moveSteps = cube.SolveRobotMoves(ResultSteps);
		System.out.print("ResultSteps:" + moveSteps.size());
		System.out.println();
		for (int i = 0; i < moveSteps.size(); i++) {
			count = moveSteps.get(i).step;
			if (count == 3)
				count = -1;
			switch (moveSteps.get(i).moveType) {
			case RotatePaw:
				// Rotate paw
				Robot.RotatePaw();
				while (count-- > 0)
					Robot.RotatePaw();
				break;
			case RotateBottom:
				// Rotate Bottom
				Robot.RotateBottom(count);
				break;
			case RotateBottomSide:
				// Rotate Bottom Side
				Robot.RotateBottomSide(count);
				break;
			default:
				break;

			}
		}

	}

	// check if the cube is still on the base
	public static boolean CheckCubeReady() throws Exception {
		// if already error, return directly to avoid play *.wav again
		// if(hasError) return false;
		return true;

		// int d;
		// SampleProvider dis = distance.getDistanceMode();
		// int sampleSize = dis.sampleSize();
		// float[] sample = new float[sampleSize];
		// dis.fetchSample(sample, 0);
		// d= (int) (sample[0] * 100.0);
		// int errorCount = 0;
		//
		// System.out.print("d=" + d );
		// while((d<4 || d>10) && errorCount < 10)
		// {
		// dis.fetchSample(sample, 0);
		// d= (int) (sample[0] * 100.0);
		// System.out.print("d=" + d );
		// errorCount++;
		// Thread.sleep(20);
		// }
		// System.out.println();
		// if(errorCount >= 10)
		// {
		// //The cube is break out;
		// hasError = true;
		// Sound.buzz(); // playSample(new File("Error.wav"));
		// }
		// return !hasError;
	}
}
