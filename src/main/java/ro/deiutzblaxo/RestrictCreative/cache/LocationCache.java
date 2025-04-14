package ro.deiutzblaxo.RestrictCreative.cache;

import ro.deiutzblaxo.cloud.data.cache.template.CacheManagerTemplate;

import java.util.Timer;
import java.util.TimerTask;

public class LocationCache extends CacheManagerTemplate<Boolean> {

    private static final int RETENTION_PERIOD = 360;

    private final Timer timer = new Timer();

    public LocationCache() {
        super(RETENTION_PERIOD);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                LocationCache.this.evictPeriodically();
            }
        }, RETENTION_PERIOD + 100, RETENTION_PERIOD + 100);
    }
}
