package com.example.mac.backendless_add_mod_del_example;


//Map and HashMap used to dynamically create object for backendless
import java.util.Map;
import java.util.HashMap;
import android.content.Context;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;





public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getName();

    private static final String BE_APP_ID = "CEEACAE5-2516-D403-FF65-DEBB7ADD3300";
    private static final String BE_ANDROID_API_KEY = "71B09051-A95B-8073-FFCA-9C4B61B38E00";
    public  static final String EMAIL_PREF = "EMAIL_PREF";
    private static final String MY_EMAIL_ADDRESS = "PUTYOUREMAILHERE";


    DatabaseHelper myDb;
    EditText editName,editSurname,editMarks ,editTextId;
    Button btnAddData;
    Button btnviewAll;
    Button btnDelete;

    Button btnviewUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BackendlessUser user = new BackendlessUser();
        user.setEmail( MY_EMAIL_ADDRESS );
        user.setPassword( "PUTPASSWORDHERE" );

        Backendless.initApp(this, BE_APP_ID, BE_ANDROID_API_KEY);
     /* Call this once to set up the user of the app
        Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>(){
            @Override
            public void handleResponse(BackendlessUser backendlessUser){
                Log.i( "User ", backendlessUser.getEmail() + " successfully registered" );
                Toast.makeText(MainActivity.this,"Registered new user",Toast.LENGTH_LONG).show();
            }
            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.e( "Backendless registration error! ", backendlessFault.getMessage());
            }
        });
*/



        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL_PREF, MY_EMAIL_ADDRESS);
        editor.commit();
        myDb = new DatabaseHelper(this);

        editName = (EditText)findViewById(R.id.editText_name);
        editSurname = (EditText)findViewById(R.id.editText_surname);
        editMarks = (EditText)findViewById(R.id.editText_Marks);
        editTextId = (EditText)findViewById(R.id.editText_id);
        btnAddData = (Button)findViewById(R.id.button_add);
        btnviewAll = (Button)findViewById(R.id.button_viewAll);
        btnviewUpdate= (Button)findViewById(R.id.button_update);
        btnDelete= (Button)findViewById(R.id.button_delete);
        AddData();
        viewAll();
        UpdateData();
        DeleteData();
    }
    public void DeleteData() {
        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer deletedRows = myDb.deleteData(editTextId.getText().toString());
                        if(deletedRows > 0)
                            Toast.makeText(MainActivity.this,"Data Deleted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this,"Data not Deleted",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
    public void UpdateData() {
        btnviewUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isUpdate = myDb.updateData(editTextId.getText().toString(),
                                editName.getText().toString(),
                                editSurname.getText().toString(),editMarks.getText().toString());
                        if(isUpdate == true)
                            Toast.makeText(MainActivity.this,"Data Update",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this,"Data not Updated",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
    public  void AddData() {



        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        boolean isInserted = myDb.insertData(editName.getText().toString(),
                                editSurname.getText().toString(),
                                editMarks.getText().toString() );

                        //Adding backendless save below:
                        String n=editName.getText().toString();
                        String sn=editSurname.getText().toString();
                        String m = editMarks.getText().toString();
                        Toast.makeText(MainActivity.this,"Before blSave- Name:"+n+" Surname:"+sn+" Marks:"+m,Toast.LENGTH_LONG).show();
                         blSaveNewRecord(n, sn, m );


                        if(isInserted == true)
                            Toast.makeText(MainActivity.this,"Data Inserted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this,"Data not Inserted",Toast.LENGTH_LONG).show();


                    }
                }
        );
    }

    public void viewAll() {
        btnviewAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res = myDb.getAllData();
                        if(res.getCount() == 0) {
                            // show message
                            showMessage("Error","Nothing found");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("Id :"+ res.getString(0)+"\n");
                            buffer.append("Name :"+ res.getString(1)+"\n");
                            buffer.append("Surname :"+ res.getString(2)+"\n");
                            buffer.append("Marks :"+ res.getString(3)+"\n\n");
                        }

                        // Show all data
                        // CHALLENGE: Convert this to a list view that is selectable, and when selected,
                        // listview popup closes, and selected record is populated into the main text area.

                        showMessage("Data",buffer.toString());
                    }
                }
        );
    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void blSaveNewRecord(String n, String sn, String m)
    {
        Toast.makeText(MainActivity.this,"Inside blSave 1: Name:"+n+" Surname:"+sn+" Marks:"+m,Toast.LENGTH_LONG).show();
        @SuppressWarnings("unchecked")
        HashMap contact = new HashMap();
        contact.put( "name", n );
        contact.put( "Surname", sn );
        contact.put( "Marks", m );
        Toast.makeText(MainActivity.this,"Inside blSave 2: Name:"+n+" Surname:"+sn+" Marks:"+m,Toast.LENGTH_LONG).show();

        // save object synchronously
    //    Map savedContact = Backendless.Persistence.of( "Records" ).save( contact );

        // save object asynchronously
        Backendless.Persistence.of( "Records" ).save( contact, new AsyncCallback<Map>() {
            public void handleResponse( Map response )
            {
                // new Contact instance has been saved
            }

            public void handleFault( BackendlessFault fault )
            {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Toast.makeText(MainActivity.this,"An error has occured : "+ fault.getCode(),Toast.LENGTH_LONG).show();
            }
        });

    }
}