/**
 * 
 */
package EV3RubikSolver;

/**
 * ��Robot������ʾ�Ĳ��裬pbs
 * @author TIMYY
 *
 */
public class MoveStep {

	public MoveType moveType;
	public int step;
	
	
	public MoveStep(MoveType oMoveType, int oneStep) {
		// TODO Auto-generated constructor stub
		this.moveType = oMoveType;
		this.step = oneStep;		
	}
}

