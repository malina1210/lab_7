package com.company;
import java.sql.*;

    public class JDBCPopulate
    {

        private static final String words[]
                = { "One",      "Two",      "Three",    "Four",     "Five",
                "Six",      "Seven",    "Eight",    "Nine",     "Ten",
                "Eleven",   "Twelve",   "Thirteen", "Fourteen", "Fifteen",
                "Sixteen",  "Seventeen","Eighteen", "Nineteen", "Twenty" };

        public static void main (String[] parameters)
        {

            if (parameters.length != 3) {
                System.out.println("");
                System.out.println("Формат:");
                System.out.println("");
                System.out.println("   JDBCPopulate system collectionName tableName");
                System.out.println("");
                System.out.println("");
                System.out.println("Например:");
                System.out.println("");
                System.out.println("");
                System.out.println("   JDBCPopulate MySystem MyLibrary MyTable");
                System.out.println("");
                return;
            }

            String system           = parameters[0];
            String collectionName   = parameters[1];
            String tableName        = parameters[2];

            Connection connection   = null;

            try {
                DriverManager.registerDriver(new com.ibm.as400.access.AS400JDBCDriver());

                connection = DriverManager.getConnection ("jdbc:as400://"
                        + system + "/" + collectionName);

                try {
                    Statement dropTable = connection.createStatement ();
                    dropTable.executeUpdate ("DROP TABLE " + tableName);
                }
                catch (SQLException e)
                {
                }


                Statement createTable = connection.createStatement ();
                createTable.executeUpdate ("Создать таблицу " + tableName
                        + " (I INTEGER, WORD VARCHAR(20), SQUARE INTEGER, "
                        + " SQUAREROOT DOUBLE)");


                PreparedStatement insert = connection.prepareStatement ("Добавить в "
                        + tableName + " (I, WORD, SQUARE, SQUAREROOT) "
                        + " VALUES (?, ?, ?, ?)");


                for (int i = 1; i <= words.length; ++i) {
                    insert.setInt (1, i);
                    insert.setString (2, words[i-1]);
                    insert.setInt (3, i*i);
                    insert.setDouble (4, Math.sqrt(i));
                    insert.executeUpdate ();
                }

                System.out.println ("В таблицу " + collectionName + "." + tableName
                        + " внесены данные.");
            }
            catch (Exception e) {
                System.out.println ();
                System.out.println ("Ошибка: " + e.getMessage());
            }
            finally
            {
                try {
                    if (connection != null)
                        connection.close ();
                }
                catch (SQLException e)
                {
                }
            }

            System.exit (0);
        }

    }

