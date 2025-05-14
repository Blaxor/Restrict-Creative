package ro.deiutzblaxo.RestrictCreative.cache;

import ro.deiutzblaxo.cloud.data.cache.template.CacheManagerTemplate;

import java.util.Timer;
import java.util.TimerTask;

public class LocationCache extends CacheManagerTemplate<Boolean> {

    private static final int RETENTION_PERIOD = 360;

    public LocationCache() {
        super(RETENTION_PERIOD);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                LocationCache.this.evictPeriodically();
            }
        }, (RETENTION_PERIOD + 100) * 1000, (RETENTION_PERIOD + 100) * 1000);
    }
}
