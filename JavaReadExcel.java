package Lottery;

import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class JavaReadExcel {

    public static void main(String[] args) {
        try {
            String fileName = "D:\\Name.xls"; // Excel�ļ�����·��

            File file = new File(fileName); // �����ļ�����
            Workbook wb = Workbook.getWorkbook(file); // ���ļ����л�ȡExcel����������WorkBook��
            Sheet sheet = wb.getSheet(0); // �ӹ�������ȡ��ҳ��Sheet��

            for (int i = 0; i < sheet.getRows(); i++) { // ѭ����ӡExcel���е�����

                for (int j = 0; j < sheet.getColumns(); j++) {
                    Cell cell = sheet.getCell(j, i);
                    System.out.println(cell.getContents());
                }
                //System.out.println();
            }
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
