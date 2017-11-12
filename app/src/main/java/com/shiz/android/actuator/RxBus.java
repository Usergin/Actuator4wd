package com.shiz.android.actuator;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by OldMan on 11.11.2017.
 */

public class RxBus {
    private static final RxBus INSTANCE = new RxBus();
    private PublishSubject<Object> sensorSubject = PublishSubject.create();

    public static RxBus getInstance() {
        return INSTANCE;
    }

    /**
     * Pass any event down to event listeners.
     */
    public void post(Object object) {
        sensorSubject.onNext(object);
    }
    /**
     * Subscribe to this Observable. On event, do something
     * e.g. replace a fragment
     */
    public Observable<Object> getEvents() {
        return sensorSubject;
    }

}
