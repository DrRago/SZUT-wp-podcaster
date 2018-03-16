package backend.LameQueue;

import backend.MediaFactory.Lame;
import backend.wordpress.Blog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class LameQueue extends ArrayList<Lame> {
    private Blog wordpress;

    public LameQueue(Blog wordpress) {
        this.wordpress = wordpress;
    }

    public boolean add(Lame encoder) {
        this.add(encoder);
        return false;
    }

    public boolean startQueue() throws Exception {
        for (Lame item : this) {
            item.executeCommand();
            wordpress.addPost(item);
        }
        return true;
    }

    public void executeSetter(final Object value, final String methodName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.out.println(Object.class.getClass().getName());
        Method method;
        for (Lame item : this) {
            method = item.getClass().getMethod(methodName, Object.class);
            method.invoke(item, value);
        }
    }

    public void setTitle(final String title) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        executeSetter(title, "setID3_Title");
    }
}
