package es.dicarea.postman.whereisthepostman;

public class StatusItem {
    private String mCode;
    private Long mTime;
    private StatusEnum mStatus;

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public Long getTime() {
        return mTime;
    }

    public void setTime(Long time) {
        mTime = time;
    }

    public StatusEnum getStatus() {
        return mStatus;
    }

    public void setStatus(StatusEnum status) {
        mStatus = status;
    }
}
