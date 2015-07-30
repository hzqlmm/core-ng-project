package core.framework.impl.template.expression;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author neo
 */
public class ExpressionTranslatorTest {
    ExpressionParser parser = new ExpressionParser();

    @Test
    public void text() {
        String expression = new ExpressionTranslator(parser.parse("\"text\""), new CallTypeStack(Object.class)).translate();
        Assert.assertEquals("\"text\"", expression);
    }

    @Test
    public void field() {
        String expression = new ExpressionTranslator(parser.parse("field"), new CallTypeStack(Object.class)).translate();
        Assert.assertEquals("$root.field", expression);
    }

    @Test
    public void method() {
        String expression = new ExpressionTranslator(parser.parse("method()"), new CallTypeStack(Object.class)).translate();
        Assert.assertEquals("$root.method()", expression);
    }

    @Test
    public void builtinMethod() {
        String expression = new ExpressionTranslator(parser.parse("#html(field)"), new CallTypeStack(Object.class)).translate();
        Assert.assertEquals("stack.function(\"html\").apply(new Object[]{$root.field})", expression);
    }

    @Test
    public void contextVariable() {
        CallTypeStack stack = new CallTypeStack(Object.class);
        stack.paramClasses.put("item", Object.class);
        String expression = new ExpressionTranslator(parser.parse("item"), stack).translate();
        Assert.assertEquals("item", expression);
    }

    @Test
    public void methodCall() {
        CallTypeStack stack = new CallTypeStack(Object.class);
        stack.paramClasses.put("item", Object.class);
        Token expression = parser.parse("#html(field.method(), item.field, \"text\")");
        Assert.assertEquals("stack.function(\"html\").apply(new Object[]{$root.field.method(),item.field,\"text\"})", new ExpressionTranslator(expression, stack).translate());
    }

    @Test
    public void methodWithNumberParam() {
        String expression = new ExpressionTranslator(parser.parse("field.method(1)"), new CallTypeStack(Object.class)).translate();
        Assert.assertEquals("$root.field.method(1)", expression);
    }
}