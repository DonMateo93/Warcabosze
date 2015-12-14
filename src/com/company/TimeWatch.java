package com.company;

import java.util.concurrent.TimeUnit;

/**
 * Klasa pozwalająca na uzyskanie informacji na temat czasu jaki upłynął od momentu rozpoczęcia gry
 */
public class TimeWatch {
    /**
     * pole przechowujące początkową wartość timera
     */
    long starts;


    public static TimeWatch start() {
        return new TimeWatch();
    }

    private TimeWatch() {
        reset();
    }


    public TimeWatch reset() {
        starts = System.currentTimeMillis();
        return this;
    }

    /**
     * Pobranie czasu jaki upłynął
     * @return
     */
    public long time() {
        long ends = System.currentTimeMillis();
        return ends - starts;
    }

    /**
     * pobranie czasu w wybranych jednostkach
     * @param unit
     * @return
     */
    public long time(TimeUnit unit) {
        return unit.convert(time(), TimeUnit.MILLISECONDS);
    }
}
