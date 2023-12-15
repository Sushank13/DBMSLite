package DBMSLite;

import Logger.ILogger;
import Logger.Logger;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides the actual implementation of the IQuery interface.
 */
public class Query implements IQuery
{
    private  ILogger iLogger;
    private  IWrite iWrite;
    public Query()
    {
        iLogger=new Logger();
        iWrite=new Write();
    }
    /**
     * This method checks the type of SQL query input by the user.
     * @param query is the SQL query input by the user
     * @return the type of query i.e. create or select or delete or insert or update.
     */
    public String checkQueryType(String query)
    {
        String regEx="^(create|insert|delete|select|update|start|begin)\\s*";
        Pattern pattern=Pattern.compile(regEx);
        Matcher matcher=pattern.matcher(query);
        boolean matchFound=matcher.find();
        if(matchFound)
        {
            String queryType=matcher.group(1);
            return queryType;
        }
        return "";
    }

    /**
     * This method creates a directory which represents the database
     * @param query is the SQL query input by the user
     * @return true if the directory is created else return false
     */

    public boolean createDataBase(String query)
    {
        String regEx="create\\s*database\\s*(\\w+);?";
        String dataBaseName="";
        Pattern pattern=Pattern.compile(regEx);
        Matcher matcher=pattern.matcher(query);
        if(matcher.find())
        {
            dataBaseName=matcher.group(1);
        }
        File dataBase=new File(dataBaseName);
        boolean dbCreated=dataBase.mkdir();
        if(dbCreated)
        {
            String info=LocalDateTime.now()+":Database "+dataBaseName+" created\n";
            iLogger.log(info);
            return true;
        }
        return false;
    }

    /**
     * This method extracts the table name and column names and their data types from the given query
     * @param query is the SQL query entered by the user
     * @return true if the table is created else return false.
     */
    public boolean createTable(String query)
    {
        String regEx="create\\s*table\\s*(\\w+)\\s*\\((.+)\\);?";
        Pattern pattern=Pattern.compile(regEx);
        Matcher matcher=pattern.matcher(query);
        String tableName="";
        String remainingQuery="";
        if(matcher.find())
        {
            tableName = matcher.group(1);
            remainingQuery = matcher.group(2);
        }
        else
        {
            String info= LocalDateTime.now()+":No pattern match could be found for the given query\n";
            iLogger.log(info);
            return  false;
        }
        String regEx2="(\\w+)\\s*(\\w+)\\s*(\\((\\w*)\\))?";
        Pattern patternTwo = Pattern.compile(regEx2);
        Matcher matcherTwo = patternTwo.matcher(remainingQuery);
        ArrayList<String> columns=new ArrayList<>();
        ArrayList<String> dataTypes=new ArrayList<>();
        while (matcherTwo.find())
        {
            columns.add(matcherTwo.group(1));
            dataTypes.add(matcherTwo.group(2));
        }
        //write all the data to the file.
        boolean writerStatus=iWrite.createTableInFile(tableName,columns,dataTypes);
        if(writerStatus==true)
        {
            String info=LocalDateTime.now()+":"+tableName+" created successfully.\n";
            iLogger.log(info);
            return true;
        }
        return false;
    }

    /**
     * This method extracts the table name and the values to be inserted into that table
     * @param query is the SQL query input by the user
     * @return true if the record inserted successfully else return false
     */
    public boolean insertInTable(String query)
    {
        String regEx="insert\\s*into\\s*(\\w+)\\s*\\((.+)\\)\\s+values\\s*\\((.+)\\);?";
        Pattern pattern=Pattern.compile(regEx);
        Matcher matcher=pattern.matcher(query);
        String tableName;
        String valuesGroup;
        ArrayList<String> values;
        if(matcher.find())
        {
            tableName=matcher.group(1);
            valuesGroup=matcher.group(3);
            values=new ArrayList<>(Arrays.asList(valuesGroup.split(",")));
            boolean insertedToFile=iWrite.insertIntoFile(tableName,values);
            if(insertedToFile)
            {
                String info=LocalDateTime.now()+":"+valuesGroup+" inserted into table "+tableName+"\n";
                iLogger.log(info);
                return true;
            }
            else
            {
                String info=LocalDateTime.now()+":"+valuesGroup+" could not be inserted into table "+tableName+"\n";
                iLogger.log(info);
                return false;
            }
        }
        return false;
    }

    /**
     * This method deletes a record or all records from the table
     * @param query is the SQL query input by the user
     * @return true if the record(s) deleted successfully else return false
     */
    public boolean deleteFromTable(String query)
    {
       boolean containsWhereClause=query.contains("where");
       if(containsWhereClause)
       {
           String regEx="delete\\s*from\\s*(\\w+)\\s*where\\s*(\\w+=\\w+);?";
           String tableName="";
           String whereCondition="";
           Pattern pattern=Pattern.compile(regEx);
           Matcher matcher=pattern.matcher(query);
           if(matcher.find())
           {
               tableName=matcher.group(1);
               whereCondition=matcher.group(2);
           }
           ArrayList<String> splitWhereCondition=new ArrayList<>(Arrays.asList(whereCondition.split("=")));
           String columnName=splitWhereCondition.get(0);
           String columnValue=splitWhereCondition.get(1);
           boolean recordDeleted=iWrite.deleteRecord(tableName,columnValue);
           if(recordDeleted)
           {
               String info=LocalDateTime.now()+":Record from table "+tableName+" with column "+columnName+ " and value "+columnValue+" deleted.\n";
               iLogger.log(info);
               return true;
           }
           else
           {
               String info=LocalDateTime.now()+":Record from table "+tableName+" with column "+columnName+ " and value "+columnValue+" could not be deleted.\n";
               iLogger.log(info);
               return false;
           }
       }
       else
       {
           String regEx="delete\\s*from\\s*(\\w+);?";
           String tableName="";
           Pattern pattern=Pattern.compile(regEx);
           Matcher matcher=pattern.matcher(query);
           if(matcher.find())
           {
               tableName=matcher.group(1);
           }
           boolean allRecordsDeleted=iWrite.deleteAllFromFile(tableName);
           if(allRecordsDeleted)
           {
               String info=LocalDateTime.now()+":All records from table "+tableName+" deleted.\n";
               iLogger.log(info);
               return true;
           }
           else
           {
               String info=LocalDateTime.now()+":All records from table "+tableName+" could not be deleted.\n";
               iLogger.log(info);
               return false;
           }
       }
    }

    /**
     * This method fetches the records from a table and prints the output on the console
     * @param query is the SQL query input by the user
     */
    public void selectFromTable(String query)
    {
        String outPut;
        String tableName="";
        String whereCondition="";
        boolean hasAsterisk=query.contains("*");
        boolean hasWhereClause=query.contains("where");
        if(hasAsterisk&&!hasWhereClause)
        {
            String regEx="select\\s*\\*\\s*from\\s*(\\w+);?";
            Pattern pattern=Pattern.compile(regEx);
            Matcher matcher=pattern.matcher(query);
            if(matcher.find())
            {
                tableName=matcher.group(1);
            }
            outPut=iWrite.fetchAllLines(tableName);
            String info=LocalDateTime.now()+":All records fetched from the table "+tableName+"\n";
            iLogger.log(info);
        }
        else //(hasAsterisk&&hasWhereClause)
        {
            String regEx="select\\s*\\*\\s*from\\s*(\\w+)\\s*where\\s*(\\w+=\\w+);?";
            Pattern pattern=Pattern.compile(regEx);
            Matcher matcher=pattern.matcher(query);
            if(matcher.find())
            {
                tableName=matcher.group(1);
                whereCondition=matcher.group(2);
            }
            ArrayList<String> splitWhereCondition=new ArrayList<>(Arrays.asList(whereCondition.split("=")));
            String columnName=splitWhereCondition.get(0);
            String columnValue=splitWhereCondition.get(1);
            outPut=iWrite.fetchLinesWhereConditionMet(tableName,columnValue);
            String info=LocalDateTime.now()+":All records fetched from the table "+tableName+" where column is "+columnName+ " and value is "+columnValue+"\n";
            iLogger.log(info);

        }
        System.out.println(outPut);
    }

    /**
     * this method updates the values of a record in a table
     * @param query is the SQL query input by the user
     * @return true if the record(s) updated else return false
     */
    public boolean updateRecords(String query)
    {
        boolean containsWhereClause=query.contains("where");
        String tableName="";
        String setClause="";
        String whereClause="";
        if(containsWhereClause)
        {
            String regEx = "update\\s*(\\w+)\\s*set\\s*(\\w+=\\w+)\\s*where\\s*(\\w+=\\w+);?";
            Pattern pattern = Pattern.compile(regEx);
            Matcher matcher = pattern.matcher(query);
            if (matcher.find())
            {
                tableName = matcher.group(1);
                setClause = matcher.group(2);
                whereClause = matcher.group(3);
            }
            ArrayList<String> splitSetClause = new ArrayList<>(Arrays.asList(setClause.split("=")));
            ArrayList<String> splitWhereClause = new ArrayList<>(Arrays.asList(whereClause.split("=")));
            String columnToBeUpdated = splitSetClause.get(0);
            String newValue = splitSetClause.get(1);
            String whereClauseValue = splitWhereClause.get(1);
            boolean updateStatus = iWrite.updateLinesWhereConditionMet(tableName, columnToBeUpdated, newValue, whereClauseValue);
            if (updateStatus)
            {
                String info = LocalDateTime.now() + ":Record(s) updated from the table " + tableName + " where column is " + columnToBeUpdated + " with value " + newValue + "where "+splitWhereClause.get(0)+" is "+whereClauseValue+"\n";
                iLogger.log(info);
                return true;
            }
            else
            {
                String info = LocalDateTime.now() + ":Record(s) could not be updated from the table " + tableName + " where column is " + columnToBeUpdated + " with value " + newValue + "where "+splitWhereClause.get(0)+" is "+whereClauseValue+"\n";
                iLogger.log(info);
                return false;
            }
        }
        else
        {
            String regEX="update\\s*(\\w+)\\s*set\\s*(\\w+=\\w+);?";
            Pattern pattern=Pattern.compile(regEX);
            Matcher matcher=pattern.matcher(query);
            if(matcher.find())
            {
                tableName=matcher.group(1);
                setClause=matcher.group(2);
            }
            ArrayList<String> splitSetClause=new ArrayList<>(Arrays.asList(setClause.split("=")));
            String columnToBeUpdated=splitSetClause.get(0);
            String newValue=splitSetClause.get(1);
            boolean updateStatus=iWrite.updateAllLines(tableName,columnToBeUpdated,newValue);
            if(updateStatus)
            {
                String info=LocalDateTime.now()+":All records updated from the table "+tableName+" where column is "+columnToBeUpdated+ " with value "+newValue+"\n";
                iLogger.log(info);
                return true;
            }
            else
            {
                String info=LocalDateTime.now()+":All records could not updated from the table "+tableName+" where column is "+columnToBeUpdated+ " with value "+newValue+"\n";
                iLogger.log(info);
                return false;
            }
        }
    }

    /**
     * This method executes the queries if a commit is found in the transaction else does not execute the queries
     * if a rollback is found
     * @param query is the transaction query input by the user
     * @return true is commit is performed else return false in case of a rollback
     */
    public boolean transaction(String query)
    {
        String regEX="(start|begin)\\s*transaction\\s*;(.+);(.+);";
        Pattern pattern=Pattern.compile(regEX);
        Matcher matcher=pattern.matcher(query);
        ArrayList<String> queriesInsideTransaction=new ArrayList<>();
        String commitOrRollback="";
        if(matcher.find())
        {
            queriesInsideTransaction=new ArrayList<>(Arrays.asList(matcher.group(2).split(";")));
            commitOrRollback=matcher.group(3);
        }
        if(commitOrRollback.equals("commit"))
        {
            for(String transactionQuery:queriesInsideTransaction)
            {
                String queryType=checkQueryType(transactionQuery);
                switch (queryType)
                {
                    case "create":
                        boolean tableCreationStatus = createTable(transactionQuery);
                        if (tableCreationStatus)
                        {
                            System.out.println("Table created successfully!");
                        }
                        else
                        {
                            System.out.println("Table could not be created.");
                        }
                        break;
                    case "select":
                        selectFromTable(transactionQuery);
                        break;
                    case "insert":
                        boolean insertionStatus = insertInTable(transactionQuery);
                        if (insertionStatus)
                        {
                            System.out.println("Record inserted successfully!");
                        }
                        else
                        {
                            System.out.println("Record could not be inserted successfully!");
                        }
                        break;
                    case "update":
                        boolean updateStatus =updateRecords(transactionQuery);
                        if (updateStatus)
                        {
                            System.out.println("Record(s) updated successfully!");
                        }
                        else
                        {
                            System.out.println("Record(s) could not be updated successfully!");
                        }
                        break;
                    case "delete":
                        boolean deletionStatus =deleteFromTable(transactionQuery);
                        if (deletionStatus)
                        {
                            System.out.println("Record(s) deleted successfully!");
                        }
                        else
                        {
                            System.out.println("Record(s) could not be deleted successfully!");
                        }
                        break;
                    default:
                        System.out.println("Not a SQL query");
                        break;
                }
            }
            return true;
        }
        else
        {
            queriesInsideTransaction.clear(); //emptying the queries stored in the list
            return false;
        }
    }

}
