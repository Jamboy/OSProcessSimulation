package model;


public class ProcessControlBlock {
	//���̿��ƿ�
	private String processNameStr;//������
	private int processIdInt;//����ID
	private int processPriorityInt;//���ȼ�
	@SuppressWarnings("unused")
	private int processArriveTiemInt;//����ʱ��
	private int processRunTimeInt;//��Ҫ����ʱ��
	private int processUsedCPUTimeInt;//����ʱ��
	private ProcessStatus processStatusStr;//����״̬
	private String processBlockingReasonStr;//����ԭ��
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
		return "������:"+processNameStr +  "    "+ "PID:"+processIdInt + "    " +"���ȼ�:"+processPriorityInt +"    "+ "���̵���ʱ��:"+processArriveTiemInt +"    "+ "��������ʱ��:"+processRunTimeInt +"    "+"CPU����ʱ��:"+processUsedCPUTimeInt+"    "+"����״̬:"+processStatusStr+"    "+"��תʱ��:"+processCycleTimeInt+"      ����ԭ��:"+processBlockingReasonStr;
	}
	
	
}
