/**
 * 
 */
package EV3RubikSolver;

/**
 * 用Robot动作表示的步骤，pbs
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

