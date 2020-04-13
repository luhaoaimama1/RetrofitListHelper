package zone.com.sdk;


import com.example.Entity2;
import com.example.ZClass;

import zone.com.sdk.API.gank.api.GankAPI;
import zone.com.sdk.API.gank.api.GankImpl;
import zone.com.sdk.API.gank2.api.Gank2API;
import zone.com.sdk.API.gank2.api.Gank2Impl;

/**
 * [2017] by Zone
 * 这个是通过编译的时候 而不是运行时，所以编译的时候就需要知道， 写入的代码字符串;
 * 通过注解： GankAPI  APIList添加注解 GankImpl 加注解，检测包名相同
 * APIList完成后 -> APICall
 * GankImpl完成后 -> entityList.add(new Entity(GankAPI.class, new GankImpl()));
 */
@ZClass(entitys = {
        @Entity2(cla = GankAPI.class, obj = GankImpl.class),
        @Entity2(cla = Gank2API.class, obj = Gank2Impl.class)})
public class Diycode {

    public static synchronized zone.com.sdk.Diycode_Injector getInstance() {
        return zone.com.sdk.Diycode_Api.getInstance();
    }


    public static class Entity {
        public final Class interfaceClass;
        public final Object instance;

        public Entity(Class interfaceClass, Object instance) {
            this.interfaceClass = interfaceClass;
            this.instance = instance;
        }
    }

}
