package com.magsambols.yapb.act01;

import java.util.ArrayList;
import java.util.List;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TestDatabaseActivity extends ListActivity {

	private ContactDataSource datasource = new ContactDataSource(this);

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_database);
        
        this.onResume();
        this.xtractContacts();
        List<Contact> values = datasource.getAllContacts();
        // Use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<Contact> adapter = new ArrayAdapter<Contact>(this,
            android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
        
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
    	super.onListItemClick(l, v, position, id);
    	Object o=this.getListAdapter().getItem(position);
    	final Contact c=(Contact)o;
    	final ArrayAdapter<Contact> listadapter = (ArrayAdapter<Contact>) getListAdapter();
	       
    	Toast.makeText(this, c.getName(), Toast.LENGTH_SHORT).show(); 	
    	     
    	//pop up box         
          AlertDialog.Builder myDialog= new AlertDialog.Builder(TestDatabaseActivity.this);        
       	  myDialog.setTitle("Delete/Edit?");
       	          
       	           TextView dialogTxt_id = new TextView(TestDatabaseActivity.this);
       	           LayoutParams dialogTxt_idLayoutParams
       	            = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
       	           
       	          
       	           final EditText ownerName = new EditText(TestDatabaseActivity.this);
       	           LayoutParams ownerNameLayoutParams
       	            = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
       	           ownerName.setLayoutParams(ownerNameLayoutParams);
       	           ownerName.setText(c.getName());
       	          
       	           final EditText phoneNum = new EditText(TestDatabaseActivity.this);
       	           LayoutParams phoneNumLayoutParams
       	            = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
       	           phoneNum.setLayoutParams(phoneNumLayoutParams);
       	           phoneNum.setText(c.getPhoneNum());
       	          
       	           LinearLayout layout = new LinearLayout(TestDatabaseActivity.this);
       	           layout.setOrientation(LinearLayout.VERTICAL);
       	           layout.addView(dialogTxt_id);
       	           layout.addView(ownerName);
       	           layout.addView(phoneNum);
       	           myDialog.setView(layout);
       	           
       	           myDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
       	        	
       	        	   public void onClick(DialogInterface arg0, int arg1) {
       	                datasource.delete_byID(c.getId());
             	          listadapter.remove(c);
             	         listadapter.notifyDataSetChanged();              	               	                }
       	               });
       	          
       	           myDialog.setNeutralButton("Update", new DialogInterface.OnClickListener() {
       	             
       	        	   public void onClick(DialogInterface arg0, int arg1) {
       	            	 
       	            	   String value1 = ownerName.getText().toString();
       	                String value2 = phoneNum.getText().toString();
       	                datasource.update_byID(c.getId(),value1, value2);
       	                c.setName(value1);
       	                c.setPhoneNum(value2);
       	                setListAdapter(listadapter);  
       	                 listadapter.notifyDataSetChanged();
       	              
       	                }
       	               });
       	          
       	           myDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
       	               // do something when the button is clicked
       	               public void onClick(DialogInterface arg0, int arg1) {
       	       
       	                }
       	               });
       	           
       	           myDialog.show();
       	   
    	  }
 
      @Override
      protected void onResume() {
        datasource.openWritable();
        super.onResume();
      }

      @Override
      protected void onPause() {
        datasource.close();
        super.onPause();
      }
      
      private void xtractContacts()
      {
      	List<Contact> contactList=new ArrayList<Contact>();
      	Uri uri=ContactsContract.Contacts.CONTENT_URI;
      	ContentResolver cr=getContentResolver();
      	String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
      	Cursor cur=cr.query(uri, null, null, null, sortOrder);     	  	
      	

    	if(cur.getCount()>0)
          	{
          		String id;
          		String name;
          		String phone="number";
          		while(cur.moveToNext())
          		{
          			Contact c =new Contact();
          			id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
          			name=cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
          			
          			
          			if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
          	            System.out.println("name : " + name + ", ID : " + id);

          	            // get the phone number
          	            Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
          	                                   ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
          	                                   new String[]{id}, sortOrder);
          	            while (pCur.moveToNext()) {
          	                   phone = pCur.getString(
          	                         pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
          	                  System.out.println("phone" + phone);
          	            }
          	            pCur.close();
    		
          			c.setId(Long.parseLong(id));
          			c.setName(name);
          			c.setPhoneNum(phone);
          			contactList.add(c);    			
          			datasource.createContact(c);
          		}
          	}
          	cur.close();
          	//return contactList;
          }

    
      }
}
