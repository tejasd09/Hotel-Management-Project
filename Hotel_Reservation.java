package com.JDBC.Statement;
import java.sql.*;
import java.util.Scanner;


public class Hotel_Reservation {
    private static final String url="jdbc:mysql://localhost:3306/hotel_db";
    private static final String user="root";
    private static final String pass="2003";



    public static void main(String[] args)throws ClassNotFoundException {
        Scanner sc=new Scanner(System.in);
        try{
            Class.forName("con.mysql.jdbc.Driver");
        }catch(ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }

        try
        {
            Connection con=DriverManager.getConnection(url,user,pass);
            Statement stmt=con.createStatement();

            while(true)
            {
                System.out.println("MENU...");
                System.out.println("1.Reserve a room");
                System.out.println("2.View Reservation");
                System.out.println("3.Get room Number");
                System.out.println("4.Update reservation");
                System.out.println("5.Delete Reservation");
                System.out.println("0.Exit");
                System.out.println("Enter choice: " );
                int choice=sc.nextInt();

                switch(choice)
                {
                    case 1:
                        reserveRoom(con,stmt,sc);
                        System.out.println();
                        break;

                    case 2:
                        ViewReservation(con,stmt,sc);
                        System.out.println();
                        break;

                    case 3:
                        GetRoomNumber(con,stmt,sc);
                        System.out.println();
                        break;

                    case 4:
                        UpdateReservation(con,stmt,sc);
                        System.out.println();
                        break;

                    case 5:
                        deleteReservation(con,stmt,sc);
                        System.out.println();
                        break;

                    case 0:
                        int a=5;
                        System.out.print("EXITING SYSTEM");
                        while(a>=0)
                        {

                            System.out.print(".");
                            Thread.sleep(450);
                            a--;
                        }
                        System.out.println();
                        System.out.println("Thank u for visiting us........!!!");
                        return;
                }
            }
        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private static void reserveRoom(Connection con,Statement stmt, Scanner sc)
    {
        System.out.println("Enter the guest name: ");
        String name=sc.next();
        System.out.println("Enter room number:");
        int room_no=sc.nextInt();
        System.out.println("Enter contact number");
        String contact_no=sc.next();

        String query="INSERT INTO reservation (guest_name,room_number,contact_number) values(' "  + name + " ', " + room_no + ", ' " + contact_no + " ');";
        try{
            int rowaffected=stmt.executeUpdate(query);
            if(rowaffected>0)
            {
                System.out.println("Reservation Successfull");
            }
            else {
                System.out.println("Reservation Unsuccessfull");
            }
        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }


    private static void ViewReservation(Connection con,Statement stmt, Scanner sc)
    {
        String query="SELECT * FROM reservation;";
        try
        {
            ResultSet rs=stmt.executeQuery(query);

            while(rs.next())
            {

                int reservation_id=rs.getInt("reservation_id");
                String guest_name=rs.getString("guest_name");
                int room_number=rs.getInt("room_number");
                String contact_number=rs.getString("contact_number");
                String reservation_date=rs.getTimestamp("reservation_date").toString();

                System.out.println("Reservation_id : " + reservation_id);
                System.out.println("Guest_name : " + guest_name);
                System.out.println("Room_Number : " + room_number);
                System.out.println("Contact_Number: " + contact_number);
                System.out.println("Reservation_Date: " + reservation_date);
                System.out.println("****************************************");
            }


        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }


    private static void GetRoomNumber(Connection con, Statement stmt, Scanner sc)
    {
        System.out.println("Enter reservation id: ");
        int id=sc.nextInt();

        String query="Select room_number from reservation where reservation_id = " + id;


        try{
            ResultSet rs=stmt.executeQuery(query);

            if(rs.next())
            {
                int room=rs.getInt("room_number");
                System.out.println("The room number for reservation id " + id + " is: " + room) ;


            }
            else {
                System.out.println("No reservation detected");
            }

        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }



    private static void UpdateReservation(Connection con , Statement stmt, Scanner sc)
    {
        System.out.println("Enter reservation id to update: ");
        int id=sc.nextInt();

        if(!reservationExists(con,stmt,id))
        {
            System.out.println("Reservation not found for given ID");
            return;
        }

        System.out.println("Enter new guest name:");
        String name=sc.next();
        System.out.println("Enter new room number:");
        int roomNo=sc.nextInt();
        System.out.println("Enter new contact:");
        String contactNo=sc.next();

        String query="Update reservation set guest_name= '" + name + "'," + "room_number="+ roomNo + ", " + "contact_number='" + contactNo+"'" + "where reservation_id="+id;

        try
        {
            int rowaffected=stmt.executeUpdate(query);

            if(rowaffected>0)
            {
                System.out.println("Reservation Updated successfully");
            }
            else {
                System.out.println("Reservation update fail");
            }
        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteReservation(Connection con,Statement stmt, Scanner sc)
    {
        System.out.println("Enter the reservation id whose data is to be deleted: ");
        int id=sc.nextInt();
        String query="Delete  from reservation where reservation_id="+id;
        try
        {
            if(!reservationExists(con,stmt,id))
            {
                System.out.println("Reservation not found for given id");
                return;
            }
            else {
                int rowaffected=stmt.executeUpdate(query);

                if(rowaffected>0)
                {
                    System.out.println("Deleted Successfully");
                }
                else {
                    System.out.println("Cannot delete");
                }
            }
        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static boolean reservationExists(Connection con ,Statement stmt, int id)
    {
        try
        {
            String query="Select reservation_id from reservation where reservation_id = " + id;
            ResultSet rs=stmt.executeQuery(query);
            return rs.next();
        }catch(SQLException q)
        {
            System.out.println(q.getMessage());
            return false;
        }
    }


}
