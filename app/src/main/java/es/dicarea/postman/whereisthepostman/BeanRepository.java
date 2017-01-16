package es.dicarea.postman.whereisthepostman;

public class BeanRepository {

    public static class StatusItem {
        private Integer mId;
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

        public Integer getId() {
            return mId;
        }

        public void setId(Integer id) {
            mId = id;
        }
    }

    public static class TrackingItem {
        private Integer mId;
        private String mCode;
        private Boolean mActive;
        private Boolean mDeleted;

        public Integer getId() {
            return mId;
        }

        public void setId(Integer id) {
            mId = id;
        }

        public String getCode() {
            return mCode;
        }

        public void setCode(String code) {
            mCode = code;
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
    }
}