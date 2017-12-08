package me.kungfucat.chainreaction;

import android.content.Context;
import android.content.res.Resources;
import android.os.CountDownTimer;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by harsh on 12/6/17.
 */

//for one single box
public class Block {

    String playerColour;
    int count, id;
    GifImageView gifImageView;

    Block(GifImageView im) {
        playerColour = "none";
        count = 0;
        gifImageView = im;
        id = gifImageView.getId();
    }

    public boolean isExplodable(int i, int j) {
        //for corner blocks
        if ((i == 0 && j == 0) ||
                (i == MainActivity.ROW_COUNT - 1 && j == 0) ||
                (i == 0 && j == MainActivity.COLUMN_COUNT - 1) ||
                (i == MainActivity.ROW_COUNT - 1 && j == MainActivity.COLUMN_COUNT - 1)) {
            if (count == 2) {
                return true;
            }
            return false;
        }

        //for blocks on the sides
        if (i == 0 || j == 0 || i == MainActivity.ROW_COUNT - 1 || j == MainActivity.COLUMN_COUNT - 1) {
            if (count == 3) {
                return true;
            }
            return false;
        }

        //for the remaining blocks
        if (count == 4) {
            return true;
        }
        return false;
    }

    public void display() {
        //connect corresponding gifs to the gifImageViews'
        if (playerColour.equals("none")) {
            gifImageView.setImageDrawable(Helper.empty);
        }
        else if (playerColour.equals("red")) {
            if (count == 0) {
                gifImageView.setImageDrawable(Helper.empty);
            } else if (count == 1) {
                gifImageView.setImageDrawable(Helper.red1);
            } else if (count == 2) {
                gifImageView.setImageDrawable(Helper.red2);
            } else if (count == 3) {
                gifImageView.setImageDrawable(Helper.red3);
            } else if (count == 4) {
                gifImageView.setImageDrawable(Helper.red4);
            }
        } else if (playerColour.equals("blue")) {
            if (count == 0) {
                gifImageView.setImageDrawable(Helper.empty);
            } else if (count == 1) {
                gifImageView.setImageDrawable(Helper.blue1);
            } else if (count == 2) {
                gifImageView.setImageDrawable(Helper.blue2);
            } else if (count == 3) {
                gifImageView.setImageDrawable(Helper.blue3);
            } else if (count == 4) {
                gifImageView.setImageDrawable(Helper.blue4);
            }
        }
    }

}
