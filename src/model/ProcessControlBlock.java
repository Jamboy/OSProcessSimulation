package model;


public class ProcessControlBlock {
	//进程控制块
	private String processNameStr;//进程名
	private int processIdInt;//进程ID
	private int processPriorityInt;//优先级
	@SuppressWarnings("unused")
	private int processArriveTiemInt;//到达时间
	private int processRunTimeInt;//需要运行时间
	private int processUsedCPUTimeInt;//已用时间
	private ProcessStatus processStatusStr;//进程状态
	private String processBlockingReasonStr;//阻塞原因
	private int processCycleTimeInt;
//	Process process;
//	
//	public ProcessControlBlock(Process process){
//			this.process = process;
//	}
	
	
	public String getProcessNameStr() {
		return processNameStr;
	}
	public void setProcessNameStr(String processNameStr) {
		this.processNameStr = processNameStr;
	}
	public int getProcessIdInt() {
		return processIdInt;
	}
	public void setProcessIdInt(int processIdInt) {
		this.processIdInt = processIdInt;
	}
	public int getProcessPriorityInt() {
		return processPriorityInt;
	}
	public void setProcessPriorityInt(int processPriorityInt) {
		this.processPriorityInt = processPriorityInt;
	}
	public int getProcessArriveTiemInt() {
		return processArriveTiemInt;
	}
	public void setProcessArriveTimeInt(int processArriveTiemInt) {
		this.processArriveTiemInt = processArriveTiemInt;
	}
	public int getProcessRunTimeInt() {
		return processRunTimeInt;
	}
	public void setProcessRunTimeInt(int processRunTimeInt) {
		this.processRunTimeInt = processRunTimeInt;
	}
	public int getProcessUsedCPUTimeInt() {
		return processUsedCPUTimeInt;
	}
	public void setProcessUsedCPUTimeInt(int processUsedCPUTimeInt) {
		this.processUsedCPUTimeInt = processUsedCPUTimeInt;
	}
	public ProcessStatus getProcessStatusStr() {
		return processStatusStr;
	}
	public void setProcessStatusStr(ProcessStatus processStatusStr) {
		this.processStatusStr = processStatusStr;
	}
	public String getProcessBlockingReasonStr() {
		return processBlockingReasonStr;
	}
	public void setProcessBlockingReasonStr(String processBlockingReasonStr) {
		this.processBlockingReasonStr = processBlockingReasonStr;
	}

	public int getProcessCycleTimeInt() {
		return processCycleTimeInt;
	}
	public void setProcessCycleTimeInt(int processCycleTimeInt) {
		this.processCycleTimeInt = processCycleTimeInt;
	}
	public String toString(){
		return "进程名:"+processNameStr +  "    "+ "PID:"+processIdInt + "    " +"优先级:"+processPriorityInt +"    "+ "进程到达时间:"+processArriveTiemInt +"    "+ "进程运行时间:"+processRunTimeInt +"    "+"CPU已用时间:"+processUsedCPUTimeInt+"    "+"进程状态:"+processStatusStr+"    "+"周转时间:"+processCycleTimeInt+"      阻塞原因:"+processBlockingReasonStr;
	}
	
	
}
