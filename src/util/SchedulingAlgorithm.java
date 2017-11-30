package util;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import model.ProcessControlBlock;
import model.ProcessStatus;

import com.jsun.jambo.ProcessSimulation_main;

public class SchedulingAlgorithm {
	static ProcessQueue pQueue = ProcessQueue.getInstance();
	static LinkedList<ProcessControlBlock> sortByArriveTimeList = new LinkedList<>();
	static LinkedList<ProcessControlBlock> sortByPriorityList = new LinkedList<>();
	boolean isAtOnceArrive = false;
	
	/*
	 * �����ȷ����㷨
	 * ���Ȱ�����ʱ������
	 * 
	 */
	public void FCFS(int cpuRunTime){
		System.out.println(cpuRunTime+"cpu");
		ProcessControlBlock [] arriveTimearray = new ProcessControlBlock[pQueue.pcbWaitQueue.size()];
		int cpuNeedTime = 0;
		int nowTime = 0; 
		int runPcbAllTime =0;
		//���䵽��ʱ��Ž�һ��������������
		for (int i = 0; i < arriveTimearray.length; i++) {
			arriveTimearray[i] = pQueue.pcbWaitQueue.get(i);
			cpuNeedTime+=pQueue.pcbWaitQueue.get(i).getProcessRunTimeInt();
//			System.out.println(arriveTimearray[i]);
		}
		sort(arriveTimearray,1); //�Խ��̰�����ʱ������
//		for (int i = 0; i < arriveTimearray.length; i++) {
//			System.out.println(sortByArriveTimeList.get(i).toString());
//		}
//		System.out.println(cpuRunTime + "cpuRunTime");
//		System.out.println(cpuNeedTime+"cpuNeedTime");
		for (int i = 0; i < sortByArriveTimeList.size(); i++) {
			//��cpu����ʱ���������ʱ����ֱ�Ӱ�˳��ִ�м���
			if (cpuRunTime > cpuNeedTime) {
				ProcessControlBlock runPCB =  sortByArriveTimeList.get(i);
//				if (i == 0) {
//					nowTime = runPCB.getProcessArriveTiemInt()+runPCB.getProcessRunTimeInt();
//					System.out.println("nowTime0" + nowTime);				
//				}
				int beginTime = sortByArriveTimeList.getFirst().getProcessArriveTiemInt();
//				System.out.println("nowTimebefor" + nowTime);
				runPcbAllTime+=runPCB.getProcessRunTimeInt();
				nowTime=beginTime+runPcbAllTime;
//				System.out.println("nowTime" + nowTime);
				runPCB.setProcessUsedCPUTimeInt(runPCB.getProcessRunTimeInt());//���ø�pcb������CPUʱ��
				runPCB.setProcessStatusStr(ProcessStatus.finish);//���ø�pcb������״̬
				runPCB.setProcessCycleTimeInt(nowTime-runPCB.getProcessArriveTiemInt());
				sortByArriveTimeList.remove(runPCB);
				pQueue.pcbWaitQueue.remove(runPCB);
			}else {
				ProcessControlBlock runPCB =  sortByArriveTimeList.get(i);
				int beginTime = sortByArriveTimeList.getFirst().getProcessArriveTiemInt();
				int runPCBTime = runPCB.getProcessRunTimeInt();
				if (cpuRunTime >= runPCBTime) {
					runPcbAllTime+=runPCB.getProcessRunTimeInt();
					nowTime=beginTime+runPcbAllTime;
//					System.out.println("nowTime" + nowTime);
					runPCB.setProcessUsedCPUTimeInt(runPCBTime);//���ø�pcb������CPUʱ��
					runPCB.setProcessStatusStr(ProcessStatus.finish);//���ø�pcb������״̬
					runPCB.setProcessCycleTimeInt(nowTime-runPCB.getProcessArriveTiemInt());
					sortByArriveTimeList.remove(runPCB);
					pQueue.pcbWaitQueue.remove(runPCB);
					cpuRunTime = cpuRunTime-runPCBTime;
				}else if (cpuRunTime < runPCBTime && cpuRunTime != 0) {
					runPCB.setProcessUsedCPUTimeInt(cpuRunTime);//���ø�pcb������CPUʱ��
					runPCB.setProcessStatusStr(ProcessStatus.runing);//���ø�pcb������״̬
					pQueue.pcbRunQueue.add(runPCB);
					pQueue.pcbWaitQueue.remove(runPCB);
					cpuRunTime = 0;
					return;
				}else{
					return;
				}
			}
		}
	}

	//ʱ��Ƭ��ת�㷨
	public void RR(int cpuRunTime){
		int cpuNeedTime = 0; 
		int nowTime = 1; 
		int runPcbAllTime =0;
		int rrTime = 1 ;//�̶�CPU����ʱ��Ƭ��todo��̬���ø���
		//��ȡ�ȴ��������ж�����ҪCPU��ʱ��
		int waitQueueSize = pQueue.pcbWaitQueue.size();
		ProcessControlBlock [] arriveTimearray = new ProcessControlBlock[waitQueueSize];
		//Ϊ��ʱ���Ⱥ�������׼��
		for (int i = 0; i < waitQueueSize; i++) {
			arriveTimearray[i] = pQueue.pcbWaitQueue.get(i);
			cpuNeedTime+=pQueue.pcbWaitQueue.get(i).getProcessRunTimeInt();
//			System.out.println(cpuNeedTime);
		}
		sort(arriveTimearray, 1);
		int beginTime = sortByArriveTimeList.getFirst().getProcessArriveTiemInt();
//		for (int i = 0; i < sortByArriveTimeList.size(); i++) {
//			System.out.println(sortByArriveTimeList.get(i).toString());
//		}
		//һֱѭ����cpu����ʱ�䵽0 ���cpuʱ�������������ʱ�����˳�ѭ��
		while(cpuRunTime != 0){
			if (cpuRunTime >= cpuNeedTime) {
				int cpuRemainTemp = cpuRunTime - cpuNeedTime;
//				int beginTime = sortByArriveTimeList.getFirst().getProcessArriveTiemInt();
				for (int i = 0; i < cpuNeedTime; i++) {
					ProcessControlBlock runPCB =  sortByArriveTimeList.pollFirst();
					if (runPCB.getProcessRunTimeInt() <= rrTime) {
						runPcbAllTime=beginTime+nowTime;
						System.out.println(runPcbAllTime+"nowTime+begin1");
						runPCB.setProcessStatusStr(ProcessStatus.finish);
						runPCB.setProcessCycleTimeInt(runPcbAllTime-runPCB.getProcessArriveTiemInt());
						runPCB.setProcessUsedCPUTimeInt(runPCB.getProcessRunTimeInt());
						pQueue.pcbWaitQueue.remove(runPCB);
					}else{
						//����С����Ҫ�ŵ���β����ѭ��
						runPCB.setProcessUsedCPUTimeInt(runPCB.getProcessUsedCPUTimeInt()+rrTime);//���õ�ǰ���н��̵�����ʱ��Ϊ֮ǰ�ļ��ϵ�ǰ��ʱ��Ƭ
						//�жϵ�ǰ�����Ƿ��Ѿ�������У����������Ϊfinish,������Ӷ�ͷɾ���ҴӾ���������ɾ��
						if (runPCB.getProcessRunTimeInt() == runPCB.getProcessUsedCPUTimeInt()) {
							runPcbAllTime=beginTime+nowTime;
//							System.out.println(runPcbAllTime+"nowTime+begin2");
							runPCB.setProcessStatusStr(ProcessStatus.finish);
							runPCB.setProcessCycleTimeInt(runPcbAllTime-runPCB.getProcessArriveTiemInt());
							pQueue.pcbWaitQueue.remove(runPCB);
						}else {
							sortByArriveTimeList.addLast(runPCB);
						}
					}
					nowTime++;
				}
				cpuRunTime = 0;
				return;
			}
			else{
				ProcessControlBlock runPCB =  sortByArriveTimeList.pollFirst();
//				System.out.println(runPCB.getProcessNameStr());
				//��ȡ����һ��PCB,�ж���������ʱ���Ƿ�С�ڵ���ʱ��Ƭ������ǵ������ڵ�һ�����ִ�в�ɾ��
				if (runPCB.getProcessRunTimeInt() <= rrTime) {
					runPcbAllTime=beginTime+nowTime;
//					System.out.println(runPcbAllTime+"nowTime+begin1");
					runPCB.setProcessStatusStr(ProcessStatus.finish);
					runPCB.setProcessCycleTimeInt(runPcbAllTime-runPCB.getProcessArriveTiemInt());
					runPCB.setProcessUsedCPUTimeInt(runPCB.getProcessRunTimeInt());
					pQueue.pcbWaitQueue.remove(runPCB);
				}else{
					//����С����Ҫ�ŵ���β����ѭ��
					runPCB.setProcessUsedCPUTimeInt(runPCB.getProcessUsedCPUTimeInt()+rrTime);//���õ�ǰ���н��̵�����ʱ��Ϊ֮ǰ�ļ��ϵ�ǰ��ʱ��Ƭ
					//�жϵ�ǰ�����Ƿ��Ѿ�������У����������Ϊfinish,������Ӷ�ͷɾ���ҴӾ���������ɾ��
					if (runPCB.getProcessRunTimeInt() == runPCB.getProcessUsedCPUTimeInt()) {
						runPcbAllTime=beginTime+nowTime;
//						System.out.println(runPcbAllTime+"nowTime+begin2");
						runPCB.setProcessStatusStr(ProcessStatus.finish);
						runPCB.setProcessCycleTimeInt(runPcbAllTime-runPCB.getProcessArriveTiemInt());
						pQueue.pcbWaitQueue.remove(runPCB);
					}else {
						sortByArriveTimeList.addLast(runPCB);
					}
				}
				
				cpuRunTime-=rrTime;
				nowTime++;
//				System.out.println(nowTime+"nowTime");
				if (cpuRunTime == 0 && sortByArriveTimeList.size() >= 1) {
//					sortByArriveTimeList.getFirst().setProcessStatusStr(ProcessStatus.runing);
//					pQueue.pcbRunQueue.add(sortByArriveTimeList.getFirst());
					for (int i = 0; i < sortByArriveTimeList.size(); i++) {
						sortByArriveTimeList.get(i).setProcessStatusStr(ProcessStatus.wait);
					}
				}
				
			}
//			System.out.println(cpuRunTime+ "cpurun");
//			for (int i = 0; i < sortByArriveTimeList.size(); i++) {
//				System.out.println(sortByArriveTimeList.get(i).toString());
//			}
		}
		
	
	}
	
	//���ȼ��㷨
	public void HPF(int cpuRunTime){
		int cpuNeedTime = 0;
		int nowTime = 1; 
		int runPcbAllTime =0;
		ProcessControlBlock [] priorityArray = new ProcessControlBlock[pQueue.pcbWaitQueue.size()];
		for (int i = 0; i < priorityArray.length; i++) {
			priorityArray[i] = pQueue.pcbWaitQueue.get(i);
			cpuNeedTime+=pQueue.pcbWaitQueue.get(i).getProcessRunTimeInt();
//			System.out.println(priorityArray[i]);
		}
//		System.out.println( cpuNeedTime + "cpuneddTime HPF");
		sort(priorityArray, 3);
		sort(priorityArray,1);
		int beginTime = sortByArriveTimeList.getLast().getProcessArriveTiemInt();
		for (int i = 0; i <  sortByPriorityList.size(); i++) {
			if (cpuRunTime > cpuNeedTime) {
				ProcessControlBlock runPCB =  sortByPriorityList.get(i);
				int runPCBTime = runPCB.getProcessRunTimeInt();
				runPcbAllTime+=runPCB.getProcessRunTimeInt();
				nowTime=beginTime+runPcbAllTime;
//				System.out.println(nowTime+"now1");
//				System.out.println(runPcbAllTime+"run1");
				runPCB.setProcessUsedCPUTimeInt(runPCBTime);//���ø�pcb������CPUʱ��
				runPCB.setProcessStatusStr(ProcessStatus.finish);//���ø�pcb������״̬
				runPCB.setProcessCycleTimeInt(nowTime-runPCB.getProcessArriveTiemInt());
				pQueue.pcbWaitQueue.remove(runPCB);
			}else {
				ProcessControlBlock runPCB =  sortByPriorityList.get(i);
				int runPCBTime = runPCB.getProcessRunTimeInt();
				if (cpuRunTime >= runPCBTime) {
					runPcbAllTime+=runPCB.getProcessRunTimeInt();
					nowTime=beginTime+runPcbAllTime;
//					System.out.println(nowTime+"now2");
//					System.out.println(runPcbAllTime+"run2");
					runPCB.setProcessUsedCPUTimeInt(runPCBTime);//���ø�pcb������CPUʱ��
					runPCB.setProcessStatusStr(ProcessStatus.finish);//���ø�pcb������״̬
					runPCB.setProcessCycleTimeInt(nowTime-runPCB.getProcessArriveTiemInt());
					pQueue.pcbWaitQueue.remove(runPCB);
					cpuRunTime = cpuRunTime-runPCBTime;
				}else if (cpuRunTime < runPCBTime && cpuRunTime != 0) {
					runPCB.setProcessUsedCPUTimeInt(cpuRunTime);//���ø�pcb������CPUʱ��
					runPCB.setProcessStatusStr(ProcessStatus.runing);//���ø�pcb������״̬
					pQueue.pcbRunQueue.add(runPCB);
					pQueue.pcbWaitQueue.remove(runPCB);
					cpuRunTime = 0;
					return;
				}else{
					return;
				}
			}
		}
	}
	
	/*
	*����Ӧ���㷨
	*�Խ��̰�����˳����������ͬʱ������������絽���
	*/
	public void HRRF(int cpuRunTime){
		int cpuNeedTime = 0; 
		int waitQueueSize = pQueue.pcbWaitQueue.size();
		ProcessControlBlock [] arriveTimearray = new ProcessControlBlock[waitQueueSize];
		//Ϊ��ʱ���Ⱥ�������׼��
		for (int i = 0; i < waitQueueSize; i++) {
			arriveTimearray[i] = pQueue.pcbWaitQueue.get(i);
			cpuNeedTime+=pQueue.pcbWaitQueue.get(i).getProcessRunTimeInt();
		}
		sort(arriveTimearray,1);
		//������ͬʱ�����
		if (!isAtOnceArrive) {
			//��cpu����ʱ��С��cpu��Ҫʱ�����ʾcpu�����0 �ҿ���CPUʱ�乻��һ����������
			if (cpuRunTime <= cpuNeedTime && cpuRunTime > sortByArriveTimeList.getFirst().getProcessRunTimeInt()) {
				int nowTime = sortByArriveTimeList.getFirst().getProcessArriveTiemInt()+sortByArriveTimeList.getFirst().getProcessRunTimeInt();
				sortByArriveTimeList.getFirst().setProcessUsedCPUTimeInt(sortByArriveTimeList.getFirst().getProcessRunTimeInt());
				sortByArriveTimeList.getFirst().setProcessStatusStr(ProcessStatus.finish);
				sortByArriveTimeList.getFirst().setProcessCycleTimeInt(nowTime-sortByArriveTimeList.getFirst().getProcessArriveTiemInt());
				cpuRunTime-=sortByArriveTimeList.getFirst().getProcessRunTimeInt();
				pQueue.pcbWaitQueue.remove(sortByArriveTimeList.getFirst());
				//��Ϊ����ͬʱ�����������ִ�е�һ�������,����ִ�����һ��
				while(cpuRunTime != 0){
					ProcessControlBlock high_pcbBlock = getHighHRRF(nowTime);
//					System.out.println(high_pcbBlock.getProcessIdInt() + "high");
					//�жϵ�ǰcpuʱ���Ƿ��㹻��ǰ�����������
					if (cpuRunTime >= high_pcbBlock.getProcessRunTimeInt()) {
						high_pcbBlock.setProcessUsedCPUTimeInt(high_pcbBlock.getProcessRunTimeInt());
						high_pcbBlock.setProcessStatusStr(ProcessStatus.finish);
						pQueue.pcbWaitQueue.remove(high_pcbBlock);
						sortByArriveTimeList.remove(high_pcbBlock);
						nowTime+=high_pcbBlock.getProcessRunTimeInt();
						high_pcbBlock.setProcessCycleTimeInt(nowTime-high_pcbBlock.getProcessArriveTiemInt());
						cpuRunTime-=high_pcbBlock.getProcessRunTimeInt();
					}else {
						high_pcbBlock.setProcessUsedCPUTimeInt(cpuRunTime);
						high_pcbBlock.setProcessStatusStr(ProcessStatus.runing);
						pQueue.pcbRunQueue.add(high_pcbBlock);
						pQueue.pcbWaitQueue.remove(high_pcbBlock);
						nowTime+=high_pcbBlock.getProcessUsedCPUTimeInt();
						cpuRunTime-=cpuRunTime;
					}
				}
			}else{
				int nowTime = sortByArriveTimeList.getFirst().getProcessArriveTiemInt()+sortByArriveTimeList.getFirst().getProcessRunTimeInt();
				sortByArriveTimeList.getFirst().setProcessUsedCPUTimeInt(sortByArriveTimeList.getFirst().getProcessRunTimeInt());
				sortByArriveTimeList.getFirst().setProcessStatusStr(ProcessStatus.finish);
				sortByArriveTimeList.getFirst().setProcessCycleTimeInt(nowTime-sortByArriveTimeList.getFirst().getProcessArriveTiemInt());;
				cpuNeedTime-=sortByArriveTimeList.getFirst().getProcessRunTimeInt();
				pQueue.pcbWaitQueue.remove(sortByArriveTimeList.getFirst());
				//��Ϊ����ͬʱ�����������ִ�е�һ�������,����ִ�����һ��
				while(cpuNeedTime != 0){
					ProcessControlBlock high_pcbBlock = getHighHRRF(nowTime);
//					System.out.println(high_pcbBlock.getProcessIdInt() + "high");
					//�жϵ�ǰcpuʱ���Ƿ��㹻��ǰ�����������
					if (cpuRunTime >= high_pcbBlock.getProcessRunTimeInt()) {
						high_pcbBlock.setProcessUsedCPUTimeInt(high_pcbBlock.getProcessRunTimeInt());
						high_pcbBlock.setProcessStatusStr(ProcessStatus.finish);
						pQueue.pcbWaitQueue.remove(high_pcbBlock);
						sortByArriveTimeList.remove(high_pcbBlock);
						nowTime+=high_pcbBlock.getProcessRunTimeInt();
						high_pcbBlock.setProcessCycleTimeInt(nowTime-high_pcbBlock.getProcessArriveTiemInt());
						cpuNeedTime-=high_pcbBlock.getProcessRunTimeInt();
					}else {
						high_pcbBlock.setProcessUsedCPUTimeInt(cpuRunTime);
						high_pcbBlock.setProcessStatusStr(ProcessStatus.runing);
						pQueue.pcbRunQueue.add(high_pcbBlock);
						pQueue.pcbWaitQueue.remove(high_pcbBlock);
						nowTime+=high_pcbBlock.getProcessUsedCPUTimeInt();
						cpuNeedTime-=cpuRunTime;
					}
				}
			}
		}else {
			System.out.println("ͬһʱ�̵����ͬ�ڰ�����Ҫ���д�С��������ִ��");
		}
	}

	//�༶���������㷨
	public void MFQ(int cpuRunTime){
	}
	
	
	//������Ӧ�Ȳ����ص�ǰ��Ӧ����ߵ�
	public ProcessControlBlock getHighHRRF(int nowTime){
		int arriveTime;
		int waitTime = 0;
		int serverTime;
		double current_ratio;
		double max_ratio = 0;
		ArrayList<ProcessControlBlock> hRRFList = new ArrayList<>();
		ProcessControlBlock pcb = null;
		int current_max_ratio_PID = 0;
		if (!isAtOnceArrive) {
//			System.out.println("!Atonce����ͬʱ����");
//			���㵱ǰÿ�����̵���Ӧ��
//			System.out.println("ʱ��"+nowTime);
			for (int i = 1; i < sortByArriveTimeList.size(); i++) {
				pcb = sortByArriveTimeList.get(i);
				//��ȡ����i�����̵ĵ���ʱ�䣻�����˵�
				arriveTime = pcb.getProcessArriveTiemInt();
				//��ȡ����i�����̵���Ҫ����ʱ��
				serverTime = pcb.getProcessRunTimeInt();
				//Ĭ�ϵ�ǰʱ������������н��̵ĵ���ʱ�䣬������Ҫ���㰴FCFS�㷨���ȼ���
				waitTime = nowTime - arriveTime;
				current_ratio = new BigDecimal((float)waitTime / serverTime).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() + 1;
				if (current_ratio > max_ratio) {
					max_ratio = current_ratio;
					current_max_ratio_PID = i;
				}
//			System.out.println(String.valueOf("PID:" +pcb.getProcessIdInt())+"   ����ʱ�䣺"+String.valueOf(arriveTime)+"   ��Ҫ����ʱ�䣺"+String.valueOf(serverTime)+"   �ȴ�ʱ�䣺"+String.valueOf(waitTime)+"   ��ǰ��Ӧ�ȣ�"+current_ratio);
			}
//			System.out.println(max_ratio + "max" +current_max_ratio_PID);
		}else{
//			System.out.println("Atonce");
		}
		return sortByArriveTimeList.get(current_max_ratio_PID);
	}
	
	
	
	
	public void sort(ProcessControlBlock [] array,int sortByindex){
		ProcessControlBlock temp;
		boolean isAtOnceArrive = false;
		switch (sortByindex) {
		case 1:
			//����ʱ���С���� ����ʱ��
			for (int i = 0; i < array.length-1; i++) {
				for (int j = 0; j < array.length-1-i; j++) {
					if (array[j].getProcessArriveTiemInt() >= array[j+1].getProcessArriveTiemInt()) {
						temp = array[j];
						array[j] = array[j+1];
						array[j+1] = temp;
					}
				}
			}
			//Ȼ���ٰ�PID�Ⱥ�����
			for (int i = 0; i < array.length-1; i++) {
				for (int j = 0; j < array.length-1-i; j++) {
					if (array[j].getProcessArriveTiemInt() == array[j+1].getProcessArriveTiemInt()) {
						if (array[j].getProcessIdInt() >= array[j+1].getProcessIdInt()) {
							temp = array[j];
							array[j] = array[j+1];
							array[j+1] = temp;
						}
						isAtOnceArrive = true;
					}
				}
			}
//			System.out.println(Arrays.toString(array));
			//���ݵ���ʱ���Ⱥ�˳��Խ�������
			for (int i = 0; i < array.length; i++) {
				sortByArriveTimeList.add(array[i]);
			}
//			return isAtOnceArrive;
			break;
		case 2:
			break;
		case 3:
			//���ȼ��Ӵ�С �������ȼ�
			for (int i = 0; i < array.length-1; i++) {
				for (int j = 0; j < array.length-1-i; j++) {
					if (array[j].getProcessPriorityInt() < array[j+1].getProcessPriorityInt()) {
						temp = array[j];
						array[j] = array[j+1];
						array[j+1] = temp;
					}
				}
			}
			
			//�������ȼ��Խ�������
			for (int i = 0; i < array.length; i++) {
				sortByPriorityList.add(array[i]);
			}
			break;
		case 4:
			break;
		default:
			break;
		}
	}
}
