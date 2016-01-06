/**
 * 
 */
package EV3RubikSolver;

/**
 * 
 * 中文数位有：个，十，百，千，万，十万，百万，千万，亿，十亿，百亿，千亿，万亿，十万亿，百万亿，千万亿，亿亿……
 * 中文数位有：个，十，百，千，万，十万，百万，千万，亿，十亿，百亿，千亿，万亿，十万亿，百万亿，千万亿，亿亿……
 *	2
 *
 *	「兆」在大陆也有使用，不过含义不一，「百万、万亿、亿亿」都可能是「兆」。中国数位其实不像英语是三位等差排列的（10^3,10^6,10^9,10^12...），中文除了「千」以外，其他的基本的数位是乘方的关系，十乘以十是百，百乘以百是万，万乘以万是亿。「亿亿」我找不到一个特定的数位，暂以「兆」记，那亿乘以亿就是兆。所以中文基本的数位是10,10^2,10^4,10^8,10^16...
 *	3
 *
 *	然后是「十」和「一十」的问题。10至19以及以这些数字开头的多位数，以「十」开头，如十五，十万，十亿等。两位数以上，在数字中部出现，则用「一十几」，如一百一十，一千零一十，一万零一十等。
//	4
//
//	「二」和「两」的问题。两亿，两万，两千，两百，都可以，但是20只能是二十，200用二百也更好。22,2222,2222是「二十二亿两千二百二十二万两千二百二十二」。
//	5
//
//	关于「零」和「〇」的问题，数字中一律用「零」，只有页码、年代等编号中数的空位才能用「〇」。数位中间无论多少个0，都读成一个「零」。2014是「两千零一十四」，20014是「二十万零一十四」，201400是「二十万零一千四百」。
 *
 * @author TIMYY
 *
 */
public class voiceChar {
	/**
	 * 
	 */
	private int[] mNumbers = {0,1,2,3,4,5,6,7,8,9,		//数字
			10,11,12,13, 14,15,16,17, 18,19,20,21, 22,23,24,25}; // 单位，字典
	
	/**
	 * 中文，也可以有英文的。
	 * 中文读数字和英文不同，一个按万分 1万，一个按千分 ten thousand。
	 */
	private String[] mChnCapitalTexts = {"零","壹","贰","叁","肆","伍","陆","柒","捌","玖",
			"拾","佰","仟","万","拾","佰","仟","亿","拾","佰","仟","万","拾","佰","仟","兆"};
	private String[] mChnTexts ={"○","一","二","三","四","五","六","七","八","九",
			"十","百","千","万","十","百","千","亿","十","百","千","万","十","百","千","兆"};
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
