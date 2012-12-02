package com.magsambols.yapb.act01;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ContactDataSource {

  // Database fields
  private SQLiteDatabase database;
  private MySQLiteHelper dbHelper;
  private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
      MySQLiteHelper.COLUMN_CONTACT, MySQLiteHelper.COLUMN_PHONE };

  public ContactDataSource(Context context) {
    dbHelper = new MySQLiteHelper(context);
  }

  public void openWritable() throws SQLException {
	 // dbHelper.onUpgrade(database, 1, 1);
	  database = dbHelper.getWritableDatabase();
  }
  
  public void openReadable() throws SQLException{
	  database=dbHelper.getReadableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public void createContact(Contact storeContact) {
	ContentValues values = new ContentValues();
    values.put(MySQLiteHelper.COLUMN_CONTACT, storeContact.getName());
    values.put(MySQLiteHelper.COLUMN_PHONE, storeContact.getPhoneNum());
    long insertId = database.insert(MySQLiteHelper.TABLE_CONTACTS, null,
        values);
    Cursor cursor = database.query(MySQLiteHelper.TABLE_CONTACTS,
        allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    cursor.close();
     }

  public void deleteContact(Contact chosenContact) {
    long id =chosenContact.getId();
    System.out.println("Contact deleted with id: " + id);
    database.delete(MySQLiteHelper.TABLE_CONTACTS, MySQLiteHelper.COLUMN_ID
        + " = " + id, null);
  }

 
  
  public List<Contact> getAllContacts() {
    List<Contact> allContacts = new ArrayList<Contact>();

    Cursor cursor = database.query(MySQLiteHelper.TABLE_CONTACTS,
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Contact contact = cursorToContact(cursor);
      allContacts.add(contact);
      cursor.moveToNext();
    }
    // Make sure to close the cursor
    cursor.close();
    return allContacts;
  }
  
  public void delete_byID(long id){
	  database.delete(MySQLiteHelper.TABLE_CONTACTS, MySQLiteHelper.COLUMN_ID+"="+id, null);
	 }
	 
 public void update_byID(long id, String owner, String phone){
	  ContentValues values = new ContentValues();
	  values.put(MySQLiteHelper.COLUMN_CONTACT, owner);
	  values.put(MySQLiteHelper.COLUMN_PHONE, phone);
	  database.update(MySQLiteHelper.TABLE_CONTACTS, values, MySQLiteHelper.COLUMN_ID+"="+id, null);
	  }
	 
  private Contact cursorToContact(Cursor cursor) {
    Contact contact = new Contact();
    contact.setId(cursor.getLong(0));
    contact.setName(cursor.getString(1));
    contact.setPhoneNum(cursor.getString(2));
    return contact;
  }
  
  
} 