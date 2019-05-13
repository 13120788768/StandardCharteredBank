package com.standard.chartered.bank.handler;

import com.standard.chartered.bank.dispatcher.Instrument;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wayyer
 * @Description: decide which type of instrument is needed
 * @Program: HelloWorld
 * @Date: 2019.05.13
 */
public class Wrapper {
    //这里也可以用map 对象来保存Hanlder对象
    private List<Handler> handlerMapping = new ArrayList<Handler>();

    public Wrapper(Instrument object, String method, String instrumentType) {
        //简单实现映射
        try {
            Class clazz = object.getClass();
            Handler handler = new Handler();
            handler.setInstrument(clazz.newInstance())
                    .setMethod(clazz.getMethod(method/*, new Class[]{String.class}*/))
                    .setType(instrumentType);
            handlerMapping.add(handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void  doPublishService(String type){
        doDispatch(type);
    }

    public void  doImportService(String type, String... parameters){
        doDispatch(type, parameters);
    }

    private void doDispatch(String type, String... parameters) {
        //1.获取用户请求的url
        String uri =   type;
        Handler handler =null;

        ////2、根据uri 去handlerMapping找到对应的hanler
        for(Handler h :handlerMapping){
            if(uri.equals(h.getType())){
                handler = h;
                break;
            }
        }
        //3.将具体的任务分发给Method（通过反射去调用其对应的方法）
        Object obj = null;
        try {
            obj =  handler.getMethod().invoke(handler.getInstrument(),parameters);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //4、获取到Method执行的结果，通过Response返回出去
        // response.getWriter().write();

    }
    /**
     * 具体的hanlder对象
     */
    class Handler{
        //controller对象
        private Object instrument;
        //controller对象映射的方法
        private  String type;
        //ulr对应的方法
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
