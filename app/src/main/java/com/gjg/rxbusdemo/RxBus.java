package com.gjg.rxbusdemo;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by JunGuang_Gao
 * on 2017/3/21  21:57.
 * 类描述：
 */

public class RxBus {
    private final Subject<Object, Object> subject = new SerializedSubject<>(PublishSubject.create());
    private final Map<String, Object> tags = new HashMap<>();

    private static RxBus rxbus;

    public static RxBus getInstance()
    {
        if(rxbus == null)
        {
            synchronized (RxBus.class) {
                if(rxbus == null) {
                    rxbus = new RxBus();
                }
            }
        }
        return rxbus;
    }

    /**
     * 发送事件消息
     * @param tag 用于区分事件
     * @param object 事件的参数
     */
    public void post(String tag, Object object)
    {
        subject.onNext(object);
        if(!tags.containsKey(tag))
        {
            tags.put(tag, object);
        }
    }

    // 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
    public <T> Observable<T> toObservable (Class<T> eventType) {
        return subject.ofType(eventType);
//        ofType = filter + cast
//        return subject.filter(new Func1<Object, Boolean>() {
//            @Override
//            public Boolean call(Object o) {
//                return eventType.isInstance(o);
//            }
//        }) .cast(eventType);
    }

    /**
     * 主线程中执行
     * @param tag
     * @param rxBusResult
     */
    public void toObserverableOnMainThread(final String tag, final RxBusResult rxBusResult) {

        subject.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                if (tags.containsKey(tag)) {
                    rxBusResult.onRxBusResult(o);
                }
            }
        });
    }

    /**
     * 子线程中执行
     * @param tag
     * @param rxBusResult
     */
    public void toObserverableChildThread(final String tag, final RxBusResult rxBusResult) {

        subject.observeOn(Schedulers.io()).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                if (tags.containsKey(tag)) {
                    rxBusResult.onRxBusResult(o);
                }
            }
        });
    }

    /**
     * 移除tag
     * @param tag
     */
    public void removeObserverable(String tag)
    {
        if(tags.containsKey(tag))
        {
            tags.remove(tag);
        }
    }

    /**
     * 退出应用时，清空资源
     */
    public void release()
    {
        tags.clear();
        rxbus = null;
    }
}
