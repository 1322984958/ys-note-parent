package com.gkys.common.constant;

public enum JobStatus {
	/**
     * 暂停
     */
    PAUSE(0),
    /**
     * 正常
     */
    NORMAL(1);
	
    private int value;

    private JobStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
