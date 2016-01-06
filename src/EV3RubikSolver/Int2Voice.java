/**
 * 
 */
package EV3RubikSolver;

import java.io.File;
import java.util.ArrayList;

import lejos.hardware.Sound;

/**
 * @author TIMYY
 *
 */
public class Int2Voice {

	/**
	 * 
	 */
	public ArrayList<voiceChar> voice;
	public Int2Voice() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 	处理字串
	 * @param text
	 */
	public ArrayList<voiceChar> parse(String number){
		this.voice = new ArrayList<voiceChar>();
		if (null == number || "" == number) return this.voice;
		if(!isNumeric(number)) return this.voice;
		
		int n = Integer.parseInt(number);
		return this.parse(n);
	}
	/**
	 * 处理数字发音
	 * @param number
	 * @return
	 */
	public ArrayList<voiceChar> parse(int number){
		// voice = new ArrayList(<voiceChar>);
		this.voice = new ArrayList<voiceChar>();
		
		int trimNumber;
		int unit = 0;
		int tmpNumber = number;
		int nextNumber = 0;
		int num;

		if(0 == number ) {
			// 边界条件，如果刚好就一个零。
			this.voice.add(new voiceChar(number));	
			return this.voice;
		}
		do {
			num = tmpNumber  % 10; // 求余数
			trimNumber = tmpNumber / 10; // 缩一位

			switch(unit) {
			case 8:		// 亿 可以在到亿的候,强行把万干掉
				if(this.voice.get(this.voice.size()-1).CapitalText().equals("万"))
					this.voice.remove(this.voice.size()-1);
				break;
			case 16:	// 兆 可以在到兆的候,强行把亿干掉,如果连着零，会出兆亿万
				if(this.voice.get(this.voice.size()-1).CapitalText().equals("亿"))
					this.voice.remove(this.voice.size()-1);
				break;
			}			
			if( 0 == num ) {
				// 零特殊，N个连续零只读一次。如：5001,读 五千零一；
				// 最后连续N个零不读。如：500， 读 五百。

				if(0 == unit % 4 &&  unit >0 ) {
						this.voice.add(new voiceChar(9+unit)); // 万位，即使为0也要把万位符表达出来,如伍百万。
						// 有BUG， 亿会后面跟着一个万。 5亿万。挨个判断不够优雅。还会出现兆万亿万。万亿还好了。兆亿又有问题。
						// 可以在到亿的候可以强行把万干掉。不能先判断，如果前面有数就没问题，出问题只在一堆零的时候。
						// 到兆的时候把后面的万亿万干掉。
				}
				else
					if(0 != nextNumber) this.voice.add(new voiceChar(num));	// 不是万位，下一位不是0，加零
				
				
			}
			else {
				if(unit > 0) this.voice.add(new voiceChar(9+unit));	// 这是单位，个，十，百，千，万
				this.voice.add(new voiceChar(num));					// 数字
																		// 0会特殊，前面处理。1有时也特殊，如12，读为十二，而不是一十二。
			}		

			unit++;
			tmpNumber = trimNumber;
			nextNumber = num;
		
		}while(trimNumber != 0);
		
//		
//		 var _change = {
//		           ary0:["零", "一", "二", "三", "四", "五", "六", "七", "八", "九"],
//		           ary1:["", "十", "百", "千"],
//		           ary2:["", "万", "亿", "兆"],
//		           init:function (name) {
//		               this.name = name;
//		           },
//		           strrev:function () {
//		               var ary = []
//		               for (var i = this.name.length; i >= 0; i--) {
//		                   ary.push(this.name[i])
//		               }
//		               return ary.join("");
//		           }, //倒转字符串。
//		           pri_ary:function () {
//		               var $this = this
//		               var ary = this.strrev();
//		               var zero = ""
//		               var newary = ""
//		               var i4 = -1
//		               for (var i = 0; i < ary.length; i++) {
//		                   if (i % 4 == 0) { //首先判断万级单位，每隔四个字符就让万级单位数组索引号递增
//		                       i4++;
//		                       newary = this.ary2[i4] + newary; //将万级单位存入该字符的读法中去，它肯定是放在当前字符读法的末尾，所以首先将它叠加入$r中，
//		                       zero = ""; //在万级单位位置的“0”肯定是不用的读的，所以设置零的读法为空
//		 
//		                   }
//		                   //关于0的处理与判断。
//		                   if (ary[i] == '0') { //如果读出的字符是“0”，执行如下判断这个“0”是否读作“零”
//		                       switch (i % 4) {
//		                           case 0:
//		                               break;
//		                           //如果位置索引能被4整除，表示它所处位置是万级单位位置，这个位置的0的读法在前面就已经设置好了，所以这里直接跳过
//		                           case 1:
//		                           case 2:
//		                           case 3:
//		                               if (ary[i - 1] != '0') {
//		                                   zero = "零"
//		                               }
//		                               ; //如果不被4整除，那么都执行这段判断代码：如果它的下一位数字（针对当前字符串来说是上一个字符，因为之前执行了反转）也是0，那么跳过，否则读作“零”
//		                               break;
//		 
//		                       }
//		 
//		                       newary = zero + newary;
//		                       zero = '';
//		                   }
//		                   else { //如果不是“0”
//		                       newary = this.ary0[parseInt(ary[i])] + this.ary1[i % 4] + newary; //就将该当字符转换成数值型,并作为数组ary0的索引号,以得到与之对应的中文读法，其后再跟上它的的一级单位（空、十、百还是千）最后再加上前面已存入的读法内容。
//		                   }
//		 
//		               }
//		               if (newary.indexOf("零") == 0) {
//		                   newary = newary.substr(1)
//		               }//处理前面的0
//		               return newary;
//		           }
//		       }		
		return this.voice;
	}
	
	public void test(){
		int[] testNumber = {1234567890, 
				0,1,2,3,4,5,6,7,8,9,
				10,100,1000,10000,100000,1000000,10000000,100000000,1000000000,
				11,201,3001,40001,5001001,60001000};
		for(int i=0; i<testNumber.length; i++ ) {
			ArrayList<voiceChar> result =  this.parse(testNumber[i]);
			System.out.print(testNumber[i] + ":");
			for(int j= result.size()-1;j>=0; j--) {
				System.out.print(result.get(j).PhoneticizeText());
			}
			System.out.println();
		}
	}
	public void testVoice(){
		int[] testNumber = {1234567890, 
				0,1,2,3,4,5,6,7,8,9,
				10,100,1000,10000,100000,1000000,10000000,100000000,1000000000,
				11,201,3001,40001,5001001,60001000};
		Sound.setVolume(100);
		for(int i=0; i<testNumber.length; i++ ) {
			ArrayList<voiceChar> result =  this.parse(testNumber[i]);
			System.out.print(testNumber[i] + ":");
			for(int j= result.size()-1;j>=0; j--) {
				System.out.print(result.get(j).PhoneticizeText());
				Sound.playSample(new File(result.get(j).Voice()));
			}
			System.out.println();
		}
	}
	private boolean isNumeric(String str){
		for (int i = 0; i < str.length(); i++){
		   if (!Character.isDigit(str.charAt(i))){
		    return false;
		   }
		}
		return true;
		}

}
