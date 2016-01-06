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
	 * 	�����ִ�
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
	 * �������ַ���
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
			// �߽�����������պþ�һ���㡣
			this.voice.add(new voiceChar(number));	
			return this.voice;
		}
		do {
			num = tmpNumber  % 10; // ������
			trimNumber = tmpNumber / 10; // ��һλ

			switch(unit) {
			case 8:		// �� �����ڵ��ڵĺ�,ǿ�а���ɵ�
				if(this.voice.get(this.voice.size()-1).CapitalText().equals("��"))
					this.voice.remove(this.voice.size()-1);
				break;
			case 16:	// �� �����ڵ��׵ĺ�,ǿ�а��ڸɵ�,��������㣬���������
				if(this.voice.get(this.voice.size()-1).CapitalText().equals("��"))
					this.voice.remove(this.voice.size()-1);
				break;
			}			
			if( 0 == num ) {
				// �����⣬N��������ֻ��һ�Ρ��磺5001,�� ��ǧ��һ��
				// �������N���㲻�����磺500�� �� ��١�

				if(0 == unit % 4 &&  unit >0 ) {
						this.voice.add(new voiceChar(9+unit)); // ��λ����ʹΪ0ҲҪ����λ��������,�������
						// ��BUG�� �ڻ�������һ���� 5���򡣰����жϲ������š�������������������ڻ����ˡ������������⡣
						// �����ڵ��ڵĺ����ǿ�а���ɵ����������жϣ����ǰ��������û���⣬������ֻ��һ�����ʱ��
						// ���׵�ʱ��Ѻ����������ɵ���
				}
				else
					if(0 != nextNumber) this.voice.add(new voiceChar(num));	// ������λ����һλ����0������
				
				
			}
			else {
				if(unit > 0) this.voice.add(new voiceChar(9+unit));	// ���ǵ�λ������ʮ���٣�ǧ����
				this.voice.add(new voiceChar(num));					// ����
																		// 0�����⣬ǰ�洦��1��ʱҲ���⣬��12����Ϊʮ����������һʮ����
			}		

			unit++;
			tmpNumber = trimNumber;
			nextNumber = num;
		
		}while(trimNumber != 0);
		
//		
//		 var _change = {
//		           ary0:["��", "һ", "��", "��", "��", "��", "��", "��", "��", "��"],
//		           ary1:["", "ʮ", "��", "ǧ"],
//		           ary2:["", "��", "��", "��"],
//		           init:function (name) {
//		               this.name = name;
//		           },
//		           strrev:function () {
//		               var ary = []
//		               for (var i = this.name.length; i >= 0; i--) {
//		                   ary.push(this.name[i])
//		               }
//		               return ary.join("");
//		           }, //��ת�ַ�����
//		           pri_ary:function () {
//		               var $this = this
//		               var ary = this.strrev();
//		               var zero = ""
//		               var newary = ""
//		               var i4 = -1
//		               for (var i = 0; i < ary.length; i++) {
//		                   if (i % 4 == 0) { //�����ж��򼶵�λ��ÿ���ĸ��ַ������򼶵�λ���������ŵ���
//		                       i4++;
//		                       newary = this.ary2[i4] + newary; //���򼶵�λ������ַ��Ķ�����ȥ�����϶��Ƿ��ڵ�ǰ�ַ�������ĩβ���������Ƚ���������$r�У�
//		                       zero = ""; //���򼶵�λλ�õġ�0���϶��ǲ��õĶ��ģ�����������Ķ���Ϊ��
//		 
//		                   }
//		                   //����0�Ĵ������жϡ�
//		                   if (ary[i] == '0') { //����������ַ��ǡ�0����ִ�������ж������0���Ƿ�������㡱
//		                       switch (i % 4) {
//		                           case 0:
//		                               break;
//		                           //���λ�������ܱ�4��������ʾ������λ�����򼶵�λλ�ã����λ�õ�0�Ķ�����ǰ����Ѿ����ú��ˣ���������ֱ������
//		                           case 1:
//		                           case 2:
//		                           case 3:
//		                               if (ary[i - 1] != '0') {
//		                                   zero = "��"
//		                               }
//		                               ; //�������4��������ô��ִ������жϴ��룺���������һλ���֣���Ե�ǰ�ַ�����˵����һ���ַ�����Ϊ֮ǰִ���˷�ת��Ҳ��0����ô����������������㡱
//		                               break;
//		 
//		                       }
//		 
//		                       newary = zero + newary;
//		                       zero = '';
//		                   }
//		                   else { //������ǡ�0��
//		                       newary = this.ary0[parseInt(ary[i])] + this.ary1[i % 4] + newary; //�ͽ��õ��ַ�ת������ֵ��,����Ϊ����ary0��������,�Եõ���֮��Ӧ�����Ķ���������ٸ������ĵ�һ����λ���ա�ʮ���ٻ���ǧ������ټ���ǰ���Ѵ���Ķ������ݡ�
//		                   }
//		 
//		               }
//		               if (newary.indexOf("��") == 0) {
//		                   newary = newary.substr(1)
//		               }//����ǰ���0
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
