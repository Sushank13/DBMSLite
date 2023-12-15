package DBMSLite;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is a class that implements the interface IWrite.
 */
public class Write implements IWrite
{
    /**
     * This method creates a file with the table name and inserts the column names as the headers
     * to the file. It also stores the variable names and their data types separately.
     * @param tableName is the name of file to be created
     * @param columns is the columns to be added as the headers to the file
     * @param dataTypes is the data type of the column
     * @return true is the files created else return false.
     */
    public boolean createTableInFile(String tableName, ArrayList<String> columns, ArrayList<String> dataTypes)
    {
        File table =new File(tableName);
        File tableColumnDataTypes=new File(tableName+"ColumnDetails");
        StringBuilder columnNames=new StringBuilder();
        StringBuilder columnAndDataTypes=new StringBuilder();
        for(int i=0;i<=dataTypes.size()-1;i++)
        {
            columnNames.append(columns.get(i)).append("|");
            columnAndDataTypes.append(columns.get(i)).append(":").append(dataTypes.get(i)).append("\n");
        }
        try(FileWriter fileWriter= new FileWriter(table))
        {
             fileWriter.write(columnNames.toString());
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
            return  false;
        }
        try(FileWriter fileWriter= new FileWriter(tableColumnDataTypes))
        {
            fileWriter.write(columnAndDataTypes.toString());
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * This method writes values to the file.
     * @param tableName is the name of the file in which values are to be inserted
     * @param values are the values to be written
     * @return true is the values written to file else return false.
     */
    public boolean insertIntoFile(String tableName,ArrayList<String> values)
    {
        try(FileWriter fileWriter=new FileWriter(tableName,true))
        {
            StringBuilder valuesToBeInserted=new StringBuilder();
           for(int i=0;i<=values.size()-1;i++)
            {
                valuesToBeInserted.append(values.get(i)).append("|");
            }
           fileWriter.write("\n"+valuesToBeInserted);
           return true;
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * This method empties the file and then re-inserts the first line of the file i.e. columns
     * @param tableName is the name of the file to be emptied
     * @return true if the file was emptied and first line re-inserted else return false
     */
    public boolean deleteAllFromFile(String tableName)
    {
        try(BufferedReader bufferedReader= new BufferedReader(new FileReader(tableName)))
        {
           String firstLine=bufferedReader.readLine();
           PrintWriter printWriter=new PrintWriter(tableName);
           printWriter.print(""); //emptying the file
           printWriter.print(firstLine);//writing the column names
           printWriter.close();
           return true;
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * This method deletes only those records for which the where condition is satisfied
     * @param tableName is the name of the file which contains the content of the table
     * @param columnValue is the value for mentioned in the where clause
     * @return true is the record(s) which satisfy the where condition are deleted else return false
     */
    public boolean deleteRecord(String tableName,String columnValue)
    {
        try(BufferedReader bufferedReader=new BufferedReader(new FileReader(tableName)))
        {
            List<String> linesNotToBeDeleted=new ArrayList<>();
            String line;
            while((line=bufferedReader.readLine())!=null)
            {
                if(!line.contains(columnValue))
                {
                  linesNotToBeDeleted.add(line);
                }
            }
            PrintWriter printWriter=new PrintWriter(tableName);
            for(String requiredLine:linesNotToBeDeleted )
            {
                printWriter.write(requiredLine+"\n");
            }
            printWriter.close();
            return true;
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * This method fetches all the content from a file.
     * @param tableName is the name of the file from where the content needs to be fetched
     * @return all the content in form of a string
     */
    public String fetchAllLines(String tableName)
    {
        StringBuilder allLines=new StringBuilder();
        try(BufferedReader bufferedReader=new BufferedReader(new FileReader(tableName)))
        {
            String line;
            while((line=bufferedReader.readLine())!=null)
            {
                allLines.append(line).append("\n");
            }
            return allLines.toString();
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
        return "";
    }

    /**
     * This method fetches only those lines which meet the where condition
     * @param tableName is the name of the file from which the records need to be fetched
     * @param columnValue is the value provided in the where clause
     * @return a string which has the required output
     */
    public String fetchLinesWhereConditionMet(String tableName, String columnValue)
    {
        StringBuilder requiredLines=new StringBuilder();
        try(BufferedReader bufferedReader=new BufferedReader(new FileReader(tableName)))
        {
            String line=bufferedReader.readLine();
            requiredLines.append(line).append("\n"); //first line is needed as it contains the column names
            while((line=bufferedReader.readLine())!=null)
            {
                if(line.contains(columnValue))
                {
                    requiredLines.append(line).append("\n");
                }
            }
            return requiredLines.toString();
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
        return "";
    }

    /**
     * this method updates all the lines of the given column with the given value
     * @param tableName is the name of the file from which the records need to be fetched
     * @param columnName is the column name provided in the set clause
     * @param columnValue is the value provided in the set clause
     * @return true is the values are written to the file else return false
     */
    public  boolean updateAllLines(String tableName,String columnName,String columnValue)
    {
        try(BufferedReader bufferedReader=new BufferedReader(new FileReader(tableName)))
        {
           String line=bufferedReader.readLine();
           String columnNames[]= line.split("\\|");
           int indexOfColumn=0;
           for(int i=0;i<=columnNames.length-1;i++)
           {
               if(columnNames[i].equals(columnName))
               {
                   indexOfColumn=i;
                   break;
               }
           }
           StringBuilder updatedLines=new StringBuilder();
           updatedLines.append(line).append("\n");
           while((line=bufferedReader.readLine())!=null)
           {
               ArrayList<String> valuesInLine=new ArrayList<>(Arrays.asList(line.split("\\|")));
               valuesInLine.set(indexOfColumn,columnValue);
               for(String value: valuesInLine)
               {
                   updatedLines.append(value).append("|");
               }
               updatedLines.append("\n");
           }
           PrintWriter printWriter=new PrintWriter(tableName);
           printWriter.write(updatedLines.toString());
           printWriter.close();
           return true;
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public boolean updateLinesWhereConditionMet(String tableName,String columnName,String newValue,String whereClauseValue)
    {
        try(BufferedReader bufferedReader=new BufferedReader(new FileReader(tableName)))
        {
            String line=bufferedReader.readLine();
            String columnNames[]= line.split("\\|");
            int indexOfColumn=0;
            for(int i=0;i<=columnNames.length-1;i++)
            {
                if(columnNames[i].equals(columnName))
                {
                    indexOfColumn=i;
                    break;
                }
            }
            StringBuilder updatedLines=new StringBuilder();
            updatedLines.append(line).append("\n");
            while((line=bufferedReader.readLine())!=null)
            {
                if(line.contains(whereClauseValue))
                {
                    ArrayList<String> valuesInLine=new ArrayList<>(Arrays.asList(line.split("\\|")));
                    valuesInLine.set(indexOfColumn,newValue);
                    for(String value: valuesInLine)
                    {
                        updatedLines.append(value).append("|");
                    }
                    updatedLines.append("\n");
                }
                else
                {
                    updatedLines.append(line).append("\n");
                }

            }
            PrintWriter printWriter=new PrintWriter(tableName);
            printWriter.write(updatedLines.toString());
            printWriter.close();
            return true;
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }

        return false;
    }
}
