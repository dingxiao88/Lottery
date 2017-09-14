package Lottery;


import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.util.Duration;


/**
 *
 * @author Jack Ding
 * @date 2011-01-13
 */
public class Servo_DisplayTimer{

	//@1-ϵͳ��ʱ
	private double system_tick = 0;
	//@2-1���ʱ
	private double second_1s_tick = 0;
	//@3-��ҳ����ʾ��ʱֵ
	private int    display_tick_count = 0;
	//@3-��ҳ�����߶�ʱֵ
	private int    display_curve_count = 0;


    //@5-ϵͳʱ���ȡ�ӿ�
	private static Calendar local_time;
	//@6-��ʾ���ݸ�ʽ
	public static java.text.NumberFormat  formater_value  =  java.text.DecimalFormat.getInstance();  //��ʾС����ʽ��


	public static String Time_Str = new String("----");


	/*------ʹ�÷�FXԭ��task����-----------*/
	public static Task<Integer> task_dis;
    private Thread Display_Thread;

    private int tick_count=0;

    public static boolean flash_flag=false;


    private List<Integer> list;

    public static int[] Lottery_ID = new int[10];





	/**����ʱ�䶨ʱ��
	 *
	 * @param delayTime1
	 */
	public Servo_DisplayTimer(int delayTime){

		//@1-���ݾ��ȸ�ʽ
		formater_value.setMaximumIntegerDigits(2);
		formater_value.setMinimumIntegerDigits(2);

		tick_count=1000/delayTime;

	    /*-------------ʹ��FXԭ��task����---------------------*/
	    task_dis = new Task<Integer>() {
	        @Override protected Integer call() throws Exception {
	            int iterations;

	            while (true)
	            {
	            	iterations=1;

	                if (isCancelled()) {
	                    updateMessage("Cancelled");
	                    break;
	                }

	                data_put();

	                //Block the thread for a short time, but be sure
	                //to check the InterruptedException for cancellation
	                try {
	                    Thread.sleep(delayTime);
	                } catch (InterruptedException interrupted) {
	                    if (isCancelled()) {
	                        updateMessage("Cancelled");
	                        break;
	                    }
	                }
	            }
	            return iterations;
	        }
	    };
	    Display_Thread=new Thread(task_dis);
	    Display_Thread.setName("display");
	    Display_Thread.setDaemon(true);
	    Display_Thread.setPriority(Thread.NORM_PRIORITY);    //�������ȼ���8
	    Display_Thread.start();

	}


	/**��ʾˢ��
	 *
	 */
	private void data_put()
	{
	    //@1-ϵͳ��ʱ�ۼ�
		system_tick=system_tick+1;
		//@2-1���ʱ�ۼ�
		second_1s_tick=second_1s_tick+1;

		if(ScreensFramework.Main_Falg==true)
		{
	    	//@1-������������ҳ��
	    	if(ScreensFramework.App_Page==1)
	    	{
	    		//@-��ҳ����ʾ��ʱ��
	    		display_tick_count = display_tick_count + 1;

	    		//@-���������
	            list = Servo_DisplayTimer.generate();
	            for (int i = 0; i < list.size(); ++i) {
	                Lottery_ID[i] = list.get(i);
	            }

	    		//@-ÿ��10��
	    		if(display_tick_count == tick_count/20)
	    		{
	    			display_tick_count = 0;

					if(flash_flag == false)
					flash_flag = true;
					else if(flash_flag == true)
					flash_flag = false;

					MainController.DisplayProperty_Main.setValue(""+flash_flag);

//					System.out.println("tick");
	    		}

	    	}

		}

//		//@4-1���ʱ
//		if(second_1s_tick==tick_count)   //1s
//		{
//			//@-1�붨ʱ��λ
//			second_1s_tick=0;
//
//			//@6-ˢ��ʱ��
//	    	local_time = Calendar.getInstance();
//
//			//@-ϵͳʱ��
////	    	Time_Str = new String(""+local_time.get(Calendar.YEAR)+"/"
////					+formater_value.format(local_time.get(Calendar.MONTH)+1)+"/"
////					+formater_value.format(local_time.get(Calendar.DATE))+" "
////					+formater_value.format(local_time.get(Calendar.HOUR_OF_DAY))+":"
////					+formater_value.format(local_time.get(Calendar.MINUTE))+":"
////					+formater_value.format(local_time.get(Calendar.SECOND)));
//
//		}
	}


	/**
	 *
	 * @return
	 */
    public static List<Integer> generate() {

        Random generator = new Random();
        List<Integer> list = new ArrayList<Integer>();

        while (list.size() < 10) {
            // from 0 to 99 thus must added one
//            int next = generator.nextInt(99) + 1;
        	int next = generator.nextInt(30);
            if (!list.contains(next)) {
                list.add(next);
            }
        }

        // sorting
        Collections.sort(list);
        return list;
    }

}

