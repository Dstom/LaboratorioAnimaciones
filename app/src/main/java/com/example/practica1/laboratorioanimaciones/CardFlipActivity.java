package com.example.practica1.laboratorioanimaciones;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Tommy on 9/04/2018.
 */

public class CardFlipActivity extends AppCompatActivity
    implements FragmentManager.OnBackStackChangedListener {

    private Handler mHandler = new Handler();
    private boolean mShowingBack = false;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        if(savedInstanceState == null){
            // Si no hay instancia previa, agregamos el fragmento que representa el frente.
            // Si existe instancia, el fragmento será el que ya ha sido agregado previamente.
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new CardFrontFragment())
                    .commit();
        }else{
            mShowingBack = (getFragmentManager().getBackStackEntryCount()>0);
        }
        getFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);


        MenuItem item = menu.add(Menu.NONE, R.id.action_flip, Menu.NONE,
                mShowingBack
                        ? R.string.action_photo
                        : R.string.action_info);
        item.setIcon(mShowingBack
                ? R.drawable.ic_action_photo
                : R.drawable.ic_action_info);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
                return true;

            case R.id.action_flip:
                flipCard();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void flipCard() {
        if (mShowingBack) {
            getFragmentManager().popBackStack();
            return;
        }
        // Gira hacia atrás.

        mShowingBack = true;
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .replace(R.id.container, new CardBackFragment())
                .addToBackStack(null)
                .commit();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                invalidateOptionsMenu();
            }
        });
    }
    @Override
    public void onBackStackChanged() {
        mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);

        invalidateOptionsMenu();
    }

    /**
     * Fragmento de la parte frontal
     */


    public static class CardFrontFragment extends Fragment {
        public CardFrontFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_card_front, container, false);
        }
    }

    /**
     * Fragmento de la parte posterior
     */
    public static class CardBackFragment extends Fragment {
        public CardBackFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_card_back, container, false);
        }
    }
}








