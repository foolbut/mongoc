package self.foolbut.mongoc.annotation;

@java.lang.annotation.Inherited
@java.lang.annotation.Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Target(value={java.lang.annotation.ElementType.TYPE})
public @interface Collection {

    String value();
    String partitionBy() default "";
}
