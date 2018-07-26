package com.ft.service.dto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("ACCESSGW")
public class VasCloudMsgDTO {

	@JsonProperty("MODULE")
	private String module;

	@JsonProperty("MESSAGE_TYPE")
    private String msgType;

	@JsonProperty("COMMAND")
    private Map<String, String> cmd = new ConcurrentHashMap<String, String>();

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public Map<String, String> getCmd() {
		return cmd;
	}

	public void setCmd(Map<String, String> cmd) {
		this.cmd = cmd;
	}

	@Override
	public String toString() {
		return "VasCloudMsgDTO [module=" + module + ", msgType=" + msgType + ", cmd=" + cmd + "]";
	}
}
