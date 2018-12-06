package mg.waitlist;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mg.waitlist.Data.Contract;

public class AdapterWait extends RecyclerView.Adapter<AdapterWait.viewholder>
{
    Context context;
    //Database return cursor
    Cursor  cursor;

    public AdapterWait(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public viewholder onCreateViewHolder( ViewGroup viewGroup, int i)
    {
        View view=LayoutInflater.from(context).inflate(R.layout.list_item,viewGroup,false);
        viewholder viewholder=new viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder viewholder, int i)
    {
        //if cursor ie empty , return
        if (!cursor.moveToPosition(i))
        {
            return;
        }
        String name = cursor.getString(cursor.getColumnIndex(Contract.waitlistEntry.COLUMN_GUEST_NAME));
        String size = cursor.getString(cursor.getColumnIndex(Contract.waitlistEntry.COLUMN_PARTY_SIZE));
        long id = cursor.getLong(cursor.getColumnIndex(Contract.waitlistEntry._ID));

        viewholder.nameVH.setText(name);
        viewholder.sizeVH.setText(size);
        //setTag use to call in mainactivty (id)
        viewholder.itemView.setTag(id);

    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class viewholder extends RecyclerView.ViewHolder
    {
        TextView sizeVH,nameVH;

        public viewholder( View itemView) {
            super(itemView);
            sizeVH=itemView.findViewById(R.id.size_item);
            nameVH=itemView.findViewById(R.id.name_item);

        }
    }
     //for refresh
    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (cursor != null)
        {
            cursor.close();
        }

        cursor = newCursor;

        if (newCursor != null) {
            // force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }
}

