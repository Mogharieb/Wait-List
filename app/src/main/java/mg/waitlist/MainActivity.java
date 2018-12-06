package mg.waitlist;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import mg.waitlist.Data.Contract;
import mg.waitlist.Data.DBhelper;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    ImageView waitimage;
    RecyclerView recyclerView;
    AdapterWait adapterWait;
    SQLiteDatabase sqLiteDatabase;
    DBhelper DBhelper;
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton =  findViewById(R.id.floating_btn);
        recyclerView =  findViewById(R.id.recycler_view);
        waitimage =  findViewById(R.id.wait_image);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        //line between items
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

         DBhelper=new DBhelper(getApplicationContext());
        sqLiteDatabase=DBhelper.getWritableDatabase();
        cursor=getAllGuest();
        adapterWait=new AdapterWait(getApplicationContext(),cursor);
        recyclerView.setAdapter(adapterWait);

        //if there are no items =  image visible
        // else no image

        if (cursor.getCount() !=0)
        {
            waitimage.setVisibility(View.GONE);
        }
        else
            {
                waitimage.setVisibility(View.VISIBLE);
            }

            // floating button click

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showdialog();
            }
        });
        //swipe for delete item (right,left)
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i)
            {
                //get the id of the item being swiped

                long id = (long) viewHolder.itemView.getTag();

                //remove from DB
                removeGuest(id);

                //update the list
                adapterWait.swapCursor(getAllGuest());

                if (getAllGuest().getCount() == 0)
                {
                    waitimage.setVisibility(View.VISIBLE);
                }
            }


        }).attachToRecyclerView(recyclerView);
        }




       private void showdialog()
       {
           final Dialog dialog = new Dialog(this);
           dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
           dialog.setContentView(R.layout.guest_dialog);
           dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
           dialog.setCancelable(false);

           WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
           lp.copyFrom(dialog.getWindow().getAttributes());
           lp.width = WindowManager.LayoutParams.MATCH_PARENT;
           lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            Button add_guest =  dialog.findViewById(R.id.add_guest_btn);
            Button back =  dialog.findViewById(R.id.back_btn);

            final EditText guest_name =  dialog.findViewById(R.id.guest_name);
            final EditText guest_number =  dialog.findViewById(R.id.guest_number);

           add_guest.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v)
               {
                   String name = guest_name.getText().toString();
                   String number = guest_number.getText().toString();

                   if (name.length() == 0 || number.length() == 0)
                   {
                       Toast.makeText(MainActivity.this,"Please Enter Data",Toast.LENGTH_SHORT).show();
                   }
                   else
                   {
                       addNewGuest(name,number);
                       //for refresh
                       adapterWait.swapCursor(getAllGuest());

                       waitimage.setVisibility(View.GONE);

                       dialog.dismiss();
                   }
               }
           });

           back.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   dialog.dismiss();

               }
           });

           dialog.show();
           dialog.getWindow().setAttributes(lp);
       }


//show all guest
    private Cursor getAllGuest()
    {
        Cursor cursor=sqLiteDatabase.query
                (
                        Contract.waitlistEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        Contract.waitlistEntry.COLUMN_TIMESTAMP //for sort
                );
        return cursor;
    }
    private long addNewGuest(String name, String partySize)
    {

        ContentValues cv = new ContentValues();

        cv.put(Contract.waitlistEntry.COLUMN_GUEST_NAME, name);
        cv.put(Contract.waitlistEntry.COLUMN_PARTY_SIZE, partySize);
        long i=sqLiteDatabase.insert(Contract.waitlistEntry.TABLE_NAME, null, cv);
        return i;
    }
    private boolean removeGuest(long id)
    {
        return sqLiteDatabase.delete(Contract.waitlistEntry.TABLE_NAME, Contract.waitlistEntry._ID + "=" + id, null) > 0;
    }
}
