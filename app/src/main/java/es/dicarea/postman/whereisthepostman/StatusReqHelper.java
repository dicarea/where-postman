package es.dicarea.postman.whereisthepostman;

import java.util.List;
import java.util.concurrent.ExecutionException;

import es.dicarea.postman.whereisthepostman.db.DataSource;

public class StatusReqHelper {

    public void executeRequest() {
        Long timeNow = System.currentTimeMillis();
        new StatusAsyncTask(timeNow).execute(getCodes());
    }

    public void executeRequestSync() {
        Long timeNow = System.currentTimeMillis();
        try {
            new StatusAsyncTask(timeNow).execute(getCodes()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    //*********************

    private BeanRepository.TrackingItem[] getCodes() {
        DataSource ds = DataSource.getInstance();
        List<BeanRepository.TrackingItem> trackingList = ds.getTrackingListActive();
        return trackingList.toArray(new BeanRepository.TrackingItem[trackingList.size()]);
    }

}
