package com.lyx.sample.annotation;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

import java.lang.reflect.Field;

/**
 * IdParser
 * <p/>
 * Created by luoyingxing on 2017/9/20.
 */
public class IdParser {

    /**
     * if it has been use in the activity, this method batter called in the onCreate();
     * <p>
     * if it has been use in the fragment, this method batter called in the onViewCreate();
     * <p>
     *
     * @param object Activity , fragment or view
     */
    public static void inject(Object object) {
        try {
            parse(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parse(Object object) throws Exception {
        final Class<?> clazz = object.getClass();
        View view = null;
        //Traverse all the members of an object variable.
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                Id injectView = field.getAnnotation(Id.class);
                int id = injectView.id();
                boolean onTouch = injectView.onTouch();
                boolean onClick = injectView.onClick();

                if (id < 0) {
                    throw new ParserException("View id must not be null!");
                } else {
                    //if field is private,must setAccessible(true), then can access it
                    field.setAccessible(true);

                    if (object instanceof Activity) {
                        view = ((Activity) object).findViewById(id);

                        if (onClick) {
                            if (object instanceof View.OnClickListener) {
                                view.setOnClickListener((View.OnClickListener) object);
                            } else {
                                throw new ParserException("this Activity must implements View.OnClickListener!");
                            }
                        }

                        if (onTouch) {
                            if (object instanceof View.OnTouchListener) {
                                view.setOnTouchListener((View.OnTouchListener) object);
                            } else {
                                throw new ParserException("this Activity must implements View.OnTouchListener!");
                            }
                        }

                    } else if (object instanceof Fragment) {
                        if (((Fragment) object).getView() == null) {
                            throw new ParserException("Fragment's root view is null");
                        }
                        view = ((Fragment) object).getView().findViewById(id);

                        if (onClick) {
                            if (object instanceof View.OnClickListener) {
                                view.setOnClickListener((View.OnClickListener) object);
                            } else {
                                throw new ParserException("this Activity must implements View.OnClickListener!");
                            }
                        }

                        if (onTouch) {
                            if (object instanceof View.OnTouchListener) {
                                view.setOnTouchListener((View.OnTouchListener) object);
                            } else {
                                throw new ParserException("this Activity must implements View.OnTouchListener!");
                            }
                        }

                    } else if (object instanceof View) {
                        view = ((View) object).findViewById(id);
                    }

                    field.set(object, view);
                }
            }
        }
    }
}