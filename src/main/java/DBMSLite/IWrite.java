package DBMSLite;

import java.util.ArrayList;

/**
 * This is an interface that has the abstract methods that will write to the table stored as file during c
 * create, insert and update query.
 */
public interface IWrite
{
    public boolean createTableInFile(String tableName, ArrayList<String> columns, ArrayList<String> dataTypes);
    public boolean insertIntoFile(String tableName,ArrayList<String> values);
    public boolean deleteAllFromFile(String tableName);
    public boolean deleteRecord(String tableName,String columnValue);
    public String fetchAllLines(String tableName);
    public String fetchLinesWhereConditionMet(String tableName, String columnValue);
    public  boolean updateAllLines(String tableName,String columnName,String columnValue);
    public boolean updateLinesWhereConditionMet(String tableName,String columnName,String newValue,String whereClauseValue);

}
