package core.framework.impl.db;

import core.framework.api.db.Column;
import core.framework.api.db.PrimaryKey;
import core.framework.api.db.Table;
import core.framework.api.validate.Length;
import core.framework.api.validate.NotNull;

import java.math.BigDecimal;

/**
 * @author neo
 */
@Table(name = "assigned_id_entity")
public class AssignedIdEntity {
    @PrimaryKey
    @Column(name = "id")
    @Length(max = 36)
    public String id;

    @Length(max = 20)
    @Column(name = "string_field")
    public String stringField;

    @NotNull
    @Column(name = "int_field")
    public Integer intField;

    @Column(name = "big_decimal_field")
    public BigDecimal bigDecimalField;
}
