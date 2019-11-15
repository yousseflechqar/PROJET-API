package dialect;

import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class CustomMySQLDialect extends MySQL5Dialect {

    public CustomMySQLDialect() {
        super();

//        registerFunction("datediff", new StandardSQLFunction("datediff", StandardBasicTypes.INTEGER));
//        registerFunction("datediff", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "datediff('DAY', ?2, ?1)"));
        registerFunction( "date_diff", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "?1-?2") );

    }


}
