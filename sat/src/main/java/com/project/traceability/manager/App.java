package com.project.traceability.manager;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Extract_Identifiers e1=new Extract_Identifiers();
        e1.extactClass("Customer details","the system shall record customer details, such as name, dob, address, telephone number and account number.");
    }
}
