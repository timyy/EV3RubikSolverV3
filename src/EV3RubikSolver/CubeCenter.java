/**
 * 
 */
package EV3RubikSolver;

/**
 * @author TIMYY
 *
 */
public class CubeCenter
{
    public String[] CenterColor = { "U", "R", "D", "L", "F", "B" };

    public void RotateBottom(boolean colockwise)
    {
        if (colockwise)
        {
            String n = CenterColor[5];
            CenterColor[5] = CenterColor[1];
            CenterColor[1] = CenterColor[4];
            CenterColor[4] = CenterColor[3];
            CenterColor[3] = n;
        }
        else
        {
            String n = CenterColor[5];
            CenterColor[5] = CenterColor[3];
            CenterColor[3] = CenterColor[4];
            CenterColor[4] = CenterColor[1];
            CenterColor[1] = n;
        }
    }

    public void RotatePaw()
    {
        //Only can move forward
        String n = CenterColor[0];
        CenterColor[0] = CenterColor[3];
        CenterColor[3] = CenterColor[2];
        CenterColor[2] = CenterColor[1];
        CenterColor[1] = n;
    }

    public int FindCenter(String position)
    {
        int center = -1;
        for (int i = 0; i < 6; i++)
        {
            if (CenterColor[i] == position) center = i;
        }
        return center;
    }
}