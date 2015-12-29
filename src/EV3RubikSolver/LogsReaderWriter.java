package EV3RubikSolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/**
 * ����һ������־��д�йص��࣬������һЩͨ�õķ���
 * @author Timyy
 *
 */
public class LogsReaderWriter {
     
    /**
     * 
     * @param filePath      �ļ�·�����ַ�����ʾ��ʽ
     * @param KeyWords      ���Ұ���ĳ���ؼ��ֵ���Ϣ����nullΪ���ؼ��ֲ�ѯ��nullΪȫ����ʾ
     * @return      ���ļ�����ʱ�������ַ��������ļ�������ʱ������null
     */
    public static String readFromFile(String filePath, String KeyWords){
        StringBuffer stringBuffer = null;
        File file = new File(filePath);
        if(file.exists()){
            stringBuffer = new StringBuffer();
            FileReader fileReader = null;
            BufferedReader bufferedReader = null;
            String temp = "";
            try {
                fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
                while((temp = bufferedReader.readLine()) != null){
                    if(KeyWords ==null){
                        stringBuffer.append(temp + "\n");
                    }else{
                        if(temp.contains(KeyWords)){
                            stringBuffer.append(temp + "\n");
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                //e.printStackTrace();
            } catch (IOException e) {
                //e.printStackTrace();
            }finally{
                try {
                    fileReader.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
        }
        if(stringBuffer == null){
            return null;
        }else{
            return stringBuffer.toString();
        }
         
         
    }
     
    /**
     * ��ָ���ַ���д���ļ�������������ļ�·�������ڣ����½��ļ���д�롣
     * @param log       Ҫд���ļ����ַ���
     * @param filePath      �ļ�·�����ַ�����ʾ��ʽ��Ŀ¼�Ĳ�ηָ������ǡ�/��Ҳ�����ǡ�\\��
     * @param isAppend      true��׷�ӵ��ļ���ĩβ��false���Ը���ԭ�ļ��ķ�ʽд��
     */        
      
    public static boolean writeIntoFile(String log, String filePath, boolean isAppend){
        boolean isSuccess = true;
        //������"\\"תΪ"/",û���򲻲����κα仯
        String filePathTurn = filePath.replaceAll("\\\\", "/");
        //�ȹ��˵��ļ���
        int index = filePath.lastIndexOf("/");
        String dir = filePath.substring(0, index);
        //�������ļ���·��
        File fileDir = new File(dir);
        fileDir.mkdirs();
        //�ٴ���·���µ��ļ�
        File file = null;
        try {
            file = new File(filePath);
            file.createNewFile();
        } catch (IOException e) {
            isSuccess = false;
            //e.printStackTrace();
        }
        //��logsд���ļ�
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file, isAppend);
            fileWriter.write(log);
            fileWriter.flush();
        } catch (IOException e) {
            isSuccess = false;
            //e.printStackTrace();
        } finally{
            try {
                fileWriter.close();
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
         
        return isSuccess;
    }
    /**
     * �����ļ���������ļ��Ѵ��ڽ����ٴ������������κ����ã�
     * @param filePath       Ҫ�����ļ���·�����ַ�����ʾ��ʽ��Ŀ¼�Ĳ�ηָ������ǡ�/��Ҳ�����ǡ�\\��
     * @return      �����ɹ�������true���������ɹ��򷵻�false
     */
    public static boolean createNewFile(String filePath){
        boolean isSuccess = true;
        //������"\\"תΪ"/",û���򲻲����κα仯
        String filePathTurn = filePath.replaceAll("\\\\", "/");
        //�ȹ��˵��ļ���
        int index = filePathTurn.lastIndexOf("/");
        String dir = filePathTurn.substring(0, index);
        //�ٴ����ļ���
        File fileDir = new File(dir);
        isSuccess = fileDir.mkdirs();
        //�����ļ�
        File file = new File(filePathTurn);
        try {
            isSuccess = file.createNewFile();
        } catch (IOException e) {
            isSuccess = false;
            //e.printStackTrace();
        }
 
        return isSuccess;
    }
}