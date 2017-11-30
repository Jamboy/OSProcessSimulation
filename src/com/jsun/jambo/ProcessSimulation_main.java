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

	static int algorithmChose;//��ǰѡ����㷨
	static String [] algorithmStrings = {"�����ȷ�������㷨","ʱ��Ƭ��ת�����㷨","���ȼ������㷨","����Ӧ�����ȵ����㷨","�༶�������е����㷨"};
	static int processNum;
	static int [] processArriveTimeInts; //��¼������̵ĵ���ʱ��
	static int [] processServiceTimeInts;//��¼������̵���Ҫ����ʱ��
	static Scanner scanner;
	public static int cpuRunTime;//cpu����ʱ��
	static ProcessQueue pQueue = ProcessQueue.getInstance();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		initOut();
		createPCB();
		setAlgorithm(algorithmChose);
		showMenu();
		//todo �����FCFS�㷨 ��Ҫ�ظ�������ʾ�˵�������CPUʱ�䣬����ѡ���㷨����ӽ��̵ȡ�
	}
	
	public static void printCurrentPCBInfo(){
		//�����ǰ����PCB��Ϣ
		System.out.println("--��ǰ����PCB��Ϣ:");
		for (int i = 0; i < pQueue.pcbAllInfoList.size(); i++) {
			System.out.println(pQueue.pcbAllInfoList.get(i).toString());
		}
		//�����ǰ���ж���PCB��Ϣ
		System.out.println("--��ǰ���ж���PCB��Ϣ:");
		for (int i = 0; i < pQueue.pcbRunQueue.size(); i++) {
			System.out.println(pQueue.pcbRunQueue.get(i).toString());
		}
		//�����ǰ��������PCB��Ϣ
		System.out.println("--��ǰ��������PCB��Ϣ:");
		for (int i = 0; i < pQueue.pcbWaitQueue.size(); i++) {
			System.out.println(pQueue.pcbWaitQueue.get(i).toString());
		}
		//�����ǰ��������PCB��Ϣ
		System.out.println("--��ǰ��������PCB��Ϣ:");
		for (int i = 0; i < pQueue.pcbBlockingQueue.size(); i++) {
			System.out.println(pQueue.pcbBlockingQueue.get(i).toString());
		}
	}
	
	public static void showMenu(){
		System.out.println("-1.ѡ������㷨:");
		System.out.println("-2.����CPU����ʱ��:");
		System.out.println("-3.��ʼ����");
		System.out.println("-4.IO�¼�:");
		int choose = scanner.nextInt();
		switch (choose) {
		case 1:
			System.out.println("-��ѡ������㷨:");
			System.out.println(" 1.�����ȷ�������㷨");
			System.out.println(" 2.ʱ��Ƭ��ת�����㷨");
			System.out.println(" 3.���ȼ������㷨");
			System.out.println(" 4.����Ӧ�����ȵ����㷨");
			System.out.println(" 5.�༶�������е����㷨");
			algorithmChose = scanner.nextInt();
			System.out.println("��ѡ��ĵ����㷨��" + algorithmStrings[algorithmChose-1]);
			showMenu();
			break;
		case 2:
			System.out.println("������CPU����ʱ��");
			cpuRunTime = scanner.nextInt();
			System.out.println("�������CPU������ʱ����"+cpuRunTime);
			showMenu();
			break;
		case 3:
			setAlgorithm(algorithmChose);
			System.out.println("�ѿ�ʼ����");
			break;
		case 4:
			System.out.println("");
			break;
		default:
			break;
		}
	}
	
	
	//���ȶ�Ӧ�㷨
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
				System.out.println("������������");
				showMenu();
				break;
		}
	}
	
	//���δ���PCBģ�飬�����ھ���״̬�����������������
	public static void createPCB(){
		//�������ȼ���
		int[] randomPriority = randomPriorityInt(1, 10, processNum);
		
		for (int i = 1; i < processNum+1; i++) {
			ProcessControlBlock PCB = new ProcessControlBlock();
			PCB.setProcessNameStr("����" + i);//����Ϊ��Ӧ�������ý�����+�±�;
			PCB.setProcessIdInt(1000 + i); //����Ϊ��Ӧ��������һ��1000+���±���ΪPID;
			PCB.setProcessPriorityInt(randomPriority[i-1]); //��ʱ���������ȼ���Ϊ1��Ӧ��Ϊ�������
			PCB.setProcessArriveTimeInt(processArriveTimeInts[i-1]);//����Ϊ��Ӧ�������ö�Ӧ�ĵ���ʱ��
			PCB.setProcessRunTimeInt(processServiceTimeInts[i-1]);//����Ϊ��Ӧ�������ö�Ӧ�ķ���ʱ��
			PCB.setProcessUsedCPUTimeInt(0);//����CPUʱ���Ϊ0
			PCB.setProcessCycleTimeInt(0);//δ���������תʱ��Ϊ0
			PCB.setProcessStatusStr(ProcessStatus.wait);//��״̬��ʼ��Ϊ����
			PCB.setProcessBlockingReasonStr(null); //δ����
			pQueue.pcbWaitQueue.add(PCB); //������������
			pQueue.pcbAllInfoList.add(PCB);//��������ж���
		}
	}
	
	
	public static void initOut(){
		System.out.println("--------����״̬ģ��ת������-----------");
		System.out.println("--------��ʼ������");
		scanner = new Scanner(System.in);
		System.out.println("-��������̸���:");
		processNum = scanner.nextInt();
		processArriveTimeInts = new int[processNum];
		processServiceTimeInts = new int[processNum];
		System.out.println("-������Ľ��̸�����" + processNum);
		System.out.println("-������������̵����ʱ��");
		for(int a = 1; a<processNum+1;a++){
			processArriveTimeInts[a-1] = scanner.nextInt();
		}
		System.out.println("-��������������������ʱ��:");
		for(int a = 1; a<processNum+1;a++){
			processServiceTimeInts[a-1] = scanner.nextInt();
		}
		System.out.println("-��ѡ������㷨:");
		System.out.println(" 1.�����ȷ�������㷨");
		System.out.println(" 2.ʱ��Ƭ��ת�����㷨");
		System.out.println(" 3.���ȼ������㷨");
		System.out.println(" 4.����Ӧ�����ȵ����㷨");
		System.out.println(" 5.�༶�������е����㷨");
		algorithmChose = scanner.nextInt();
		System.out.println("��ѡ��ĵ����㷨��" + algorithmStrings[algorithmChose-1]);
		System.out.println("������    " + "����ʱ��    " +"�������ʱ��");
		for(int a = 1; a<processNum+1;a++ ){
			System.out.println("����" + a +  "    " +processArriveTimeInts[a-1] + "  " +"     " + processServiceTimeInts[a-1] );
		}
		System.out.println("������CPU������ʱ��");
		cpuRunTime = scanner.nextInt();
		System.out.println("�������CPU������ʱ����"+cpuRunTime);
		System.out.println("�ѿ�ʼ����");
	}
	
	
	public static int[] randomPriorityInt(int min,int max,int n){
		int len = max-min+1;  
	      
	    if(max < min || n > len){  
	        return null;  
	    }  
	      
	    //��ʼ��������Χ�Ĵ�ѡ����  
	    int[] source = new int[len];  
	       for (int i = min; i < min+len; i++){  
	        source[i-min] = i;  
	       }  
	         
	       int[] result = new int[n];  
	       Random rd = new Random();  
	       int index = 0;  
	       for (int i = 0; i < result.length; i++) {  
	        //��ѡ����0��(len-2)���һ���±�  
	           index = Math.abs(rd.nextInt() % len--);  
	           //�������������������  
	           result[i] = source[index];  
	           //����ѡ�����б�������������ô�ѡ����(len-1)�±��Ӧ�����滻  
	           source[index] = source[len];  
	       }  
	       return result;  
	}
}
