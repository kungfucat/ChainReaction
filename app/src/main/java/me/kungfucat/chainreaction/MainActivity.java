package me.kungfucat.chainreaction;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

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
    int RED_COLOUR = Color.parseColor("#E57373");
    int BLUE_COLOUR = Color.parseColor("#42A5F5");

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
        currentColor = RED_COLOUR;
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
            sublinearLayout.setPadding(PADDING, PADDING, PADDING, PADDING);

            for (int j = 0; j < COLUMN_COUNT; j++) {

//                Add the gifImageViews to the linear layout created above
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
                Block block = new Block(gifImageView);
                block.display();
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
        final int x = id / COLUMN_COUNT;
        final int y = id % COLUMN_COUNT;

        Block currentBlock = arrayList.get(id);

        if (currentBlock.playerColour.equals("none")) {
            currentBlock.count = 1;
            currentBlock.playerColour = currentPlayer;
            currentBlock.display();

            //don't know why, but had to make the thread sleep for a while to prevent freezing of gifs
            slowDownProcess();

            toggle();
        } else if (currentBlock.playerColour.equals(currentPlayer)) {
            //Before explosion, make the screen unresponsive
            for (int i = 0; i < arrayList.size(); i++) {
                arrayList.get(i).gifImageView.setOnClickListener(null);
            }

            currentBlock.count++;
            currentBlock.display();


            //testing slowing down of the explosion to make the players actually see it
            if (currentBlock.isExplodable(x, y)) {
                explode(x, y);
            } else {
                setBackListener();
            }
            //don't know why, but had to make the thread sleep for a while to prevent freezing of gifs
            slowDownProcess();
            toggle();
        }
    }

    public void setBackListener() {

//      After explosion, attach on click back
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.get(i).gifImageView.setOnClickListener(this);
        }

    }

    //just reset the whole board
    public void slowDownProcess() {

        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.get(i).gifImageView.setImageDrawable(null);
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.get(i).display();
        }
    }

    //toggle between the players
    public void toggle() {
        if (currentPlayer.equals("red")) {
            currentColor = BLUE_COLOUR;
            currentPlayer = "blue";
        } else {
            currentColor = RED_COLOUR;
            currentPlayer = "red";
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

    public void slowExplode(int id) {
        Block currentBlock = arrayList.get(id);
        currentBlock.display();

    }

    public void explode(int x, int y) {
        int id = x * COLUMN_COUNT + y;

        Queue<Block> queue = new LinkedList<>();
        ArrayList<Block> temp = new ArrayList<>();

        queue.add(arrayList.get(id));
        queue.add(null);

        final int c[] = {0, 0, 1, -1};
        final int r[] = {1, -1, 0, 0};

        while (!queue.isEmpty()) {
            Block current = queue.remove();
            if (current == null) {
                temp.clear();
                if (!queue.isEmpty()) {
                    queue.add(null);
                }
                continue;
            }
                current.count = 0;
                current.playerColour = "none";
                current.display();
            for (int i = 0; i < 4; i++) {
                int x1 = (current.id) / COLUMN_COUNT, y1 = (current.id) % COLUMN_COUNT;

                int X = x1 + r[i], Y = y1 + c[i];
                if (X >= 0 && X < ROW_COUNT && Y >= 0 && Y < COLUMN_COUNT) {

                    int newId = X * COLUMN_COUNT + Y;
                        arrayList.get(newId).count++;
                        arrayList.get(newId).playerColour = currentPlayer;
                        arrayList.get(newId).display();

                    if (arrayList.get(newId).isExplodable(X, Y)) {
                        queue.add(arrayList.get(newId));
                        //update the list for that level
                        temp.add(arrayList.get(newId));
                    }
                }
            }
        }
        setBackListener();
    }
}
