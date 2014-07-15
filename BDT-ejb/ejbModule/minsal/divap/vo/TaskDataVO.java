package minsal.divap.vo;

import java.io.Serializable;
import java.util.Map;

public class TaskDataVO  implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1729550086531023319L;

	private TaskVO task;
	private Map<String, Object> data;

	public TaskDataVO(TaskVO task, Map<String, Object> data)
	{
		this.task = task;
		this.data = data;
	}

	public TaskVO getTask() {
		return this.task;
	}

	public void setTask(TaskVO task) {
		this.task = task;
	}

	public Map<String, Object> getData() {
		return this.data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "TaskDataVO [task=" + task + ", data=" + data + "]";
	}
	
}