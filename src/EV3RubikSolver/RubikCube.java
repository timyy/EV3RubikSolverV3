/**
 * 
 */
package EV3RubikSolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TIMYY
 *
 */
public class RubikCube {
	private ArrayList<ColorItem> colorItems; 
	private Map<Integer, String> ColorSortResult;   
	
	
	public RubikCube() {
		this.colorItems = new ArrayList<ColorItem>(); 
		this.ColorSortResult = new HashMap<Integer,String>();
	}
	public void AddColorItem(int i, int j, int k, int r, int g, int b) {
		ColorItem newItem = new ColorItem(i,j,k,r,g,b);
		this.colorItems.add(newItem);
	}
	
	/**
	 * 区分识别Item的颜色
	 */
	private void DistinguishColor() {
		String[] resultColor = {"W","G","B","R","O","Y"};
		
		for (int n = 0; n < 6; n++)  
		{  
		    ColorItem.setCompareType(resultColor[n]);
			Collections.sort(this.colorItems);  
		    for (int i = 0; i < 9; i++)  
		    {  
		        ColorItem item = this.colorItems.get(this.colorItems.size() - 1);  
		        int ijk = item.I * 100 + item.J * 10 + item.K;  
		        this.ColorSortResult.put(ijk, resultColor[n]);  
		        this.colorItems.remove(this.colorItems.size() - 1);  
		    }  
		} 
	}
	
	// 返回两个字串，一个是颜色字串，一个是数字串，用于SolveReadColors的输入
	private String[] ReadColors()  
	{  
		String ColorStr = "";  
		String RealStr = "";  
	    for (int i = 0; i < 6; i++)  
	    {  
	        for (int j = 0; j < 3; j++)  
	        {  
	            for (int k = 0; k < 3; k++)  
	            {  
	                if (!( ColorStr == null || ColorStr.length() <= 0))  
	                {  
	                    ColorStr += ",";  
	                    RealStr += ",";  
	                }  
	                int c = i * 100 + j * 10 + k;  
	                String r = (String) this.ColorSortResult.get(c);  
	                ColorStr += ColorValue(r);  
	                RealStr += r;  
	            }  
	        }  
	    }  
	    return new String[] { ColorStr, RealStr };  
	}  
	  
	private int ColorValue(String c)  
	{  
	    if (c.contains("Y") || c.contains("y")) return 1;  
	    if (c.contains("B") || c.contains("b")) return 2;  
	    if (c.contains("R") || c.contains("r")) return 3;  
	    if (c.contains("W") || c.contains("w")) return 4;  
	    if (c.contains("O") || c.contains("o")) return 5;  
	    if (c.contains("G") || c.contains("g")) return 6;  
	    return 0;  
	}
	/**
	 * 从s解魔方字串
	 * @param s 其中s是把6*3*3的数组，用逗号按顺序连接成的字符串 
	 * @return 返回解魔方的字串
	 */

     String SolveReadColors(String s)  
    {  
        String[] ArrColors = s.split(",");   
        String sInput = "";  
        String ReadQ = "URDLFB";  
        String[] PosQ = new String[6];
        
		System.out.print("SolveReadColors:" + s);
		System.out.println();
		
        for (int i = 0; i < 6; i++) PosQ[Integer.parseInt(ArrColors[4 + i * 9]) - 1] = ReadQ.substring(i, i+1);  
        for (int i = 0; i < 6; i++)  {
        	System.out.print(4 + i * 9 + "!");
        	System.out.print(ArrColors[4 + i * 9]+ "@");
        	System.out.print(ReadQ+ "#" + i +"#");
        	System.out.print(ReadQ.substring(i, i+1)+ "$");
        	System.out.print(PosQ[i] +"%\n");
        }
        System.out.println();
        sInput += PosQ[Integer.parseInt(ArrColors[7]) - 1] + PosQ[Integer.parseInt(ArrColors[37]) - 1] + " ";  //UF  
        sInput += PosQ[Integer.parseInt(ArrColors[5]) - 1] + PosQ[Integer.parseInt(ArrColors[12]) - 1] + " ";  //UR  
        sInput += PosQ[Integer.parseInt(ArrColors[1]) - 1] + PosQ[Integer.parseInt(ArrColors[52]) - 1] + " ";  //UB  
        sInput += PosQ[Integer.parseInt(ArrColors[3]) - 1] + PosQ[Integer.parseInt(ArrColors[32]) - 1] + " ";  //UL  
        sInput += PosQ[Integer.parseInt(ArrColors[25]) - 1] + PosQ[Integer.parseInt(ArrColors[43]) - 1] + " ";  //DF  
        sInput += PosQ[Integer.parseInt(ArrColors[21]) - 1] + PosQ[Integer.parseInt(ArrColors[14]) - 1] + " ";  //DR  
        sInput += PosQ[Integer.parseInt(ArrColors[19]) - 1] + PosQ[Integer.parseInt(ArrColors[46]) - 1] + " ";  //DB  
        sInput += PosQ[Integer.parseInt(ArrColors[23]) - 1] + PosQ[Integer.parseInt(ArrColors[30]) - 1] + " ";  //DL  
        sInput += PosQ[Integer.parseInt(ArrColors[41]) - 1] + PosQ[Integer.parseInt(ArrColors[16]) - 1] + " ";  //FR  
        sInput += PosQ[Integer.parseInt(ArrColors[39]) - 1] + PosQ[Integer.parseInt(ArrColors[34]) - 1] + " ";  //FL  
        sInput += PosQ[Integer.parseInt(ArrColors[50]) - 1] + PosQ[Integer.parseInt(ArrColors[10]) - 1] + " ";  //BR  
        sInput += PosQ[Integer.parseInt(ArrColors[48]) - 1] + PosQ[Integer.parseInt(ArrColors[28]) - 1] + " ";  //BL  
      
        sInput += PosQ[Integer.parseInt(ArrColors[8]) - 1] + PosQ[Integer.parseInt(ArrColors[38]) - 1] + PosQ[Integer.parseInt(ArrColors[15]) - 1] + " ";  //UFR  
        sInput += PosQ[Integer.parseInt(ArrColors[2]) - 1] + PosQ[Integer.parseInt(ArrColors[9]) - 1] + PosQ[Integer.parseInt(ArrColors[53]) - 1] + " ";  //URB  
        sInput += PosQ[Integer.parseInt(ArrColors[0]) - 1] + PosQ[Integer.parseInt(ArrColors[51]) - 1] + PosQ[Integer.parseInt(ArrColors[29]) - 1] + " ";  //UBL  
        sInput += PosQ[Integer.parseInt(ArrColors[6]) - 1] + PosQ[Integer.parseInt(ArrColors[35]) - 1] + PosQ[Integer.parseInt(ArrColors[36]) - 1] + " ";  //ULF  
      
        sInput += PosQ[Integer.parseInt(ArrColors[24]) - 1] + PosQ[Integer.parseInt(ArrColors[17]) - 1] + PosQ[Integer.parseInt(ArrColors[44]) - 1] + " ";  //DRF  
        sInput += PosQ[Integer.parseInt(ArrColors[26]) - 1] + PosQ[Integer.parseInt(ArrColors[42]) - 1] + PosQ[Integer.parseInt(ArrColors[33]) - 1] + " ";  //DFL  
        sInput += PosQ[Integer.parseInt(ArrColors[20]) - 1] + PosQ[Integer.parseInt(ArrColors[27]) - 1] + PosQ[Integer.parseInt(ArrColors[45]) - 1] + " ";  //DLB  
        sInput += PosQ[Integer.parseInt(ArrColors[18]) - 1] + PosQ[Integer.parseInt(ArrColors[47]) - 1] + PosQ[Integer.parseInt(ArrColors[11]) - 1];  //DBR  
      
        System.out.print(sInput);
        System.out.println();
        String ResultSteps = EV3RubikSolver.solver.Solver.GetResult(sInput);  
        return ResultSteps;
    }  

    public String SolveRubik() {
    	String[] cubeString ;
    	
		System.out.print("SolveRubik:");
		System.out.println();
		
    	this.DistinguishColor();
		System.out.print("DistinguishColor:");
		System.out.println();
		
    	cubeString = this.ReadColors();
		System.out.print("ReadColors:" + cubeString[0] + "|"+ cubeString[1]);
		System.out.println();
		
    	String ResultSteps = this.SolveReadColors(cubeString[0]);
		System.out.print("ResultSteps:" + ResultSteps);
		System.out.println();
		return ResultSteps;
    }
    /**
     * 把URDLFB表示法的解魔方步骤，转化成萝卜头能识别的PBS表示法。
     * @param ResultSteps :
     *       var InputText = "RU LF UB DR DL BL UL FU BD RF BR FD LDF LBD FUL RFD UFR RDB UBL RBU";
     *       var OutputText = "D1 B3 F1 U3 B1 L2 U3 B2 D3 L2 U1 R1 D1 F2 D1 L2 D2 B2 D3 L2 D1 B2 U2 L2 D2 B2 U2 L2 B2 R2 ";
     * @return 从URDLFB操作到PBS操作的转换: P B3 P S2 B1 P S1
     */
    public ArrayList<MoveStep> SolveRobotMoves(String ResultSteps) {
    	
    	String targetSide;
    	CubeCenter CenterStatus = new CubeCenter();
        ArrayList<MoveStep> Steps = new ArrayList<MoveStep>();
        MoveStep currentStep;
        int rotateCount;
        
        if(( null == ResultSteps) || 0 == ResultSteps.length())
        	return Steps;
        String[] urdSteps = ResultSteps.split(" ");
        
        for(int i=0; i<urdSteps.length; i++) {
        	targetSide = urdSteps[i].substring(0, 1);
        	rotateCount =Integer.parseInt(urdSteps[i].substring(1, 2));		
        	
        	int findSidePosition = CenterStatus.FindCenter(targetSide);  
        
			// Rotate to corrent bottom
			switch (findSidePosition) {
			case 2:
				// Do Nothing
				break;
			case 1:
				CenterStatus.RotatePaw();
				Steps.add(new MoveStep(MoveType.RotatePaw, 0));
				break;
			case 0:
				CenterStatus.RotatePaw();
				Steps.add(new MoveStep(MoveType.RotatePaw, 0));
				CenterStatus.RotatePaw();
				Steps.add(new MoveStep(MoveType.RotatePaw, 0));
				break;
			case 3:
				CenterStatus.RotateBottom(true);
				CenterStatus.RotateBottom(true);
				Steps.add(new MoveStep(MoveType.RotateBottom, 2));
				CenterStatus.RotatePaw();
				Steps.add(new MoveStep(MoveType.RotatePaw, 0));
				break;
			case 4:
				CenterStatus.RotateBottom(true);
				Steps.add(new MoveStep(MoveType.RotateBottom, 1));
				CenterStatus.RotatePaw();
				Steps.add(new MoveStep(MoveType.RotatePaw, 0));
				break;
			case 5:
				CenterStatus.RotateBottom(false);
				Steps.add(new MoveStep(MoveType.RotateBottom, 3));
				CenterStatus.RotatePaw();
				Steps.add(new MoveStep(MoveType.RotatePaw, 0));
				break;
			}

			Steps.add(new MoveStep(MoveType.RotateBottomSide,rotateCount));
//			Steps[Steps.Count - 1].OrginStep = currentStep;
		}
		return Steps;
	}
	public void rest() {
		// TODO Auto-generated method stub
		colorItems.clear();
		ColorSortResult.clear();
	}
}
