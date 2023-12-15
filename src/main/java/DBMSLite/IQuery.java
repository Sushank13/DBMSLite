package DBMSLite;

/**
 * This is an interface which has abstract methods to check the type of query input by the user,
 * and then execute that type of query such as create table, select from table , insert into table,
 * update table, delete from table.
 */
public interface IQuery
{
    public String checkQueryType(String query);
    public boolean createTable(String query);
    public boolean insertInTable(String query);
    public boolean deleteFromTable(String query);
    public void selectFromTable(String query);
    public boolean updateRecords(String query);
    public boolean transaction(String query);
    public boolean createDataBase(String query);

}
