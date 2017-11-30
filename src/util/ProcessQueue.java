package util;

import java.util.LinkedList;

import model.ProcessControlBlock;

public class ProcessQueue {
	 private static ProcessQueue processQueue;
	 public LinkedList<ProcessControlBlock> pcbWaitQueue; //���̵ľ�������
	 public LinkedList<ProcessControlBlock> pcbRunQueue; //���̵����ж���
	 public LinkedList<ProcessControlBlock> pcbBlockingQueue;//���̵���������
	 public LinkedList<ProcessControlBlock> pcbAllInfoList;//��ǰ���н�����Ϣ
	
	 private ProcessQueue(){
			pcbWaitQueue = new LinkedList<>();
			pcbRunQueue = new LinkedList<>();
			pcbBlockingQueue = new LinkedList<>();
			pcbAllInfoList = new LinkedList<>();
	 }
	 
	 public static synchronized ProcessQueue getInstance(){
		 if (processQueue == null) {
			processQueue = new ProcessQueue();
		}
		 return processQueue;
	 } 
	 
	 
	 public LinkedList<ProcessControlBlock> getPcbWaitQueue() {
		return pcbWaitQueue;
	}
	public void setPcbWaitQueue(LinkedList<ProcessControlBlock> pcbWaitQueue) {
		this.pcbWaitQueue = pcbWaitQueue;
	}
	public LinkedList<ProcessControlBlock> getPcbRunQueue() {
		return pcbRunQueue;
	}
	public void setPcbRunQueue(LinkedList<ProcessControlBlock> pcbRunQueue) {
		this.pcbRunQueue = pcbRunQueue;
	}
	public LinkedList<ProcessControlBlock> getPcbBlockingQueue() {
		return pcbBlockingQueue;
	}
	public void setPcbBlockingQueue(LinkedList<ProcessControlBlock> pcbBlockingQueue) {
		this.pcbBlockingQueue = pcbBlockingQueue;
	}
	public LinkedList<ProcessControlBlock> getPcbAllInfoList() {
		return pcbAllInfoList;
	}
	public void setPcbAllInfoList(LinkedList<ProcessControlBlock> pcbAllInfoList) {
		this.pcbAllInfoList = pcbAllInfoList;
	}
	 
	 
}
