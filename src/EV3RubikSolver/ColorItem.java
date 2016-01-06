package EV3RubikSolver;

public class ColorItem   implements Comparable <ColorItem>
{  
	private static String compareType ="B";
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

		case "B":
			// �̣�����,R+g-2b ��С
			return -((this.R+this.G - 2* this.B) -(ci.R+ci.G-2*ci.B));
		case "R":
			// ��ɫ��G+B ��С��
			return -((this.G+this.B)-(ci.G+ci.B));
		
		case "G":
			// �̡� r-g+b ��С��
			return -((this.R - this.G+this.B) -(ci.R-ci.G+this.B));
		
		case "Y":
			// �ƣ��̣����� ����죺��
			return ((this.G-this.B)-(ci.G-ci.B));
		
		case "O":
			// ��ɫ��R-g-b ���
			return ((this.R-this.G-this.B)-(ci.R-ci.G-ci.B));
		case "W":
			// ��ɫ�� blue ��ǿ�� ��blue�������ġ�
			return (this.B-ci.B) ;
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