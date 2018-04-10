package com.mylocarson.medmanager.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mylocarson.medmanager.R;
import com.mylocarson.medmanager.activities.MedicationActivity;
import com.mylocarson.medmanager.activities.ProfileActivity;
import com.mylocarson.medmanager.activities.ReminderActivity;


/**
 * Created by user on 01/04/2018.
 */

@SuppressWarnings("ALL")
public class DrawerUtil {
    public static void getDrawer(final Activity activity, Toolbar toolbar) {
        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem medicationActivity= new PrimaryDrawerItem().withIdentifier(0).withName(R.string.medication);
//        medicationActivity.withEnabled(false);

//        PrimaryDrawerItem medicationDetailsActivity = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.title_activity_medication_details);
//                .withName(R.string.manage_player).withIcon(R.drawable.ic_people_outline_black_48px);
//        PrimaryDrawerItem reminderActivity = new PrimaryDrawerItem()
//                .withIdentifier(2).withName(R.string.title_activity_reminder);
//                .withName(R.string.tournament).withIcon(R.drawable.tournamenticon);


//                .withName(R.string.settings).withIcon(R.drawable.ic_settings_black_48px);
        SecondaryDrawerItem reminderActivity = new SecondaryDrawerItem().withIdentifier(1).withName(R.string.title_activity_reminder);
        SecondaryDrawerItem profileActivity = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.profile);

//



        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .build();

        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(activity)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withCloseOnClick(true)
                .withSelectedItem(-1)
                .addDrawerItems(
//                        drawerEmptyItem,drawerEmptyItem,drawerEmptyItem,
                        medicationActivity,
                        new DividerDrawerItem(),
                        reminderActivity,
                        new DividerDrawerItem(),
                        profileActivity
//                        drawerItemSettings
//                        drawerItemHelp,
//                        drawerItemDonate
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//                        Toast.makeText(activity,""+drawerItem.getIdentifier(),Toast.LENGTH_SHORT).show();
                        if (drawerItem.getIdentifier() == 0 && !(activity instanceof MedicationActivity)) {
                            // load tournament screen
                            Intent intent = new Intent(activity, MedicationActivity.class);
                            view.getContext().startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 1 && !(activity instanceof ReminderActivity) ){
                            Intent intent = new Intent(activity, ReminderActivity.class);
                            view.getContext().startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 2 && !(activity instanceof ProfileActivity)) {
                            Intent intent = new Intent(activity, ProfileActivity.class);
                            view.getContext().startActivity(intent);
                        }
                        return true;
                    }
                })
                .build();
    }
}
