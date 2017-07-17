package zone.com.retrofitlisthelper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import zone.com.retrofitlisthelper.gank.GankAPI;

/**
 * 实现在方法调用前后向控制台输出两句字符串
 *
 * @author jiqinlin
 */
public class HelloWorldHandler implements InvocationHandler {
    //要代理的原始对象
    private APIController2 obj;

    public HelloWorldHandler(APIController2 obj) {
        super();
        this.obj = obj;

    }

    /**
     * 在代理实例上处理方法调用并返回结果
     *
     * @param proxy  代理类
     * @param method 被代理的方法
     * @param args   该方法的参数数组
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        //调用之前
        doBefore();
        for (APIController2.Entity entity : obj.entityList) {
            if (method.getDeclaringClass().isAssignableFrom(entity.interfaceClass)) {
                method.invoke(entity.instance, args);
            }
        }
        //调用原始对象的方法
//        result = method.invoke(obj, args);
        //调用之后
        doAfter();
        return result;
    }

    private void doBefore() {
        System.out.println("before method invoke");
    }

    private void doAfter() {
        System.out.println("after method invoke");
    }

}