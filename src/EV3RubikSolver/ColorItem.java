package EV3RubikSolver;

public class ColorItem   implements Comparable <ColorItem>
{  
	private static String compareType ="W";
	private  int R, G, B;  
	private  int Max, Min;  
	int I;
	int J;
	int K;
	
    public ColorItem() {
    }

    public ColorItem(int i, int j, int k,  int r, int g, int b) {
        this.setI(i);
        this.setJ(j);
        this.setK(k);
        this.setR(r);
        this.setG(g);
        this.setB(b);
    } 
    
	@Override	
	public int compareTo(ColorItem ci) {
		// TODO Auto-generated method stub
		switch(compareType)
		{

		case "W":
			// 白色， blue 最强。 按blue排序，最大的。
			return (this.B-ci.B) ;
		case "G":
			// 绿。 r-g 最小。
			return -((this.R - this.G) -(ci.R-ci.G));
		case "B":
			// 绿，蓝。,R 最小
			return -(this.R -ci.R);
		case "R":
			// 红色，G+B 最小。
			return -((this.G+this.B)-(ci.G+ci.B));

			
		case "O":
			// 橙色，R-g-b 最大。
			return ((this.R-this.G-this.B)-(ci.R-ci.G-ci.B));


			
		case "Y":
			// 黄，绿，蓝。 黄最红：）
			return 1;


		}
		return 0;
	}

	public int getR() {
		return R;
	}

	public void setR(int r) {
		R = r;
	}

	public int getG() {
		return G;
	}

	public void setG(int g) {
		G = g;
	}

	public int getB() {
		return B;
	}

	public void setB(int b) {
		B = b;
	}

	public int getJ() {
		return J;
	}

	public void setJ(int j) {
		J = j;
	}

	public int getK() {
		return K;
	}

	public void setK(int k) {
		K = k;
	}

	public int getI() {
		return I;
	}

	public void setI(int i) {
		I = i;
	}

	public static String getCompareType() {
		return compareType;
	}

	public static void setCompareType(String compareType) {
		ColorItem.compareType = compareType;
	}  
    
} 