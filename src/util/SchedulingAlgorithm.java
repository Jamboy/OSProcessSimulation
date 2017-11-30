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
	 * 先来先服务算法
	 * 首先按到达时间排序
	 * 
	 */
	public void FCFS(int cpuRunTime){
		System.out.println(cpuRunTime+"cpu");
		ProcessControlBlock [] arriveTimearray = new ProcessControlBlock[pQueue.pcbWaitQueue.size()];
		int cpuNeedTime = 0;
		int nowTime = 0; 
		int runPcbAllTime =0;
		//将其到达时间放进一个数组中做排序
		for (int i = 0; i < arriveTimearray.length; i++) {
			arriveTimearray[i] = pQueue.pcbWaitQueue.get(i);
			cpuNeedTime+=pQueue.pcbWaitQueue.get(i).getProcessRunTimeInt();
//			System.out.println(arriveTimearray[i]);
		}
		sort(arriveTimearray,1); //对进程按到达时间排序
//		for (int i = 0; i < arriveTimearray.length; i++) {
//			System.out.println(sortByArriveTimeList.get(i).toString());
//		}
//		System.out.println(cpuRunTime + "cpuRunTime");
//		System.out.println(cpuNeedTime+"cpuNeedTime");
		for (int i = 0; i < sortByArriveTimeList.size(); i++) {
			//若cpu可用时间大于所需时间则直接按顺序执行即可
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
				runPCB.setProcessUsedCPUTimeInt(runPCB.getProcessRunTimeInt());//设置该pcb的已用CPU时间
				runPCB.setProcessStatusStr(ProcessStatus.finish);//设置该pcb的运行状态
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
					runPCB.setProcessUsedCPUTimeInt(runPCBTime);//设置该pcb的已用CPU时间
					runPCB.setProcessStatusStr(ProcessStatus.finish);//设置该pcb的运行状态
					runPCB.setProcessCycleTimeInt(nowTime-runPCB.getProcessArriveTiemInt());
					sortByArriveTimeList.remove(runPCB);
					pQueue.pcbWaitQueue.remove(runPCB);
					cpuRunTime = cpuRunTime-runPCBTime;
				}else if (cpuRunTime < runPCBTime && cpuRunTime != 0) {
					runPCB.setProcessUsedCPUTimeInt(cpuRunTime);//设置该pcb的已用CPU时间
					runPCB.setProcessStatusStr(ProcessStatus.runing);//设置该pcb的运行状态
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

	//时间片轮转算法
	public void RR(int cpuRunTime){
		int cpuNeedTime = 0; 
		int nowTime = 1; 
		int runPcbAllTime =0;
		int rrTime = 1 ;//固定CPU处理时间片，todo动态设置更新
		//获取等待队列所有队列需要CPU的时间
		int waitQueueSize = pQueue.pcbWaitQueue.size();
		ProcessControlBlock [] arriveTimearray = new ProcessControlBlock[waitQueueSize];
		//为按时间先后做排序准备
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
		//一直循环到cpu可用时间到0 如果cpu时间大于所需总是时间则退出循环
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
						//若不小于则要放到队尾继续循环
						runPCB.setProcessUsedCPUTimeInt(runPCB.getProcessUsedCPUTimeInt()+rrTime);//设置当前运行进程的已用时间为之前的加上当前的时间片
						//判断当前进程是否已经完成运行，若完成则至为finish,并将其从对头删除且从就绪队列中删除
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
				//获取到第一个PCB,判断它的运行时间是否小于等于时间片，如果是的则能在第一次完成执行并删除
				if (runPCB.getProcessRunTimeInt() <= rrTime) {
					runPcbAllTime=beginTime+nowTime;
//					System.out.println(runPcbAllTime+"nowTime+begin1");
					runPCB.setProcessStatusStr(ProcessStatus.finish);
					runPCB.setProcessCycleTimeInt(runPcbAllTime-runPCB.getProcessArriveTiemInt());
					runPCB.setProcessUsedCPUTimeInt(runPCB.getProcessRunTimeInt());
					pQueue.pcbWaitQueue.remove(runPCB);
				}else{
					//若不小于则要放到队尾继续循环
					runPCB.setProcessUsedCPUTimeInt(runPCB.getProcessUsedCPUTimeInt()+rrTime);//设置当前运行进程的已用时间为之前的加上当前的时间片
					//判断当前进程是否已经完成运行，若完成则至为finish,并将其从对头删除且从就绪队列中删除
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
	
	//优先级算法
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
				runPCB.setProcessUsedCPUTimeInt(runPCBTime);//设置该pcb的已用CPU时间
				runPCB.setProcessStatusStr(ProcessStatus.finish);//设置该pcb的运行状态
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
					runPCB.setProcessUsedCPUTimeInt(runPCBTime);//设置该pcb的已用CPU时间
					runPCB.setProcessStatusStr(ProcessStatus.finish);//设置该pcb的运行状态
					runPCB.setProcessCycleTimeInt(nowTime-runPCB.getProcessArriveTiemInt());
					pQueue.pcbWaitQueue.remove(runPCB);
					cpuRunTime = cpuRunTime-runPCBTime;
				}else if (cpuRunTime < runPCBTime && cpuRunTime != 0) {
					runPCB.setProcessUsedCPUTimeInt(cpuRunTime);//设置该pcb的已用CPU时间
					runPCB.setProcessStatusStr(ProcessStatus.runing);//设置该pcb的运行状态
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
	*高响应比算法
	*对进程按到达顺序排序，若非同时到达，先运行最早到达的
	*/
	public void HRRF(int cpuRunTime){
		int cpuNeedTime = 0; 
		int waitQueueSize = pQueue.pcbWaitQueue.size();
		ProcessControlBlock [] arriveTimearray = new ProcessControlBlock[waitQueueSize];
		//为按时间先后做排序准备
		for (int i = 0; i < waitQueueSize; i++) {
			arriveTimearray[i] = pQueue.pcbWaitQueue.get(i);
			cpuNeedTime+=pQueue.pcbWaitQueue.get(i).getProcessRunTimeInt();
		}
		sort(arriveTimearray,1);
		//若不是同时到达的
		if (!isAtOnceArrive) {
			//若cpu可用时间小于cpu需要时间则表示cpu会减至0 且可用CPU时间够第一个进程运行
			if (cpuRunTime <= cpuNeedTime && cpuRunTime > sortByArriveTimeList.getFirst().getProcessRunTimeInt()) {
				int nowTime = sortByArriveTimeList.getFirst().getProcessArriveTiemInt()+sortByArriveTimeList.getFirst().getProcessRunTimeInt();
				sortByArriveTimeList.getFirst().setProcessUsedCPUTimeInt(sortByArriveTimeList.getFirst().getProcessRunTimeInt());
				sortByArriveTimeList.getFirst().setProcessStatusStr(ProcessStatus.finish);
				sortByArriveTimeList.getFirst().setProcessCycleTimeInt(nowTime-sortByArriveTimeList.getFirst().getProcessArriveTiemInt());
				cpuRunTime-=sortByArriveTimeList.getFirst().getProcessRunTimeInt();
				pQueue.pcbWaitQueue.remove(sortByArriveTimeList.getFirst());
				//因为不是同时到达的所以先执行第一个到达的,可以执行完第一个
				while(cpuRunTime != 0){
					ProcessControlBlock high_pcbBlock = getHighHRRF(nowTime);
//					System.out.println(high_pcbBlock.getProcessIdInt() + "high");
					//判断当前cpu时间是否足够当前进程完成运行
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
				//因为不是同时到达的所以先执行第一个到达的,可以执行完第一个
				while(cpuNeedTime != 0){
					ProcessControlBlock high_pcbBlock = getHighHRRF(nowTime);
//					System.out.println(high_pcbBlock.getProcessIdInt() + "high");
					//判断当前cpu时间是否足够当前进程完成运行
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
			System.out.println("同一时刻到达等同于按其需要运行从小到大依次执行");
		}
	}

	//多级反馈队列算法
	public void MFQ(int cpuRunTime){
	}
	
	
	//计算响应比并返回当前响应比最高的
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
//			System.out.println("!Atonce不是同时到达");
//			计算当前每个进程的响应比
//			System.out.println("时刻"+nowTime);
			for (int i = 1; i < sortByArriveTimeList.size(); i++) {
				pcb = sortByArriveTimeList.get(i);
				//获取到第i个进程的到达时间；排序了的
				arriveTime = pcb.getProcessArriveTiemInt();
				//获取到第i个进程的需要服务时间
				serverTime = pcb.getProcessRunTimeInt();
				//默认当前时间大于现在所有进程的到达时间，否则不需要计算按FCFS算法调度即可
				waitTime = nowTime - arriveTime;
				current_ratio = new BigDecimal((float)waitTime / serverTime).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() + 1;
				if (current_ratio > max_ratio) {
					max_ratio = current_ratio;
					current_max_ratio_PID = i;
				}
//			System.out.println(String.valueOf("PID:" +pcb.getProcessIdInt())+"   到达时间："+String.valueOf(arriveTime)+"   需要运行时间："+String.valueOf(serverTime)+"   等待时间："+String.valueOf(waitTime)+"   当前响应比："+current_ratio);
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
			//到达时间从小到大 排序时间
			for (int i = 0; i < array.length-1; i++) {
				for (int j = 0; j < array.length-1-i; j++) {
					if (array[j].getProcessArriveTiemInt() >= array[j+1].getProcessArriveTiemInt()) {
						temp = array[j];
						array[j] = array[j+1];
						array[j+1] = temp;
					}
				}
			}
			//然后再按PID先后排序
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
			//根据到达时间先后顺序对进程排序
			for (int i = 0; i < array.length; i++) {
				sortByArriveTimeList.add(array[i]);
			}
//			return isAtOnceArrive;
			break;
		case 2:
			break;
		case 3:
			//优先级从大到小 排序优先级
			for (int i = 0; i < array.length-1; i++) {
				for (int j = 0; j < array.length-1-i; j++) {
					if (array[j].getProcessPriorityInt() < array[j+1].getProcessPriorityInt()) {
						temp = array[j];
						array[j] = array[j+1];
						array[j+1] = temp;
					}
				}
			}
			
			//根据优先级对进程排序
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
