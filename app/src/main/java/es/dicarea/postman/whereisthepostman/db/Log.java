package es.dicarea.postman.whereisthepostman.db;

public class Log {
    private Integer mId;
    private Long mDate;
    private Integer mStatus;

    public Log(Long date, Integer status) {
        mDate = date;
        mStatus = status;
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        mId = id;
    }

    public Long getDate() {
        return mDate;
    }

    public void setDate(Long date) {
        mDate = date;
    }

    public Integer getStatus() {
        return mStatus;
    }

    public void setStatus(Integer status) {
        mStatus = status;
    }
}
