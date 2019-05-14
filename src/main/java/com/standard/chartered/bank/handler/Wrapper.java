package com.standard.chartered.bank.handler;

import com.standard.chartered.bank.dispatcher.Instrument;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;

/**
 * @Author: wayyer
 * @Description: decide which type of instrument is needed
 * @Program: HelloWorld
 * @Date: 2019.05.13
 */
public class Wrapper {
    //enable the security by concurrentHashMap
    private ConcurrentHashMap<String, Handler> handlerMappingMap = new ConcurrentHashMap<>();

    public Wrapper(Instrument object, String method, String instrumentType) {
        //register
        try {
            Class clazz = object.getClass();
            Handler handler = new Handler();
            handler.setInstrument(clazz.newInstance())
                    .setMethod(clazz.getMethod(method, new Class[]{Object.class}))
                    .setType(instrumentType);
            handlerMappingMap.putIfAbsent(instrumentType, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * do this file job async
     * @param type
     * @param parameters
     */
    public void saftyDoService(String type, Object... parameters){
        Callable callable = new Callable() {
            @Override
            public Object call() throws Exception {
                return doDispatch(type, parameters);
            }
        };
        FutureTask newTask = new FutureTask(callable);
        new Thread(newTask).start();
    }

    /**
     *
     * @param type
     * @param parameters
     * @throws Exception
     */
    public void doPublishService(String type, Object... parameters) throws Exception{
        saftyDoService(type, parameters);
    }

    public Object doImportService(String type, Object... parameters) throws Exception{
        return doDispatch(type, parameters);
    }

    private Object doDispatch(String type, Object... parameters) throws Exception {
        Handler handler = handlerMappingMap.get(type);

        Object obj = null;
        try {
            if(parameters.length == 1){
                obj =  handler.getMethod().invoke(handler.getInstrument(), parameters[0]);
            }else {
                obj =  handler.getMethod().invoke(handler.getInstrument(), parameters);
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return obj;

    }

    /**
     * handler is used to store the dispatcher
     */
    class Handler{
        private Object instrument;
        private  String type;
        private Method method;

        public Object getInstrument() {
            return instrument;
        }

        public Handler setInstrument(Object instrument) {
            this.instrument = instrument;
            return this;
        }

        public String getType() {
            return type;
        }

        public Handler setType(String type) {
            this.type = type;
            return  this;
        }

        public Method getMethod() {
            return method;
        }

        public Handler setMethod(Method method) {
            this.method = method;
            return this;
        }
    }
}
