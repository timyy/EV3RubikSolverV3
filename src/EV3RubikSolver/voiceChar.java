/**
 * 
 */
package EV3RubikSolver;

/**
 * 
 * ������λ�У�����ʮ���٣�ǧ����ʮ�򣬰���ǧ���ڣ�ʮ�ڣ����ڣ�ǧ�ڣ����ڣ�ʮ���ڣ������ڣ�ǧ���ڣ����ڡ���
 * ������λ�У�����ʮ���٣�ǧ����ʮ�򣬰���ǧ���ڣ�ʮ�ڣ����ڣ�ǧ�ڣ����ڣ�ʮ���ڣ������ڣ�ǧ���ڣ����ڡ���
 *	2
 *
 *	���ס��ڴ�½Ҳ��ʹ�ã��������岻һ�����������ڡ����ڡ��������ǡ��ס����й���λ��ʵ����Ӣ������λ�Ȳ����еģ�10^3,10^6,10^9,10^12...�������ĳ��ˡ�ǧ�����⣬�����Ļ�������λ�ǳ˷��Ĺ�ϵ��ʮ����ʮ�ǰ٣��ٳ��԰���������������ڡ������ڡ����Ҳ���һ���ض�����λ�����ԡ��ס��ǣ����ڳ����ھ����ס��������Ļ�������λ��10,10^2,10^4,10^8,10^16...
 *	3
 *
 *	Ȼ���ǡ�ʮ���͡�һʮ�������⡣10��19�Լ�����Щ���ֿ�ͷ�Ķ�λ�����ԡ�ʮ����ͷ����ʮ�壬ʮ��ʮ�ڵȡ���λ�����ϣ��������в����֣����á�һʮ��������һ��һʮ��һǧ��һʮ��һ����һʮ�ȡ�
//	4
//
//	�������͡����������⡣���ڣ�������ǧ�����٣������ԣ�����20ֻ���Ƕ�ʮ��200�ö���Ҳ���á�22,2222,2222�ǡ���ʮ������ǧ���ٶ�ʮ������ǧ���ٶ�ʮ������
//	5
//
//	���ڡ��㡹�͡����������⣬������һ���á��㡹��ֻ��ҳ�롢����ȱ�������Ŀ�λ�����á���������λ�м����۶��ٸ�0��������һ�����㡹��2014�ǡ���ǧ��һʮ�ġ���20014�ǡ���ʮ����һʮ�ġ���201400�ǡ���ʮ����һǧ�İ١���
 *
 * @author TIMYY
 *
 */
public class voiceChar {
	/**
	 * 
	 */
	private int[] mNumbers = {0,1,2,3,4,5,6,7,8,9,		//����
			10,11,12,13, 14,15,16,17, 18,19,20,21, 22,23,24,25}; // ��λ���ֵ�
	
	/**
	 * ���ģ�Ҳ������Ӣ�ĵġ�
	 * ���Ķ����ֺ�Ӣ�Ĳ�ͬ��һ������� 1��һ����ǧ�� ten thousand��
	 */
	private String[] mChnCapitalTexts = {"��","Ҽ","��","��","��","��","½","��","��","��",
			"ʰ","��","Ǫ","��","ʰ","��","Ǫ","��","ʰ","��","Ǫ","��","ʰ","��","Ǫ","��"};
	private String[] mChnTexts ={"��","һ","��","��","��","��","��","��","��","��",
			"ʮ","��","ǧ","��","ʮ","��","ǧ","��","ʮ","��","ǧ","��","ʮ","��","ǧ","��"};
	private String[] mChnPhoneticizeTexts ={"Ling","Yi","Er","San","Si","Wu","Liu","Qi","Ba","Jiu",
			"Shi","Bai","Qian","Wan","Shi","Bai","Qian","YI","Shi","Bai","Qian","Wan","Shi","Bai","Qian","Zhao",};
	private String[] mChnVoices = {"0.wav","1.wav","2.wav","3.wav","4.wav","5.wav","6.wav","7.wav","8.wav","9.wav",
			"10.wav","bai.wav","qian.wav","wan.wav","10.wav","bai.wav","qian.wav","yi.wav",
			"10.wav","bai.wav","qian.wav","wan.wav","10.wav","bai.wav","qian.wav","zhao.wav"};
	private int mIndex  =0;

	
	public voiceChar(int number) {
		// TODO Auto-generated constructor stub
		for (int i=0; i<mNumbers.length;i++) {			
			if(mNumbers[i] == number) mIndex=i;
		}
	}
	public String Text(){
		return mChnTexts[mIndex];
	}
	public String CapitalText(){
		return mChnCapitalTexts[mIndex];
	}
	public String PhoneticizeText(){
		return mChnPhoneticizeTexts[mIndex];
	}
	public String Voice(){
		return mChnVoices[mIndex];
	}
}
