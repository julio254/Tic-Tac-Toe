package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];
    private Button buttonReset;

    private boolean player1Turn = true;

    private int roundCount;
    private int player1Points;
    private int player2Points;

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;

    MediaPlayer player1;
    MediaPlayer player2;
    MediaPlayer draw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);

        // Assigns the buttons to the multidimensional array
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);

            }
        }

        buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(!((Button) v).getText().toString().equals("")) {
            return;
        }

        // Setting X = playstation logo
        if(player1Turn) {
            ((Button) v).setText("X");
            v.setBackgroundResource(R.drawable.playstationlogo);
        }
        // setting O = xbox logo
        else {
            ((Button) v).setText("O");
            v.setBackgroundResource(R.drawable.xboxlogo);
        }

        roundCount++;

        if(checkForWin()) {
            if(player1Turn) {
                player1Wins();
            }
            else {
                player2Wins();
            }
        }
        else if(roundCount == 9) {
            draw();
        }
        else {
            player1Turn = !player1Turn;
        }
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        // Goes through rows checking that empty lines != win condition
        for(int i = 0; i < 3; i++) {
            if(field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                return true;
            }
        }

        // Goes through columns checking that empty lines != win condition
        for(int i = 0; i < 3; i++) {
            if(field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                return true;
            }
        }

        // if cases that check the diagonals for a win condition
        if(field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }
        if(field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            return true;
        }

        return false;
    }

    private void player1Wins() {
        player1Points++;

        // Sound effects to play when player 1 wins
        player1 = MediaPlayer.create(this, R.raw.winner);
        player1.start();

        Toast.makeText(this, "Team Playstation wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        // Handler that delays resetting the board
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //do something
                resetBoard();
            }
        }, 2000);
    }

    private void player2Wins() {
        player2Points++;

        // Sound effects to play when player 2 wins
        player2 = MediaPlayer.create(this, R.raw.winner);
        player2.start();

        Toast.makeText(this, "Team Xbox wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        // Handler that delays resetting the board
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //do something
                resetBoard();
            }
        }, 2000);
    }

    private void draw() {

        // Sound effects if players draw
        draw = MediaPlayer.create(this, R.raw.draw);
        draw.start();

        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        // Handler that delays resetting the board
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //do something
                resetBoard();
            }
        }, 1000);
        //resetBoard();
    }

    private void updatePointsText() {
        textViewPlayer1.setText("PlayStation: " + player1Points);
        textViewPlayer2.setText("Xbox: " + player2Points);
    }

    private void resetBoard() {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setBackground(buttonReset.getBackground());
            }
        }

        roundCount = 0;
        player1Turn = true;
    }

    private void resetGame() {
        player1Points = 0;
        player2Points = 0;
        updatePointsText();
        resetBoard();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", player2Points);
        outState.putBoolean("player1Turn", player1Turn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");

        restoreMyImages();
    }

    void restoreMyImages() {
        // for loop through the buttons
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {

                Button thisButton = buttons[i][j];

                if(thisButton.getText() == "X") {
                    thisButton.setBackgroundResource(R.drawable.playstationlogo);
                }
                else if(thisButton.getText() == "O") {
                    thisButton.setBackgroundResource(R.drawable.xboxlogo);
                }
            }
        }
    }
}