package com.service.guarantee.domain.response;

public class ResultEntity {
	
	private Object data;
	private String message = "操作成功";
	private int status = 200;
	
	public ResultEntity() {
		super();
	}
	
	public ResultEntity(Object data, String message, int status) {
		super();
		this.data = data;
		this.message = message;
		this.status = status;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ResultEntity{" +
				"data=" + data +
				", message='" + message + '\'' +
				", status=" + status +
				'}';
	}
}
