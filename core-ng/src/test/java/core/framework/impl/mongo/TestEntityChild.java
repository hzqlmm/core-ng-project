package core.framework.impl.mongo;

import core.framework.api.mongo.Field;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * @author neo
 */
public class TestEntityChild {
    @Field(name = "boolean_field")
    public Boolean booleanField;

    @Field(name = "enum_field")
    public TestEnum enumField;

    @Field(name = "enum_list_field")
    public List<TestEnum> enumListField;

    @Field(name = "ref_id_field")
    public ObjectId refId;

    public enum TestEnum {
        ITEM1, ITEM2
    }
}
