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

	//@1-系统计时
	private double system_tick = 0;
	//@2-1秒计时
	private double second_1s_tick = 0;
	//@3-主页面显示定时值
	private int    display_tick_count = 0;
	//@3-主页面曲线定时值
	private int    display_curve_count = 0;


    //@5-系统时间获取接口
	private static Calendar local_time;
	//@6-显示数据格式
	public static java.text.NumberFormat  formater_value  =  java.text.DecimalFormat.getInstance();  //显示小数格式化


	public static String Time_Str = new String("----");


	/*------使用非FX原生task方法-----------*/
	public static Task<Integer> task_dis;
    private Thread Display_Thread;

    private int tick_count=0;

    public static boolean flash_flag=false;


    private List<Integer> list;

    public static int[] Lottery_ID = new int[10];





	/**构建时间定时器
	 *
	 * @param delayTime1
	 */
	public Servo_DisplayTimer(int delayTime){

		//@1-数据精度格式
		formater_value.setMaximumIntegerDigits(2);
		formater_value.setMinimumIntegerDigits(2);

		tick_count=1000/delayTime;

	    /*-------------使用FX原生task方法---------------------*/
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
	    Display_Thread.setPriority(Thread.NORM_PRIORITY);    //设置优先级别8
	    Display_Thread.start();

	}


	/**显示刷新
	 *
	 */
	private void data_put()
	{
	    //@1-系统计时累加
		system_tick=system_tick+1;
		//@2-1秒计时累加
		second_1s_tick=second_1s_tick+1;

		if(ScreensFramework.Main_Falg==true)
		{
	    	//@1-程序运行在主页面
	    	if(ScreensFramework.App_Page==1)
	    	{
	    		//@-主页面显示定时器
	    		display_tick_count = display_tick_count + 1;

	    		//@-产生随机数
	            list = Servo_DisplayTimer.generate();
	            for (int i = 0; i < list.size(); ++i) {
	                Lottery_ID[i] = list.get(i);
	            }

	    		//@-每秒10次
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

//		//@4-1秒计时
//		if(second_1s_tick==tick_count)   //1s
//		{
//			//@-1秒定时复位
//			second_1s_tick=0;
//
//			//@6-刷新时间
//	    	local_time = Calendar.getInstance();
//
//			//@-系统时间
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

