package Data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class DataAccess {

    private Connection con;
    private final String className = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private final String connectURL = "jdbc:sqlserver://localhost;databaseName=ManagementSystemDB;integratedSecurity=true";

    public static void main(String[] args)
    {

        DataAccess dao = new DataAccess();
        
        // Add Cell
        //dao.addCell("Users", "12345", "first", "last", "a@b.com", "true", "goalkeeper", "history");
        //dao.addCell("Users", "123456", "first", "last", "a@cb.com", "true", "goalkeeper", "history");
        //dao.addCell("Users", "123457", "first", "last", "a@bd.com", "true", "goalkeeper", "history");


        // Check if id exists
        //System.out.println(dao.isIDExists("Users", "12345"));

        // Delete a row
        // boolean success = dao.deleteRow("Users", "12345");

        // Add value to existing cell's value
        //dao.addToExistingCellValue("Complaints", "Description", "123456", "GG");

        // Get Cell specific value
        //String val = dao.getCellValue("Complaints", "Description", "12345");

        // Get Cell values (whole row)
        //List<String> res = dao.getAllCellValues("Complaints", "12345");

        // Get all table
        ///List<String> res2 = dao.getAllTableValues("Complaints");

        // Update cell to new value
        //dao.updateCellValue("Complaints", "Description", "12345", "new Value");


    }

    public DataAccess()
    {

        try
        {
            Class.forName(className);
            con = DriverManager.getConnection(connectURL);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public boolean deleteRow(String tableName, String id)
    {
        PreparedStatement ps = null;
        String val = "";

        String statement = "DELETE FROM " + tableName + " WHERE ID = ?";

        try
        {
            ps = con.prepareStatement(statement);
            ps.setString(1, id);

            ps.executeUpdate();
            closePS(ps);
            return true;

        }
        catch (Exception e)
        {
            closePS(ps);
            e.printStackTrace();
            return false;
        }

    }

    public boolean deleteIdFromAllTables(String id)
    {

        PreparedStatement ps = null;
        String[] tablesNames = {"Admins", "Passwords", "Users", "Referees", "UnionRepresentatives", "Coaches", "Fans", "Fields", "Players", "TeamManagers", "TeamOwners", "PersonalPages", "Teams", "Leagues", "Seasons", "LeaguesInSeasons", "Complaints", "Games", "EventReports", "OfflineUsersNotifications", "Events"};
        String statement;

        for (String tableName : tablesNames)
        {
            statement = "DELETE FROM " + tableName + " WHERE ID = ?";

            try
            {
                ps = con.prepareStatement(statement);
                ps.setString(1, id);
                ps.executeUpdate();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            List<String> fieldNames = getTableColumnNames(tableName);
            fieldNames.remove("ID");

            for (String fieldName : fieldNames)
            {
                String secStatement = "(SELECT ID , " + fieldName + " FROM " + tableName + " WHERE (" + fieldName + " NOT LIKE '%,%' AND " + fieldName + " LIKE '%" + id + "') OR (" + fieldName + " LIKE '%," + id + "%' OR " + fieldName + " LIKE '%" + id + ",%' OR " + fieldName + " LIKE '%," + id + ",%'))";

                try
                {
                    ps = con.prepareStatement(secStatement);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next())
                    {
                        String tmpID = rs.getString(1);
                        String value = rs.getString(2);

                        value = removeSubString(value, id);
                        updateCellValue(tableName, fieldName, tmpID, value);
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

        }

        try
        {
            closePS(ps);
            return true;
        }
        catch (Exception e)
        {
            closePS(ps);
            e.printStackTrace();
            return false;
        }
    }

    private String removeSubString(String original, String removeSt)
    {

        if (!original.contains(","))
            original = original.replace(removeSt, "");

        else
        {
            String[] opts = {","+removeSt+"," , removeSt+"," , ","+removeSt};

            for (String opt : opts)
                if (original.contains(opt))
                {
                    original = original.replace(opt, "");
                    break;
                }
        }

        return original;

    }

    public List<String> getAllCellValues(String TableName, String ID)
    {
        List<String> res = new LinkedList<>();

        PreparedStatement ps = null;
        String val = "";

        String statement = "SELECT * FROM " + TableName + " WHERE ID = ?";

        try
        {
            ps = con.prepareStatement(statement);
            ps.setString(1, ID);

            ResultSet rs = ps.executeQuery();
            int columns = rs.getMetaData().getColumnCount();

            while (rs.next())
            {
                for (int i = 1; i < columns + 1; i++)
                    res.add(rs.getString(i).trim());
            }

            closePS(ps);

        }
        catch (Exception e)
        {
            closePS(ps);
            e.printStackTrace();
        }

        return res;

    }

    public String getCellValue(String TableName, String ColumnName, String ID)
    {
        PreparedStatement ps = null;
        String val = "";


        String statement = "SELECT " + ColumnName + " FROM " + TableName + " WHERE ID = ?";

        try
        {
            ps = con.prepareStatement(statement);
            ps.setString(1, ID);

            ResultSet rs = ps.executeQuery();

            rs.next();
            val = rs.getString(1);
            closePS(ps);

        }
        catch (Exception e)
        {
            closePS(ps);
            e.printStackTrace();
        }

        return val.trim();

    }

    public List<String> getAllFieldValues(String tableName, String fieldName)
    {
        List<String> res = new LinkedList<>();

        PreparedStatement ps = null;

        String statement = "SELECT " + fieldName + " FROM " + tableName;

        try
        {
            ps = con.prepareStatement(statement);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
                res.add(rs.getString(1).trim());

            closePS(ps);

        }
        catch (Exception e)
        {
            closePS(ps);
            e.printStackTrace();
        }

        return res;
    }

    public String addToExistingCellValue(String TableName, String ColumnName, String ID, String valueToAdd)
    {

        String oldValue = getCellValue(TableName, ColumnName, ID);

        PreparedStatement ps = null;
        String val = oldValue + valueToAdd;


        String statement = "UPDATE " + TableName + " SET " + ColumnName + " = ? WHERE ID = ?";

        try
        {
            ps = con.prepareStatement(statement);
            ps.setString(1, val);
            ps.setString(2, ID);

            ps.executeUpdate();
            closePS(ps);

        }
        catch (Exception e)
        {
            closePS(ps);
            e.printStackTrace();
        }

        return val.trim();

    }

    public boolean addCell(String TableName, String ... values)
    {

        PreparedStatement ps = null;
        String val="";
        int valuesNumber = values.length;
        String statement = "INSERT INTO " + TableName + " VALUES (";
        for (int i = 0; i < valuesNumber; i++)
            statement = statement + "?,";
        statement = statement.substring(0, statement.length()-1) + ")";

        try
        {
            ps = con.prepareStatement(statement);


            for (int i = 0; i < valuesNumber; i++)
            {
                val = values[i];

                if (isBoolean(val))
                    ps.setBoolean(i+1,  Boolean.parseBoolean(val));

                else if (isDateTime(val))
                    ps.setTimestamp(i+1, stringToDateTimeSQL(val));

                else if (isDate(val))
                    ps.setDate(i+1, stringToDateSQL(val));

                else if (isDouble(val))
                    ps.setDouble(i+1, Double.parseDouble(val));

                else if (isInt(val))
                    ps.setInt(i+1, Integer.parseInt(val));

                else
                    ps.setString(i+1, val);

            }
            ps.executeUpdate();
            closePS(ps);

        }
        catch (Exception e)
        {
            closePS(ps);
            return false;
        }

        return true;
    }


    private List<String> getTableColumnNames(String tableName)
    {
        List<String> res = new LinkedList<>();

        PreparedStatement ps = null;

        String statement = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + tableName +"'";

        try
        {
            ps = con.prepareStatement(statement);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
                res.add(rs.getString(1));

            closePS(ps);

        }
        catch (Exception e)
        {
            closePS(ps);
            e.printStackTrace();
        }

        return res;
    }


    public boolean isIDExists(String tableName, String id)
    {
        PreparedStatement ps = null;
        int res = 0;

        String statement = "SELECT COUNT(*) FROM " + tableName + " WHERE ID = ?";

        try
        {
            ps = con.prepareStatement(statement);
            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();

            rs.next();
            res = rs.getInt(1);
            closePS(ps);

        }
        catch (Exception e)
        {
            closePS(ps);
            e.printStackTrace();
        }

        return res == 1;

    }

    public boolean isMailExists(String mail)
    {
        PreparedStatement ps = null;
        int res = 0;

        String statement = "SELECT COUNT(*) FROM Users WHERE Mail = ?";

        try
        {
            ps = con.prepareStatement(statement);
            ps.setString(1, mail);

            ResultSet rs = ps.executeQuery();

            rs.next();
            res = rs.getInt(1);
            closePS(ps);

        }
        catch (Exception e)
        {
            closePS(ps);
            e.printStackTrace();
        }

        return res == 1;

    }

    public Date stringToDateSQL(String st)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        java.sql.Date date = null;

        try
        {
            date = new Date(sdf.parse(st).getTime());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return date;
    }

    public java.sql.Timestamp stringToDateTimeSQL(String st)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        java.sql.Timestamp dateTime = null;

        try {
            dateTime = new java.sql.Timestamp(sdf.parse(st).getTime());
        }
        catch (Exception e)
        {
            try
            {
                sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                dateTime = new java.sql.Timestamp(sdf.parse(st).getTime());

            }
            catch (Exception e2)
            {
                e.printStackTrace();
            }
        }

        return dateTime;
    }



    public boolean isDouble(String s)
    {
        try
        {
            Double.parseDouble(s);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }

    }

    public boolean isInt(String s)
    {
        try
        {
            Integer.parseInt(s);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public boolean isDateTime(String s)
    {
        return isDateTimeWithoutSeconds(s) || isDateTimeWithSeconds(s);
    }

    private boolean isDateTimeWithoutSeconds(String s)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy H:m:s");
        dateFormat.setLenient(false);

        try
        {
            dateFormat.parse(s.trim());
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private boolean isDateTimeWithSeconds(String s)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        dateFormat.setLenient(false);

        try
        {
            dateFormat.parse(s.trim());
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public boolean isDate(String s)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormat.setLenient(false);

        try
        {
            dateFormat.parse(s.trim());
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public boolean isStringExistsInField(String tableName, String fieldName, String stringToCheck)
    {

        PreparedStatement ps = null;
        int res = 0;

        String statement = "(SELECT COUNT(ID) FROM " + tableName + " WHERE (" + fieldName + " NOT LIKE '%,%' AND " + fieldName + " LIKE '%" + stringToCheck + "') OR (" + fieldName + " LIKE '%," + stringToCheck + "%' OR " + fieldName + " LIKE '%" + stringToCheck + ",%' OR " + fieldName + " LIKE '%," + stringToCheck + ",%'))";

        try
        {
            ps = con.prepareStatement(statement);
            ResultSet rs = ps.executeQuery();


            rs.next();
            res = rs.getInt(1);
            closePS(ps);

        }
        catch (Exception e)
        {
            closePS(ps);
            e.printStackTrace();
        }

        return res > 0;


    }

    public boolean isBoolean(String s)
    {
        return (s.toLowerCase().equals("true") || s.toLowerCase().equals("false"));
    }


    public boolean updateCellValue(String TableName, String ColumnName, String ID, String valueToSet)
    {
        PreparedStatement ps = null;

        try
        {
            ps = con.prepareStatement("UPDATE " + TableName + " SET " + ColumnName + " = ? WHERE ID = ?");

            if (isBoolean(valueToSet))
                ps.setBoolean(1,  Boolean.parseBoolean(valueToSet));

            else if (isDateTime(valueToSet))
                ps.setTimestamp(1, stringToDateTimeSQL(valueToSet));

            else if (isDate(valueToSet))
                ps.setDate(1, stringToDateSQL(valueToSet));

            else if (isDouble(valueToSet))
                ps.setDouble(1, Double.parseDouble(valueToSet));

            else if (isInt(valueToSet))
                ps.setInt(1, Integer.parseInt(valueToSet));

            else
                ps.setString(1, valueToSet);

            ps.setString(2, ID);
            ps.executeUpdate();
            closePS(ps);
            return true;

        }
        catch (Exception e)
        {
            if (isDateTime(valueToSet))
                System.out.println("true");
            closePS(ps);
            e.printStackTrace();
            return false;
        }
    }

    public Boolean stringToBoolean(String s)
    {
        if (s.toLowerCase().equals("true"))
            return true;

        return false;
    }
    private void closePS(PreparedStatement ps)
    {
        if (ps != null)
            try {
                ps.close();
            } catch (Exception e) {}
    }

    private void closeConnection(Connection con)
    {
        if (con != null)
            try {
                con.close();
            } catch (Exception e) {}
    }

    public void createDB()
    {
        PreparedStatement ps = null;

        try {
            String line = "";
            String query = "";

            try (BufferedReader br = new BufferedReader(new FileReader(""))) {

                while ((line = br.readLine()) != null) {

                    if (line.contains("GO") || line.contains("Go") || line.contains("GO;") || line.contains("Go;"))
                    {
                        ps = con.prepareStatement(query);
                        ps.execute();
                        query = "";
                        ps.close();
                    }
                    else
                    {
                        query = query + line;
                        query = query + "\n";
                    }
                }

                if (query != "")
                {
                    ps = con.prepareStatement(query);
                    ps.execute();
                    ps.close();
                }

                con.close();

            } catch (Exception e) {
                e.printStackTrace();

                if (ps != null)
                    try {
                        ps.close();
                    } catch (Exception e2) {}

                if (con != null)
                    try {
                        con.close();
                    } catch (Exception e3) {}

            }

        }
        catch (Exception e0) {
            e0.printStackTrace();

            if (ps != null)
                try {
                    ps.close();
                } catch (Exception e4) {}

            if (con != null)
                try {
                    con.close();
                } catch (Exception e5) {}
        }


    }
}
