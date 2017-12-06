package me.kungfucat.chainreaction;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout linearLayout;
    public static final int ROW_COUNT = 9;
    public static final int COLUMN_COUNT = 6;
    public static final int PADDING = 1;
    public ArrayList<Block> arrayList;
    public String currentPlayer;
    Context context;
    int currentColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.linearLayout);
        arrayList = new ArrayList<>();
        context = this;
        //Helper class needed the context to access resources, and hence get the gifs
        Helper.initialise(context);
        currentPlayer = "red";
        currentColor = Color.RED;
        fillTheLayout();
    }

    public void fillTheLayout() {
        //tried grid layout, didn't work properly, and the imageViews went off screen
        for (int i = 0; i < ROW_COUNT; i++) {
            //these are horizontal linear layouts that will be part of the vertical linear layout, shown in XML
            LinearLayout sublinearLayout = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1;

            sublinearLayout.setLayoutParams(layoutParams);
            //center align the contents
            sublinearLayout.setGravity(Gravity.CENTER);
            sublinearLayout.setPadding(2 * PADDING, 2 * PADDING, 2 * PADDING, 2 * PADDING);

            for (int j = 0; j < COLUMN_COUNT; j++) {

                //Add the gifImageViews to the linear layout created above
                GifImageView gifImageView = new GifImageView(this);
                LinearLayout.LayoutParams imageViewlayoutParam = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                imageViewlayoutParam.weight = 1;

                gifImageView.setLayoutParams(imageViewlayoutParam);
                //this padding helps in showing the white boundaries
                gifImageView.setPadding(PADDING, PADDING, PADDING, PADDING);
                //Similar to storing in row-major form, used that logic to set id's
                gifImageView.setId(i * COLUMN_COUNT + j);
                //main activity implements the onClick
                gifImageView.setOnClickListener(this);
                Block block = new Block(gifImageView, context);
                arrayList.add(block);
                sublinearLayout.addView(gifImageView);

            }
            linearLayout.addView(sublinearLayout);
        }
        //change grid's color, after initialising the layout
        changeColor();
    }

    @Override
    public void onClick(View v) {
        GifImageView imageView = (GifImageView) v;
        int id = imageView.getId();
        int x = id / COLUMN_COUNT;
        int y = id % COLUMN_COUNT;

        Log.i("TAG", "onLongClick: x = " + x + ", y = " + y);

        Block currentBlock = arrayList.get(id);

        if (currentBlock.playerColour.equals("none")) {
            currentBlock.count = 1;
            currentBlock.playerColour = currentPlayer;
            currentBlock.display();
            toggle();
        } else if (currentBlock.playerColour.equals(currentPlayer)) {
            //Before explosion, make the screen unresponsive
            for (int i = 0; i < arrayList.size(); i++) {
                arrayList.get(i).gifImageView.setOnClickListener(null);
            }
            explode(x, y);
//                After explosion, attach on click back
            for (int i = 0; i < arrayList.size(); i++) {
                arrayList.get(i).gifImageView.setOnClickListener(this);
            }
            toggle();
        }
    }

    //toggle between the players
    public void toggle() {
        if (currentPlayer.equals("red")) {
            currentColor = Color.BLUE;
            currentPlayer = "blue";
        } else {
            currentPlayer = "red";
            currentColor = Color.RED;
        }
        //change color of grid
        changeColor();
    }

    //change color depending on the basis of the color of the current player
    public void changeColor() {
        linearLayout.setBackgroundColor(currentColor);
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.get(i).gifImageView.setBackgroundColor(currentColor);
        }
    }

    //TODO : implement a bfs on the 2d grid, with time gaps between each expansion
    public void explode(int x, int y) {
        if (x < 0 || y < 0 || x > MainActivity.ROW_COUNT - 1 || y > MainActivity.COLUMN_COUNT - 1) {
            return;
        }
        int id = x * COLUMN_COUNT + y;
        Block currentBlock = arrayList.get(id);
        currentBlock.count++;
        currentBlock.playerColour = currentPlayer;
        currentBlock.display();
        if (currentBlock.isExplodable(x, y)) {
            currentBlock.count = 0;
            currentBlock.playerColour = "none";
            currentBlock.display();
//            currentBlock.gifImageView.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    currentBlock.count = 0;
//                    currentBlock.display();
//                }
//            }, 1000);

            int r[] = {1, -1, 0, 0};
            int c[] = {0, 0, 1, -1};
            for (int i = 0; i < 4; i++) {
                explode(x + r[i], y + c[i]);
            }
        }
    }
}
