package com.jsun.jambo;
import java.util.*;

import model.ProcessControlBlock;
import model.ProcessStatus;

import org.omg.CORBA.INTF_REPOS;

import util.ProcessQueue;
import util.ProcessQueue;
import util.SchedulingAlgorithm;
/**
 * @author JAMBO
 *
 */
/**
 * @author JAMBO
 *
 */
public class ProcessSimulation_main {

	static int algorithmChose;//当前选择的算法
	static String [] algorithmStrings = {"先来先服务调度算法","时间片轮转调度算法","优先级调度算法","高响应比优先调度算法","多级反馈队列调度算法"};
	static int processNum;
	static int [] processArriveTimeInts; //记录输入进程的到达时间
	static int [] processServiceTimeInts;//记录输入进程的需要服务时间
	static Scanner scanner;
	public static int cpuRunTime;//cpu运行时间
	static ProcessQueue pQueue = ProcessQueue.getInstance();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		initOut();
		createPCB();
		setAlgorithm(algorithmChose);
		showMenu();
		//todo 已完成FCFS算法 需要重复调用显示菜单，输入CPU时间，重新选择算法，添加进程等。
	}
	
	public static void printCurrentPCBInfo(){
		//输出当前所有PCB信息
		System.out.println("--当前所有PCB信息:");
		for (int i = 0; i < pQueue.pcbAllInfoList.size(); i++) {
			System.out.println(pQueue.pcbAllInfoList.get(i).toString());
		}
		//输出当前运行队列PCB信息
		System.out.println("--当前运行队列PCB信息:");
		for (int i = 0; i < pQueue.pcbRunQueue.size(); i++) {
			System.out.println(pQueue.pcbRunQueue.get(i).toString());
		}
		//输出当前就绪队列PCB信息
		System.out.println("--当前就绪队列PCB信息:");
		for (int i = 0; i < pQueue.pcbWaitQueue.size(); i++) {
			System.out.println(pQueue.pcbWaitQueue.get(i).toString());
		}
		//输出当前阻塞队列PCB信息
		System.out.println("--当前阻塞队列PCB信息:");
		for (int i = 0; i < pQueue.pcbBlockingQueue.size(); i++) {
			System.out.println(pQueue.pcbBlockingQueue.get(i).toString());
		}
	}
	
	public static void showMenu(){
		System.out.println("-1.选择调度算法:");
		System.out.println("-2.输入CPU运行时间:");
		System.out.println("-3.开始调度");
		System.out.println("-4.IO事件:");
		int choose = scanner.nextInt();
		switch (choose) {
		case 1:
			System.out.println("-请选择调度算法:");
			System.out.println(" 1.先来先服务调度算法");
			System.out.println(" 2.时间片轮转调度算法");
			System.out.println(" 3.优先级调度算法");
			System.out.println(" 4.高响应比优先调度算法");
			System.out.println(" 5.多级反馈队列调度算法");
			algorithmChose = scanner.nextInt();
			System.out.println("您选择的调度算法是" + algorithmStrings[algorithmChose-1]);
			showMenu();
			break;
		case 2:
			System.out.println("请输入CPU运行时间");
			cpuRunTime = scanner.nextInt();
			System.out.println("您输入的CPU可运行时间是"+cpuRunTime);
			showMenu();
			break;
		case 3:
			setAlgorithm(algorithmChose);
			System.out.println("已开始调度");
			break;
		case 4:
			System.out.println("");
			break;
		default:
			break;
		}
	}
	
	
	//调度对应算法
	public static void setAlgorithm(int algorithmChose){
		SchedulingAlgorithm schedulingAlgorithm = new SchedulingAlgorithm();
		switch (algorithmChose) {
			case 1:
				schedulingAlgorithm.FCFS(cpuRunTime);
				printCurrentPCBInfo();
				showMenu();
				break;
			case 2:
				schedulingAlgorithm.RR(cpuRunTime);
				printCurrentPCBInfo();
				showMenu();
				break;
			case 3:
				schedulingAlgorithm.HPF(cpuRunTime);
				printCurrentPCBInfo();
				showMenu();
				break;
			case 4:
				schedulingAlgorithm.HRRF(cpuRunTime);
				printCurrentPCBInfo();
				showMenu();
				break;
			case 5:
				schedulingAlgorithm.MFQ(cpuRunTime);
				printCurrentPCBInfo();
				showMenu();
				break;
			default:
				System.out.println("您的输入有误");
				showMenu();
				break;
		}
	}
	
	//初次创建PCB模块，均处于就绪状态，并添加至就绪队列
	public static void createPCB(){
		//生成优先级数
		int[] randomPriority = randomPriorityInt(1, 10, processNum);
		
		for (int i = 1; i < processNum+1; i++) {
			ProcessControlBlock PCB = new ProcessControlBlock();
			PCB.setProcessNameStr("进程" + i);//依次为对应进程设置进程名+下标;
			PCB.setProcessIdInt(1000 + i); //依次为对应进程设置一个1000+其下标作为PID;
			PCB.setProcessPriorityInt(randomPriority[i-1]); //暂时将所有优先级设为1，应该为随机生成
			PCB.setProcessArriveTimeInt(processArriveTimeInts[i-1]);//依次为对应进程设置对应的到达时间
			PCB.setProcessRunTimeInt(processServiceTimeInts[i-1]);//依次为对应进程设置对应的服务时间
			PCB.setProcessUsedCPUTimeInt(0);//已用CPU时间均为0
			PCB.setProcessCycleTimeInt(0);//未运行完成周转时间为0
			PCB.setProcessStatusStr(ProcessStatus.wait);//将状态初始化为就绪
			PCB.setProcessBlockingReasonStr(null); //未阻塞
			pQueue.pcbWaitQueue.add(PCB); //添加至就需队列
			pQueue.pcbAllInfoList.add(PCB);//添加至所有队列
		}
	}
	
	
	public static void initOut(){
		System.out.println("--------进程状态模拟转换程序-----------");
		System.out.println("--------初始化进程");
		scanner = new Scanner(System.in);
		System.out.println("-请输入进程个数:");
		processNum = scanner.nextInt();
		processArriveTimeInts = new int[processNum];
		processServiceTimeInts = new int[processNum];
		System.out.println("-你输入的进程个数是" + processNum);
		System.out.println("-请依次输入进程到达的时间");
		for(int a = 1; a<processNum+1;a++){
			processArriveTimeInts[a-1] = scanner.nextInt();
		}
		System.out.println("-请依次输入进程所需服务时间:");
		for(int a = 1; a<processNum+1;a++){
			processServiceTimeInts[a-1] = scanner.nextInt();
		}
		System.out.println("-请选择调度算法:");
		System.out.println(" 1.先来先服务调度算法");
		System.out.println(" 2.时间片轮转调度算法");
		System.out.println(" 3.优先级调度算法");
		System.out.println(" 4.高响应比优先调度算法");
		System.out.println(" 5.多级反馈队列调度算法");
		algorithmChose = scanner.nextInt();
		System.out.println("您选择的调度算法是" + algorithmStrings[algorithmChose-1]);
		System.out.println("进程名    " + "到达时间    " +"所需服务时间");
		for(int a = 1; a<processNum+1;a++ ){
			System.out.println("进程" + a +  "    " +processArriveTimeInts[a-1] + "  " +"     " + processServiceTimeInts[a-1] );
		}
		System.out.println("请输入CPU可运行时间");
		cpuRunTime = scanner.nextInt();
		System.out.println("您输入的CPU可运行时间是"+cpuRunTime);
		System.out.println("已开始调度");
	}
	
	
	public static int[] randomPriorityInt(int min,int max,int n){
		int len = max-min+1;  
	      
	    if(max < min || n > len){  
	        return null;  
	    }  
	      
	    //初始化给定范围的待选数组  
	    int[] source = new int[len];  
	       for (int i = min; i < min+len; i++){  
	        source[i-min] = i;  
	       }  
	         
	       int[] result = new int[n];  
	       Random rd = new Random();  
	       int index = 0;  
	       for (int i = 0; i < result.length; i++) {  
	        //待选数组0到(len-2)随机一个下标  
	           index = Math.abs(rd.nextInt() % len--);  
	           //将随机到的数放入结果集  
	           result[i] = source[index];  
	           //将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换  
	           source[index] = source[len];  
	       }  
	       return result;  
	}
}
