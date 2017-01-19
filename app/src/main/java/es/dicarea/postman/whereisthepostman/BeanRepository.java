package es.dicarea.postman.whereisthepostman;

public class BeanRepository {

    public static class StatusItem {
        private Integer mId;
        private TrackingItem mTracking;
        private Long mTime;
        private StatusEnum mStatus;

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

        public Integer getId() {
            return mId;
        }

        public void setId(Integer id) {
            mId = id;
        }

        public TrackingItem getTracking() {
            return mTracking;
        }

        public void setTracking(TrackingItem tracking) {
            mTracking = tracking;
        }
    }

    public static class TrackingItem {
        private Integer mId;
        private String mCode;
        private String mDesc;
        private Boolean mActive;
        private Boolean mDeleted;
        private StatusItem mLastStatus;

        public Integer getId() {
            return mId;
        }

        public void setId(Integer id) {
            mId = id;
        }


        public Boolean getActive() {
            return mActive;
        }

        public void setActive(Boolean active) {
            mActive = active;
        }

        public Boolean getDeleted() {
            return mDeleted;
        }

        public void setDeleted(Boolean deleted) {
            mDeleted = deleted;
        }

        public String getCode() {
            return mCode;
        }

        public void setCode(String code) {
            mCode = code;
        }

        public StatusItem getLastStatus() {
            return mLastStatus;
        }

        public void setLastStatus(StatusItem lastStatus) {
            mLastStatus = lastStatus;
        }

        public String getDesc() {
            return mDesc;
        }

        public void setDesc(String desc) {
            mDesc = desc;
        }
    }
}